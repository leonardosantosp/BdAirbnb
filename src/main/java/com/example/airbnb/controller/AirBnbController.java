package com.example.airbnb.controller;

import com.example.airbnb.entity.*;
import com.example.airbnb.exception.PlaceNotFoundException;
import com.example.airbnb.exception.PlaceToBookNotFoundException;
import com.example.airbnb.exception.UserNotFoundException;
import com.example.airbnb.service.*;
import org.springframework.ui.Model;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@AllArgsConstructor
@Controller
public class AirBnbController {

    private final UserService service;
    private final PlaceService placeService;
    private final HostService hostService;
    private final GuestService guestService;
    private final PlaceToBookService placeToBookService;
    private final ReviewService reviewService;
    private final BookingService bookingService;

    /* Home */
    @GetMapping("/")
    public String getLogin() {
        return "login";
    }

    @GetMapping("/home")
    public String getHome(Model model){
        return "home";
    }

    @PostMapping(value = "/home")
    public String login(@ModelAttribute User user, Model model) {
        if (service.autenticate(user.getEmail(), user.getPassword())) {
            return "/home";
        } else {
            model.addAttribute("mensagem", "Falha na autenticação");
            return "/login";
        }
    }

    /* User */
    @GetMapping("/users")
    public String user(Model model) {
        List<User> users = service.getAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @GetMapping("/user")
    public String user(User user) {
        return "newUser";
    }

    @PostMapping("/user")
    public String newUser(@ModelAttribute("user") User user) {
        log.info("Entrou no cadastro de usuário");
        service.addUser(user);
        User addUser = service.addUser(user);
        return "redirect:/users";
    }

    @GetMapping("/deleteuser/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        try {
            service.deleteById(id);
            return "redirect:/users";
        } catch (UserNotFoundException exception) {
            return "redirect:/users?error=UserNotFound";
        }
    }


    @GetMapping("user/{id}")
    public String showUser(@PathVariable("id") Integer id,
                           Model model){
        User user = service.userFindById(id);
        model.addAttribute("user", user);
        return "showUser";
    }

    @GetMapping("edituser/{id}")
    public String editUser(@PathVariable("id") Integer id, Model model){
        try {
            User user = service.userFindById(id);
            model.addAttribute("user", user);
            return "edituser";
        } catch (UserNotFoundException e) {
            // Se o usuário não for encontrado, redirecione para a tela de novo usuário
            return "redirect:/user";
        }
    }

    @PostMapping("/edituser/{id}")
    public String editUser(@PathVariable("id") Integer id, @ModelAttribute User user, Model model){
        try {
            log.info("Entrou na edição de usuario!");
            User updatedUser = service.editUser(id, user);
            return "redirect:/user/" + updatedUser.getIdUser();
        } catch (UserNotFoundException e) {
            // Se o usuário não for encontrado, redirecione para a tela de novo usuário
            return "redirect:/user";
        }
    }


    @GetMapping("/searchuser")
    public String searchUser(@RequestParam(required = false) String name,
                             @RequestParam(required = false) String email,
                             @RequestParam(required = false) String phoneNumber,
                             Model model
    ){
        List<User> user = service.searchUser(name, email, phoneNumber);
        model.addAttribute("user", user);
        return "searchUser";

    }

    /* Place */
    @GetMapping("/places")
    public String place(Model model) {
        List<Place> places = placeService.getAllPlaces();
        model.addAttribute("places", places);
        return "places";
    }

    @GetMapping("/place")
    public String place(Place place) {
        return "newPlace";
    }

    @PostMapping("/place")
    public String newPlace(@ModelAttribute("place") Place place) {
        log.info("Entrou no cadastro de Locais");
        placeService.addPlace(place);
        Place addPlace = placeService.addPlace(place);
        return "redirect:/places";
    }

    @GetMapping("place/{id}")
    public String showPlace(@PathVariable("id") Integer id,
                            Model model){
        Place place = placeService.placeFindById(id);
        model.addAttribute("place", place);
        return "showPlace";
    }

    @GetMapping("editplace/{id}")
    public String editPlace(@PathVariable("id") Integer id, Model model){
        try {
            Place place = placeService.placeFindById(id);
            model.addAttribute("place", place);
            return "editplace";
        } catch (PlaceNotFoundException e) {
            // Se o place não for encontrado, redirecione para a tela de novo place
            return "redirect:/place";
        }
    }

    @PostMapping ("/editplace/{id}")
    public String editPlace(@PathVariable("id") Integer id, @ModelAttribute Place place, Model model){
        try {
            log.info("Entrou na edição de local!");
            Place updatedPlace = placeService.editPlace(id, place);
            return "redirect:/place/" + updatedPlace.getIdPlace();
        } catch (PlaceNotFoundException e) {
            // Se o usuário não for encontrado, redirecione para a tela de novo usuário
            return "redirect:/place";
        }
    }

    @GetMapping("/searchplace")
    public String searchPlace(@RequestParam(required = false) String state,
                              @RequestParam(required = false) String country,
                              @RequestParam(required = false) String city,
                              Model model
    ){
        List<Place> place = placeService.searchPlace(state, country, city);
        model.addAttribute("place", place);
        return "searchPlace";

    }

