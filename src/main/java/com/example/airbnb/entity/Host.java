
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
@Table(schema = "db2022108011", name = "host_bd")
public class Host implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "host_id")
    private Integer idHost;

    @Column(name = "n_place_to_book")
    private Integer nPlaceToBook;

    @Column(name = "availability_status")
    private Boolean availabilityStatus;

    @Column(name = "average_rating")
    private Double averageRating;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

}