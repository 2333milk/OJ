package com.jueye.nchuojbackendmodel.model.enums;

import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户角色枚举
 *
 
 */
public enum JudgeInfoMessageEnum {
    ACCEPTED("成功", "accepted"),
    WRONG_ANSWER("答案错误", "wrong_answer"),
    COMPILE_ERROR("编译错误", "compile_error"),
    MEMORY_LIMIT_EXCEEDED("内存溢出","memory_limit_exceeded"),
    TIME_LIMIT_EXCEEDED("超时","time_limit_exceeded"),
    PRESENTATION_ERROR("展示错误","presentation_error"),
    OUTPUT_LIMIT_EXCEEDED("输出溢出","output_limit_exceeded"),
    WAITING("等待中","waiting"),
    DANGEROUS_OPERATION("危险操作","dangerous_operation"),
    RUNTIME_ERROR("运行错误","runtime_error"),
    SYSTEM_ERROR("系统错误","system_error");
    private final String text;

    private final String value;

    JudgeInfoMessageEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static JudgeInfoMessageEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JudgeInfoMessageEnum anEnum : JudgeInfoMessageEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
