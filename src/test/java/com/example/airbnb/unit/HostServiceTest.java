package com.example.airbnb.unit;

import com.example.airbnb.entity.Guest;
import com.example.airbnb.entity.Host;
import com.example.airbnb.entity.User;
import com.example.airbnb.exception.HostNotFoundException;
import com.example.airbnb.exception.InvalidGuestException;
import com.example.airbnb.exception.InvalidHostException;
import com.example.airbnb.exception.UserNotFoundException;
import com.example.airbnb.repository.HostRepository;
import com.example.airbnb.service.HostService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HostServiceTest {
    @InjectMocks
    private HostService service;

    @Mock
    private HostRepository repository;

    @Test
    @DisplayName("#getAllHosts > When there are no registered hosts > Return an empty list")
    void getAllHostsWhenThereAreNoRegisteredHostsReturnAnEmptyList(){
        List<Host> hosts =  service.getAllHosts();
        Assertions.assertTrue(hosts.isEmpty());
    }
    @Test
    @DisplayName("#getAllHosts > When there are registered users > Returns the list of users")
    void getAllHostsWhenThereAreRegisteredHostsReturnsTheListOfHosts(){
        User user = new User(1, "Leandro", "leandro@email.com", "leandro123", "123456789");
        when(service.getAllHosts()).thenReturn(List.of(Host.builder()
                .idHost(1)
                .nPlaceToBook(0)
                .averageRating(0.0)
                .availabilityStatus(Boolean.FALSE)
                .user(user)
                .build()));
        List<Host> response = service.getAllHosts();
        Assertions.assertAll(
                () -> Assertions.assertEquals(1,response.get(0).getIdHost()),
                () -> Assertions.assertEquals(0,response.get(0).getNPlaceToBook()),
                () -> Assertions.assertEquals(0.0,response.get(0).getAverageRating()),
                () -> Assertions.assertEquals(Boolean.FALSE,response.get(0).getAvailabilityStatus()),
                () -> Assertions.assertEquals("Leandro",response.get(0).getUser().getName()),
                () -> Assertions.assertEquals("leandro@email.com",response.get(0).getUser().getEmail()),
                () -> Assertions.assertEquals("leandro123",response.get(0).getUser().getPassword()),
                () -> Assertions.assertEquals("123456789",response.get(0).getUser().getPhoneNumber())

        );
    }

    @Test
    @DisplayName("#hostFindById > When the id is null > Throw an exception")
    void hostFindByIdWhenTheIdIsNullThrowAnException() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                service.hostFindById(null));
    }

    @Test
    @DisplayName("#hostFindById > When the id is not null > When a host is found > Return the host")
    void userFindByIDWhenTheIdIsNotNullWhenAHostIsFoundReturnTheHost() {
        User user = new User(1,"Leandro","leandro@email.com","leandro321","123456789" );
        when(repository.findById(1)).thenReturn(Optional.of(Host.builder()
                .idHost(1)
                        .nPlaceToBook(0)
                        .availabilityStatus(Boolean.FALSE)
                        .averageRating(0.0)
                        .user(user)
                .build()));
        Host response = service.hostFindById(1);
        Assertions.assertAll(
                () -> Assertions.assertEquals(1,response.getIdHost()),
                () -> Assertions.assertEquals(0,response.getNPlaceToBook()),
                () -> Assertions.assertEquals(Boolean.FALSE,response.getAvailabilityStatus()),
                () -> Assertions.assertEquals(0.0,response.getAverageRating()),
                () -> Assertions.assertEquals("Leandro",response.getUser().getName()),
                () -> Assertions.assertEquals("leandro@email.com",response.getUser().getEmail()),
                () -> Assertions.assertEquals("leandro321",response.getUser().getPassword()),
                () -> Assertions.assertEquals("123456789",response.getUser().getPhoneNumber())
        );
    }

    @Test
    @DisplayName("#editHost > When the Host exists > Return the changed Host and store the changes in the database")
    void editHostWhenTheHostExistsReturnTheChangedHostAndStoreTheChangesInTheDatabase(){

        Integer id = 1;
        User existingUser = new User(id, "Leandro", "leandro@email.com", "leandro123", "123456789");
        Host existingHost = new Host(id,0,Boolean.FALSE,0.0,existingUser);
        Host newHost = new Host(id, 5,Boolean.TRUE,4.3,existingUser);

        when(repository.findById(id)).thenReturn(java.util.Optional.of(existingHost));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Host updatedHost = service.editHost(id, newHost);

        Assertions.assertAll(
                () -> Assertions.assertEquals(1,updatedHost.getIdHost()),
                () -> Assertions.assertEquals(5,updatedHost.getNPlaceToBook()),
                () -> Assertions.assertEquals(Boolean.TRUE,updatedHost.getAvailabilityStatus()),
                () -> Assertions.assertEquals(4.3,updatedHost.getAverageRating()),
                () -> Assertions.assertNotNull(updatedHost),
                () -> Assertions.assertEquals(1, updatedHost.getUser().getIdUser()),
                () -> Assertions.assertEquals("Leandro", updatedHost.getUser().getName()),
                () -> Assertions.assertEquals("leandro@email.com", updatedHost.getUser().getEmail()),
                () -> Assertions.assertEquals("leandro123", updatedHost.getUser().getPassword()),
                () -> Assertions.assertEquals("123456789", updatedHost.getUser().getPhoneNumber())
        );
    }

    @Test
    @DisplayName("#editHost > When the host does not exist > Throw an exception")
    void editHostWhenTheHostDoesNotExistThrowAnException(){
        Integer id = 1;
        User user = new User(id,"Leandro Novo Nome", "novoleandro@email.com", "novoleandro123", "987654321");
        Host newHost = new Host(id,0,Boolean.FALSE,0.0,user);

        when(repository.findById(id)).thenReturn(java.util.Optional.empty());

        Assertions.assertThrows(HostNotFoundException.class, () ->
                service.editHost(id, newHost));

    }

    @Test
    @DisplayName("#addHost > When the host is null > Throw an exception")
    void addHostWhenTheHostIsNullThrowAnException(){

        Assertions.assertThrows(NullPointerException.class, () ->
                service.addHost(null));
    }

    @Test
    @DisplayName("#addHost > When host attributes are null > Throw an exception")
    void addHostWhenHostAttributesAreNullThrowAnException(){

        Host host = new Host(null,null,null,null,null);
        Assertions.assertThrows(InvalidHostException.class, () ->
                service.addHost(host));
    }@Test
    @DisplayName("#addHost > When the Host passed is valid > Add the Host > Return the added Host")
    void addUserWhenTheHostPassedIsValidAddTheHostReturnTheAddedHost(){
        User user = new User(1,"Leandro","leandro@email.com","leandro123","123456789");
        Host host = new Host(1,0,Boolean.FALSE,0.0,user);
        when(repository.save(host)).thenReturn(host);

        Host savedHost = service.addHost(host);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedHost),
                () -> Assertions.assertEquals(1,savedHost.getIdHost()),
                () -> Assertions.assertEquals(0,savedHost.getNPlaceToBook()),
                () -> Assertions.assertEquals(0.0,savedHost.getAverageRating()),
                () -> Assertions.assertEquals(Boolean.FALSE,savedHost.getAvailabilityStatus()),
                () -> Assertions.assertEquals(1,savedHost.getUser().getIdUser()),
                () -> Assertions.assertEquals("Leandro", savedHost.getUser().getName()),
                () -> Assertions.assertEquals("leandro@email.com", savedHost.getUser().getEmail()),
                () -> Assertions.assertEquals("leandro123", savedHost.getUser().getPassword()),
                () -> Assertions.assertEquals("123456789", savedHost.getUser().getPhoneNumber())
        );
    }
}
