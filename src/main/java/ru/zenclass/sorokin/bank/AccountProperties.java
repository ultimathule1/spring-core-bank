package ru.zenclass.sorokin.bank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AccountProperties {
    private final BigDecimal defaultAmount;
    private final double transferCommission;

    public AccountProperties(@Value("${account.default-amount}") BigDecimal defaultAmount,
                             @Value("${account.transfer-commission}") double transferCommission) {
        this.defaultAmount = defaultAmount;
        this.transferCommission = transferCommission;
    }

    public BigDecimal getDefaultAmount() {
        return defaultAmount;
    }

    public double getTransferCommission() {
        return transferCommission;
    }
}
