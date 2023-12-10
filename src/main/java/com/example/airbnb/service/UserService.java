package com.example.airbnb.service;

import com.example.airbnb.entity.Host;
import com.example.airbnb.entity.Place;
import com.example.airbnb.entity.User;
import com.example.airbnb.exception.InvalidHostException;
import com.example.airbnb.exception.InvalidUserException;
import com.example.airbnb.exception.UserNotFoundException;
import com.example.airbnb.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User addUser(User user) {
        if(Objects.isNull(user) || Objects.isNull(user.getName())
                || Objects.isNull(user.getEmail()) || Objects.isNull(user.getPassword())
                || Objects.isNull(user.getPhoneNumber())) {
            throw new InvalidUserException("Erro ao cadastrar usuário!");
        }

        return repository.save(user);
    }

    public User userFindById(Integer id){
        if (Objects.isNull(id)){
            throw new IllegalArgumentException("Id null when fetching for an user.");
        }
        return repository.findById(id).orElseThrow(() ->
                new InvalidHostException(
                        String.format("No user found for id %d",id))
        );


    }

    public User editUser(Integer id, User newUser ){
        User existingUser = repository.findById(id).
                orElseThrow(() -> new UserNotFoundException("Usuario não encontrado!"));

        existingUser.setName(newUser.getName());
        existingUser.setEmail(newUser.getEmail());
        existingUser.setPassword(newUser.getPassword());
        existingUser.setPhoneNumber(newUser.getPhoneNumber());
        return repository.save(existingUser);
    }

    public List<User> searchUser(String name, String email, String phoneNumber){
        return repository.findUserBy(name, email, phoneNumber);

    }

    public boolean autenticate(String email, String password){
        if(email != null && password != null){
            User user = repository.findByEmailAndPassword(email, password);
            return user != null;
        }
        else{
            throw new IllegalArgumentException("Email or password must not be null.");
        }
    }

    public void deleteById(Integer id) {

        if (Objects.isNull(id)){
            throw new IllegalArgumentException("Id null when fetching for an user.");
        }
        repository.deleteById(id);
    }


}
