
package com.jueye.nchuojbackendmodel.model.dto.sandbox;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcuteCodeResponse {
    /**
     * 输出列表
     */
    private List<String> outputlist;
    /**
     * 接口信息
     */
    private String message;
    /**
     *执行状态
     */
    private Integer status;
    /**
     *判题信息
     */
    private JudgeInfo judgeInfo;

}
