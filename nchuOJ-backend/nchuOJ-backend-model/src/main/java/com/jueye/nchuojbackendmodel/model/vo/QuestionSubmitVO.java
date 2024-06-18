package com.jueye.nchuojbackendmodel.model.vo;

import cn.hutool.json.JSONUtil;
import com.jueye.nchuojbackendmodel.model.dto.sandbox.JudgeInfo;
import com.jueye.nchuojbackendmodel.model.entity.QuestionSubmit;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 问题提交封装类
 * @TableName question
 */
@Data
public class QuestionSubmitVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 提交代码语言类型
     */
    private String language;

    /**
     * 提交代码
     */
    private String code;

    /**
     * 判题信息(json对象)
     */
    private JudgeInfo judgeInfo;

    /**
     * 判题状态
     */
    private Integer status;

    /**
     * 题目 id
     */
    private Long questionId;
    /**
     * 创建时间
     */
    private Date createTime;

    private UserVO userVO;

    private QuestionVO questionVO;
    


    private static final long serialVersionUID = 1L;

    /**
     * 包装类转对象
     *
     * @param questionSubmitVO
     * @return
     */
    public static QuestionSubmit voToObj(QuestionSubmitVO questionSubmitVO) {
        if (questionSubmitVO == null) {
            return null;
        }
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitVO, questionSubmit);
        JudgeInfo voJudegeConfig = questionSubmitVO.getJudgeInfo();
        if (voJudegeConfig!=null){
            questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(voJudegeConfig));
        }
        return questionSubmit;
    }

    /**
     * 对象转包装类
     *
     * @param questionSubmit
     * @return
     */
    public static QuestionSubmitVO objToVo(QuestionSubmit questionSubmit) {
        if (questionSubmit == null) {
            return null;
        }
        QuestionSubmitVO questionSubmitVO = new QuestionSubmitVO();
        BeanUtils.copyProperties(questionSubmit, questionSubmitVO);
        questionSubmitVO.setJudgeInfo(JSONUtil.toBean(questionSubmit.getJudgeInfo(),JudgeInfo.class));
        return questionSubmitVO;
    }
}