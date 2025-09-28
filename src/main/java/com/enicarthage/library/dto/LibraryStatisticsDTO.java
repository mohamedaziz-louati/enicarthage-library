package com.enicarthage.library.dto;

import java.time.LocalDateTime;

public class LibraryStatisticsDTO {
    
    private Long totalBooks;
    private Long totalUsers;
    private Long totalBorrowings;
    private Long activeReservations;
    private Double averageBookPrice;
    private Long booksAddedThisMonth;
    private Long usersRegisteredThisMonth;
    private LocalDateTime lastUpdated;
    private String mostPopularCategory;
    private Long overdueBooksCount;
    
    public LibraryStatisticsDTO() {
        this.lastUpdated = LocalDateTime.now();
    }
    
    public LibraryStatisticsDTO(Long totalBooks, Long totalUsers, Long totalBorrowings) {
        this();
        this.totalBooks = totalBooks;
        this.totalUsers = totalUsers;
        this.totalBorrowings = totalBorrowings;
    }
    
    // Getters and Setters
    public Long getTotalBooks() {
        return totalBooks;
    }
    
    public void setTotalBooks(Long totalBooks) {
        this.totalBooks = totalBooks;
    }
    
    public Long getTotalUsers() {
        return totalUsers;
    }
    
    public void setTotalUsers(Long totalUsers) {
        this.totalUsers = totalUsers;
    }
    
    public Long getTotalBorrowings() {
        return totalBorrowings;
    }
    
    public void setTotalBorrowings(Long totalBorrowings) {
        this.totalBorrowings = totalBorrowings;
    }
    
    public Long getActiveReservations() {
        return activeReservations;
    }
    
    public void setActiveReservations(Long activeReservations) {
        this.activeReservations = activeReservations;
    }
    
    public Double getAverageBookPrice() {
        return averageBookPrice;
    }
    
    public void setAverageBookPrice(Double averageBookPrice) {
        this.averageBookPrice = averageBookPrice;
    }
    
    public Long getBooksAddedThisMonth() {
        return booksAddedThisMonth;
    }
    
    public void setBooksAddedThisMonth(Long booksAddedThisMonth) {
        this.booksAddedThisMonth = booksAddedThisMonth;
    }
    
    public Long getUsersRegisteredThisMonth() {
        return usersRegisteredThisMonth;
    }
    
    public void setUsersRegisteredThisMonth(Long usersRegisteredThisMonth) {
        this.usersRegisteredThisMonth = usersRegisteredThisMonth;
    }
    
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    public String getMostPopularCategory() {
        return mostPopularCategory;
    }
    
    public void setMostPopularCategory(String mostPopularCategory) {
        this.mostPopularCategory = mostPopularCategory;
    }
    
    public Long getOverdueBooksCount() {
        return overdueBooksCount;
    }
    
    public void setOverdueBooksCount(Long overdueBooksCount) {
        this.overdueBooksCount = overdueBooksCount;
    }
    
    @Override
    public String toString() {
        return "LibraryStatisticsDTO{" +
                "totalBooks=" + totalBooks +
                ", totalUsers=" + totalUsers +
                ", totalBorrowings=" + totalBorrowings +
                ", activeReservations=" + activeReservations +
                ", averageBookPrice=" + averageBookPrice +
                ", booksAddedThisMonth=" + booksAddedThisMonth +
                ", usersRegisteredThisMonth=" + usersRegisteredThisMonth +
                ", lastUpdated=" + lastUpdated +
                ", mostPopularCategory='" + mostPopularCategory + '\'' +
                ", overdueBooksCount=" + overdueBooksCount +
                '}';
    }
}
