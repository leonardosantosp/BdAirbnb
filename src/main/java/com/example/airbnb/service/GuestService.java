package com.example.airbnb.service;

import com.example.airbnb.entity.Guest;
import com.example.airbnb.exception.InvalidGuestException;
import com.example.airbnb.repository.GuestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class GuestService {
    public final GuestRepository repository;

    public List<Guest> getAllGuests(){
        return repository.findAll();
    }

    public Guest addGuest(Guest guest){
        guest.setBookingStatus(false);
        if(Objects.isNull(guest) || Objects.isNull(guest.getBookingStatus())
                || Objects.isNull(guest.getUser())){
            throw new InvalidGuestException("Erro ao cadastrar o guest!");
        }
        if(repository.isUserReferenced(guest.getUser().getIdUser())){
            throw new InvalidGuestException("Chave duplicada! o usuário " + guest.getUser().getName() + " já é um guest!");
        }
        return repository.save(guest);
    }

    public Guest guestFindById(Integer id){
        if (Objects.isNull(id)){
            throw new IllegalArgumentException("Id null when fetching for an guest.");
        }
        return repository.findById(id).orElseThrow(() ->
                new InvalidGuestException(
                        String.format("No guest found for id %d",id))
        );

    }

}
