package com.food.ordering.system.domain.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Money {

    private final BigDecimal amount;

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public boolean isGreaterThanZero() {
        return this.amount != null && this.amount.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isGreaterThan(Money money) {
        return this.amount != null && this.amount.compareTo(money.getAmount()) > 0;
    }

    public Money add(Money money) {
        return new Money(setScale(this.amount.add(money.getAmount())));
    }

    public Money subtract(Money money) {
        return new Money(setScale(this.amount.subtract(money.getAmount())));
    }

    public Money multiply(Money money) {
        return new Money(setScale(this.amount.multiply(money.getAmount())));
    }

    public Money multiply(int multiplier) {
        return new Money(setScale(this.amount.multiply(new BigDecimal(multiplier))));
    }

    /**
     * 십진수(1/3)에서와 마찬가지로 이진법(7/10 = 111/1010)에서도 정확하게 나누지 못하여 오차가 발생한다.
     * 이러한 오차를 최소화하기 위해 계산 시 setScale 메서드를 사용한다.
     * ref. https://medium.com/@ali.gelenler/using-fractional-numbers-in-java-d070f1b9b8c5
     */
    private BigDecimal setScale(BigDecimal input) {
        return input.setScale(2, RoundingMode.HALF_EVEN);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Money money = (Money) o;

        return amount.equals(money.amount);
    }

    @Override
    public int hashCode() {
        return amount.hashCode();
    }
}
