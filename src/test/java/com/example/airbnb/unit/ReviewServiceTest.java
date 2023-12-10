package com.example.airbnb.unit;

import com.example.airbnb.entity.*;
import com.example.airbnb.exception.InvalidGuestException;
import com.example.airbnb.exception.InvalidReviewException;
import com.example.airbnb.exception.ReviewNotFoundException;
import com.example.airbnb.exception.UserNotFoundException;
import com.example.airbnb.repository.HostRepository;
import com.example.airbnb.repository.PlaceToBookRepository;
import com.example.airbnb.repository.ReviewRepository;
import com.example.airbnb.service.ReviewService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @InjectMocks
    private ReviewService service;

    @Mock
    private ReviewRepository repository;

    @Mock
    private HostRepository hostRepository;

    @Mock
    private PlaceToBookRepository placeToBookRepository;

    @Test
    @DisplayName("#getAllReviews > When there are no registered reviews > Returns an empty list")
    void getAllReviewsWhenThereAreNoRegisteredReviewsReturnsAnEmptyList(){
        List<Review> response = service.getAllReviews();
        Assertions.assertTrue(response.isEmpty());
    }
    @Test
    @DisplayName("#getAllReviews > When there are registered review > Returns the list of reviews")
    void getAllReviewsWhenThereAreRegisteredReviewsReturnsTheListOfReviews(){
        User user = new User(1,"Leandro","leandro@email.com","leandro123","123456789");
        Host host = new Host(1,0,Boolean.FALSE,0.0,user);
        Guest guest = new Guest(1,Boolean.FALSE,user);
        Place place = new Place(1,"Minas Gerais","Brasil","Rua dos bobos, 0", "Governador Valadares");
        PlaceToBook placeToBook = new PlaceToBook(1,1,0.0,1,1,500.0,1,1,Boolean.FALSE,place,host,0.0,0.0);
        when(service.getAllReviews()).thenReturn(List.of(Review.builder()
                .idReview(1)
                        .textReview("Excelente!!!")
                        .reviewValue(5)
                        .placeToBook(placeToBook)
                        .guest(guest)
                .build()));
        List<Review> response = service.getAllReviews();
        Assertions.assertAll(
                () -> Assertions.assertEquals(1,response.get(0).getIdReview()),
                () -> Assertions.assertEquals("Excelente!!!",response.get(0).getTextReview()),
                () -> Assertions.assertEquals(5,response.get(0).getReviewValue())
        );
    }

    @Test
    @DisplayName("#addReview > When the Review passed is valid > Add the Review > Return the Review")
    void addReviewWhenTheReviewPassedIsValidAddTheReviewReturnTheReview() {

        User user = new User(1,"Leandro","leandro@email.com","leandro123","123456789");
        Host host = new Host(1,0,Boolean.FALSE,0.0,user);
        Guest guest = new Guest(1,Boolean.FALSE,user);
        Place place = new Place(1,"Minas Gerais","Brasil","Rua dos bobos, 0", "Governador Valadares");
        PlaceToBook placeToBook = new PlaceToBook(1,1,0.0,1,1,500.0,1,1,Boolean.FALSE,place,host,0.0,0.0);
        Review review = new Review(1,5,"Excelenete", placeToBook,guest);

        when(repository.save(any(Review.class))).thenReturn(review);
        when(repository.findByPlaceToBook(placeToBook)).thenReturn(new ArrayList<>());

        when(hostRepository.calculateAverageMeanReviewByHost(host.getIdHost())).thenReturn(4.2);

        Review savedReview = service.addReview(review);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedReview),
                () -> Assertions.assertEquals(review, savedReview),
                () -> Assertions.assertEquals(0.0, placeToBook.getSumReview()),
                () -> Assertions.assertEquals(0.0, placeToBook.getNReview()),
                () -> Assertions.assertEquals(0.0, placeToBook.getMeanReview()),
                () ->Assertions.assertEquals(4.2, host.getAverageRating())
        );

    }

    @Test
    @DisplayName("#addReview > When the Review is null > Throw an exception")
    void addReviewWhenTheReviewIsNullThrowAnException(){

        Assertions.assertThrows(InvalidReviewException.class, () ->
                service.addReview(null));
    }

    @Test
    @DisplayName("#addReview > When review attributes are null > Throw an exception")
    void addReviewWhenReviewAttributesAreNullThrowAnException(){

        Review review = new Review(null,null,null,null,null);
        Assertions.assertThrows(InvalidReviewException.class, () ->
                service.addReview(review));
    }

    @Test
    @DisplayName("#reviewFindById > When the id is null > Throw an exception")
    void reviewFindByIdWhenTheIdIsNullThrowAnException() {
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                service.ReviewFindById(null));
    }

    @Test
    @DisplayName("#reviewFindById > When the id is not null > When a review is found > Return the review")
    void reviewFindByIDWhenTheIdIsNotNullWhenAReviewIsFoundReturnTheReview() {

        User user = new User(1,"Leandro","leandro@email.com","leandro123","123456789");
        Host host = new Host(1,0,Boolean.FALSE,0.0,user);
        Guest guest = new Guest(1,Boolean.FALSE,user);
        Place place = new Place(1,"Minas Gerais","Brasil","Rua dos bobos, 0", "Governador Valadares");
        PlaceToBook placeToBook = new PlaceToBook(1,1,0.0,1,1,500.0,1,1,Boolean.FALSE,place,host,0.0,0.0);

        when(repository.findById(1)).thenReturn(Optional.of(Review.builder()
                .idReview(1)
                .reviewValue(5)
                .textReview("Excelente!!!")
                .placeToBook(placeToBook)
                .guest(guest)
                .build()));
        Review response = service.ReviewFindById(1);
        Assertions.assertAll(
                () -> Assertions.assertEquals(1,response.getIdReview()),
                () -> Assertions.assertEquals(5,response.getReviewValue()),
                () -> Assertions.assertEquals("Excelente!!!",response.getTextReview())
        );
    }

    @Test
    @DisplayName("#editReview > When the Review exists > Return the changed Review and store the changes in the database")
    void editReviewWhenTheReviewExistsReturnTheChangedReviewAndStoreTheChangesInTheDatabase(){
        User user = new User(1,"Leandro","leandro@email.com","leandro123","123456789");
        Host host = new Host(1,0,Boolean.FALSE,0.0,user);
        Guest guest = new Guest(1,Boolean.FALSE,user);
        Place place = new Place(1,"Minas Gerais","Brasil","Rua dos bobos, 0", "Governador Valadares");
        PlaceToBook placeToBook = new PlaceToBook(1,1,0.0,1,1,500.0,1,1,Boolean.FALSE,place,host,0.0,0.0);
        Review existingReview = new Review(1,5,"Excelenete", placeToBook,guest);

        Review newReview = new Review(1,5,"Não tão excelente!!!",placeToBook,guest);

        when(repository.findById(1)).thenReturn(java.util.Optional.of(existingReview));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Review updatedReview = service.editReview(1, newReview);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(updatedReview),
                () -> Assertions.assertEquals(5, updatedReview.getReviewValue()),
                () -> Assertions.assertEquals("Não tão excelente!!!",updatedReview.getTextReview())
        );
    }

    @Test
    @DisplayName("#editReview > When the review does not exist > Throw an exception")
    void editReviewWhenTheReviewDoesNotExistThrowAnException(){

        User user = new User(1,"Leandro","leandro@email.com","leandro123","123456789");
        Host host = new Host(1,0,Boolean.FALSE,0.0,user);
        Guest guest = new Guest(1,Boolean.FALSE,user);
        Place place = new Place(1,"Minas Gerais","Brasil","Rua dos bobos, 0", "Governador Valadares");
        PlaceToBook placeToBook = new PlaceToBook(1,1,0.0,1,1,500.0,1,1,Boolean.FALSE,place,host,0.0,0.0);
        Review review = new Review(1,5,"Excelenete", placeToBook,guest);
        when(repository.findById(1)).thenReturn(java.util.Optional.empty());

        Assertions.assertThrows(ReviewNotFoundException.class, () ->
                service.editReview(1, review));

    }

    @Test
    @DisplayName("#deleteById > When the review id is valid > Delete the review")
    void deleteByIdWhenTheReviewIdIsValidDeleteTheReview(){

        int id = 1;
        service.deleteById(id);
        verify(repository,times(1)).deleteById(1);
    }

    @Test
    @DisplayName("#deleteById > When the review id is not valid > Throw an exception")
    void deleteByIdWhenTheReviewIdIsNotValidThrowAnException(){
        Assertions.assertThrows(IllegalArgumentException.class, () ->
                service.deleteById(null)
        );
}

}