package com.example.airbnb.unit;

import com.example.airbnb.entity.Guest;
import com.example.airbnb.entity.User;
import com.example.airbnb.exception.InvalidGuestException;
import com.example.airbnb.repository.GuestRepository;
import com.example.airbnb.service.GuestService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GuestServiceTest {

    @InjectMocks
    private GuestService service;

    @Mock
    private GuestRepository repository;

    @Test
    @DisplayName("#getAllGuests > When there are no registered guests > Return an empty list")
    void getAllGuestsWhenThereAreNoRegisteredGuestsReturnAnEmptyList(){
        List<Guest> guests =  service.getAllGuests();
        Assertions.assertTrue(guests.isEmpty());
    }
    @Test
    @DisplayName("#getAllGuest > When there are registered guests > Returns the list of guests")
    void getAllGuestsWhenThereAreRegisteredGuestsReturnsTheListOfGuests(){
        User user = new User(1, "Leandro", "leandro@email.com", "leandro123", "123456789");
        when(service.getAllGuests()).thenReturn(List.of(Guest.builder()
                .idGuest(1)
                .bookingStatus(Boolean.TRUE)
                .user(user)
                .build()));
        List<Guest> response = service.getAllGuests();
        Assertions.assertAll(
                () -> Assertions.assertEquals(1,response.get(0).getIdGuest()),
                () -> Assertions.assertEquals(Boolean.TRUE,response.get(0).getBookingStatus()),
                () -> Assertions.assertEquals("Leandro",response.get(0).getUser().getName()),
                () -> Assertions.assertEquals("leandro@email.com",response.get(0).getUser().getEmail()),
                () -> Assertions.assertEquals("leandro123",response.get(0).getUser().getPassword()),
                () -> Assertions.assertEquals("123456789",response.get(0).getUser().getPhoneNumber())

        );
    }

    @Test
    @DisplayName("#guestFindById > When the id is null > Throw an exception")
    void guestFindByIdWhenTheIdIsNullThrowAnException() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                service.guestFindById(null));
    }

    @Test
    @DisplayName("#guestFindById > When the id is not null > When a host is found > Return the guest")
    void userFindByIDWhenTheIdIsNotNullWhenAHostIsFoundReturnTheHost() {
        User user = new User(1,"Leandro","leandro@email.com","leandro321","123456789" );
        when(repository.findById(1)).thenReturn(Optional.of(Guest.builder()
                .idGuest(1)
                .bookingStatus(Boolean.TRUE)
                .user(user)
                .build()));
        Guest response = service.guestFindById(1);
        Assertions.assertAll(
                () -> Assertions.assertEquals(1,response.getIdGuest()),
                () -> Assertions.assertEquals(Boolean.TRUE,response.getBookingStatus()),
                () -> Assertions.assertEquals("Leandro",response.getUser().getName()),
                () -> Assertions.assertEquals("leandro@email.com",response.getUser().getEmail()),
                () -> Assertions.assertEquals("leandro321",response.getUser().getPassword()),
                () -> Assertions.assertEquals("123456789",response.getUser().getPhoneNumber())
        );
    }

    @Test
    @DisplayName("#addGuest > When the Guest passed is valid > Add the Guest > Return the added guest")
    void addUserWhenTheGuestPassedIsValidAddTheGuestReturnTheAddedGuest(){
        User user = new User(1,"Leandro","leandro@email.com","leandro123","123456789");
        Guest guest = new Guest(1,Boolean.FALSE,user);
        when(repository.save(guest)).thenReturn(guest);

        Guest savedGuest = service.addGuest(guest);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedGuest),
                () -> Assertions.assertEquals(1,savedGuest.getIdGuest()),
                () -> Assertions.assertEquals(Boolean.FALSE,savedGuest.getBookingStatus()),
                () -> Assertions.assertEquals(1,savedGuest.getUser().getIdUser()),
                () -> Assertions.assertEquals("Leandro", savedGuest.getUser().getName()),
                () -> Assertions.assertEquals("leandro@email.com", savedGuest.getUser().getEmail()),
                () -> Assertions.assertEquals("leandro123", savedGuest.getUser().getPassword()),
                () -> Assertions.assertEquals("123456789", savedGuest.getUser().getPhoneNumber())
        );
    }

    @Test
    @DisplayName("#addGuest > When the guest is null > Throw an exception")
    void addGuestWhenTheGuestIsNullThrowAnException(){

        Assertions.assertThrows(NullPointerException.class, () ->
                service.addGuest(null));
    }

    @Test
    @DisplayName("#addGuest > When guest attributes are null > Throw an exception")
    void addGuestWhenGuestAttributesAreNullThrowAnException(){

        Guest guest = new Guest(null,null,null);
        Assertions.assertThrows(InvalidGuestException.class, () ->
                service.addGuest(guest));
    }

}
