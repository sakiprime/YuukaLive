package com.sakiprime.yuukalikejava.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("day_record")
public class DayRecordEntity {
    @TableId
    private String dayDate;
    private float dayTotalExpenditure;
    private float dayTotalIncome;
    private float dayBalance;

    // 日分类统计
    private float breakfast;
    private float lunch;
    private float dinner;
    private float snacks;
    private float entertainment;
    private float otherTransportation;
    private float VariousIncome;

    // 日状态
    private String dayLastUpdateTime;
    private int dayTotalRecords;
}
