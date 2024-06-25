package com.jueye.nchuojbackendexamservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jueye.nchuojbackendmodel.model.dto.exam.ExamQueryRequest;
import com.jueye.nchuojbackendmodel.model.entity.Exam;
import com.jueye.nchuojbackendmodel.model.vo.ExamVO;

/**
* @author ASUS
* @description 针对表【exam(考试)】的数据库操作Service
* @createDate 2024-06-20 21:37:40
*/
public interface ExamService extends IService<Exam> {

    ExamVO getExamVo(Exam exam);

    QueryWrapper<Exam> getQueryWrapper(ExamQueryRequest examQueryRequest);

    Page<ExamVO> getExamVOPage(Page<Exam> examPage);

    void validExam(Exam exam, boolean add);

    Boolean endExam(long id);

    void endExamCount(Exam exam);

    void addExamQuestionNum(long examId, long questionId);

    void removeExamQuestion(Long examId, long id);
}
