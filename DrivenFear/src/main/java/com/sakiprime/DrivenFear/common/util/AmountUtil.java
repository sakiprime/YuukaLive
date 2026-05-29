package com.sakiprime.DrivenFear.util;
import java.math.BigDecimal;
import java.math.RoundingMode;
public class AmountUtil {
    public static BigDecimal fenToYuan(Integer fen) {
        return new BigDecimal(fen)
                .divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
    }

}
