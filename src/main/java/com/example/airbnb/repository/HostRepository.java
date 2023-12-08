package com.example.airbnb.repository;

import com.example.airbnb.entity.Host;
import com.example.airbnb.entity.PlaceToBook;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HostRepository extends JpaRepository<Host, Integer> {
    @Query("SELECT COUNT(h) > 0 FROM Host h WHERE h.user.idUser = :userId")
    boolean isUserReferenced(@Param("userId") Integer userId);

    @Modifying
    @Transactional
    @Query("UPDATE Host h " +
            "SET h.availabilityStatus = CASE " +
            "WHEN (SELECT COUNT(p) FROM PlaceToBook p WHERE p.host.idHost = h.idHost AND p.reservationStatus = false) > 0 THEN true " +
            "ELSE false END")
    void updateHostAvailabilityStatus();

    @Query("SELECT COALESCE(SUM(ptb.meanReview), 0) / COUNT(ptb) " +
            "FROM PlaceToBook ptb " +
            "WHERE ptb.host.idHost = :hostId")
    Double calculateAverageMeanReviewByHost(@Param("hostId") Integer hostId);

}

