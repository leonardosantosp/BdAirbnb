package com.example.airbnb.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "db2022108011", name = "user_bd")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer idUser;

    private String name;

    private String email;

    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

}
