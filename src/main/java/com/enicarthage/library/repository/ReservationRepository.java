package com.enicarthage.library.repository;

import com.enicarthage.library.entity.Book;
import com.enicarthage.library.entity.Reservation;
import com.enicarthage.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);
    List<Reservation> findByBook(Book book);
    List<Reservation> findByStatus(Reservation.ReservationStatus status);

    @Query("SELECT r FROM Reservation r WHERE r.user = :user AND r.book = :book AND r.status IN ('PENDING','CONFIRMED')")
    List<Reservation> findActiveReservationsByUserAndBook(@Param("user") User user, @Param("book") Book book);

    @Query("SELECT r FROM Reservation r WHERE r.expiryDate IS NOT NULL AND r.expiryDate < :now AND r.status IN ('PENDING','CONFIRMED')")
    List<Reservation> findExpiredReservations(@Param("now") LocalDateTime now);
}

