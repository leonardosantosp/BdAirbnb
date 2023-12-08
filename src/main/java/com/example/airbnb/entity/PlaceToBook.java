package com.example.airbnb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "db2022108011", name = "place_to_book_bd")
public class PlaceToBook implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "place_to_book_id")
    private Integer idPlaceToBook;

    private Integer capacity;

    @Column(name = "mean_review")
    private Double meanReview;

    @Column(name = "n_rooms")
    private Integer nRooms;

    @Column(name = "n_bathroom")
    private Integer nBathroom;

    private Double price;

    @Column(name = "n_kitchen")
    private Integer nKitchen;

    @Column(name = "n_living_rooms")
    private Integer nLivingRooms;

    @Column(name = "reservation_status")
    private Boolean reservationStatus;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    @ManyToOne
    @JoinColumn(name = "host_id")
    private Host host;

    @Transient
    Double sumReview = 0.0;

    @Transient
    Double nReview = 0.0;

}
