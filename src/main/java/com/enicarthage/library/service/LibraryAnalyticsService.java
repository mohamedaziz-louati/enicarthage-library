package com.enicarthage.library.service;

import com.enicarthage.library.entity.Book;
import com.enicarthage.library.entity.User;
import com.enicarthage.library.repository.BookRepository;
import com.enicarthage.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LibraryAnalyticsService {
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public LibraryAnalyticsService() {
        // Service for analyzing library usage patterns and statistics
    }
    
    public int getTotalBooksCount() {
        return bookRepository.findAll().size();
    }
    
    public int getTotalUsersCount() {
        return userRepository.findAll().size();
    }
    
    public List<Book> getMostPopularBooks() {
        // Placeholder for most popular books logic
        return bookRepository.findAll().subList(0, Math.min(5, getTotalBooksCount()));
    }
    
    public double getAverageBookPrice() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .filter(book -> book.getPrice() != null)
                .mapToDouble(Book::getPrice)
                .average()
                .orElse(0.0);
    }
    
    public Optional<Book> getNewestBook() {
        return bookRepository.findAll().stream()
                .max((b1, b2) -> b1.getCreatedAt().compareTo(b2.getCreatedAt()));
    }
    
    public Optional<Book> getOldestBook() {
        return bookRepository.findAll().stream()
                .min((b1, b2) -> b1.getCreatedAt().compareTo(b2.getCreatedAt()));
    }
    
    public long getBooksAddedThisMonth() {
        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        return bookRepository.findAll().stream()
                .filter(book -> book.getCreatedAt().isAfter(monthStart))
                .count();
    }
    
    public long getUsersRegisteredThisMonth() {
        LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        return userRepository.findAll().stream()
                .filter(user -> user.getCreatedAt().isAfter(monthStart))
                .count();
    }
    
    public String generateLibraryReport() {
        StringBuilder report = new StringBuilder();
        report.append("=== Library Analytics Report ===\n");
        report.append("Total Books: ").append(getTotalBooksCount()).append("\n");
        report.append("Total Users: ").append(getTotalUsersCount()).append("\n");
        report.append("Average Book Price: $").append(String.format("%.2f", getAverageBookPrice())).append("\n");
        report.append("Books Added This Month: ").append(getBooksAddedThisMonth()).append("\n");
        report.append("New Users This Month: ").append(getUsersRegisteredThisMonth()).append("\n");
        report.append("Report Generated: ").append(LocalDateTime.now()).append("\n");
        return report.toString();
    }
}
