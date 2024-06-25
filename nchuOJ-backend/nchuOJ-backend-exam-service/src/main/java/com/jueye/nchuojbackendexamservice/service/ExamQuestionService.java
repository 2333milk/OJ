package com.jueye.nchuojbackendexamservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jueye.nchuojbackendmodel.model.entity.ExamQuestion;
import com.jueye.nchuojbackendmodel.model.vo.ExamQuestionVO;

import java.util.List;

/**
* @author ASUS
* @description 针对表【exam_question(考试题目关联表)】的数据库操作Service
* @createDate 2024-06-20 21:37:40
*/
public interface ExamQuestionService extends IService<ExamQuestion> {

    void validExam(ExamQuestion examQuestion, boolean b);

    List<ExamQuestionVO> getQuestionVoList(List<Long> examQuestionIds);

    ExamQuestion getByExamIdAndQuestionId(long examId, long questionId);
}
