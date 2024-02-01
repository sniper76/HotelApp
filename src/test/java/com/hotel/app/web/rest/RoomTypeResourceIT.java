package com.hotel.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hotel.app.IntegrationTest;
import com.hotel.app.domain.RoomType;
import com.hotel.app.repository.RoomTypeRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RoomTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoomTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_CREATOR = 1L;
    private static final Long UPDATED_CREATOR = 2L;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_UPDATER = 1L;
    private static final Long UPDATED_UPDATER = 2L;

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/room-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoomTypeMockMvc;

    private RoomType roomType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomType createEntity(EntityManager em) {
        RoomType roomType = new RoomType()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .creator(DEFAULT_CREATOR)
            .createdAt(DEFAULT_CREATED_AT)
            .updater(DEFAULT_UPDATER)
            .updatedAt(DEFAULT_UPDATED_AT);
        return roomType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomType createUpdatedEntity(EntityManager em) {
        RoomType roomType = new RoomType()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .creator(UPDATED_CREATOR)
            .createdAt(UPDATED_CREATED_AT)
            .updater(UPDATED_UPDATER)
            .updatedAt(UPDATED_UPDATED_AT);
        return roomType;
    }

    @BeforeEach
    public void initTest() {
        roomType = createEntity(em);
    }

    @Test
    @Transactional
    void createRoomType() throws Exception {
        int databaseSizeBeforeCreate = roomTypeRepository.findAll().size();
        // Create the RoomType
        restRoomTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomType)))
            .andExpect(status().isCreated());

        // Validate the RoomType in the database
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeCreate + 1);
        RoomType testRoomType = roomTypeList.get(roomTypeList.size() - 1);
        assertThat(testRoomType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRoomType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRoomType.getCreator()).isEqualTo(DEFAULT_CREATOR);
        assertThat(testRoomType.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testRoomType.getUpdater()).isEqualTo(DEFAULT_UPDATER);
        assertThat(testRoomType.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createRoomTypeWithExistingId() throws Exception {
        // Create the RoomType with an existing ID
        roomType.setId(1L);

        int databaseSizeBeforeCreate = roomTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomType)))
            .andExpect(status().isBadRequest());

        // Validate the RoomType in the database
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomTypeRepository.findAll().size();
        // set the field null
        roomType.setName(null);

        // Create the RoomType, which fails.

        restRoomTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomType)))
            .andExpect(status().isBadRequest());

        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRoomTypes() throws Exception {
        // Initialize the database
        roomTypeRepository.saveAndFlush(roomType);

        // Get all the roomTypeList
        restRoomTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].creator").value(hasItem(DEFAULT_CREATOR.intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updater").value(hasItem(DEFAULT_UPDATER.intValue())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getRoomType() throws Exception {
        // Initialize the database
        roomTypeRepository.saveAndFlush(roomType);

        // Get the roomType
        restRoomTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, roomType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roomType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.creator").value(DEFAULT_CREATOR.intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updater").value(DEFAULT_UPDATER.intValue()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRoomType() throws Exception {
        // Get the roomType
        restRoomTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRoomType() throws Exception {
        // Initialize the database
        roomTypeRepository.saveAndFlush(roomType);

        int databaseSizeBeforeUpdate = roomTypeRepository.findAll().size();

        // Update the roomType
        RoomType updatedRoomType = roomTypeRepository.findById(roomType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRoomType are not directly saved in db
        em.detach(updatedRoomType);
        updatedRoomType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .creator(UPDATED_CREATOR)
            .createdAt(UPDATED_CREATED_AT)
            .updater(UPDATED_UPDATER)
            .updatedAt(UPDATED_UPDATED_AT);

        restRoomTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRoomType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRoomType))
            )
            .andExpect(status().isOk());

        // Validate the RoomType in the database
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeUpdate);
        RoomType testRoomType = roomTypeList.get(roomTypeList.size() - 1);
        assertThat(testRoomType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRoomType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRoomType.getCreator()).isEqualTo(UPDATED_CREATOR);
        assertThat(testRoomType.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testRoomType.getUpdater()).isEqualTo(UPDATED_UPDATER);
        assertThat(testRoomType.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingRoomType() throws Exception {
        int databaseSizeBeforeUpdate = roomTypeRepository.findAll().size();
        roomType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roomType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomType in the database
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoomType() throws Exception {
        int databaseSizeBeforeUpdate = roomTypeRepository.findAll().size();
        roomType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomType in the database
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoomType() throws Exception {
        int databaseSizeBeforeUpdate = roomTypeRepository.findAll().size();
        roomType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomType in the database
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoomTypeWithPatch() throws Exception {
        // Initialize the database
        roomTypeRepository.saveAndFlush(roomType);

        int databaseSizeBeforeUpdate = roomTypeRepository.findAll().size();

        // Update the roomType using partial update
        RoomType partialUpdatedRoomType = new RoomType();
        partialUpdatedRoomType.setId(roomType.getId());

        partialUpdatedRoomType.creator(UPDATED_CREATOR).createdAt(UPDATED_CREATED_AT).updatedAt(UPDATED_UPDATED_AT);

        restRoomTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoomType))
            )
            .andExpect(status().isOk());

        // Validate the RoomType in the database
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeUpdate);
        RoomType testRoomType = roomTypeList.get(roomTypeList.size() - 1);
        assertThat(testRoomType.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRoomType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRoomType.getCreator()).isEqualTo(UPDATED_CREATOR);
        assertThat(testRoomType.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testRoomType.getUpdater()).isEqualTo(DEFAULT_UPDATER);
        assertThat(testRoomType.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateRoomTypeWithPatch() throws Exception {
        // Initialize the database
        roomTypeRepository.saveAndFlush(roomType);

        int databaseSizeBeforeUpdate = roomTypeRepository.findAll().size();

        // Update the roomType using partial update
        RoomType partialUpdatedRoomType = new RoomType();
        partialUpdatedRoomType.setId(roomType.getId());

        partialUpdatedRoomType
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .creator(UPDATED_CREATOR)
            .createdAt(UPDATED_CREATED_AT)
            .updater(UPDATED_UPDATER)
            .updatedAt(UPDATED_UPDATED_AT);

        restRoomTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoomType))
            )
            .andExpect(status().isOk());

        // Validate the RoomType in the database
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeUpdate);
        RoomType testRoomType = roomTypeList.get(roomTypeList.size() - 1);
        assertThat(testRoomType.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRoomType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRoomType.getCreator()).isEqualTo(UPDATED_CREATOR);
        assertThat(testRoomType.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testRoomType.getUpdater()).isEqualTo(UPDATED_UPDATER);
        assertThat(testRoomType.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingRoomType() throws Exception {
        int databaseSizeBeforeUpdate = roomTypeRepository.findAll().size();
        roomType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roomType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomType in the database
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoomType() throws Exception {
        int databaseSizeBeforeUpdate = roomTypeRepository.findAll().size();
        roomType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomType))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomType in the database
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoomType() throws Exception {
        int databaseSizeBeforeUpdate = roomTypeRepository.findAll().size();
        roomType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(roomType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomType in the database
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoomType() throws Exception {
        // Initialize the database
        roomTypeRepository.saveAndFlush(roomType);

        int databaseSizeBeforeDelete = roomTypeRepository.findAll().size();

        // Delete the roomType
        restRoomTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, roomType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RoomType> roomTypeList = roomTypeRepository.findAll();
        assertThat(roomTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
