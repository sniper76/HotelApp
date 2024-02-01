package com.hotel.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Room.
 */
@Entity
@Table(name = "room")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "no", nullable = false)
    private String no;

    @Column(name = "price")
    private Integer price;

    @Column(name = "promo_price")
    private Integer promoPrice;

    @Column(name = "creator")
    private Long creator;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updater")
    private Long updater;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @JsonIgnoreProperties(value = { "room" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private RoomType roomType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Hotel hotel;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Room id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNo() {
        return this.no;
    }

    public Room no(String no) {
        this.setNo(no);
        return this;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Integer getPrice() {
        return this.price;
    }

    public Room price(Integer price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPromoPrice() {
        return this.promoPrice;
    }

    public Room promoPrice(Integer promoPrice) {
        this.setPromoPrice(promoPrice);
        return this;
    }

    public void setPromoPrice(Integer promoPrice) {
        this.promoPrice = promoPrice;
    }

    public Long getCreator() {
        return this.creator;
    }

    public Room creator(Long creator) {
        this.setCreator(creator);
        return this;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Room createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdater() {
        return this.updater;
    }

    public Room updater(Long updater) {
        this.setUpdater(updater);
        return this;
    }

    public void setUpdater(Long updater) {
        this.updater = updater;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Room updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public RoomType getRoomType() {
        return this.roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public Room roomType(RoomType roomType) {
        this.setRoomType(roomType);
        return this;
    }

    public Hotel getHotel() {
        return this.hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Room hotel(Hotel hotel) {
        this.setHotel(hotel);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        return getId() != null && getId().equals(((Room) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Room{" +
            "id=" + getId() +
            ", no='" + getNo() + "'" +
            ", price=" + getPrice() +
            ", promoPrice=" + getPromoPrice() +
            ", creator=" + getCreator() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updater=" + getUpdater() +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
