package com.hotel.app.service;

import com.hotel.app.domain.Voucher;
import com.hotel.app.repository.VoucherRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.hotel.app.domain.Voucher}.
 */
@Service
@Transactional
public class VoucherService {

    private final Logger log = LoggerFactory.getLogger(VoucherService.class);

    private final VoucherRepository voucherRepository;

    public VoucherService(VoucherRepository voucherRepository) {
        this.voucherRepository = voucherRepository;
    }

    /**
     * Save a voucher.
     *
     * @param voucher the entity to save.
     * @return the persisted entity.
     */
    public Voucher save(Voucher voucher) {
        log.debug("Request to save Voucher : {}", voucher);
        return voucherRepository.save(voucher);
    }

    /**
     * Update a voucher.
     *
     * @param voucher the entity to save.
     * @return the persisted entity.
     */
    public Voucher update(Voucher voucher) {
        log.debug("Request to update Voucher : {}", voucher);
        return voucherRepository.save(voucher);
    }

    /**
     * Partially update a voucher.
     *
     * @param voucher the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Voucher> partialUpdate(Voucher voucher) {
        log.debug("Request to partially update Voucher : {}", voucher);

        return voucherRepository
            .findById(voucher.getId())
            .map(existingVoucher -> {
                if (voucher.getPrice() != null) {
                    existingVoucher.setPrice(voucher.getPrice());
                }
                if (voucher.getSalePrice() != null) {
                    existingVoucher.setSalePrice(voucher.getSalePrice());
                }
                if (voucher.getCreator() != null) {
                    existingVoucher.setCreator(voucher.getCreator());
                }
                if (voucher.getCreatedAt() != null) {
                    existingVoucher.setCreatedAt(voucher.getCreatedAt());
                }
                if (voucher.getUpdater() != null) {
                    existingVoucher.setUpdater(voucher.getUpdater());
                }
                if (voucher.getUpdatedAt() != null) {
                    existingVoucher.setUpdatedAt(voucher.getUpdatedAt());
                }

                return existingVoucher;
            })
            .map(voucherRepository::save);
    }

    /**
     * Get all the vouchers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Voucher> findAll(Pageable pageable) {
        log.debug("Request to get all Vouchers");
        return voucherRepository.findAll(pageable);
    }

    /**
     * Get one voucher by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Voucher> findOne(Long id) {
        log.debug("Request to get Voucher : {}", id);
        return voucherRepository.findById(id);
    }

    /**
     * Delete the voucher by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Voucher : {}", id);
        voucherRepository.deleteById(id);
    }
}
