package com.example.airbnb.repository;

import com.example.airbnb.entity.PlaceToBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceToBookRepository extends JpaRepository<PlaceToBook, Integer> {
    @Query("SELECT p FROM PlaceToBook p ORDER BY p.price DESC")
    List<PlaceToBook> findAllOrderByPrice();

    @Query("SELECT pb FROM PlaceToBook pb WHERE " +
            "(:nRooms is null OR pb.nRooms = :nRooms) AND " +
            "(:nBathroom is null OR pb.nBathroom = :nBathroom) AND " +
            "(:nKitchen is null OR pb.nKitchen = :nKitchen) AND " +
            "(:nLivingRooms is null OR pb.nLivingRooms = :nLivingRooms)")
    List<PlaceToBook> findPlaceToBookByBy(
            @Param("nRooms") Integer nRooms,
            @Param("nBathroom") Integer nBathroom,
            @Param("nKitchen") Integer nKitchen,
            @Param("nLivingRooms") Integer nLivingRooms);

    @Query("SELECT p FROM PlaceToBook p ORDER BY p.price DESC")
    List<PlaceToBook> decreasingFindAllOrderByPrice();

    @Query("SELECT p FROM PlaceToBook p ORDER BY p.price ASC")
    List<PlaceToBook> increasingFindAllOrderByPrice();

    @Query("SELECT p FROM PlaceToBook p WHERE p.reservationStatus = false AND p.capacity >= :minCapacity")
    List<PlaceToBook> findReservedPlacesWithCapacityGreaterThan(@Param("minCapacity") Integer minCapacity);

    @Query("select p from PlaceToBook p WHERE p.price > :minValue and p.price < :maxValue")
    List<PlaceToBook> findPlaceToBookByPriceRange(@Param("minValue") Double minValue,
                                                  @Param("maxValue") Double maxValue
    );
}

