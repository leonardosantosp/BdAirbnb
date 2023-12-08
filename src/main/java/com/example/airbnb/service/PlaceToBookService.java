package com.example.airbnb.service;

import com.example.airbnb.entity.Host;
import com.example.airbnb.entity.PlaceToBook;
import com.example.airbnb.exception.InvalidHostException;
import com.example.airbnb.exception.InvalidPlaceToBookException;
import com.example.airbnb.exception.PlaceToBookNotFoundException;
import com.example.airbnb.repository.HostRepository;
import com.example.airbnb.repository.PlaceToBookRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class PlaceToBookService {

    public final PlaceToBookRepository repository;
    public final HostRepository hostRepository;

    public List<PlaceToBook> getAllPlaceToBooks(){
        return repository.findAll();
    }

    public PlaceToBook addPlaceToBook(PlaceToBook placeToBook){
        placeToBook.setMeanReview(0.0);
        placeToBook.setReservationStatus(false);
        if(Objects.isNull(placeToBook) || Objects.isNull(placeToBook.getCapacity())
                || Objects.isNull(placeToBook.getNRooms())
                || Objects.isNull(placeToBook.getNBathroom())
                || Objects.isNull(placeToBook.getPrice())
                || Objects.isNull(placeToBook.getNKitchen())
                || Objects.isNull(placeToBook.getNLivingRooms())
                || Objects.isNull(placeToBook.getPlace())
                || Objects.isNull(placeToBook.getHost())){
            throw new InvalidHostException("Erro ao cadastrar o placeToBook!");
        }
        Integer sum = placeToBook.getHost().getNPlaceToBook() + 1;
        placeToBook.getHost().setNPlaceToBook(sum);
        placeToBook.getHost().setAvailabilityStatus(true);
        hostRepository.save(placeToBook.getHost());
        return repository.save(placeToBook);
    }

    public PlaceToBook placeToBookFindById(Integer id){
        if (Objects.isNull(id)){
            throw new IllegalArgumentException("Id null when fetching for an placeToBook.");
        }
        return repository.findById(id).orElseThrow(() ->
                new InvalidPlaceToBookException(
                        String.format("No placeToBook found for id %d",id))
        );

    }

    public List<PlaceToBook> searchPlaceToBook(Integer nRooms, Integer nBathroom, Integer  nKitchen, Integer nLivingRooms){
        return repository.findPlaceToBookByBy(nRooms, nBathroom, nKitchen, nLivingRooms);
    }

    public PlaceToBook editPlaceToBook(Integer id, PlaceToBook newPlace ) {
        PlaceToBook existingPlaceToBook = repository.findById(id).
                orElseThrow(() -> new PlaceToBookNotFoundException("PlaceToBook não encontrado!"));

        existingPlaceToBook.setCapacity(newPlace.getCapacity());
        existingPlaceToBook.setNRooms(newPlace.getNRooms());
        existingPlaceToBook.setNBathroom(newPlace.getNBathroom());
        existingPlaceToBook.setNKitchen(newPlace.getNKitchen());
        existingPlaceToBook.setNLivingRooms(newPlace.getNLivingRooms());
        existingPlaceToBook.setPrice(newPlace.getPrice());
        return repository.save(existingPlaceToBook);
    }

    public List<PlaceToBook> placeToBookSortInDescendingOrderPrice(){
        return repository.decreasingFindAllOrderByPrice();
    }

    public List<PlaceToBook> placeToBookSortInAscendingOrderPrice(){
        return repository.increasingFindAllOrderByPrice();
    }

    public List<PlaceToBook> findReservedPlacesWithCapacityGreaterThan(Integer minCapacity) {
        return repository.findReservedPlacesWithCapacityGreaterThan(minCapacity);
    }

    public List<PlaceToBook> findPriceRange(Double minvalue, Double maxvalue){
        return repository.findPlaceToBookByPriceRange(minvalue, maxvalue);
    }
    public List<PlaceToBook> orderThePlaceToBookByPrice(){
        return repository.findAllOrderByPrice();
    }

    @Transactional
    public void updateHostAvailabilityStatus(){
        hostRepository.updateHostAvailabilityStatus();
    }

    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

}
