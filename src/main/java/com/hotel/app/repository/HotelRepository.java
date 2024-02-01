package com.hotel.app.repository;

import com.hotel.app.domain.Hotel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Hotel entity.
 */
@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
    @Query("select hotel from Hotel hotel where hotel.user.login = ?#{authentication.name}")
    List<Hotel> findByUserIsCurrentUser();

    default Optional<Hotel> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Hotel> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Hotel> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select hotel from Hotel hotel left join fetch hotel.user", countQuery = "select count(hotel) from Hotel hotel")
    Page<Hotel> findAllWithToOneRelationships(Pageable pageable);

    @Query("select hotel from Hotel hotel left join fetch hotel.user")
    List<Hotel> findAllWithToOneRelationships();

    @Query("select hotel from Hotel hotel left join fetch hotel.user where hotel.id =:id")
    Optional<Hotel> findOneWithToOneRelationships(@Param("id") Long id);
}
