package com.hotel.app.service;

import com.hotel.app.domain.RoomType;
import com.hotel.app.repository.RoomTypeRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.hotel.app.domain.RoomType}.
 */
@Service
@Transactional
public class RoomTypeService {

    private final Logger log = LoggerFactory.getLogger(RoomTypeService.class);

    private final RoomTypeRepository roomTypeRepository;

    public RoomTypeService(RoomTypeRepository roomTypeRepository) {
        this.roomTypeRepository = roomTypeRepository;
    }

    /**
     * Save a roomType.
     *
     * @param roomType the entity to save.
     * @return the persisted entity.
     */
    public RoomType save(RoomType roomType) {
        log.debug("Request to save RoomType : {}", roomType);
        return roomTypeRepository.save(roomType);
    }

    /**
     * Update a roomType.
     *
     * @param roomType the entity to save.
     * @return the persisted entity.
     */
    public RoomType update(RoomType roomType) {
        log.debug("Request to update RoomType : {}", roomType);
        return roomTypeRepository.save(roomType);
    }

    /**
     * Partially update a roomType.
     *
     * @param roomType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RoomType> partialUpdate(RoomType roomType) {
        log.debug("Request to partially update RoomType : {}", roomType);

        return roomTypeRepository
            .findById(roomType.getId())
            .map(existingRoomType -> {
                if (roomType.getName() != null) {
                    existingRoomType.setName(roomType.getName());
                }
                if (roomType.getDescription() != null) {
                    existingRoomType.setDescription(roomType.getDescription());
                }
                if (roomType.getCreator() != null) {
                    existingRoomType.setCreator(roomType.getCreator());
                }
                if (roomType.getCreatedAt() != null) {
                    existingRoomType.setCreatedAt(roomType.getCreatedAt());
                }
                if (roomType.getUpdater() != null) {
                    existingRoomType.setUpdater(roomType.getUpdater());
                }
                if (roomType.getUpdatedAt() != null) {
                    existingRoomType.setUpdatedAt(roomType.getUpdatedAt());
                }

                return existingRoomType;
            })
            .map(roomTypeRepository::save);
    }

    /**
     * Get all the roomTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RoomType> findAll(Pageable pageable) {
        log.debug("Request to get all RoomTypes");
        return roomTypeRepository.findAll(pageable);
    }

    /**
     *  Get all the roomTypes where Room is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RoomType> findAllWhereRoomIsNull() {
        log.debug("Request to get all roomTypes where Room is null");
        return StreamSupport
            .stream(roomTypeRepository.findAll().spliterator(), false)
            .filter(roomType -> roomType.getRoom() == null)
            .toList();
    }

    /**
     * Get one roomType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RoomType> findOne(Long id) {
        log.debug("Request to get RoomType : {}", id);
        return roomTypeRepository.findById(id);
    }

    /**
     * Delete the roomType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RoomType : {}", id);
        roomTypeRepository.deleteById(id);
    }
}
