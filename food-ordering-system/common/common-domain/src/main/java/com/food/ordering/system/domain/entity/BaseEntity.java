package com.food.ordering.system.domain.entity;

/**
 * BaseEntity는 엔티티에 아래와 같은 제약을 준다.
 * 엔티티는 ID를 가져야 하고 ID를 사용해서 동등성(equality)를 판단해야 한다.
 */
public abstract class BaseEntity<ID> {
    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseEntity<?> that = (BaseEntity<?>) o;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
