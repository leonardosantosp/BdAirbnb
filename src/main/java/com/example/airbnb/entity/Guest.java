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
@Table(schema = "db2022108011", name = "guest_bd")
public class Guest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guest_id")
    private Integer idGuest;

    @Column(name = "booking_status")
    private Boolean bookingStatus;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

}
