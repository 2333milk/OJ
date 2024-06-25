package com.jueye.nchuojbackendmodel.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.jueye.nchuojbackendmodel.model.entity.Exam;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 考试
 * @TableName exam
 */
@Data
public class ExamVO implements Serializable {
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
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 状态 1 进行中/0 未开始/2 已结束
     */
    private Integer status;

    /**
     * 考试题目关联Id列表（json 数组）
     */
    private List<Long> examQuestionIds;

    /**
     * 考试题目列表
     */
    private List<ExamQuestionVO> examQuestions;

    /**
     * 创建用户 id
     */
    private UserVO user;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 对象转包装类
     *
     * @param exam
     * @return
     */
    public static ExamVO objToVo(Exam exam) {
        if (exam == null) {
            return null;
        }
        ExamVO examVO = new ExamVO();
        BeanUtils.copyProperties(exam, examVO);
        examVO.setExamQuestionIds(JSONUtil.toList(exam.getExamQuestionIds(), Long.class));
        return examVO;
    }
}