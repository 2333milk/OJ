package com.nchu.nchuojsendbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcuteCodeRequest {

    /**
     * 输入列表
     */
    private List<String> inputlist;
    /**
     * 提交代码
     */
    private String code;
    /**
     * 代码语言类型
     */
    private String language;

}
