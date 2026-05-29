package com.sakiprime.yuukalikejava.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("month_record")
public class MonthRecordEntity {
    @TableId
    private String monthDate;
    private float monthTotalExpenditure;
    private float monthTotalIncome;
    private float monthBalance;

    // 月分类统计
    private float monthBreakfast;
    private float monthLunch;
    private float monthDinner;
    private float monthSnacks;
    private float monthEntertainment;
    private float monthOtherTransportation;
    private float monthVariousIncome;

    // 月状态
    private String monthLastUpdateTime;
}
