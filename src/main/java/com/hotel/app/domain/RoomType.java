package com.hotel.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A RoomType.
 */
@Entity
@Table(name = "room_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RoomType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

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

    @JsonIgnoreProperties(value = { "roomType", "hotel" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "roomType")
    private Room room;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RoomType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public RoomType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public RoomType description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreator() {
        return this.creator;
    }

    public RoomType creator(Long creator) {
        this.setCreator(creator);
        return this;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public RoomType createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdater() {
        return this.updater;
    }

    public RoomType updater(Long updater) {
        this.setUpdater(updater);
        return this;
    }

    public void setUpdater(Long updater) {
        this.updater = updater;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public RoomType updatedAt(Instant updatedAt) {
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
        if (this.room != null) {
            this.room.setRoomType(null);
        }
        if (room != null) {
            room.setRoomType(this);
        }
        this.room = room;
    }

    public RoomType room(Room room) {
        this.setRoom(room);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoomType)) {
            return false;
        }
        return getId() != null && getId().equals(((RoomType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoomType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", creator=" + getCreator() +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updater=" + getUpdater() +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
