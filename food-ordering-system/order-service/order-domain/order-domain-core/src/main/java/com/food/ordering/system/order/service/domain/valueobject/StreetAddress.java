package com.food.ordering.system.order.service.domain.valueobject;

import java.util.UUID;

public class StreetAddress {
    /**
     * 영속성을 위해 필요, equals & hashCode 에서는 제외
     */
    private final UUID id;
    private final String street;
    private final String postalCode;
    private final String city;

    public StreetAddress(UUID id, String street, String postalCode, String city) {
        this.id = id;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
    }

    public UUID getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StreetAddress that = (StreetAddress) o;

        if (!street.equals(that.street)) return false;
        if (!postalCode.equals(that.postalCode)) return false;
        return city.equals(that.city);
    }

    @Override
    public int hashCode() {
        int result = street.hashCode();
        result = 31 * result + postalCode.hashCode();
        result = 31 * result + city.hashCode();
        return result;
    }
}
