package com.example.airbnb.repository;

import com.example.airbnb.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place, Integer>{

    @Query("SELECT p FROM Place p WHERE " +
            "(:state is null OR lower(p.state) LIKE lower(concat('%', :state, '%'))) AND " +
            "(:country is null OR lower(p.country) LIKE lower(concat('%', :country, '%'))) AND " +
            "(:city is null OR lower(p.city) LIKE lower(concat('%', :city, '%')))")
    List<Place> findPlaceBy(@Param("state") String state,
                            @Param("country") String country,
                            @Param("city") String city);
}
