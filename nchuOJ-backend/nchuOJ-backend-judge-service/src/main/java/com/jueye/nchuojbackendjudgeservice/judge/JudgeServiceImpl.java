package com.jueye.nchuojbackendjudgeservice.judge;

import cn.hutool.json.JSONUtil;
import com.jueye.nchuojbackendcommon.common.ErrorCode;
import com.jueye.nchuojbackendcommon.exception.BusinessException;
import com.jueye.nchuojbackendjudgeservice.judge.codeSendbox.CodeSendboxProxy;
import com.jueye.nchuojbackendjudgeservice.judge.strategy.JudgeContext;
import com.jueye.nchuojbackendjudgeservice.judge.strategy.JudgeManager;
import com.jueye.nchuojbackendmodel.model.dto.question.JudgeCase;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeRequest;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.ExcuteCodeResponse;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.JudgeInfo;
import com.jueye.nchuojbackendmodel.model.entity.Question;
import com.jueye.nchuojbackendmodel.model.entity.QuestionSubmit;
import com.jueye.nchuojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.jueye.nchuojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class JudgeServiceImpl implements JudgeService {
    @Value("${codeSendbox.type}")
    private String type;
    @Resource
    private QuestionFeignClient questionFeignClient;
    @Resource
    private JudgeManager judgeManager;

    @Resource
    private CodeSendboxProxy codeSendboxProxy;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        QuestionSubmit questionSubmit = questionFeignClient.getByld(questionSubmitId);
        if(questionSubmit==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"提交信息不存在");
        }
        Long questionid = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getById(questionid);
        if(question==null){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"题目信息不存在");
        }
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        questionSubmitUpdate.setId(questionSubmitId);
        boolean update = questionFeignClient.updateByld(questionSubmitUpdate);
        if(!update){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目更新状态错误");
        }

        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();
        //获取输入用例
        String judgeCaseStr= question.getJudgeCase();
        List<JudgeCase> judgeCases =  JSONUtil.toList(judgeCaseStr,JudgeCase.class);
        List<String> inputList = judgeCases.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExcuteCodeRequest excuteCodeRequest = ExcuteCodeRequest.builder()
                .code(code)
                .inputlist(inputList)
                .language(language)
                .build();
        ExcuteCodeResponse excuteCodeResponse = codeSendboxProxy.excuteCode(excuteCodeRequest,type);

        //根据执行结果，设置判题信息
        List<String> outputlist = excuteCodeResponse.getOutputlist();
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(excuteCodeResponse.getJudgeInfo());
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputlist);
        judgeContext.setJudgeCaseList(judgeCases);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        //修改数据库判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        update = questionFeignClient.updateByld(questionSubmitUpdate);
        if(!update){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"题目状态更新错误");
        }
        QuestionSubmit questionSubmitResult = questionFeignClient.getByld(questionSubmitId);
        return questionSubmitResult;
    }
}
