package com.xwl.prisonbreak.common.util.lfasr;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author xwl
 * @date 2020-04-17 10:30
 * @description 转写结果输出实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LfasrResDTO implements Serializable {
    private static final long serialVersionUID = 7419419117504259061L;

    /**
     * 句子相对于本音频的起始时间，单位为ms
     */
    private String bg;

    /**
     * 句子相对于本音频的终止时间，单位为ms
     */
    private String ed;

    /**
     * 句子内容
     */
    private String onebest;

    /**
     * 说话人编号，从1开始，未开启说话人分离时speaker都为0
     */
    private String speaker;
}
