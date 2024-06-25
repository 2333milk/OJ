package com.jueye.nchuojbackendmodel.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * 考试题目关联表
 * @TableName exam_question
 */
@Data
public class ExamQuestionVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 考试 id
     */
    private Long examId;

    /**
     * 题目
     */
    private QuestionVO question;

    /**
     * 题目分数
     */
    private Integer score;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}