package com.jueye.nchuojbackendmodel.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 更新请求
 *
 
 */
@Data
public class QuestionUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private List<String> tags;

    /**
     * 答案
     */
    private String answer;

    /**
     * 提交数
     */
    private Integer submitNum;

    /**
     * 正确数
     */
    private Integer acceptedNum;

    /**
     * 判题用例 json数组
     */
    private List<JudgeCase> judgeCase;

    /**
     * 判题配置 json数组
     */
    private JudgeConfig judgeConfig;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;


    private static final long serialVersionUID = 1L;
}