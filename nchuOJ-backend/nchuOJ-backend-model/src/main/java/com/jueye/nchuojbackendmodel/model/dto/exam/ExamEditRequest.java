package com.jueye.nchuojbackendmodel.model.dto.exam;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jueye.nchuojbackendmodel.model.vo.ExamQuestionVO;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 考试
 * @TableName exam
 */
@Data
public class ExamEditRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 开始时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 状态 1 进行中/0 未开始/2 已结束
     */
    private Integer status;

    /**
     * 考试题目关联列表（json 数组）
     */
    private String examQuestionIds;

    /**
     * 考试题目关联列表（json 数组）
     */
    private List<ExamQuestionVO> examQuestionList;

    /**
     * 创建用户 id
     */
    private Long userId;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}