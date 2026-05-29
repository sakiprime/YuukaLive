package com.sakiprime.DrivenFear.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AICallRequestEntity {
    private Long orderId;
    private String userId;
    private String prompt;
    private String AIModel;//记得校验合法性。
    private String taskType;
    private String params;
    private String createTime;
    private String updateTime;
}
