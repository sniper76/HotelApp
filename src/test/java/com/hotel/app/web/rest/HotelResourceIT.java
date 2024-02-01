package com.hotel.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hotel.app.IntegrationTest;
import com.hotel.app.domain.Hotel;
import com.hotel.app.domain.User;
import com.hotel.app.domain.enumeration.HotelType;
import com.hotel.app.repository.HotelRepository;
import com.hotel.app.service.HotelService;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link HotelResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HotelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final HotelType DEFAULT_TYPE = HotelType.HOTEL;
    private static final HotelType UPDATED_TYPE = HotelType.POOL_VILLA;

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/hotels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private HotelRepository hotelRepository;

    @Mock
    private HotelRepository hotelRepositoryMock;

    @Mock
    private HotelService hotelServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHotelMockMvc;

    private Hotel hotel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hotel createEntity(EntityManager em) {
        Hotel hotel = new Hotel()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .location(DEFAULT_LOCATION)
            .description(DEFAULT_DESCRIPTION)
            .creator(DEFAULT_CREATOR)
            .createdAt(DEFAULT_CREATED_AT)
            .updater(DEFAULT_UPDATER)
            .updatedAt(DEFAULT_UPDATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        hotel.setUser(user);
        return hotel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hotel createUpdatedEntity(EntityManager em) {
        Hotel hotel = new Hotel()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .location(UPDATED_LOCATION)
            .description(UPDATED_DESCRIPTION)
            .creator(UPDATED_CREATOR)
            .createdAt(UPDATED_CREATED_AT)
            .updater(UPDATED_UPDATER)
            .updatedAt(UPDATED_UPDATED_AT);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        hotel.setUser(user);
        return hotel;
    }

    @BeforeEach
    public void initTest() {
        hotel = createEntity(em);
    }

    @Test
    @Transactional
    void createHotel() throws Exception {
        int databaseSizeBeforeCreate = hotelRepository.findAll().size();
        // Create the Hotel
        restHotelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hotel)))
            .andExpect(status().isCreated());

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll();
        assertThat(hotelList).hasSize(databaseSizeBeforeCreate + 1);
        Hotel testHotel = hotelList.get(hotelList.size() - 1);
        assertThat(testHotel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHotel.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testHotel.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testHotel.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testHotel.getCreator()).isEqualTo(DEFAULT_CREATOR);
        assertThat(testHotel.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testHotel.getUpdater()).isEqualTo(DEFAULT_UPDATER);
        assertThat(testHotel.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createHotelWithExistingId() throws Exception {
        // Create the Hotel with an existing ID
        hotel.setId(1L);

        int databaseSizeBeforeCreate = hotelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHotelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hotel)))
            .andExpect(status().isBadRequest());

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll();
        assertThat(hotelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = hotelRepository.findAll().size();
        // set the field null
        hotel.setName(null);

        // Create the Hotel, which fails.

        restHotelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hotel)))
            .andExpect(status().isBadRequest());

        List<Hotel> hotelList = hotelRepository.findAll();
        assertThat(hotelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = hotelRepository.findAll().size();
        // set the field null
        hotel.setType(null);

        // Create the Hotel, which fails.

        restHotelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hotel)))
            .andExpect(status().isBadRequest());

        List<Hotel> hotelList = hotelRepository.findAll();
        assertThat(hotelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHotels() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get all the hotelList
        restHotelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hotel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].creator").value(hasItem(DEFAULT_CREATOR.intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updater").value(hasItem(DEFAULT_UPDATER.intValue())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHotelsWithEagerRelationshipsIsEnabled() throws Exception {
        when(hotelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHotelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(hotelServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHotelsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(hotelServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHotelMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(hotelRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getHotel() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        // Get the hotel
        restHotelMockMvc
            .perform(get(ENTITY_API_URL_ID, hotel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hotel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.creator").value(DEFAULT_CREATOR.intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updater").value(DEFAULT_UPDATER.intValue()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingHotel() throws Exception {
        // Get the hotel
        restHotelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHotel() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        int databaseSizeBeforeUpdate = hotelRepository.findAll().size();

        // Update the hotel
        Hotel updatedHotel = hotelRepository.findById(hotel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHotel are not directly saved in db
        em.detach(updatedHotel);
        updatedHotel
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .location(UPDATED_LOCATION)
            .description(UPDATED_DESCRIPTION)
            .creator(UPDATED_CREATOR)
            .createdAt(UPDATED_CREATED_AT)
            .updater(UPDATED_UPDATER)
            .updatedAt(UPDATED_UPDATED_AT);

        restHotelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedHotel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedHotel))
            )
            .andExpect(status().isOk());

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll();
        assertThat(hotelList).hasSize(databaseSizeBeforeUpdate);
        Hotel testHotel = hotelList.get(hotelList.size() - 1);
        assertThat(testHotel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHotel.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testHotel.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testHotel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testHotel.getCreator()).isEqualTo(UPDATED_CREATOR);
        assertThat(testHotel.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testHotel.getUpdater()).isEqualTo(UPDATED_UPDATER);
        assertThat(testHotel.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingHotel() throws Exception {
        int databaseSizeBeforeUpdate = hotelRepository.findAll().size();
        hotel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHotelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hotel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hotel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll();
        assertThat(hotelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHotel() throws Exception {
        int databaseSizeBeforeUpdate = hotelRepository.findAll().size();
        hotel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHotelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(hotel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll();
        assertThat(hotelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHotel() throws Exception {
        int databaseSizeBeforeUpdate = hotelRepository.findAll().size();
        hotel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHotelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(hotel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll();
        assertThat(hotelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHotelWithPatch() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        int databaseSizeBeforeUpdate = hotelRepository.findAll().size();

        // Update the hotel using partial update
        Hotel partialUpdatedHotel = new Hotel();
        partialUpdatedHotel.setId(hotel.getId());

        partialUpdatedHotel.name(UPDATED_NAME).description(UPDATED_DESCRIPTION).createdAt(UPDATED_CREATED_AT).updater(UPDATED_UPDATER);

        restHotelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHotel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHotel))
            )
            .andExpect(status().isOk());

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll();
        assertThat(hotelList).hasSize(databaseSizeBeforeUpdate);
        Hotel testHotel = hotelList.get(hotelList.size() - 1);
        assertThat(testHotel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHotel.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testHotel.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testHotel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testHotel.getCreator()).isEqualTo(DEFAULT_CREATOR);
        assertThat(testHotel.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testHotel.getUpdater()).isEqualTo(UPDATED_UPDATER);
        assertThat(testHotel.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateHotelWithPatch() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        int databaseSizeBeforeUpdate = hotelRepository.findAll().size();

        // Update the hotel using partial update
        Hotel partialUpdatedHotel = new Hotel();
        partialUpdatedHotel.setId(hotel.getId());

        partialUpdatedHotel
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .location(UPDATED_LOCATION)
            .description(UPDATED_DESCRIPTION)
            .creator(UPDATED_CREATOR)
            .createdAt(UPDATED_CREATED_AT)
            .updater(UPDATED_UPDATER)
            .updatedAt(UPDATED_UPDATED_AT);

        restHotelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHotel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedHotel))
            )
            .andExpect(status().isOk());

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll();
        assertThat(hotelList).hasSize(databaseSizeBeforeUpdate);
        Hotel testHotel = hotelList.get(hotelList.size() - 1);
        assertThat(testHotel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHotel.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testHotel.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testHotel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testHotel.getCreator()).isEqualTo(UPDATED_CREATOR);
        assertThat(testHotel.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testHotel.getUpdater()).isEqualTo(UPDATED_UPDATER);
        assertThat(testHotel.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingHotel() throws Exception {
        int databaseSizeBeforeUpdate = hotelRepository.findAll().size();
        hotel.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHotelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hotel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hotel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll();
        assertThat(hotelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHotel() throws Exception {
        int databaseSizeBeforeUpdate = hotelRepository.findAll().size();
        hotel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHotelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(hotel))
            )
            .andExpect(status().isBadRequest());

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll();
        assertThat(hotelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHotel() throws Exception {
        int databaseSizeBeforeUpdate = hotelRepository.findAll().size();
        hotel.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHotelMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(hotel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Hotel in the database
        List<Hotel> hotelList = hotelRepository.findAll();
        assertThat(hotelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHotel() throws Exception {
        // Initialize the database
        hotelRepository.saveAndFlush(hotel);

        int databaseSizeBeforeDelete = hotelRepository.findAll().size();

        // Delete the hotel
        restHotelMockMvc
            .perform(delete(ENTITY_API_URL_ID, hotel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Hotel> hotelList = hotelRepository.findAll();
        assertThat(hotelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
