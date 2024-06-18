package com.jueye.nchuojbackendmodel.model.dto.questionsubmit;

import com.jueye.nchuojbackendcommon.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 帖子点赞请求
 *
 
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {

    /**
     * 提交代码语言类型
     */
    private String language;

    /**
     * 提交代码
     */
    private Integer status;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 用户 id
     */
    private Long userid;

    private static final long serialVersionUID = 1L;
}