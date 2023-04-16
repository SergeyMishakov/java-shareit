package ru.practicum.shareit.booking;

import java.util.List;

public interface BookingService {

    Booking createBooking(long bookerId, Booking booking);

    Booking updateBooking(long ownerId, long bookingId, Boolean approved);

    Booking getBookingById(long userId, long id);

    List<Booking> getBookingList(long bookerId, String state, Integer from, Integer size);

    List<Booking> getBookingByOwner(long ownerId, String state, Integer from, Integer size);

    void checkState(String state);
}
