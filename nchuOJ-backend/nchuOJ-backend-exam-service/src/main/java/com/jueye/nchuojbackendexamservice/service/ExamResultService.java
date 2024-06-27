package com.jueye.nchuojbackendexamservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jueye.nchuojbackendmodel.model.dto.exam.ExamResultQueryRequest;
import com.jueye.nchuojbackendmodel.model.entity.ExamResult;
import com.jueye.nchuojbackendmodel.model.vo.ExamResultVO;

import java.util.List;

/**
* @author ASUS
* @description 针对表【exam_result(考试结果)】的数据库操作Service
* @createDate 2024-06-20 21:37:40
*/
public interface ExamResultService extends IService<ExamResult> {
    ExamResult getByUserId(long id, Long id1);

    QueryWrapper<ExamResult> getQueryWrapper(ExamResultQueryRequest examResultQueryRequest);

//    Page<ExamResultVO> getExamResultVOPage(Page<ExamResult> examResultPage);

    List<ExamResultVO> getExamResultVOList(List<ExamResult> examResultList);
}
