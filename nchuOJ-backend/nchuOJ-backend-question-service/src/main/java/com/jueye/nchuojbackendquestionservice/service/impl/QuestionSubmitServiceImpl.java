package com.jueye.nchuojbackendquestionservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jueye.nchuojbackendcommon.common.ErrorCode;
import com.jueye.nchuojbackendcommon.constant.CommonConstant;
import com.jueye.nchuojbackendcommon.exception.BusinessException;
import com.jueye.nchuojbackendcommon.utils.SqlUtils;
import com.jueye.nchuojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.jueye.nchuojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.jueye.nchuojbackendmodel.model.entity.Question;
import com.jueye.nchuojbackendmodel.model.entity.QuestionSubmit;
import com.jueye.nchuojbackendmodel.model.entity.User;
import com.jueye.nchuojbackendmodel.model.enums.QuestionSubmitLanguageEnum;
import com.jueye.nchuojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.jueye.nchuojbackendmodel.model.vo.QuestionSubmitVO;
import com.jueye.nchuojbackendquestionservice.Mq.JudgeMessageProducer;
import com.jueye.nchuojbackendquestionservice.mapper.QuestionSubmitMapper;
import com.jueye.nchuojbackendquestionservice.service.QuestionService;
import com.jueye.nchuojbackendquestionservice.service.QuestionSubmitService;
import com.jueye.nchuojbackendserviceclient.service.JudgeFeignClient;
import com.jueye.nchuojbackendserviceclient.service.UserFeignClient;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author ASUS
* @description 针对表【question_submit(题目提交)】的数据库操作Service实现
* @createDate 2024-03-05 16:41:33
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService {
    @Resource
    private QuestionService questionService;
    @Resource
    private UserFeignClient userFeignClient;
    @Resource
    private JudgeMessageProducer judgeMessageProducer;
    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        //判断编程语言是否合法
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if(languageEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"编程语言错误");
        }
        long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已题目提交
        long userId = loginUser.getId();
        // 每个用户串行题目提交
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setLanguage(language);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        //设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean save = this.save(questionSubmit);
        if(!save){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"数据插入失败");
        }
        // 执行判题服务
        Long questionSubmitId = questionSubmit.getId();
        //发送消息
        judgeMessageProducer.sendMessage("code_exchange","my_routingKey",String.valueOf(questionSubmitId));
        // 执行判题服务
//        CompletableFuture.runAsync(()->{
//
//            judgeService.doJudge(questionSubmitId);
//        });
        return questionSubmitId;
    }

    /**
     * 获取查询包装类
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userid = questionSubmitQueryRequest.getUserid();

        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();


        queryWrapper.eq(ObjectUtils.isNotEmpty(language), "language", language);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status)!=null, "status", status);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userid), "userid", userid);
        queryWrapper.orderByDesc("createTime");
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        long userId = loginUser.getId();
        if(userId!=questionSubmit.getUserId()&&!userFeignClient.isAdmin(loginUser)){
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollUtil.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionSubmitList.stream().map(QuestionSubmit::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userFeignClient.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 1. 关联查询题目信息
        Set<Long> questionIdSet = questionSubmitList.stream().map(QuestionSubmit::getQuestionId).collect(Collectors.toSet());
        Map<Long, List<Question>> questionIdUserListMap = questionService.listByIds(questionIdSet).stream()
                .collect(Collectors.groupingBy(Question::getId));
        // 填充信息
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit ->
                {
                    QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
                    Long userId = questionSubmit.getUserId();
                    // 确保userIdUserListMap.get(userId)不为null且不为空
                    User user = userIdUserListMap.containsKey(userId) && !userIdUserListMap.get(userId).isEmpty() ?
                            userIdUserListMap.get(userId).get(0) : null;
                    Long questionId = questionSubmit.getQuestionId();
                    // 确保questionIdUserListMap.get(questionId)不为null且不为空
                    Question question = questionIdUserListMap.containsKey(questionId) && !questionIdUserListMap.get(questionId).isEmpty() ?
                            questionIdUserListMap.get(questionId).get(0) : null;

                    // 只有当user和question不是null时才设置值
                    if (user != null) {
                        questionSubmitVO.setUserVO(userFeignClient.getUserVO(user));
                    }
                    if (question != null) {
                        questionSubmitVO.setQuestionVO(questionService.getQuestionVO(question));
                    }
                    return questionSubmitVO;
                })
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }

}




