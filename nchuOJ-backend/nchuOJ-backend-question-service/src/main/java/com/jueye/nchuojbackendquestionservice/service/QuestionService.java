package com.jueye.nchuojbackendquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jueye.nchuojbackendmodel.model.dto.question.QuestionQueryRequest;
import com.jueye.nchuojbackendmodel.model.entity.Question;
import com.jueye.nchuojbackendmodel.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author ASUS
* @description 针对表【question(问题)】的数据库操作Service
* @createDate 2024-03-05 16:29:44
*/
public interface QuestionService extends IService<Question> {

    /**
     * 校验
     *
     * @param Question
     * @param add
     */
    void validQuestion(Question Question, boolean add);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 获取问题封装
     *
     * @param question
     * @return
     */
    QuestionVO getQuestionVO(Question question);

    /**
     * 分页获取问题封装
     *
     * @param questionPage
     * @param request
     * @return
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);
}
