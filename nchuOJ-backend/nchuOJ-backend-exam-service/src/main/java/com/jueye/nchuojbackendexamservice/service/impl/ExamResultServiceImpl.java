package com.jueye.nchuojbackendexamservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jueye.nchuojbackendcommon.constant.CommonConstant;
import com.jueye.nchuojbackendcommon.utils.SqlUtils;
import com.jueye.nchuojbackendexamservice.mapper.ExamResultMapper;
import com.jueye.nchuojbackendexamservice.service.ExamResultService;
import com.jueye.nchuojbackendmodel.model.dto.exam.ExamResultQueryRequest;
import com.jueye.nchuojbackendmodel.model.entity.ExamResult;
import org.springframework.stereotype.Service;

/**
* @author ASUS
* @description 针对表【exam_result(考试结果)】的数据库操作Service实现
* @createDate 2024-06-20 21:37:40
*/
@Service
public class ExamResultServiceImpl extends ServiceImpl<ExamResultMapper, ExamResult>
    implements ExamResultService {


    @Override
    public ExamResult getByUserId(long id, Long id1) {
        QueryWrapper<ExamResult> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId", id1).eq("examId", id);
        return this.getOne(queryWrapper);
    }

    @Override
    public QueryWrapper<ExamResult> getQueryWrapper(ExamResultQueryRequest examResultQueryRequest) {
        QueryWrapper<ExamResult> queryWrapper = new QueryWrapper<>();
        if(examResultQueryRequest.getExamId() == null){
            return queryWrapper;
        }
        Long examId = examResultQueryRequest.getExamId();
        Long userId = examResultQueryRequest.getUserId();
        String sortField = examResultQueryRequest.getSortField();
        String sortOrder = examResultQueryRequest.getSortOrder();
        queryWrapper.eq("exam_id", examId);
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }


}




