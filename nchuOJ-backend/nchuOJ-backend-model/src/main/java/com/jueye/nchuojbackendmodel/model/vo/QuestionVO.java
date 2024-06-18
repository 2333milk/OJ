package com.jueye.nchuojbackendmodel.model.vo;

import cn.hutool.json.JSONUtil;
import com.jueye.nchuojbackendmodel.model.dto.question.JudgeCase;
import com.jueye.nchuojbackendmodel.model.dto.question.JudgeConfig;
import com.jueye.nchuojbackendmodel.model.entity.Question;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 问题
 * @TableName question
 */
@Data
public class QuestionVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;
    /**
     * 答案
     */
    private String answer;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;


    /**
     * 提交数
     */
    private Integer submitNum;

    /**
     * 正确数
     */
    private Integer acceptedNum;

    /**
     * 判题用例 json数组
     */
    private List<JudgeCase> judgeCase;


    /**
     * 判题配置 json数组
     */
    private JudgeConfig judgeConfig;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建题目人信息
     */
    private UserVO userVO;


    private static final long serialVersionUID = 1L;

    /**
     * 包装类转对象
     *
     * @param questionVO
     * @return
     */
    public static Question voToObj(QuestionVO questionVO) {
        if (questionVO == null) {
            return null;
        }
        Question question = new Question();
        BeanUtils.copyProperties(questionVO, question);
        List<String> tagList = questionVO.getTags();
        if(tagList!=null){
            question.setTags(JSONUtil.toJsonStr(tagList));
        }
        JudgeConfig voJudegeConfig = questionVO.getJudgeConfig();
        if (voJudegeConfig!=null){
            question.setJudgeConfig(JSONUtil.toJsonStr(voJudegeConfig));
        }
        return question;
    }

    /**
     * 对象转包装类
     *
     * @param question
     * @return
     */
    public static QuestionVO objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionVO questionVO = new QuestionVO();
        BeanUtils.copyProperties(question, questionVO);
        questionVO.setTags(JSONUtil.toList(question.getTags(), String.class));
        questionVO.setJudgeCase(JSONUtil.toList(question.getJudgeCase(),JudgeCase.class));
        questionVO.setJudgeConfig(JSONUtil.toBean(question.getJudgeConfig(),JudgeConfig.class));
        return questionVO;
    }
}