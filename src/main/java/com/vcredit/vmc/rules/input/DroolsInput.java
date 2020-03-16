package com.vcredit.vmc.rules.input;

import lombok.Data;

import java.util.List;

/**
 * @author wjp
 * @date 2020/3/10 15:16
 */
@Data
public class DroolsInput {
    private String oldTaskLabel;

    private String taskNo;

    private String eventType;

    private String taskLabel;

    private List<Long> planIds;
}
