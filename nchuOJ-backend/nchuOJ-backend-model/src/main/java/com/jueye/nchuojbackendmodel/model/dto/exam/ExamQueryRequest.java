package com.jueye.nchuojbackendmodel.model.dto.exam;

import com.baomidou.mybatisplus.annotation.TableField;
import com.jueye.nchuojbackendcommon.common.PageRequest;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 考试
 * @TableName exam
 */
@Data
public class ExamQueryRequest extends PageRequest implements Serializable {
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
    @DateTimeFormat(fallbackPatterns = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(fallbackPatterns = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 状态 1 进行中/0 未开始/2 已结束
     */
    private Integer status;

    /**
     * 创建用户 id
     */
    private Long userId;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}