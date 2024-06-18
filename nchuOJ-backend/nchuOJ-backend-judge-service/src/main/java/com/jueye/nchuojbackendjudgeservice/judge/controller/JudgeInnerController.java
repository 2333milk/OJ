package com.jueye.nchuojbackendjudgeservice.judge.controller;

import com.jueye.nchuojbackendjudgeservice.judge.JudgeService;
import com.jueye.nchuojbackendmodel.model.entity.QuestionSubmit;
import com.jueye.nchuojbackendserviceclient.service.JudgeFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 用户接口
 *
 
 */
@RestController("/inner")
@Slf4j
public class JudgeInnerController implements JudgeFeignClient {

    @Resource
    private JudgeService judgeService;


    @Override
    @PostMapping("/doJudge")
    public QuestionSubmit doJudge(long questionSubmitId) {
        return this.judgeService.doJudge(questionSubmitId);
    }
}
