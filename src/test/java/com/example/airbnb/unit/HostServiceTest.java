package com.example.airbnb.unit;

import com.example.airbnb.entity.Host;
import com.example.airbnb.entity.User;
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
    void getAllUsersWhenThereAreRegisteredUsersReturnsTheListOfUsers(){
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
}
