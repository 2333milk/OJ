package com.jueye.nchuojbackendserviceclient.service;

import com.jueye.nchuojbackendmodel.model.entity.Question;
import com.jueye.nchuojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
* @author ASUS
* @description 针对表【question(问题)】的数据库操作Service
* @createDate 2024-03-05 16:29:44
*/
@FeignClient(name = "nchuOJ-backend-question-service",path = "/api/question/inner")
public interface QuestionFeignClient{
    /**
     * 通过id查询题目
     * @param questionId
     * @return
     */
    @GetMapping("/get/{id}")
    Question getById(@RequestParam("questionId") Long questionId);

    /**
     * 通过id查询题目提交记录
     * @param questionSubmitId
     * @return
     */
    @GetMapping("/question_submit/get/{id}")
    QuestionSubmit getByld(@RequestParam("questionSubmitId") Long questionSubmitId);

    /**
     * 通过id修改提交记录
     * @param questionSubmitUpdate
     * @return
     */
    @PostMapping("/question_submit/update")
    boolean updateByld(@RequestBody QuestionSubmit questionSubmitUpdate);

}
