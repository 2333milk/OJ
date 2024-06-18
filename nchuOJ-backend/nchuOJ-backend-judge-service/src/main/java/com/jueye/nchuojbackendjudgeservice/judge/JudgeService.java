package com.jueye.nchuojbackendjudgeservice.judge;


import com.jueye.nchuojbackendmodel.model.entity.QuestionSubmit;

/**
 * 判题服务
 */

public interface JudgeService {
    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    public QuestionSubmit doJudge(long questionSubmitId);
}
