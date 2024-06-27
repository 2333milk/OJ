package com.jueye.nchuojbackendexamservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jueye.nchuojbackendcommon.constant.CommonConstant;
import com.jueye.nchuojbackendcommon.utils.SqlUtils;
import com.jueye.nchuojbackendexamservice.mapper.ExamResultMapper;
import com.jueye.nchuojbackendexamservice.service.ExamResultService;
import com.jueye.nchuojbackendexamservice.service.ExamService;
import com.jueye.nchuojbackendmodel.model.dto.exam.ExamResultQueryRequest;
import com.jueye.nchuojbackendmodel.model.entity.Exam;
import com.jueye.nchuojbackendmodel.model.entity.ExamResult;
import com.jueye.nchuojbackendmodel.model.entity.User;
import com.jueye.nchuojbackendmodel.model.vo.ExamResultVO;
import com.jueye.nchuojbackendserviceclient.service.UserFeignClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author ASUS
* @description 针对表【exam_result(考试结果)】的数据库操作Service实现
* @createDate 2024-06-20 21:37:40
*/
@Service
public class ExamResultServiceImpl extends ServiceImpl<ExamResultMapper, ExamResult>
    implements ExamResultService {

    @Resource
    private ExamService examService;
    @Resource
    private UserFeignClient userFeignClient;
    @Override
    public ExamResult getByUserId(long id, Long id1) {
        QueryWrapper<ExamResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", id1).eq("examId", id);
        return this.getOne(queryWrapper);
    }

    @Override
    public QueryWrapper<ExamResult> getQueryWrapper(ExamResultQueryRequest examResultQueryRequest) {
        QueryWrapper<ExamResult> queryWrapper = new QueryWrapper<>();
        if(examResultQueryRequest == null){
            return queryWrapper;
        }
        Long examId = examResultQueryRequest.getExamId();
        Long userId = examResultQueryRequest.getUserId();
        String sortField = examResultQueryRequest.getSortField();
        String sortOrder = examResultQueryRequest.getSortOrder();
        queryWrapper.eq(examId!=null,"examId", examId);
        queryWrapper.eq(userId!=null,"userId", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public List<ExamResultVO> getExamResultVOList(List<ExamResult> examResultList) {
        List<ExamResultVO> examResultVOList = new ArrayList<>();
        if (CollUtil.isEmpty(examResultList)) {
            return examResultVOList;
        }
        Set<Long> userIdSet = examResultList.stream().map(ExamResult::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userFeignClient.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        //2 关联查询考试信息
        Set<Long> examIdSet = examResultList.stream().map(ExamResult::getExamId).collect(Collectors.toSet());
        Map<Long, List<Exam>> examIdExamListMap = examService.listByIds(examIdSet).stream()
                .collect(Collectors.groupingBy(Exam::getId));
        // 填充信息
        for (ExamResult examResult : examResultList) {
            ExamResultVO examResultVO = new ExamResultVO();
            BeanUtils.copyProperties(examResult, examResultVO);
            Long userId = examResult.getUserId();
            Long examId = examResult.getExamId();
            User user = null;
            Exam exam = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            if (examIdExamListMap.containsKey(examId)) {
                exam = examIdExamListMap.get(examId).get(0);
            }
            examResultVO.setUser(userFeignClient.getUserVO(user));
            examResultVO.setExam(examService.getExamVo(exam));
            examResultVOList.add(examResultVO);
        }
        return examResultVOList;
    }

//    @Override
//    public Page<ExamResultVO> getExamResultVOPage(Page<ExamResult> examResultPage) {
//        List<ExamResult> examResultList = examResultPage.getRecords();
//        Page<ExamResultVO> examResultVOPage = new Page<>(examResultPage.getCurrent(), examResultPage.getSize(), examResultPage.getTotal());
//        if (CollUtil.isEmpty(examResultList)) {
//            return examResultVOPage;
//        }
        // 1. 关联查询用户信息
//        Set<Long> userIdSet = examResultList.stream().map(ExamResult::getUserId).collect(Collectors.toSet());
//        Map<Long, List<User>> userIdUserListMap = userFeignClient.listByIds(userIdSet).stream()
//                .collect(Collectors.groupingBy(User::getId));
//        //2 关联查询考试信息
//        Set<Long> examIdSet = examResultList.stream().map(ExamResult::getExamId).collect(Collectors.toSet());
//        Map<Long, List<Exam>> examIdExamListMap = examService.listByIds(examIdSet).stream()
//                .collect(Collectors.groupingBy(Exam::getId));
//        // 填充信息
//        List<ExamResultVO> examResultVOList = examResultList.stream().map(examResult -> {
//            ExamResultVO examResultVO = new ExamResultVO();
//            BeanUtils.copyProperties(examResult, examResultVO);
//            Long userId = examResult.getUserId();
//            Long examId = examResult.getExamId();
//            User user = null;
//            Exam exam = null;
//            if (userIdUserListMap.containsKey(userId)) {
//                user = userIdUserListMap.get(userId).get(0);
//            }
//
//            if (examIdExamListMap.containsKey(examId)) {
//                exam = examIdExamListMap.get(examId).get(0);
//            }
//            examResultVO.setUser(userFeignClient.getUserVO(user));
//            examResultVO.setExam(examService.getExamVo(exam));
//            return examResultVO;
//        }).collect(Collectors.toList());
//        examResultVOPage.setRecords(examResultVOList);
//        return examResultVOPage;
//    }


}




