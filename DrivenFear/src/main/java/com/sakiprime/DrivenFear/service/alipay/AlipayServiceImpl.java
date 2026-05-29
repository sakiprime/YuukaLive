package com.sakiprime.DrivenFear.service;

import com.sakiprime.DrivenFear.entity.PurchaseTopUpPackageEntity;
import com.sakiprime.DrivenFear.entity.UpdateTopUpPackageEntity;
import com.sakiprime.DrivenFear.mapper.PurchasePackageMapper;
import com.sakiprime.DrivenFear.mapper.UpdatePackageMapper;
import com.sakiprime.DrivenFear.util.AmountUtil;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AlipayServiceImpl implements AlipayService {
    private final UpdatePackageMapper updatePackageMapper;
    private final PurchasePackageMapper purchasePackageMapper;
    public AlipayServiceImpl(UpdatePackageMapper updatePackageMapper, PurchasePackageMapper purchasePackageMapper) {
        this.updatePackageMapper = updatePackageMapper;
        this.purchasePackageMapper = purchasePackageMapper;
    }
    @Override
    public UpdateTopUpPackageEntity getPackageInfo(Long id){

        return  updatePackageMapper.selectById(id);
    }

    @Override
    public PurchaseTopUpPackageEntity getPurchaseInfo(Long orderId){

        return purchasePackageMapper.selectById(orderId);
    }

    @Override
    public boolean updatePurchaseInfo(PurchaseTopUpPackageEntity purchaseTopUpPackageEntity) {

        return purchasePackageMapper.updateById(purchaseTopUpPackageEntity) > 0;
    }

    @Override
    public boolean handleAlipayNotify(Map<String, String> params){
        String tradeStatus = params.get("trade_status");
        String outTradeNoStr = params.get("out_trade_no");
        String gmtCreateTime = params.get("gmt_create");
        String notifyTime = params.get("notify_time");
        if (outTradeNoStr == null || outTradeNoStr.isBlank()) {
            return false;
        }
        Long outTradeNo = Long.parseLong(outTradeNoStr);//转成雪花Long类型
        PurchaseTopUpPackageEntity purchaseInfo = getPurchaseInfo(outTradeNo);
        if (purchaseInfo == null) {
            return false;
        }
        purchaseInfo.setTradeStatus(tradeStatus);//更新基础状态
        purchaseInfo.setCreateTime(gmtCreateTime);
        purchaseInfo.setUpdateTime(notifyTime);
        if (!updatePurchaseInfo(purchaseInfo)) {
            return false;
        }
        if (tradeStatus.equals("TRADE_SUCCESS")) {
            String paymentAmount = params.get("total_amount");
            String paymentTime = params.get("gmt_payment");
            purchaseInfo.setPaymentTime(paymentTime);

            //获取套餐实际金额
            UpdateTopUpPackageEntity packageInfo = getPackageInfo(purchaseInfo.getId());
            String localAmount = AmountUtil.fenToYuan(packageInfo.getDiscountedPrice()).toString();

            boolean isValid = localAmount.equals(paymentAmount);
            if (!isValid) {
                return false; // 金额不一致，直接拒绝
            }

            purchaseInfo.setPaid(true);
            if (!updatePurchaseInfo(purchaseInfo)) {//利用支付宝回调进行重试
                return false;
            }
        }//支付成功

        return true;
    }
}
