package com.enicarthage.library.util;

import com.enicarthage.library.entity.Book;
import com.enicarthage.library.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

@Component
public class LibraryValidationUtil {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern ISBN_PATTERN = Pattern.compile(
        "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$"
    );
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public LibraryValidationUtil() {
        // Utility class for library-related validations
    }
    
    public boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
    
    public boolean isValidISBN(String isbn) {
        return isbn != null && ISBN_PATTERN.matcher(isbn.replaceAll("[ -]", "")).matches();
    }
    
    public boolean isValidBookTitle(String title) {
        return title != null && title.trim().length() >= 2 && title.trim().length() <= 200;
    }
    
    public boolean isValidAuthorName(String author) {
        return author != null && author.trim().length() >= 2 && author.trim().length() <= 100;
    }
    
    public boolean isValidUser(User user) {
        if (user == null) return false;
        
        return isValidEmail(user.getEmail()) &&
               isValidName(user.getFirstName()) &&
               isValidName(user.getLastName()) &&
               user.getPassword() != null &&
               user.getPassword().length() >= 6;
    }
    
    public boolean isValidBook(Book book) {
        if (book == null) return false;
        
        return isValidBookTitle(book.getTitle()) &&
               isValidAuthorName(book.getAuthor()) &&
               (book.getIsbn() == null || isValidISBN(book.getIsbn())) &&
               (book.getPrice() == null || book.getPrice() >= 0) &&
               (book.getTotalCopies() == null || book.getTotalCopies() >= 0);
    }
    
    public boolean isValidName(String name) {
        return name != null && name.trim().length() >= 2 && name.trim().length() <= 50;
    }
    
    public boolean isValidYear(Integer year) {
        return year != null && year >= 1000 && year <= LocalDateTime.now().getYear() + 1;
    }
    
    public boolean isValidPages(Integer pages) {
        return pages != null && pages > 0 && pages <= 10000;
    }
    
    public String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(DATE_FORMATTER) : "";
    }
    
    public boolean isValidPhoneNumber(String phone) {
        return phone != null && phone.matches("^[+]?[0-9]{10,15}$");
    }
    
    public boolean isValidAddress(String address) {
        return address != null && address.trim().length() >= 5 && address.trim().length() <= 200;
    }
    
    public String sanitizeInput(String input) {
        if (input == null) return "";
        return input.trim().replaceAll("[<>\"'&]", "");
    }
}
