package com.hotel.app.service;

import com.hotel.app.domain.Room;
import com.hotel.app.repository.RoomRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.hotel.app.domain.Room}.
 */
@Service
@Transactional
public class RoomService {

    private final Logger log = LoggerFactory.getLogger(RoomService.class);

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * Save a room.
     *
     * @param room the entity to save.
     * @return the persisted entity.
     */
    public Room save(Room room) {
        log.debug("Request to save Room : {}", room);
        return roomRepository.save(room);
    }

    /**
     * Update a room.
     *
     * @param room the entity to save.
     * @return the persisted entity.
     */
    public Room update(Room room) {
        log.debug("Request to update Room : {}", room);
        return roomRepository.save(room);
    }

    /**
     * Partially update a room.
     *
     * @param room the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Room> partialUpdate(Room room) {
        log.debug("Request to partially update Room : {}", room);

        return roomRepository
            .findById(room.getId())
            .map(existingRoom -> {
                if (room.getNo() != null) {
                    existingRoom.setNo(room.getNo());
                }
                if (room.getPrice() != null) {
                    existingRoom.setPrice(room.getPrice());
                }
                if (room.getPromoPrice() != null) {
                    existingRoom.setPromoPrice(room.getPromoPrice());
                }
                if (room.getCreator() != null) {
                    existingRoom.setCreator(room.getCreator());
                }
                if (room.getCreatedAt() != null) {
                    existingRoom.setCreatedAt(room.getCreatedAt());
                }
                if (room.getUpdater() != null) {
                    existingRoom.setUpdater(room.getUpdater());
                }
                if (room.getUpdatedAt() != null) {
                    existingRoom.setUpdatedAt(room.getUpdatedAt());
                }

                return existingRoom;
            })
            .map(roomRepository::save);
    }

    /**
     * Get all the rooms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Room> findAll(Pageable pageable) {
        log.debug("Request to get all Rooms");
        return roomRepository.findAll(pageable);
    }

    /**
     * Get all the rooms with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Room> findAllWithEagerRelationships(Pageable pageable) {
        return roomRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one room by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Room> findOne(Long id) {
        log.debug("Request to get Room : {}", id);
        return roomRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the room by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Room : {}", id);
        roomRepository.deleteById(id);
    }
}
