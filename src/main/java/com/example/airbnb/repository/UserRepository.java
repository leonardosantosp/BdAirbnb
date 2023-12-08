package com.example.airbnb.repository;

import com.example.airbnb.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE " +
            "(:name is null OR lower(u.name) LIKE lower(concat('%', :name, '%'))) AND " +
            "(:email is null OR lower(u.email) LIKE lower(concat('%', :email, '%'))) AND " +
            "(:phoneNumber is null OR u.phoneNumber LIKE concat('%', :phoneNumber, '%'))")
    List<User> findUserBy(@Param("name") String name,
                          @Param("email") String email,
                          @Param("phoneNumber") String phoneNumber);

    @Query(
            nativeQuery = true,
            value = "SELECT * FROM user_bd WHERE " +
                    "UPPER(email) = UPPER(:email) " + " AND " + " UPPER(password) = UPPER(:password)"
    )
    User findByEmailAndPassword(String email, String password);

}
