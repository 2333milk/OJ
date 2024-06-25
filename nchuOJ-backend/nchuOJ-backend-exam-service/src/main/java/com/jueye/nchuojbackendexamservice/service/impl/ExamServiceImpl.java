package com.jueye.nchuojbackendexamservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jueye.nchuojbackendcommon.common.ErrorCode;
import com.jueye.nchuojbackendcommon.constant.CommonConstant;
import com.jueye.nchuojbackendcommon.exception.BusinessException;
import com.jueye.nchuojbackendcommon.exception.ThrowUtils;
import com.jueye.nchuojbackendcommon.utils.SqlUtils;
import com.jueye.nchuojbackendexamservice.mapper.ExamMapper;
import com.jueye.nchuojbackendexamservice.service.ExamQuestionService;
import com.jueye.nchuojbackendexamservice.service.ExamResultService;
import com.jueye.nchuojbackendexamservice.service.ExamService;
import com.jueye.nchuojbackendmodel.model.dto.exam.ExamQueryRequest;
import com.jueye.nchuojbackendmodel.model.entity.Exam;
import com.jueye.nchuojbackendmodel.model.entity.ExamResult;
import com.jueye.nchuojbackendmodel.model.entity.QuestionSubmit;
import com.jueye.nchuojbackendmodel.model.vo.ExamQuestionVO;
import com.jueye.nchuojbackendmodel.model.vo.ExamVO;
import com.jueye.nchuojbackendserviceclient.service.QuestionFeignClient;
import javafx.util.Pair;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
* @author ASUS
* @description 针对表【exam(考试)】的数据库操作Service实现
* @createDate 2024-06-20 21:37:40
*/
@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam>
    implements ExamService {

    @Resource
    private ExamQuestionService examQuestionService;

    @Resource
    private ExamResultService examResultService;

    @Resource
    private QuestionFeignClient questionFeignClient;

    @Override
    public ExamVO getExamVo(Exam exam) {
        ExamVO examVO = ExamVO.objToVo(exam);
        List<Long> examQuestionIds = examVO.getExamQuestionIds();
        if(examQuestionIds!=null && !examQuestionIds.isEmpty()){
            List<ExamQuestionVO> examQuestions = examQuestionService.getQuestionVoList(examQuestionIds);
            examVO.setExamQuestions(examQuestions);
        }
        return examVO;
    }

    @Override
    public QueryWrapper<Exam> getQueryWrapper(ExamQueryRequest examQueryRequest) {
        QueryWrapper<Exam> queryWrapper = new QueryWrapper<>();
        if(examQueryRequest==null){
            return queryWrapper;
        }
        Long id = examQueryRequest.getId();
        String title = examQueryRequest.getTitle();
        String description = examQueryRequest.getDescription();
        Date startTime = examQueryRequest.getStartTime();
        Date endTime = examQueryRequest.getEndTime();
        Integer status = examQueryRequest.getStatus();
        Long userId = examQueryRequest.getUserId();
        String sortField = examQueryRequest.getSortField();
        String sortOrder = examQueryRequest.getSortOrder();


        queryWrapper.like(StringUtils.isNotBlank(title),"title",title);
        queryWrapper.like(StringUtils.isNotBlank(description),"description",description);
        queryWrapper.ge(startTime!=null,"startTime",startTime);
        queryWrapper.le(endTime!=null,"endTime",endTime);
        queryWrapper.eq(status!=null,"status",status);
        queryWrapper.eq(userId!=null,"userId",userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public Page<ExamVO> getExamVOPage(Page<Exam> examPage) {
        List<Exam> examList = examPage.getRecords();
        Page<ExamVO> examVOPage = new Page<>(examPage.getCurrent(),examPage.getSize(),examPage.getTotal());
        if(CollUtil.isEmpty(examList)){
            return examVOPage;
        }
        List<ExamVO> examVOList = examList.stream().map(exam ->{
            ExamVO examVO = ExamVO.objToVo(exam);
            return examVO;
        }).collect(Collectors.toList());
        examVOPage.setRecords(examVOList);
        return examVOPage;
    }

    @Override
    public void validExam(Exam exam, boolean add) {
        if(exam==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String title = exam.getTitle();
        String description = exam.getDescription();
        Date startTime = exam.getStartTime();
        Date endTime = exam.getEndTime();
        if(add){
            ThrowUtils.throwIf(StringUtils.isAnyBlank(title,description),ErrorCode.PARAMS_ERROR);
        }
        if(StringUtils.isNotBlank(title) && title.length() > 80){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"标题过长");
        }
        if(StringUtils.isNotBlank(description) && description.length() > 8192){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"描述过长");
        }
        if(startTime==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"开始时间不能为空");
        }
        if(endTime==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"结束时间不能为空");
        }
        if(startTime.after(endTime)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"开始时间不能晚于结束时间");
        }
    }

    @Override
    public Boolean endExam(long id) {
        Exam exam = this.getById(id);
        Date now = new Date();
        if(exam.getStatus()==2){
            return true;
        }
        if(exam.getEndTime().before(now)){
            this.endExamCount(exam);
            exam.setStatus(2);
        }
        return this.updateById(exam);
    }

    @Override
    public void endExamCount(Exam exam) {
        ExamVO examVO = this.getExamVo(exam);
        List<ExamQuestionVO> examQuestions = examVO.getExamQuestions();
        Map<Long, ExamResult> examResultMap = new HashMap<>();
        examQuestions.forEach(examQuestionVO ->{
            Set<Pair<Long,Long>> questionSubmitSet = new HashSet<>();
            QueryWrapper<QuestionSubmit> questionSubmitQueryWrapper = new QueryWrapper<>();
            questionSubmitQueryWrapper.eq("questionId",examQuestionVO.getQuestion().getId());
            questionSubmitQueryWrapper.eq("status",2);
            List<QuestionSubmit> questionSubmitList = questionFeignClient.list(questionSubmitQueryWrapper);
            questionSubmitList.forEach(questionSubmit ->{
                if(!questionSubmitSet.contains(new Pair<>(questionSubmit.getUserId(),questionSubmit.getQuestionId()))){
                    if(examResultMap.containsKey(questionSubmit.getUserId())){
                        ExamResult examResult = examResultMap.get(questionSubmit.getUserId());
                        examResult.setScore(examResult.getScore()+examQuestionVO.getScore());
                    }else {
                        ExamResult examResult = new ExamResult();
                        examResult.setUserId(questionSubmit.getUserId());
                        examResult.setScore(examQuestionVO.getScore());
                        examResultMap.put(questionSubmit.getUserId(),examResult);
                    }
                    questionSubmitSet.add(new Pair<>(questionSubmit.getUserId(),questionSubmit.getQuestionId()));
                }
            });
        });
        examResultMap.forEach((userId,examResult) ->{
            examResultService.save(examResult);
        });
    }

    @Override
    public void addExamQuestionNum(long examId, long examQuestionId) {
        Exam exam = this.getById(examId);
        List<Long> questionIds = JSONUtil.toList(exam.getExamQuestionIds(),Long.class);
        if(questionIds.contains(examQuestionId)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"该题目已经添加");
        }else {
            questionIds.add(examQuestionId);
            exam.setExamQuestionIds(JSONUtil.toJsonStr(questionIds));
            this.updateById(exam);
        }
    }

    @Override
    public void removeExamQuestion(Long examId, long id) {
        Exam exam = this.getById(examId);
        List<Long> questionIds = JSONUtil.toList(exam.getExamQuestionIds(),Long.class);
        if(!questionIds.contains(id)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"该题目已经移除");
        }else {
            questionIds.remove(id);
            exam.setExamQuestionIds(JSONUtil.toJsonStr(questionIds));
            this.updateById(exam);
        }
    }
}




