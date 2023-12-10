package com.example.airbnb.unit;

import com.example.airbnb.entity.Place;
import com.example.airbnb.entity.User;
import com.example.airbnb.exception.InvalidPlaceException;
import com.example.airbnb.exception.PlaceNotFoundException;
import com.example.airbnb.repository.PlaceRepository;
import com.example.airbnb.service.PlaceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlaceServiceTest {
    @InjectMocks
    private PlaceService service;

    @Mock
    private PlaceRepository repository;

    @Test
    @DisplayName("#getAllPlaces > When there are no registered places > Returns an empty list")
    void getAllPlacesWhenThereAreNoRegisteredPlacesReturnsAnEmptyList(){

        List<Place> response = service.getAllPlaces();
        Assertions.assertTrue(response.isEmpty());
    }

    @Test
    @DisplayName("#getAllPlaces > When there are registered places > Returns the list of places")
    void getAllPlacesWhenThereAreRegisteredPlacesReturnsTheListOfPlaces(){

        when(service.getAllPlaces()).thenReturn(List.of(Place.builder()
                .idPlace(1)
                .state("Minas Gerais")
                .country("Brasil")
                .address("Rua dos bobos, 0")
                .city("Pequenópolis")
                .build()));
        List<Place> response = service.getAllPlaces();
        Assertions.assertAll(
                () -> Assertions.assertEquals(1,response.get(0).getIdPlace()),
                () -> Assertions.assertEquals("Minas Gerais",response.get(0).getState()),
                () -> Assertions.assertEquals("Brasil",response.get(0).getCountry()),
                () -> Assertions.assertEquals("Rua dos bobos, 0",response.get(0).getAddress()),
                () -> Assertions.assertEquals("Pequenópolis",response.get(0).getCity())
        );
    }
    @Test
    @DisplayName("#addPlace > When the place passed is valid > Add the place > Return the added place")
    void addPlaceWhenThePlacePassedIsValidAddThePlaceReturnTheAddedPlace(){

        Place place = new Place(null,"Minas Gerais","Brasil","Rua dos bobos, 0", "Pequenópolis");

        when(repository.save(place)).thenReturn(place);

        Place savedPlace = service.addPlace(place);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedPlace),
                () -> Assertions.assertEquals("Minas Gerais",savedPlace.getState()),
                () -> Assertions.assertEquals("Brasil",savedPlace.getCountry()),
                () -> Assertions.assertEquals("Rua dos bobos, 0",savedPlace.getAddress()),
                () -> Assertions.assertEquals("Pequenópolis",savedPlace.getCity())
        );
    }

    @Test
    @DisplayName("#addPlace > When the place is null > Throw an exceptio")
    void addPlaceWhenThePlaceIsNullThrowAnExceptio(){

        Assertions.assertThrows(InvalidPlaceException.class, () ->
                service.addPlace(null));
    }

    @Test
    @DisplayName("#addPlace > When place attributes are null > Throw an excepetio")
    void addPlaceWhenPlaceAttributesAreNullThrowAnExcepetio(){

        Place place = new Place(null,null,null,null,null);
        Assertions.assertThrows(InvalidPlaceException.class, () ->
                service.addPlace(place));
    }

    @Test
    @DisplayName("#placeFindById > When the id is null > Throw an exception")
    void placeFindByIdWhenTheIdIsNullThrowAnException(){

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                service.placeFindById(null));
    }

    @Test
    @DisplayName("#placeFindById > When id not null > When a user is found > Return the user")
    void placeFindByIdWhenIdNotNullWhenAUserIsFoundReturnTheUser(){

        when(repository.findById(1)).thenReturn(Optional.of(Place.builder()
                .idPlace(1)
                .state("Minas Gerais")
                .country("Brasil")
                .address("Rua dos bobos, 0")
                .city("Pequenópolis")
                .build()));
        Place response = service.placeFindById(1);
        Assertions.assertAll(
                () -> Assertions.assertNotNull(response),
                () -> Assertions.assertEquals("Minas Gerais",response.getState()),
                () -> Assertions.assertEquals("Brasil",response.getCountry()),
                () -> Assertions.assertEquals("Rua dos bobos, 0",response.getAddress()),
                () -> Assertions.assertEquals("Pequenópolis",response.getCity())
        );
    }

    @Test
    @DisplayName("#editPlace > When the place exists > Return the changed usar and store the changes in the databasa")
    void editPlaceWhenThePlaceExistsReturnTheChangedUsarAndStoreTheChangesInTheDatabasa(){

        Integer placeId = 1;
        Place place = new Place(placeId,"Minas Gerais","Brasil","Rua dos bobos, 0", "Pequenópolis");
        Place editPlace = new Place(placeId,"Texas","Estados Unidos","Street of fools, 0","Smallville");

        when(repository.findById(placeId)).thenReturn(java.util.Optional.of(place));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Place updatedPlace = service.editPlace(placeId,editPlace);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(updatedPlace),
                () -> Assertions.assertEquals("Texas",updatedPlace.getState()),
                () -> Assertions.assertEquals("Estados Unidos",updatedPlace.getCountry()),
                () -> Assertions.assertEquals("Street of fools, 0",updatedPlace.getAddress()),
                () -> Assertions.assertEquals("Smallville",updatedPlace.getCity())
        );
    }

    @Test
    @DisplayName("#editPlace > When the place does not exist > Throw an exception")
    void editPlaceWhenThePlaceDoesNotExistThrowAnException(){

        Integer placeId = 1;
        Place place = new Place(placeId,"Minas Gerais","Brasil","Rua dos bobos, 0", "Pequenópolis");

        when(repository.findById(placeId)).thenReturn(java.util.Optional.empty());

        Assertions.assertThrows(PlaceNotFoundException.class, () ->
                service.editPlace(placeId, place));
    }

    @Test
    @DisplayName("#searchPlace > When user attributes are not null and place exists > Return a list of places")
    void searchUserWhenUserAttributesAreNotNullAndPlaceExistsReturnAListOfPlaces() {
        when(repository.findPlaceBy("Minas Gearis","Brasil","Governador Valadares")).thenReturn(List.of(Place.builder()
                .idPlace(1)
                .state("Minas Gerais")
                .city("Governador Valadares")
                .address("Rua dos bobos, 0")
                .country("Brasil")
                .build()));
        List<Place> response = service.searchPlace("Minas Gearis","Brasil","Governador Valadares");
        Assertions.assertAll(
                () -> Assertions.assertEquals(1,response.get(0).getIdPlace()),
                () -> Assertions.assertEquals("Minas Gerais",response.get(0).getState()),
                () -> Assertions.assertEquals("Governador Valadares",response.get(0).getCity()),
                () -> Assertions.assertEquals("Rua dos bobos, 0",response.get(0).getAddress()),
                () -> Assertions.assertEquals("Brasil",response.get(0).getCountry())
        );
    }

    @Test
    @DisplayName("#deleteById > When the place id is valid > Delete the place")
    void deleteByIdWhenThePlaceIdIsValidDeleteThePlace(){
        int placeId = 1;
        service.deleteById(placeId);
        verify(repository,times(1)).deleteById(1);
    }

    @Test
    @DisplayName("#deleteById > When the place id is not valid > Throw an exception")
    void deleteByIdWhenThePlaceIdIsNotValidThrowAnException(){
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                service.deleteById(null)
        );

    }
}
