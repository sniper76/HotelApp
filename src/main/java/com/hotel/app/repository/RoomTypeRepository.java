package com.hotel.app.repository;

import com.hotel.app.domain.RoomType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RoomType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {}
