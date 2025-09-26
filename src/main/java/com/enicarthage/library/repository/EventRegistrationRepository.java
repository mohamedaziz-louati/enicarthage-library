package com.enicarthage.library.repository;

import com.enicarthage.library.entity.Event;
import com.enicarthage.library.entity.EventRegistration;
import com.enicarthage.library.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRegistrationRepository extends JpaRepository<EventRegistration, Long> {
    boolean existsByUserAndEvent(User user, Event event);
    Optional<EventRegistration> findByUserAndEvent(User user, Event event);
    List<EventRegistration> findByUser(User user);
    List<EventRegistration> findByEvent(Event event);
    long countByEvent(Event event);
}

