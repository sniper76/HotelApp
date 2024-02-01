package com.hotel.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hotel.app.IntegrationTest;
import com.hotel.app.domain.Room;
import com.hotel.app.repository.RoomRepository;
import com.hotel.app.service.RoomService;
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
 * Integration tests for the {@link RoomResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class RoomResourceIT {

    private static final String DEFAULT_NO = "AAAAAAAAAA";
    private static final String UPDATED_NO = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;

    private static final Integer DEFAULT_PROMO_PRICE = 1;
    private static final Integer UPDATED_PROMO_PRICE = 2;

    private static final Long DEFAULT_CREATOR = 1L;
    private static final Long UPDATED_CREATOR = 2L;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_UPDATER = 1L;
    private static final Long UPDATED_UPDATER = 2L;

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/rooms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoomRepository roomRepository;

    @Mock
    private RoomRepository roomRepositoryMock;

    @Mock
    private RoomService roomServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoomMockMvc;

    private Room room;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Room createEntity(EntityManager em) {
        Room room = new Room()
            .no(DEFAULT_NO)
            .price(DEFAULT_PRICE)
            .promoPrice(DEFAULT_PROMO_PRICE)
            .creator(DEFAULT_CREATOR)
            .createdAt(DEFAULT_CREATED_AT)
            .updater(DEFAULT_UPDATER)
            .updatedAt(DEFAULT_UPDATED_AT);
        return room;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Room createUpdatedEntity(EntityManager em) {
        Room room = new Room()
            .no(UPDATED_NO)
            .price(UPDATED_PRICE)
            .promoPrice(UPDATED_PROMO_PRICE)
            .creator(UPDATED_CREATOR)
            .createdAt(UPDATED_CREATED_AT)
            .updater(UPDATED_UPDATER)
            .updatedAt(UPDATED_UPDATED_AT);
        return room;
    }

    @BeforeEach
    public void initTest() {
        room = createEntity(em);
    }

    @Test
    @Transactional
    void createRoom() throws Exception {
        int databaseSizeBeforeCreate = roomRepository.findAll().size();
        // Create the Room
        restRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(room)))
            .andExpect(status().isCreated());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeCreate + 1);
        Room testRoom = roomList.get(roomList.size() - 1);
        assertThat(testRoom.getNo()).isEqualTo(DEFAULT_NO);
        assertThat(testRoom.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testRoom.getPromoPrice()).isEqualTo(DEFAULT_PROMO_PRICE);
        assertThat(testRoom.getCreator()).isEqualTo(DEFAULT_CREATOR);
        assertThat(testRoom.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testRoom.getUpdater()).isEqualTo(DEFAULT_UPDATER);
        assertThat(testRoom.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createRoomWithExistingId() throws Exception {
        // Create the Room with an existing ID
        room.setId(1L);

        int databaseSizeBeforeCreate = roomRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(room)))
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoIsRequired() throws Exception {
        int databaseSizeBeforeTest = roomRepository.findAll().size();
        // set the field null
        room.setNo(null);

        // Create the Room, which fails.

        restRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(room)))
            .andExpect(status().isBadRequest());

        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRooms() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get all the roomList
        restRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(room.getId().intValue())))
            .andExpect(jsonPath("$.[*].no").value(hasItem(DEFAULT_NO)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].promoPrice").value(hasItem(DEFAULT_PROMO_PRICE)))
            .andExpect(jsonPath("$.[*].creator").value(hasItem(DEFAULT_CREATOR.intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updater").value(hasItem(DEFAULT_UPDATER.intValue())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRoomsWithEagerRelationshipsIsEnabled() throws Exception {
        when(roomServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRoomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(roomServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllRoomsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(roomServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restRoomMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(roomRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        // Get the room
        restRoomMockMvc
            .perform(get(ENTITY_API_URL_ID, room.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(room.getId().intValue()))
            .andExpect(jsonPath("$.no").value(DEFAULT_NO))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.promoPrice").value(DEFAULT_PROMO_PRICE))
            .andExpect(jsonPath("$.creator").value(DEFAULT_CREATOR.intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updater").value(DEFAULT_UPDATER.intValue()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRoom() throws Exception {
        // Get the room
        restRoomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        int databaseSizeBeforeUpdate = roomRepository.findAll().size();

        // Update the room
        Room updatedRoom = roomRepository.findById(room.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRoom are not directly saved in db
        em.detach(updatedRoom);
        updatedRoom
            .no(UPDATED_NO)
            .price(UPDATED_PRICE)
            .promoPrice(UPDATED_PROMO_PRICE)
            .creator(UPDATED_CREATOR)
            .createdAt(UPDATED_CREATED_AT)
            .updater(UPDATED_UPDATER)
            .updatedAt(UPDATED_UPDATED_AT);

        restRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRoom.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRoom))
            )
            .andExpect(status().isOk());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
        Room testRoom = roomList.get(roomList.size() - 1);
        assertThat(testRoom.getNo()).isEqualTo(UPDATED_NO);
        assertThat(testRoom.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testRoom.getPromoPrice()).isEqualTo(UPDATED_PROMO_PRICE);
        assertThat(testRoom.getCreator()).isEqualTo(UPDATED_CREATOR);
        assertThat(testRoom.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testRoom.getUpdater()).isEqualTo(UPDATED_UPDATER);
        assertThat(testRoom.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingRoom() throws Exception {
        int databaseSizeBeforeUpdate = roomRepository.findAll().size();
        room.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, room.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(room))
            )
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoom() throws Exception {
        int databaseSizeBeforeUpdate = roomRepository.findAll().size();
        room.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(room))
            )
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoom() throws Exception {
        int databaseSizeBeforeUpdate = roomRepository.findAll().size();
        room.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(room)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoomWithPatch() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        int databaseSizeBeforeUpdate = roomRepository.findAll().size();

        // Update the room using partial update
        Room partialUpdatedRoom = new Room();
        partialUpdatedRoom.setId(room.getId());

        partialUpdatedRoom
            .no(UPDATED_NO)
            .price(UPDATED_PRICE)
            .creator(UPDATED_CREATOR)
            .createdAt(UPDATED_CREATED_AT)
            .updater(UPDATED_UPDATER);

        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoom))
            )
            .andExpect(status().isOk());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
        Room testRoom = roomList.get(roomList.size() - 1);
        assertThat(testRoom.getNo()).isEqualTo(UPDATED_NO);
        assertThat(testRoom.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testRoom.getPromoPrice()).isEqualTo(DEFAULT_PROMO_PRICE);
        assertThat(testRoom.getCreator()).isEqualTo(UPDATED_CREATOR);
        assertThat(testRoom.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testRoom.getUpdater()).isEqualTo(UPDATED_UPDATER);
        assertThat(testRoom.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateRoomWithPatch() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        int databaseSizeBeforeUpdate = roomRepository.findAll().size();

        // Update the room using partial update
        Room partialUpdatedRoom = new Room();
        partialUpdatedRoom.setId(room.getId());

        partialUpdatedRoom
            .no(UPDATED_NO)
            .price(UPDATED_PRICE)
            .promoPrice(UPDATED_PROMO_PRICE)
            .creator(UPDATED_CREATOR)
            .createdAt(UPDATED_CREATED_AT)
            .updater(UPDATED_UPDATER)
            .updatedAt(UPDATED_UPDATED_AT);

        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoom))
            )
            .andExpect(status().isOk());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
        Room testRoom = roomList.get(roomList.size() - 1);
        assertThat(testRoom.getNo()).isEqualTo(UPDATED_NO);
        assertThat(testRoom.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testRoom.getPromoPrice()).isEqualTo(UPDATED_PROMO_PRICE);
        assertThat(testRoom.getCreator()).isEqualTo(UPDATED_CREATOR);
        assertThat(testRoom.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testRoom.getUpdater()).isEqualTo(UPDATED_UPDATER);
        assertThat(testRoom.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingRoom() throws Exception {
        int databaseSizeBeforeUpdate = roomRepository.findAll().size();
        room.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, room.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(room))
            )
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoom() throws Exception {
        int databaseSizeBeforeUpdate = roomRepository.findAll().size();
        room.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(room))
            )
            .andExpect(status().isBadRequest());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoom() throws Exception {
        int databaseSizeBeforeUpdate = roomRepository.findAll().size();
        room.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(room)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Room in the database
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoom() throws Exception {
        // Initialize the database
        roomRepository.saveAndFlush(room);

        int databaseSizeBeforeDelete = roomRepository.findAll().size();

        // Delete the room
        restRoomMockMvc
            .perform(delete(ENTITY_API_URL_ID, room.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Room> roomList = roomRepository.findAll();
        assertThat(roomList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
