package com.jueye.nchuojbackendquestionservice.controller.inner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jueye.nchuojbackendmodel.model.entity.Question;
import com.jueye.nchuojbackendmodel.model.entity.QuestionSubmit;
import com.jueye.nchuojbackendmodel.model.vo.QuestionVO;
import com.jueye.nchuojbackendquestionservice.service.QuestionService;
import com.jueye.nchuojbackendquestionservice.service.QuestionSubmitService;
import com.jueye.nchuojbackendserviceclient.service.QuestionFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * 用户接口
 *
 
 */
@RestController
@RequestMapping("/inner")
@Slf4j
public class QuestionInnerController implements QuestionFeignClient {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;


    @Override
    @GetMapping("/get/id")
    public Question getById(@RequestParam("questionId") Long questionId) {
        return this.questionService.getById(questionId);
    }

    @Override
    @GetMapping("/question_submit/get/id")
    public QuestionSubmit getByld(@RequestParam("questionSubmitId") Long questionSubmitId) {
        return this.questionSubmitService.getById(questionSubmitId);
    }

    @Override
    @PostMapping("/question_submit/update")
    public boolean updateByld(@RequestBody QuestionSubmit questionSubmitUpdate) {
        return this.questionSubmitService.updateById(questionSubmitUpdate);
    }

    @Override
    @PostMapping("/get/vo")
    public QuestionVO getQuestionVO(@RequestBody Question question) {
        return this.questionService.getQuestionVO(question);
    }

    @Override
    @PostMapping("/question_submit/list")
    public List<QuestionSubmit> list(@RequestBody QueryWrapper<QuestionSubmit> questionSubmitQueryWrapper) {
        return this.questionSubmitService.list(questionSubmitQueryWrapper);
    }
}
