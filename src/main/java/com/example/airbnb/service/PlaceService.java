package com.example.airbnb.service;
import com.example.airbnb.entity.Place;
import com.example.airbnb.entity.User;
import com.example.airbnb.exception.InvalidHostException;
import com.example.airbnb.exception.InvalidPlaceException;
import com.example.airbnb.exception.PlaceNotFoundException;
import com.example.airbnb.exception.UserNotFoundException;
import com.example.airbnb.repository.PlaceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class PlaceService {
        private final PlaceRepository repository;

        public List<Place> getAllPlaces() {
            return repository.findAll();
        }

        public Place addPlace(Place place) {
            if(Objects.isNull(place) || Objects.isNull(place.getState())
                    || Objects.isNull(place.getCountry()) || Objects.isNull(place.getAddress())
                    || Objects.isNull(place.getCity())) {
                throw new InvalidPlaceException("Erro ao cadastrar o local!");
            }

            return repository.save(place);
        }

    public Place placeFindById(Integer id){
        if (Objects.isNull(id)){
            throw new IllegalArgumentException("Id null when fetching for an place.");
        }
        return repository.findById(id).orElseThrow(() ->
                new InvalidPlaceException(
                        String.format("No place found for id %d",id))
        );


    }

    public Place editPlace(Integer id, Place newPlace ){
        Place existingPlace = repository.findById(id).
                orElseThrow(() -> new PlaceNotFoundException("Place não encontrado!"));

        existingPlace.setState(newPlace.getState());
        existingPlace.setCountry(newPlace.getCountry());
        existingPlace.setAddress(newPlace.getAddress());
        existingPlace.setCity(newPlace.getCity());
        return repository.save(existingPlace);
    }

    public List<Place> searchPlace(String state, String country, String city){
        return repository.findPlaceBy(state, country, city);
    }

    public void deleteById(Integer id) {
        if (Objects.isNull(id)){
            throw new IllegalArgumentException("Id null when fetching for an place.");
        }
        repository.deleteById(id);
    }


}
