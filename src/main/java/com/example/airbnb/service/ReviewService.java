package com.example.airbnb.service;

import com.example.airbnb.entity.Host;
import com.example.airbnb.entity.PlaceToBook;
import com.example.airbnb.entity.Review;
import com.example.airbnb.exception.InvalidHostException;
import com.example.airbnb.exception.InvalidReviewException;
import com.example.airbnb.exception.PlaceNotFoundException;
import com.example.airbnb.exception.ReviewNotFoundException;
import com.example.airbnb.repository.HostRepository;
import com.example.airbnb.repository.PlaceToBookRepository;
import com.example.airbnb.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
@AllArgsConstructor
@Service
public class ReviewService {

    public ReviewRepository repository;
    public PlaceToBookRepository placeToBookRepository;
    public HostRepository hostRepository;

    public List<Review> getAllReviews(){
        return repository.findAll();
    }

    public Review addReview(Review review){
        if(Objects.isNull(review) || Objects.isNull(review.getReviewValue())
                || Objects.isNull(review.getTextReview())
                || Objects.isNull(review.getPlaceToBook())
                || Objects.isNull(review.getGuest())){
            throw new InvalidReviewException("Erro ao cadastrar a review!");
        }

        Review savedReview = repository.save(review);

        PlaceToBook placeToBook = review.getPlaceToBook();
        Host host = review.getPlaceToBook().getHost();

        List<Review> reviews = repository.findByPlaceToBook(placeToBook);

        double sumReview = reviews.stream().mapToDouble(Review::getReviewValue).sum();
        int nReview = reviews.size();

        double meanReview = (nReview > 0) ? sumReview / nReview : 0.0;

        placeToBook.setSumReview(sumReview);
        placeToBook.setNReview((double) nReview);
        placeToBook.setMeanReview(meanReview);

        Double averageRating = hostRepository.calculateAverageMeanReviewByHost(host.getIdHost());
        host.setAverageRating(averageRating);
        hostRepository.save(host);
        placeToBookRepository.save(placeToBook);


        return savedReview;

    }

    public Review ReviewFindById(Integer id){
        if (Objects.isNull(id)){
            throw new IllegalArgumentException("Id null when fetching for an review.");
        }
        return repository.findById(id).orElseThrow(() ->
                new InvalidReviewException(
                        String.format("No review found for id %d",id))
        );

    }

    public Review editReview(Integer id, Review newReview){
        Review existingReview = repository.findById(id)
                .orElseThrow(() -> new ReviewNotFoundException("PlaceTobook não encontrado!"));
        existingReview.setTextReview(newReview.getTextReview());
        return repository.save(existingReview);
    }

    public void deleteById(Integer id) {
        if (Objects.isNull(id)){
            throw new IllegalArgumentException("Id null when fetching for an user.");
        }
        repository.deleteById(id);
    }

}
