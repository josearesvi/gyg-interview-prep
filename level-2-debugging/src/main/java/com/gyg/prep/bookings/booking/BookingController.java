package com.gyg.prep.bookings.booking;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public record CreateBookingRequest(@NotNull Long activityId,
                                       @NotBlank @Email String customerEmail,
                                       @Min(1) int participants,
                                       @NotBlank String currency) {}

    public record BookingResponse(Long id, Long activityId, int participants, BigDecimal totalPrice, String status) {
        static BookingResponse of(Booking b) {
            return new BookingResponse(b.getId(), b.getActivity().getId(), b.getParticipants(), b.getTotalPrice(),
                    b.getStatus().name());
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse create(@Valid @RequestBody CreateBookingRequest request) {
        return BookingResponse.of(bookingService.book(request.activityId(), request.customerEmail(),
                request.participants(), request.currency()));
    }

    @PostMapping("/{id}/cancel")
    public BookingResponse cancel(@PathVariable Long id) {
        return BookingResponse.of(bookingService.cancel(id));
    }
}
