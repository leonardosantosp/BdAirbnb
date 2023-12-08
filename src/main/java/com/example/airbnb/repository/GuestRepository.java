package com.example.airbnb.repository;

import com.example.airbnb.entity.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GuestRepository extends JpaRepository<Guest, Integer> {
    @Query("SELECT COUNT(g) > 0 FROM Guest g WHERE g.user.idUser = :userId")
    boolean isUserReferenced(@Param("userId") Integer userId);
}
