package com.jueye.nchuojbackendjudgeservice.judge;


import com.jueye.nchuojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题服务
 */
@Service
public interface JudgeService {
    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    public QuestionSubmit doJudge(long questionSubmitId);
}
