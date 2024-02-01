package com.hotel.app.domain;

import com.hotel.app.domain.enumeration.HotelType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * Product sold by the Online store
 */
@Schema(description = "Product sold by the Online store")
@Entity
@Table(name = "hotel")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Hotel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private HotelType type;

    @Column(name = "location")
    private String location;

    @Column(name = "description")
    private String description;

    @Column(name = "creator")
    private Long creator;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updater")
    private Long updater;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Hotel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Hotel name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HotelType getType() {
        return this.type;
    }

    public Hotel type(HotelType type) {
        this.setType(type);
        return this;
    }

    public void setType(HotelType type) {
        this.type = type;
    }

    public String getLocation() {
        return this.location;
    }

    public Hotel location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return this.description;
    }

    public Hotel description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreator() {
        return this.creator;
    }

    public Hotel creator(Long creator) {
        this.setCreator(creator);
        return this;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Hotel createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdater() {
        return this.updater;
    }

    public Hotel updater(Long updater) {
        this.setUpdater(updater);
        return this;
    }

    public void setUpdater(Long updater) {
        this.updater = updater;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Hotel updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Hotel user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hotel)) {
            return false;
        }
        return getId() != null && getId().equals(((Hotel) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Hotel{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", location='" + getLocation() + "'" +
            ", description='" + getDescription() + "'" +
            ", creator=" + getCreator() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updater=" + getUpdater() +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