    @GetMapping("/deleteplace/{id}")
    public String deletePlace(@PathVariable("id") Integer id) {
        try {
            placeService.deleteById(id);
            return "redirect:/places";
        } catch (UserNotFoundException exception) {
            return "redirect:/places?error=PlaceNotFound";
        }
    }

    /* Host */
    @GetMapping("/hosts")
    public String host(Model model) {
        List<Host> hosts = hostService.getAllHosts();
        model.addAttribute("hosts", hosts);
        return "hosts";
    }

    @GetMapping("/host")
    public String newHost(Model model) {
        List<User> users = service.getAllUsers();
        List<Host> hosts = hostService.getAllHosts();
        model.addAttribute("users",users);
        model.addAttribute("hosts", hosts);
        return "newHost";

    }

    @PostMapping("/host")
    public String newHost(@ModelAttribute("host") Host host) {
        log.info("Entrou no cadastro de Host");
        hostService.addHost(host);
        return "redirect:/hosts";
    }

    /* Guest */
    @GetMapping("/guests")
    public String guest(Model model) {
        List<Guest> guests = guestService.getAllGuests();
        model.addAttribute("guests", guests);
        return "guests";
    }

    @GetMapping("/guest")
    public String newGuest(Model model) {
        List<User> users = service.getAllUsers();
        List<Guest> guests = guestService.getAllGuests();
        model.addAttribute("users",users);
        model.addAttribute("guests", guests);
        return "newGuest";

    }

    @PostMapping("/guest")
    public String newGuest(@ModelAttribute("guest") Guest guest) {
        log.info("Entrou no cadastro de Guest");
        guestService.addGuest(guest);
        return "redirect:/guests";
    }

    /* Place To Book*/
    // Página inicial para exibir o formulário
    @GetMapping("/placetobook")
    public String showPlaceToBookForm(Model model) {
        List<Host> hosts = hostService.getAllHosts();
        List<Place> places = placeService.getAllPlaces();
        model.addAttribute("hosts",hosts);
        model.addAttribute("places", places);
        // Crie um novo objeto PlaceToBook e adicione ao modelo
        model.addAttribute("placeToBook", new PlaceToBook());
        return "newPlaceToBook";
    }

    @GetMapping("/placetobooks")
    public String placeToBook(Model model) {
        List<PlaceToBook> placeToBooks = placeToBookService.getAllPlaceToBooks();
        model.addAttribute("placeToBooks", placeToBooks);
        return "placetobooks";
    }

    @PostMapping("/placetobook")
    public String newPlaceToBook(
            @ModelAttribute("placeToBook") PlaceToBook placeToBook) {
        // Lógica para processar o objeto PlaceToBook, por exemplo, salvar no banco de dados
        log.info("Entrou no cadastro de Host");
        // Adicione o objeto placeToBook aos atributos de redirecionamento
        placeToBookService.addPlaceToBook(placeToBook);
        // Redirecione para a página de listagem ou para a página de confirmação
        return "redirect:/placetobooks";
    }

    @GetMapping("/placetobook/order")
    public String orderThePlaceToBookByPrice(Model model){
        List<PlaceToBook> placeToBooks = placeToBookService.orderThePlaceToBookByPrice();
        model.addAttribute("placeToBooks", placeToBooks);
        return "orderPlaceToBook";
    }
    @GetMapping("/searchplacetobook")
    public String searchPlaceToBook(@RequestParam(required = false) Integer nRooms,
                                    @RequestParam(required = false) Integer nBathroom,
                                    @RequestParam(required = false) Integer nKitchen,
                                    @RequestParam(required = false) Integer nLivingRooms,
                                    Model model )
    {
        List<PlaceToBook> placeToBooks = placeToBookService.searchPlaceToBook(nRooms,nBathroom,nKitchen,nLivingRooms);
        model.addAttribute("placeToBooks", placeToBooks);
        return "searchPlaceToBook";

    }

    @GetMapping("/deleteplacetobook/{id}")
    public String deletePlaceToBook(@PathVariable("id") Integer id) {
        try {
            placeToBookService.deleteById(id);
            return "redirect:/placetobooks";
        } catch (UserNotFoundException exception) {
            return "redirect:/placetobooks?error=PlaceToBookNotFound";
        }
    }

    @GetMapping("editplacetobook/{id}")
    public String editPlaceToBook(@PathVariable("id") Integer id, Model model){
        try {
            PlaceToBook placeToBook = placeToBookService.placeToBookFindById(id);
            model.addAttribute("placeToBook", placeToBook);
            return "editplacetobook";
        } catch (PlaceToBookNotFoundException e) {
            // Se o place não for encontrado, redirecione para a tela de novo place
            return "redirect:/placetobook";
        }
    }

