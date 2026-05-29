package com.sakiprime.yuukalikejava.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("item_record")
public class TempRecordEntity {

    private Integer itemId;
    private String type;
    private float count;
    private String subclass;
    @TableId
    private String tempDate;
}
