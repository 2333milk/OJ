package com.jueye.nchuojbackendmodel.model.dto.exam;

import com.baomidou.mybatisplus.annotation.TableField;
import com.jueye.nchuojbackendcommon.common.PageRequest;
import lombok.Data;

import java.io.Serializable;

/**
 * 考试结果
 * @TableName exam_result
 */
@Data
public class ExamResultQueryRequest extends PageRequest implements Serializable {

    /**
     * 考试 id
     */
    private Long examId;

    /**
     * 用户 id
     */
    private Long userId;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}