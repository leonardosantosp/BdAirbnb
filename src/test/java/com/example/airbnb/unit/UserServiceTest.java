package com.example.airbnb.unit;

import com.example.airbnb.entity.User;
import com.example.airbnb.exception.InvalidUserException;
import com.example.airbnb.exception.UserNotFoundException;
import com.example.airbnb.repository.UserRepository;
import com.example.airbnb.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService service;

    @Mock
    private UserRepository repository;

    @Test
    @DisplayName("#getAllUsers > When there are no registered users > Returns an empty list")
    void getAllUsersWhenThereAreNoRegisteredUsersReturnsAnEmptyList(){
        List<User> response = service.getAllUsers();
        Assertions.assertTrue(response.isEmpty());
    }
    @Test
    @DisplayName("#getAllUsers > When there are registered users > Returns the list of users")
    void getAllUsersWhenThereAreRegisteredUsersReturnsTheListOfUsers(){
        when(service.getAllUsers()).thenReturn(List.of(User.builder()
                .idUser(1)
                .name("Leandro")
                .email("leandro@email.com")
                .password("leandro321")
                .phoneNumber("123456789")
                .build()));
        List<User> response = service.getAllUsers();
        Assertions.assertAll(
                () -> Assertions.assertEquals(1,response.get(0).getIdUser()),
                () -> Assertions.assertEquals("Leandro",response.get(0).getName()),
                () -> Assertions.assertEquals("leandro@email.com",response.get(0).getEmail()),
                () -> Assertions.assertEquals("leandro321",response.get(0).getPassword()),
                () -> Assertions.assertEquals("123456789",response.get(0).getPhoneNumber())

        );
    }

    @Test
    @DisplayName("#addUser > When the User passed is valid > Add the User > Return the added user")
    void addUserWhenTheUserPassedIsValidAddTheUserReturnTheAddedUser(){
        User user = new User(null,"Leandro","leandro@email.com","leandro123","123456789");

        when(repository.save(user)).thenReturn(user);

        User savedUser = service.addUser(user);

        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals("Leandro", savedUser.getName());
        Assertions.assertEquals("leandro@email.com", savedUser.getEmail());
        Assertions.assertEquals("leandro123", savedUser.getPassword());
        Assertions.assertEquals("123456789", savedUser.getPhoneNumber());
    }

    @Test
    @DisplayName("#addUser > When the user is null > Throw an exception")
    void addUserWhenTheUserIsNullThrowAnException(){

        Assertions.assertThrows(InvalidUserException.class, () ->
                service.addUser(null));
    }

    @Test
    @DisplayName("#addUser > When user attributes are null > Throw an exception")
    void addUserWhenUserAttributesAreNullThrowAnException(){

        User user = new User(null,null,null,null,null);
        Assertions.assertThrows(InvalidUserException.class, () ->
                service.addUser(user));
    }

    @Test
    @DisplayName("#userFindById > When the id is null > Throw an exception")
    void userFindByIdWhenTheIdIsNullThrowAnException() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                service.userFindById(null));
    }

    @Test
    @DisplayName("#userFindById > When the id is not null > When a user is found > Return the user")
    void userFindByIDWhenTheIdIsNotNullWhenAUserIsFoundReturnTheUser() {
        when(repository.findById(1)).thenReturn(Optional.of(User.builder()
                .idUser(1)
                .name("Leandro")
                .email("leandro@email.com")
                .password("leandro321")
                .phoneNumber("123456789")
                .build()));
        User response = service.userFindById(1);
        Assertions.assertAll(
                () -> Assertions.assertEquals(1,response.getIdUser()),
                () -> Assertions.assertEquals("Leandro",response.getName()),
                () -> Assertions.assertEquals("leandro@email.com",response.getEmail()),
                () -> Assertions.assertEquals("leandro321",response.getPassword()),
                () -> Assertions.assertEquals("123456789",response.getPhoneNumber())
        );
    }
    @Test
    @DisplayName("#editUser > When the User exists > Return the changed user and store the changes in the database")
    void editUserWhenTheUserExistsReturnTheChangedUserAndStoreTheChangesInTheDatabase(){

        Integer userId = 1;
        User existingUser = new User(userId, "Leandro", "leandro@email.com", "leandro123", "123456789");
        User newUser = new User(userId,"Leandro Novo Nome", "novoleandro@email.com", "novoleandro123", "987654321");

        when(repository.findById(userId)).thenReturn(java.util.Optional.of(existingUser));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = service.editUser(userId, newUser);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(updatedUser),
                () -> Assertions.assertEquals("Leandro Novo Nome", updatedUser.getName()),
                () -> Assertions.assertEquals("novoleandro@email.com", updatedUser.getEmail()),
                () -> Assertions.assertEquals("novoleandro123", updatedUser.getPassword()),
                () -> Assertions.assertEquals("987654321", updatedUser.getPhoneNumber())
        );
    }

    @Test
    @DisplayName("#editUser > When the user does not exist > Throw an exception")
    void editUserWhenTheUserDoesNotExistThrowAnException(){

        Integer userId = 1;
        User newUser = new User(userId,"Leandro Novo Nome", "novoleandro@email.com", "novoleandro123", "987654321");

        when(repository.findById(userId)).thenReturn(java.util.Optional.empty());

        Assertions.assertThrows(UserNotFoundException.class, () ->
                service.editUser(userId, newUser));

    }

    @Test
    @DisplayName("#searchUser > When user attributes are not null and user exists > Return a list of users")
    void searchUserWhenUserAttributesAreNotNullAndUserExistsReturnAListOfUsers() {
        when(repository.findUserBy("Leandro","leandro@email.com","123456789")).thenReturn(List.of(User.builder()
                .idUser(1)
                .name("Leandro")
                .email("leandro@email.com")
                .password("leandro321")
                .phoneNumber("123456789")
                .build()));
        List<User> response = service.searchUser("Leandro","leandro@email.com","123456789");
        Assertions.assertAll(
                () -> Assertions.assertEquals("Leandro",response.get(0).getName()),
                () -> Assertions.assertEquals("leandro@email.com",response.get(0).getEmail()),
                () -> Assertions.assertEquals("leandro321",response.get(0).getPassword()),
                () -> Assertions.assertEquals("123456789",response.get(0).getPhoneNumber())
        );
    }

    @Test
    @DisplayName("#autenticate > When user attributes are not null and user exists > Return true")
    void autenticateWhenUserAttributesAreNotNullAndUserExistsReturnTrue() {
        when(repository.findByEmailAndPassword("leandro@email.com", "leandro321")).thenReturn(User.builder()
                .idUser(1)
                .name("Leandro")
                .email("leandro@email.com")
                .password("leandro321")
                .phoneNumber("123456789")
                .build());

        boolean authenticated = service.autenticate("leandro@email.com", "leandro321");

        Assertions.assertTrue(authenticated);
    }


    @Test
    @DisplayName("#autenticate > When user credentials are invalids > Return false")
    void autenticateWhenUserCredentialsAreInvalidsReturnFalse() {
        when(repository.findByEmailAndPassword("invalido@email.com", "senhaerrada"))
                .thenReturn(null);

        boolean authenticated = service.autenticate("invalido@email.com", "senhaerrada");

        Assertions.assertFalse(authenticated);
    }

    @Test
    @DisplayName("#autenticate > When email is null > Throw an exception")
    void autenticateWhenEmailIsNullThrowAnException(){

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                service.autenticate(null,"senhaerrada")
        );

    }

    @Test
    @DisplayName("#autenticate > When password is null > Throw an exception")
    void autenticateWhenPasswordIsNullThrowAnException(){

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                service.autenticate("invalido@email.com",null)
        );
    }
    
    @Test
    @DisplayName("#deleteById > When the user id is valid > Delete the user")
    void deleteByIdWhenTheUserIdIsValidDeleteTheUser(){
        
        int userId = 1;
        service.deleteById(userId);
        verify(repository,times(1)).deleteById(1);
    }

    @Test
    @DisplayName("#deleteById > When the user id is not valid > Throw an exception")
    void deleteByIdWhenTheUserIdIsNotValidThrowAnException(){
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                service.deleteById(null)
        );

    }






}
