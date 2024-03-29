package com.hotel.app.repository;

import com.hotel.app.domain.Room;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Room entity.
 */
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    default Optional<Room> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Room> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Room> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select room from Room room left join fetch room.hotel", countQuery = "select count(room) from Room room")
    Page<Room> findAllWithToOneRelationships(Pageable pageable);

    @Query("select room from Room room left join fetch room.hotel")
    List<Room> findAllWithToOneRelationships();

    @Query("select room from Room room left join fetch room.hotel where room.id =:id")
    Optional<Room> findOneWithToOneRelationships(@Param("id") Long id);
}
