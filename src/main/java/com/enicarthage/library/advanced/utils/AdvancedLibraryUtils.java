package com.enicarthage.library.advanced.utils;

import com.enicarthage.library.entity.Book;
import com.enicarthage.library.entity.User;
import com.enicarthage.library.entity.Borrowing;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

@Component
public class AdvancedLibraryUtils {
    
    private static final Random random = new Random();
    private static final Pattern ISBN_PATTERN = Pattern.compile("^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$");
    
    public AdvancedLibraryUtils() {
        // Advanced utility class for library operations
    }
    
    public static String generateRandomISBN() {
        // Placeholder for ISBN generation
        return "978-" + String.format("%010d", random.nextInt(1000000000));
    }
    
    public static boolean isValidISBN(String isbn) {
        return isbn != null && ISBN_PATTERN.matcher(isbn.replaceAll("[ -]", "")).matches();
    }
    
    public static String generateBookId() {
        return "BOOK-" + System.currentTimeMillis() + "-" + random.nextInt(1000);
    }
    
    public static String generateUserId() {
        return "USER-" + System.currentTimeMillis() + "-" + random.nextInt(1000);
    }
    
    public static String generateBorrowingId() {
        return "BORROW-" + System.currentTimeMillis() + "-" + random.nextInt(1000);
    }
    
    public static LocalDateTime calculateDueDate(LocalDateTime borrowDate, Integer daysAllowed) {
        return borrowDate.plusDays(daysAllowed != null ? daysAllowed : 14);
    }
    
    public static Long calculateOverdueDays(LocalDateTime dueDate) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(dueDate)) {
            return ChronoUnit.DAYS.between(dueDate, now);
        }
        return 0L;
    }
    
    public static Double calculateFine(Long overdueDays) {
        return overdueDays * 0.50; // $0.50 per day
    }
    
    public static String generateBarcode() {
        return String.format("%013d", random.nextInt(1000000000));
    }
    
    public static String generateQRCodeData(Book book) {
        return "LIBRARY:" + book.getId() + ":" + book.getIsbn();
    }
    
    public static boolean isBookAvailable(Book book) {
        return book.getAvailableCopies() != null && book.getAvailableCopies() > 0;
    }
    
    public static String getBookStatusDescription(Book.BookStatus status) {
        switch (status) {
            case AVAILABLE: return "Available for borrowing";
            case BORROWED: return "Currently borrowed";
            case RESERVED: return "Reserved for a user";
            case MAINTENANCE: return "Under maintenance";
            case LOST: return "Reported as lost";
            case DAMAGED: return "Damaged and unavailable";
            default: return "Unknown status";
        }
    }
    
    public static String getUserTypeDescription(String userType) {
        switch (userType.toLowerCase()) {
            case "student": return "Student Member";
            case "faculty": return "Faculty Member";
            case "staff": return "Staff Member";
            case "alumni": return "Alumni Member";
            case "guest": return "Guest User";
            default: return "Regular Member";
        }
    }
    
    public static boolean isEmailValid(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }
    
    public static boolean isPhoneNumberValid(String phone) {
        return phone != null && phone.matches("^[+]?[0-9]{10,15}$");
    }
    
    public static String formatBookTitle(String title) {
        if (title == null) return "";
        return title.substring(0, 1).toUpperCase() + title.substring(1).toLowerCase();
    }
    
    public static String formatAuthorName(String author) {
        if (author == null) return "";
        String[] parts = author.split(" ");
        StringBuilder formatted = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) {
                formatted.append(part.substring(0, 1).toUpperCase())
                        .append(part.substring(1).toLowerCase())
                        .append(" ");
            }
        }
        return formatted.toString().trim();
    }
    
    public static String generateLibraryCardNumber() {
        return "LC-" + System.currentTimeMillis() + "-" + random.nextInt(100);
    }
    
    public static boolean isBorrowingOverdue(Borrowing borrowing) {
        return borrowing.getDueDate() != null && LocalDateTime.now().isAfter(borrowing.getDueDate());
    }
    
    public static String generateReservationCode() {
        return "RES-" + random.nextInt(1000000);
    }
    
    public static boolean isBookAgeAppropriate(Book book, Integer userAge) {
        // Placeholder for age appropriateness logic
        return true;
    }
    
    public static String generateReportId() {
        return "RPT-" + System.currentTimeMillis() + "-" + random.nextInt(100);
    }
    
    public static String generateSessionId() {
        return "SESSION-" + System.currentTimeMillis() + "-" + random.nextInt(10000);
    }
    
    public static boolean isWithinOperatingHours(LocalDateTime dateTime) {
        int hour = dateTime.getHour();
        return hour >= 8 && hour <= 20; // 8 AM to 8 PM
    }
    
    public static String generateNotificationId() {
        return "NOTIF-" + System.currentTimeMillis() + "-" + random.nextInt(1000);
    }
    
    public static boolean isWeekend(LocalDateTime dateTime) {
        int dayOfWeek = dateTime.getDayOfWeek().getValue();
        return dayOfWeek == 6 || dayOfWeek == 7; // Saturday or Sunday
    }
    
    public static String generateEventId() {
        return "EVENT-" + System.currentTimeMillis() + "-" + random.nextInt(100);
    }
    
    public static boolean isBookInHighDemand(Book book) {
        return book.getAvailableCopies() != null && 
               book.getTotalCopies() != null && 
               book.getAvailableCopies() < book.getTotalCopies() * 0.2;
    }
    
    public static String generateFeedbackId() {
        return "FEEDBACK-" + System.currentTimeMillis() + "-" + random.nextInt(1000);
    }
    
    public static boolean isValidLibraryCard(String cardNumber) {
        return cardNumber != null && cardNumber.startsWith("LC-") && cardNumber.length() > 5;
    }
    
    public static String generateBackupFileName(String prefix) {
        return prefix + "_backup_" + System.currentTimeMillis() + ".dat";
    }
    
    public static boolean isPrime(Integer number) {
        if (number <= 1) return false;
        if (number <= 3) return true;
        if (number % 2 == 0 || number % 3 == 0) return false;
        for (int i = 5; i * i <= number; i += 6) {
            if (number % i == 0 || number % (i + 2) == 0) return false;
        }
        return true;
    }
    
    public static String generateChecksum(String data) {
        // Simple checksum placeholder
        return String.valueOf(data.hashCode());
    }
    
    public static boolean isDataIntegrityValid(String data, String checksum) {
        return generateChecksum(data).equals(checksum);
    }
}
