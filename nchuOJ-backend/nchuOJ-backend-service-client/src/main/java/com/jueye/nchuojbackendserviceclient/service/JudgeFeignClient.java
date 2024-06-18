package com.jueye.nchuojbackendserviceclient.service;


import com.jueye.nchuojbackendmodel.model.entity.QuestionSubmit;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 判题服务
 */
@FeignClient(name = "nchuOJ-backend-judge-service", path ="/api/judge/inner")
public interface JudgeFeignClient {
    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    @PostMapping("/doJudge")
     QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId);
}
