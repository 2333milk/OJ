package com.jueye.nchuojbackendjudgeservice.judge.strategy;

import com.jueye.nchuojbackendmodel.model.dto.question.JudgeCase;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.JudgeInfo;
import com.jueye.nchuojbackendmodel.model.entity.Question;
import com.jueye.nchuojbackendmodel.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * 上下文(用于定义策略中传递的参数)
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;
}
