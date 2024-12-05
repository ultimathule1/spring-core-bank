package ru.zenclass.sorokin.bank.models;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Account {
    private final long id;
    private final long userId;
    private BigDecimal moneyAmount;

    public Account(long id, long userId, BigDecimal moneyAmount) {
        this.id = id;
        this.userId = userId;
        this.moneyAmount = moneyAmount;
    }

    public long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }

    public BigDecimal getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(BigDecimal moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + userId +
                ", moneyAmount=" + moneyAmount.setScale(2, RoundingMode.HALF_UP) +
                '}';
    }
}