    @PostMapping ("/editplacetobook/{id}")
    public String editPlaceToBook(@PathVariable("id") Integer id, @ModelAttribute PlaceToBook placeToBook, Model model){
        try {
            PlaceToBook updatedPlaceToBook = placeToBookService.editPlaceToBook(id, placeToBook);
            return "redirect:/placetobook/" + updatedPlaceToBook.getIdPlaceToBook();
        } catch (PlaceToBookNotFoundException e) {
            // Se o usuário não for encontrado, redirecione para a tela de novo usuário
            return "redirect:/placetobook";
        }
    }

    @GetMapping("/placetobook/descorder")
    public String orderThePlaceToBookByPriceDecreasing(Model model){
        List<PlaceToBook> placeToBooks = placeToBookService.placeToBookSortInDescendingOrderPrice();
        model.addAttribute("placeToBooks", placeToBooks);
        return "orderPlaceToBookDecreasing";
    }

    @GetMapping("/placetobook/ascorder")
    public String orderThePlaceToBookByPriceIncreasing(Model model){
        List<PlaceToBook> placeToBooks = placeToBookService.placeToBookSortInAscendingOrderPrice();
        model.addAttribute("placeToBooks", placeToBooks);
        return "orderPlaceToBookIncreasing";
    }

    @GetMapping("/reserved-places")
    public String getReservedPlacesWithCapacityGreaterThan(@RequestParam(required = false, defaultValue = "0") Integer minCapacity, Model model) {
        List<PlaceToBook> placeToBooks = placeToBookService.findReservedPlacesWithCapacityGreaterThan(minCapacity);
        model.addAttribute("placeToBooks", placeToBooks);
        model.addAttribute("minCapacity", minCapacity);
        return "reservedPlaces";
    }

    @GetMapping("/pricerange")
    public String findPriceRange(@RequestParam(required = false, defaultValue = "0") Double minvalue,
                                 @RequestParam(required = false, defaultValue = "10000") Double maxvalue,
                                 Model model) {

        List<PlaceToBook> placeToBooks = placeToBookService.findPriceRange(minvalue, maxvalue);
        model.addAttribute("placeToBooks", placeToBooks);
        model.addAttribute("minvalue", minvalue);
        model.addAttribute("maxvalue", maxvalue);

        return "priceRange";
    }

    @PostMapping("/update-host-availability")
    public void updateHostAvailability() {
        placeToBookService.updateHostAvailabilityStatus();
    }

    /* Review */
    @GetMapping("/review")
    public String showReviewForm(Model model) {
        List<PlaceToBook> placeToBooks = placeToBookService.getAllPlaceToBooks();
        List<Guest> guests = guestService.getAllGuests();
        model.addAttribute("placeToBooks",placeToBooks);
        model.addAttribute("guests", guests);
        model.addAttribute("review", new Review());
        return "newReview";
    }

    @GetMapping("/reviews")
    public String review(Model model) {
        List<Review> reviews = reviewService.getAllReviews();
        model.addAttribute("reviews", reviews);
        return "reviews";
    }

    @PostMapping("/review")
    public String newReview(
            @ModelAttribute("review") Review review) {
        log.info("Entrou no cadastro de Review");
        reviewService.addReview(review);
        return "newReview";
    }

    @GetMapping("review/{id}")
    public String showReview(@PathVariable("id") Integer id, Model model) {
            Review review = reviewService.ReviewFindById(id);
            model.addAttribute("review", review);
            return "showreview";

    }

    @GetMapping("editreview/{id}")
    public String editReview(@PathVariable("id") Integer id, Model model) {
        try {
            Review review = reviewService.ReviewFindById(id);
            model.addAttribute("review", review);
            return "editreview";
        } catch (PlaceNotFoundException exception) {
            return "redirect:/review";
        }
    }

    @PostMapping("/editreview/{id}")
    public String editReview(@PathVariable("id") Integer id, @ModelAttribute Review review, Model model) {
        try {
            Review updateReview = reviewService.editReview(id,review);
            return "redirect:/review/" + updateReview.getIdReview();
        } catch (PlaceToBookNotFoundException exception) {
            return "redirect:/review";
        }
    }

    @GetMapping("/deletereview/{id}")
    public String deleteReview(@PathVariable("id") Integer id) {
        try {
            reviewService.deleteById(id);
            return "redirect:/reviews";
        } catch (UserNotFoundException exception) {
            return "redirect:/reviews?error=ReviewNotFound";
        }
    }

    /* Booking */
    @GetMapping("/booking")
    public String showBookingForm(Model model) {
        List<PlaceToBook> placeToBooks = placeToBookService.getAllPlaceToBooks();
        List<Guest> guests = guestService.getAllGuests();
        model.addAttribute("placeToBooks",placeToBooks);
        model.addAttribute("guests", guests);
        model.addAttribute("booking", new Booking());
        return "newBooking";
    }

    @GetMapping("/bookings")
    public String booking(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();
        model.addAttribute("bookings", bookings);
        return "bookings";
    }

    @PostMapping("/booking")
    public String newBooking(
            @ModelAttribute("booking") Booking booking) {
        log.info("Entrou no cadastro de Booking");
        bookingService.addBooking(booking);
        return "newBooking";
    }

    @GetMapping("/reports")
    public String report(Model model){
        return "reports";
    }

    /* Login */



}
