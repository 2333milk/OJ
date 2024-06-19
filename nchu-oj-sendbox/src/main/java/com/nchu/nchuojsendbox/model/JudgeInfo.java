package com.nchu.nchuojsendbox.model;

import lombok.Data;

/**
 * 题目配置
 */
@Data
public class JudgeInfo {

    /**
     *时间限制 ms
     */
    private String message;
    /**
     * 内存限制 kb
     */
    private Long memory;
    /**
     * 堆栈限制 
     */
    private Long time;
}
