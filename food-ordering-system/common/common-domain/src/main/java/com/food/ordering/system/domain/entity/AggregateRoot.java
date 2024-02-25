package com.food.ordering.system.domain.entity;

/**
 * AggregateRoot Marker Class
 * 엔티티인지 애그리거트 루트인지 알 수 있다.
 */
public abstract class AggregateRoot<ID> extends BaseEntity<ID> {
}
