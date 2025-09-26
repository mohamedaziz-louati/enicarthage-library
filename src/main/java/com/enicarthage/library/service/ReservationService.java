package com.enicarthage.library.service;

import com.enicarthage.library.entity.Book;
import com.enicarthage.library.entity.Reservation;
import com.enicarthage.library.entity.User;
import com.enicarthage.library.repository.BookRepository;
import com.enicarthage.library.repository.ReservationRepository;
import com.enicarthage.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ReservationService {

    @Autowired private ReservationRepository reservationRepository;
    @Autowired private BookRepository bookRepository;
    @Autowired private UserRepository userRepository;

    public Reservation reserveBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found"));
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        if (book.getAvailableCopies() != null && book.getAvailableCopies() > 0) {
            // Allow reservation even if copies exist (some libraries do); but most want borrowing instead.
            // We'll still allow it, but keep status pending.
        }

        List<Reservation> existing = reservationRepository.findActiveReservationsByUserAndBook(user, book);
        if (!existing.isEmpty()) {
            throw new RuntimeException("You already have an active reservation for this book");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setBook(book);
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setExpiryDate(LocalDateTime.now().plusDays(3));
        reservation.setStatus(Reservation.ReservationStatus.PENDING);

        // Optionally mark book reserved if no copies are available
        if (book.getAvailableCopies() != null && book.getAvailableCopies() <= 0) {
            book.setStatus(Book.BookStatus.RESERVED);
            bookRepository.save(book);
        }

        return reservationRepository.save(reservation);
    }

    public Reservation cancelReservation(Long reservationId, Long userId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservation.getUser() == null || reservation.getUser().getId() == null ||
            !reservation.getUser().getId().equals(userId)) {
            throw new RuntimeException("Not allowed to cancel this reservation");
        }

        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getReservationsByUser(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return reservationRepository.findByUser(user);
    }

    public Page<Reservation> getAllReservations(Pageable pageable) {
        return reservationRepository.findAll(pageable);
    }
}

