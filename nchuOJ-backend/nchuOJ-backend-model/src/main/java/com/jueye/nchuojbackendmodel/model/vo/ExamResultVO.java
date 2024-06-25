package com.jueye.nchuojbackendmodel.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * 考试结果
 * @TableName exam_result
 */
@Data
public class ExamResultVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 考试 id
     */
    private ExamVO exam;

    /**
     * 用户 id
     */
    private UserVO user;

    /**
     * 分数
     */
    private Integer score;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}