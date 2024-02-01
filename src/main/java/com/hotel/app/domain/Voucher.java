package com.hotel.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A Voucher.
 */
@Entity
@Table(name = "voucher")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Voucher implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "price", nullable = false)
    private Integer price;

    @NotNull
    @Column(name = "sale_price", nullable = false)
    private Integer salePrice;

    @Column(name = "creator")
    private Long creator;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updater")
    private Long updater;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "voucher")
    @JsonIgnoreProperties(value = { "room", "voucher" }, allowSetters = true)
    private Set<Reservation> reservations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Voucher id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPrice() {
        return this.price;
    }

    public Voucher price(Integer price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getSalePrice() {
        return this.salePrice;
    }

    public Voucher salePrice(Integer salePrice) {
        this.setSalePrice(salePrice);
        return this;
    }

    public void setSalePrice(Integer salePrice) {
        this.salePrice = salePrice;
    }

    public Long getCreator() {
        return this.creator;
    }

    public Voucher creator(Long creator) {
        this.setCreator(creator);
        return this;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Voucher createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdater() {
        return this.updater;
    }

    public Voucher updater(Long updater) {
        this.setUpdater(updater);
        return this;
    }

    public void setUpdater(Long updater) {
        this.updater = updater;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Voucher updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Set<Reservation> getReservations() {
        return this.reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        if (this.reservations != null) {
            this.reservations.forEach(i -> i.setVoucher(null));
        }
        if (reservations != null) {
            reservations.forEach(i -> i.setVoucher(this));
        }
        this.reservations = reservations;
    }

    public Voucher reservations(Set<Reservation> reservations) {
        this.setReservations(reservations);
        return this;
    }

    public Voucher addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        reservation.setVoucher(this);
        return this;
    }

    public Voucher removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
        reservation.setVoucher(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Voucher)) {
            return false;
        }
        return getId() != null && getId().equals(((Voucher) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Voucher{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", salePrice=" + getSalePrice() +
            ", creator=" + getCreator() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updater=" + getUpdater() +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
