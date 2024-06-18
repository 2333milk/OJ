package com.jueye.nchuojbackendquestionservice.controller.inner;

import com.jueye.nchuojbackendmodel.model.entity.Question;
import com.jueye.nchuojbackendmodel.model.entity.QuestionSubmit;
import com.jueye.nchuojbackendquestionservice.service.QuestionService;
import com.jueye.nchuojbackendquestionservice.service.QuestionSubmitService;
import com.jueye.nchuojbackendserviceclient.service.QuestionFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 用户接口
 *
 
 */
@RestController("/inner")
@Slf4j
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;


    @Override
    @GetMapping("/get/{id}")
    public Question getById(Long questionId) {
        return this.questionService.getById(questionId);
    }

    @Override
    @GetMapping("/question_submit/get/{id}")
    public QuestionSubmit getByld(Long questionSubmitId) {
        return this.questionSubmitService.getById(questionSubmitId);
    }

    @Override
    @PostMapping("/question_submit/update")
    public boolean updateByld(QuestionSubmit questionSubmitUpdate) {
        return this.questionSubmitService.updateById(questionSubmitUpdate);
    }
}
