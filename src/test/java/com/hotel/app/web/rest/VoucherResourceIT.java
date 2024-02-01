package com.hotel.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.hotel.app.IntegrationTest;
import com.hotel.app.domain.Voucher;
import com.hotel.app.repository.VoucherRepository;
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
 * Integration tests for the {@link VoucherResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VoucherResourceIT {

    private static final Integer DEFAULT_PRICE = 1;
    private static final Integer UPDATED_PRICE = 2;

    private static final Integer DEFAULT_SALE_PRICE = 1;
    private static final Integer UPDATED_SALE_PRICE = 2;

    private static final Long DEFAULT_CREATOR = 1L;
    private static final Long UPDATED_CREATOR = 2L;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_UPDATER = 1L;
    private static final Long UPDATED_UPDATER = 2L;

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/vouchers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVoucherMockMvc;

    private Voucher voucher;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Voucher createEntity(EntityManager em) {
        Voucher voucher = new Voucher()
            .price(DEFAULT_PRICE)
            .salePrice(DEFAULT_SALE_PRICE)
            .creator(DEFAULT_CREATOR)
            .createdAt(DEFAULT_CREATED_AT)
            .updater(DEFAULT_UPDATER)
            .updatedAt(DEFAULT_UPDATED_AT);
        return voucher;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Voucher createUpdatedEntity(EntityManager em) {
        Voucher voucher = new Voucher()
            .price(UPDATED_PRICE)
            .salePrice(UPDATED_SALE_PRICE)
            .creator(UPDATED_CREATOR)
            .createdAt(UPDATED_CREATED_AT)
            .updater(UPDATED_UPDATER)
            .updatedAt(UPDATED_UPDATED_AT);
        return voucher;
    }

    @BeforeEach
    public void initTest() {
        voucher = createEntity(em);
    }

    @Test
    @Transactional
    void createVoucher() throws Exception {
        int databaseSizeBeforeCreate = voucherRepository.findAll().size();
        // Create the Voucher
        restVoucherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voucher)))
            .andExpect(status().isCreated());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeCreate + 1);
        Voucher testVoucher = voucherList.get(voucherList.size() - 1);
        assertThat(testVoucher.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testVoucher.getSalePrice()).isEqualTo(DEFAULT_SALE_PRICE);
        assertThat(testVoucher.getCreator()).isEqualTo(DEFAULT_CREATOR);
        assertThat(testVoucher.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testVoucher.getUpdater()).isEqualTo(DEFAULT_UPDATER);
        assertThat(testVoucher.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void createVoucherWithExistingId() throws Exception {
        // Create the Voucher with an existing ID
        voucher.setId(1L);

        int databaseSizeBeforeCreate = voucherRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoucherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voucher)))
            .andExpect(status().isBadRequest());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = voucherRepository.findAll().size();
        // set the field null
        voucher.setPrice(null);

        // Create the Voucher, which fails.

        restVoucherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voucher)))
            .andExpect(status().isBadRequest());

        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSalePriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = voucherRepository.findAll().size();
        // set the field null
        voucher.setSalePrice(null);

        // Create the Voucher, which fails.

        restVoucherMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voucher)))
            .andExpect(status().isBadRequest());

        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllVouchers() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get all the voucherList
        restVoucherMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voucher.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.[*].salePrice").value(hasItem(DEFAULT_SALE_PRICE)))
            .andExpect(jsonPath("$.[*].creator").value(hasItem(DEFAULT_CREATOR.intValue())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updater").value(hasItem(DEFAULT_UPDATER.intValue())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getVoucher() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        // Get the voucher
        restVoucherMockMvc
            .perform(get(ENTITY_API_URL_ID, voucher.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(voucher.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE))
            .andExpect(jsonPath("$.salePrice").value(DEFAULT_SALE_PRICE))
            .andExpect(jsonPath("$.creator").value(DEFAULT_CREATOR.intValue()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updater").value(DEFAULT_UPDATER.intValue()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVoucher() throws Exception {
        // Get the voucher
        restVoucherMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingVoucher() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        int databaseSizeBeforeUpdate = voucherRepository.findAll().size();

        // Update the voucher
        Voucher updatedVoucher = voucherRepository.findById(voucher.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedVoucher are not directly saved in db
        em.detach(updatedVoucher);
        updatedVoucher
            .price(UPDATED_PRICE)
            .salePrice(UPDATED_SALE_PRICE)
            .creator(UPDATED_CREATOR)
            .createdAt(UPDATED_CREATED_AT)
            .updater(UPDATED_UPDATER)
            .updatedAt(UPDATED_UPDATED_AT);

        restVoucherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVoucher.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVoucher))
            )
            .andExpect(status().isOk());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeUpdate);
        Voucher testVoucher = voucherList.get(voucherList.size() - 1);
        assertThat(testVoucher.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testVoucher.getSalePrice()).isEqualTo(UPDATED_SALE_PRICE);
        assertThat(testVoucher.getCreator()).isEqualTo(UPDATED_CREATOR);
        assertThat(testVoucher.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testVoucher.getUpdater()).isEqualTo(UPDATED_UPDATER);
        assertThat(testVoucher.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void putNonExistingVoucher() throws Exception {
        int databaseSizeBeforeUpdate = voucherRepository.findAll().size();
        voucher.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoucherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, voucher.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voucher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVoucher() throws Exception {
        int databaseSizeBeforeUpdate = voucherRepository.findAll().size();
        voucher.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoucherMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voucher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVoucher() throws Exception {
        int databaseSizeBeforeUpdate = voucherRepository.findAll().size();
        voucher.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoucherMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voucher)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVoucherWithPatch() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        int databaseSizeBeforeUpdate = voucherRepository.findAll().size();

        // Update the voucher using partial update
        Voucher partialUpdatedVoucher = new Voucher();
        partialUpdatedVoucher.setId(voucher.getId());

        partialUpdatedVoucher.createdAt(UPDATED_CREATED_AT);

        restVoucherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoucher.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVoucher))
            )
            .andExpect(status().isOk());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeUpdate);
        Voucher testVoucher = voucherList.get(voucherList.size() - 1);
        assertThat(testVoucher.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testVoucher.getSalePrice()).isEqualTo(DEFAULT_SALE_PRICE);
        assertThat(testVoucher.getCreator()).isEqualTo(DEFAULT_CREATOR);
        assertThat(testVoucher.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testVoucher.getUpdater()).isEqualTo(DEFAULT_UPDATER);
        assertThat(testVoucher.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
    }

    @Test
    @Transactional
    void fullUpdateVoucherWithPatch() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        int databaseSizeBeforeUpdate = voucherRepository.findAll().size();

        // Update the voucher using partial update
        Voucher partialUpdatedVoucher = new Voucher();
        partialUpdatedVoucher.setId(voucher.getId());

        partialUpdatedVoucher
            .price(UPDATED_PRICE)
            .salePrice(UPDATED_SALE_PRICE)
            .creator(UPDATED_CREATOR)
            .createdAt(UPDATED_CREATED_AT)
            .updater(UPDATED_UPDATER)
            .updatedAt(UPDATED_UPDATED_AT);

        restVoucherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoucher.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVoucher))
            )
            .andExpect(status().isOk());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeUpdate);
        Voucher testVoucher = voucherList.get(voucherList.size() - 1);
        assertThat(testVoucher.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testVoucher.getSalePrice()).isEqualTo(UPDATED_SALE_PRICE);
        assertThat(testVoucher.getCreator()).isEqualTo(UPDATED_CREATOR);
        assertThat(testVoucher.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testVoucher.getUpdater()).isEqualTo(UPDATED_UPDATER);
        assertThat(testVoucher.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void patchNonExistingVoucher() throws Exception {
        int databaseSizeBeforeUpdate = voucherRepository.findAll().size();
        voucher.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoucherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, voucher.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voucher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVoucher() throws Exception {
        int databaseSizeBeforeUpdate = voucherRepository.findAll().size();
        voucher.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoucherMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voucher))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVoucher() throws Exception {
        int databaseSizeBeforeUpdate = voucherRepository.findAll().size();
        voucher.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoucherMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(voucher)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Voucher in the database
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVoucher() throws Exception {
        // Initialize the database
        voucherRepository.saveAndFlush(voucher);

        int databaseSizeBeforeDelete = voucherRepository.findAll().size();

        // Delete the voucher
        restVoucherMockMvc
            .perform(delete(ENTITY_API_URL_ID, voucher.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Voucher> voucherList = voucherRepository.findAll();
        assertThat(voucherList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
