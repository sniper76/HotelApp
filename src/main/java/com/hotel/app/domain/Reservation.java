package com.hotel.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Reservation.
 */
@Entity
@Table(name = "reservation")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Reservation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "check_in_date")
    private Instant checkInDate;

    @Column(name = "check_out_date")
    private Instant checkOutDate;

    @Column(name = "creator")
    private Long creator;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updater")
    private Long updater;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "roomType", "hotel" }, allowSetters = true)
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "reservations" }, allowSetters = true)
    private Voucher voucher;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Reservation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getCheckInDate() {
        return this.checkInDate;
    }

    public Reservation checkInDate(Instant checkInDate) {
        this.setCheckInDate(checkInDate);
        return this;
    }

    public void setCheckInDate(Instant checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Instant getCheckOutDate() {
        return this.checkOutDate;
    }

    public Reservation checkOutDate(Instant checkOutDate) {
        this.setCheckOutDate(checkOutDate);
        return this;
    }

    public void setCheckOutDate(Instant checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public Long getCreator() {
        return this.creator;
    }

    public Reservation creator(Long creator) {
        this.setCreator(creator);
        return this;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Reservation createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdater() {
        return this.updater;
    }

    public Reservation updater(Long updater) {
        this.setUpdater(updater);
        return this;
    }

    public void setUpdater(Long updater) {
        this.updater = updater;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Reservation updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Room getRoom() {
        return this.room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Reservation room(Room room) {
        this.setRoom(room);
        return this;
    }

    public Voucher getVoucher() {
        return this.voucher;
    }

    public void setVoucher(Voucher voucher) {
        this.voucher = voucher;
    }

    public Reservation voucher(Voucher voucher) {
        this.setVoucher(voucher);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reservation)) {
            return false;
        }
        return getId() != null && getId().equals(((Reservation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reservation{" +
            "id=" + getId() +
            ", checkInDate='" + getCheckInDate() + "'" +
            ", checkOutDate='" + getCheckOutDate() + "'" +
            ", creator=" + getCreator() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updater=" + getUpdater() +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
