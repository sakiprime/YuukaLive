package com.sakiprime.DrivenFear.service;


import com.sakiprime.DrivenFear.entity.PurchaseTopUpPackageEntity;
import com.sakiprime.DrivenFear.entity.UpdateTopUpPackageEntity;

import java.util.Map;

public interface AlipayService {
    UpdateTopUpPackageEntity getPackageInfo(Long id);
    PurchaseTopUpPackageEntity getPurchaseInfo(Long orderId);
    boolean updatePurchaseInfo(PurchaseTopUpPackageEntity purchaseTopUpPackageEntity);
    boolean handleAlipayNotify(Map<String, String> params);
}
