package com.food.ordering.system.domain.valueobject;

/**
 * ID 값 객체에 대한 추상 클래스
 * ID는 여러 도메인 간에서 사용되기 때문에 공통 도메인에 넣어야 한다.
 */
public abstract class BaseId<T> {
    private final T value;

    protected BaseId(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseId<?> baseId = (BaseId<?>) o;

        return value.equals(baseId.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
