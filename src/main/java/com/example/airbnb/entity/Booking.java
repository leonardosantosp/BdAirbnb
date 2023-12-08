package com.example.airbnb.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "db2022108011", name = "booking_bd")
public class Booking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer idBooking;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name="arrival_date")
    private java.util.Date arrivalDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name="departure_date")
    private java.util.Date departureDate;

    @Column(name = "n_people")
    private Integer nPeople;


    @ManyToOne
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @ManyToOne
    @JoinColumn(name = "place_to_book_id")
    private PlaceToBook placeToBook;

}
