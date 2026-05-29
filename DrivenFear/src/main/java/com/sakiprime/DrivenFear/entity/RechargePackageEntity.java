package com.sakiprime.DrivenFear.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("top_up_package")
public class RechargePackageEntity {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Integer sortId;
    private String packageName;
    private String description;
    private Integer tokensAmount;//到账的点数
    private Integer originalPrice;//单位为分
    private Integer discountedPrice;
    private String imageUrl;
    private Boolean isOnSale;
}
