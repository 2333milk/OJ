package com.jueye.nchuojbackendmodel.model.dto.exam;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * 考试题目关联表
 * @TableName exam_question
 */
@Data
public class ExamQuestionAddRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 考试 id
     */
    private Long examId;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 题目分数
     */
    private Integer score;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}