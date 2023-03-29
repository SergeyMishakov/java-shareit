package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {

    Booking createBooking(long bookerId, Booking booking);

    Booking updateBooking(long ownerId, long bookingId, Boolean approved);

    Booking getBookingById(long userId, long id);

    List<Booking> getBookingList(long bookerId, String state);

    List<Booking> getBookingByOwner(long ownerId, String state);
}
