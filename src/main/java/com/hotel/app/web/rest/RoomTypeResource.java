package com.hotel.app.web.rest;

import com.hotel.app.domain.RoomType;
import com.hotel.app.repository.RoomTypeRepository;
import com.hotel.app.service.RoomTypeService;
import com.hotel.app.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hotel.app.domain.RoomType}.
 */
@RestController
@RequestMapping("/api/room-types")
public class RoomTypeResource {

    private final Logger log = LoggerFactory.getLogger(RoomTypeResource.class);

    private static final String ENTITY_NAME = "roomType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoomTypeService roomTypeService;

    private final RoomTypeRepository roomTypeRepository;

    public RoomTypeResource(RoomTypeService roomTypeService, RoomTypeRepository roomTypeRepository) {
        this.roomTypeService = roomTypeService;
        this.roomTypeRepository = roomTypeRepository;
    }

    /**
     * {@code POST  /room-types} : Create a new roomType.
     *
     * @param roomType the roomType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roomType, or with status {@code 400 (Bad Request)} if the roomType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RoomType> createRoomType(@Valid @RequestBody RoomType roomType) throws URISyntaxException {
        log.debug("REST request to save RoomType : {}", roomType);
        if (roomType.getId() != null) {
            throw new BadRequestAlertException("A new roomType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RoomType result = roomTypeService.save(roomType);
        return ResponseEntity
            .created(new URI("/api/room-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /room-types/:id} : Updates an existing roomType.
     *
     * @param id the id of the roomType to save.
     * @param roomType the roomType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomType,
     * or with status {@code 400 (Bad Request)} if the roomType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the roomType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoomType> updateRoomType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RoomType roomType
    ) throws URISyntaxException {
        log.debug("REST request to update RoomType : {}, {}", id, roomType);
        if (roomType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roomType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roomTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RoomType result = roomTypeService.update(roomType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roomType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /room-types/:id} : Partial updates given fields of an existing roomType, field will ignore if it is null
     *
     * @param id the id of the roomType to save.
     * @param roomType the roomType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomType,
     * or with status {@code 400 (Bad Request)} if the roomType is not valid,
     * or with status {@code 404 (Not Found)} if the roomType is not found,
     * or with status {@code 500 (Internal Server Error)} if the roomType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RoomType> partialUpdateRoomType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RoomType roomType
    ) throws URISyntaxException {
        log.debug("REST request to partial update RoomType partially : {}, {}", id, roomType);
        if (roomType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roomType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roomTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoomType> result = roomTypeService.partialUpdate(roomType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, roomType.getId().toString())
        );
    }

    /**
     * {@code GET  /room-types} : get all the roomTypes.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roomTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RoomType>> getAllRoomTypes(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter
    ) {
        if ("room-is-null".equals(filter)) {
            log.debug("REST request to get all RoomTypes where room is null");
            return new ResponseEntity<>(roomTypeService.findAllWhereRoomIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of RoomTypes");
        Page<RoomType> page = roomTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /room-types/:id} : get the "id" roomType.
     *
     * @param id the id of the roomType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roomType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoomType> getRoomType(@PathVariable("id") Long id) {
        log.debug("REST request to get RoomType : {}", id);
        Optional<RoomType> roomType = roomTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roomType);
    }

    /**
     * {@code DELETE  /room-types/:id} : delete the "id" roomType.
     *
     * @param id the id of the roomType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomType(@PathVariable("id") Long id) {
        log.debug("REST request to delete RoomType : {}", id);
        roomTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
