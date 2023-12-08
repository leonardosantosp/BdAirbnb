package com.example.airbnb.repository;

import com.example.airbnb.entity.PlaceToBook;
import com.example.airbnb.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByPlaceToBook(PlaceToBook placeToBook);

}
