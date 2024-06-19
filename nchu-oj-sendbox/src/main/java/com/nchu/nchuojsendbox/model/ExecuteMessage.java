package com.nchu.nchuojsendbox.model;

import lombok.Data;

/**
 * 进程执行信息
 */
@Data
public class ExecuteMessage {
    /**
     * 退出码
     */
    private Integer exitValue;
    /**
     * 执行正确信息
     */
    private String message;
    /**
     * 执行错误信息
     */
    private String errorMessage;

    /**
     * 执行时间
     */
    private Long time;

    /**
     * 执行空间
     */
    private Long memory;
}
