package com.example.airbnb.service;

import com.example.airbnb.entity.Booking;
import com.example.airbnb.entity.Guest;
import com.example.airbnb.entity.PlaceToBook;
import com.example.airbnb.exception.InvalidBookingException;
import com.example.airbnb.repository.BookingRepository;
import com.example.airbnb.repository.GuestRepository;
import com.example.airbnb.repository.PlaceToBookRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class BookingService {
    public final PlaceToBookRepository placeToBookRepository;
    public final GuestRepository guestRepository;
    public final BookingRepository repository;
    public final PlaceToBookService placeToBookService;

    public List<Booking> getAllBookings(){
        return repository.findAll();
    }

    public Booking addBooking(Booking booking){
        if(Objects.isNull(booking) || Objects.isNull(booking.getArrivalDate())
                || Objects.isNull(booking.getDepartureDate())
                || Objects.isNull(booking.getNPeople())
                || Objects.isNull(booking.getGuest())
                || Objects.isNull(booking.getPlaceToBook())){
            throw new InvalidBookingException("Erro ao cadastrar o placeToBook!");
        }

        PlaceToBook placeToBook = booking.getPlaceToBook();
        Guest guest = booking.getGuest();
        Integer nPeople = booking.getNPeople();

        if(nPeople > placeToBook.getCapacity()){
            throw new InvalidBookingException("Capacidade excedida! o Número de pessoas é maior do que o número disponível para o placeToBook");
        }

        if(placeToBook.getReservationStatus()){
            throw new InvalidBookingException("Já existe uma reserva atual para esse placeToBook!");
        }

        if(guest.getBookingStatus()){
            throw new InvalidBookingException("O usuário já possui uma reserva ativa, finalize a reserva atual antes de iniciar outra!!");
        }

        if(guest.getUser().getIdUser() == placeToBook.getHost().getUser().getIdUser()){
            throw new InvalidBookingException("O usuário não pode reservar seu próprio apartamento!");
        }

        placeToBook.setReservationStatus(true);
        guest.setBookingStatus(true);

        guestRepository.save(guest);
        placeToBookRepository.save(placeToBook);
        placeToBookService.updateHostAvailabilityStatus();
        return repository.save(booking);

    }


}
