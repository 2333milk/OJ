package com.jueye.nchuojbackendquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jueye.nchuojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.jueye.nchuojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.jueye.nchuojbackendmodel.model.entity.QuestionSubmit;
import com.jueye.nchuojbackendmodel.model.entity.User;
import com.jueye.nchuojbackendmodel.model.vo.QuestionSubmitVO;

/**
* @author ASUS
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2024-03-05 16:41:33
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param qestionQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest qestionQueryRequest);

    /**
     * 获取问题封装
     *
     * @param questionSubmit
     * @param User
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取问题封装
     *
     * @param questionSubmitPage
     * @param User
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}
