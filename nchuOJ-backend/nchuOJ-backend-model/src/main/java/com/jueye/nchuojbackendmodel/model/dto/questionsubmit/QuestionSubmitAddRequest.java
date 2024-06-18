package com.jueye.nchuojbackendmodel.model.dto.questionsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 帖子点赞请求
 *
 
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {

    /**
     * 提交代码语言类型
     */
    private String language;

    /**
     * 提交代码
     */
    private String code;

    /**
     * 题目 id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}