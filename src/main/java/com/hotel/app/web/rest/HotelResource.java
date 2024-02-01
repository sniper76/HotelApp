package com.hotel.app.web.rest;

import com.hotel.app.domain.Hotel;
import com.hotel.app.repository.HotelRepository;
import com.hotel.app.service.HotelService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.hotel.app.domain.Hotel}.
 */
@RestController
@RequestMapping("/api/hotels")
public class HotelResource {

    private final Logger log = LoggerFactory.getLogger(HotelResource.class);

    private static final String ENTITY_NAME = "hotel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HotelService hotelService;

    private final HotelRepository hotelRepository;

    public HotelResource(HotelService hotelService, HotelRepository hotelRepository) {
        this.hotelService = hotelService;
        this.hotelRepository = hotelRepository;
    }

    /**
     * {@code POST  /hotels} : Create a new hotel.
     *
     * @param hotel the hotel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hotel, or with status {@code 400 (Bad Request)} if the hotel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Hotel> createHotel(@Valid @RequestBody Hotel hotel) throws URISyntaxException {
        log.debug("REST request to save Hotel : {}", hotel);
        if (hotel.getId() != null) {
            throw new BadRequestAlertException("A new hotel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Hotel result = hotelService.save(hotel);
        return ResponseEntity
            .created(new URI("/api/hotels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hotels/:id} : Updates an existing hotel.
     *
     * @param id the id of the hotel to save.
     * @param hotel the hotel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hotel,
     * or with status {@code 400 (Bad Request)} if the hotel is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hotel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Hotel hotel)
        throws URISyntaxException {
        log.debug("REST request to update Hotel : {}, {}", id, hotel);
        if (hotel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hotel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hotelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Hotel result = hotelService.update(hotel);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hotel.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /hotels/:id} : Partial updates given fields of an existing hotel, field will ignore if it is null
     *
     * @param id the id of the hotel to save.
     * @param hotel the hotel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hotel,
     * or with status {@code 400 (Bad Request)} if the hotel is not valid,
     * or with status {@code 404 (Not Found)} if the hotel is not found,
     * or with status {@code 500 (Internal Server Error)} if the hotel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Hotel> partialUpdateHotel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Hotel hotel
    ) throws URISyntaxException {
        log.debug("REST request to partial update Hotel partially : {}, {}", id, hotel);
        if (hotel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hotel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hotelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Hotel> result = hotelService.partialUpdate(hotel);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hotel.getId().toString())
        );
    }

    /**
     * {@code GET  /hotels} : get all the hotels.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hotels in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Hotel>> getAllHotels(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Hotels");
        Page<Hotel> page;
        if (eagerload) {
            page = hotelService.findAllWithEagerRelationships(pageable);
        } else {
            page = hotelService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hotels/:id} : get the "id" hotel.
     *
     * @param id the id of the hotel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hotel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotel(@PathVariable("id") Long id) {
        log.debug("REST request to get Hotel : {}", id);
        Optional<Hotel> hotel = hotelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hotel);
    }

    /**
     * {@code DELETE  /hotels/:id} : delete the "id" hotel.
     *
     * @param id the id of the hotel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable("id") Long id) {
        log.debug("REST request to delete Hotel : {}", id);
        hotelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
