package com.enicarthage.library.advanced.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_ratings")
public class BookRating {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "book_id", nullable = false)
    private Long bookId;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "rating", nullable = false)
    private Integer rating;
    
    @Column(name = "review", columnDefinition = "TEXT")
    private String review;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "helpful_count")
    private Integer helpfulCount;
    
    @Column(name = "verified_purchase")
    private Boolean verifiedPurchase;
    
    @Column(name = "rating_category")
    private String ratingCategory;
    
    @Column(name = "anonymous")
    private Boolean anonymous;
    
    public BookRating() {
        this.createdAt = LocalDateTime.now();
        this.helpfulCount = 0;
        this.verifiedPurchase = false;
        this.anonymous = false;
    }
    
    public BookRating(Long bookId, Long userId, Integer rating, String review) {
        this();
        this.bookId = bookId;
        this.userId = userId;
        this.rating = rating;
        this.review = review;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getBookId() {
        return bookId;
    }
    
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    
    public String getReview() {
        return review;
    }
    
    public void setReview(String review) {
        this.review = review;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Integer getHelpfulCount() {
        return helpfulCount;
    }
    
    public void setHelpfulCount(Integer helpfulCount) {
        this.helpfulCount = helpfulCount;
    }
    
    public Boolean getVerifiedPurchase() {
        return verifiedPurchase;
    }
    
    public void setVerifiedPurchase(Boolean verifiedPurchase) {
        this.verifiedPurchase = verifiedPurchase;
    }
    
    public String getRatingCategory() {
        return ratingCategory;
    }
    
    public void setRatingCategory(String ratingCategory) {
        this.ratingCategory = ratingCategory;
    }
    
    public Boolean getAnonymous() {
        return anonymous;
    }
    
    public void setAnonymous(Boolean anonymous) {
        this.anonymous = anonymous;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    
    @Override
    public String toString() {
        return "BookRating{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", userId=" + userId +
                ", rating=" + rating +
                ", review='" + review + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", helpfulCount=" + helpfulCount +
                ", verifiedPurchase=" + verifiedPurchase +
                ", ratingCategory='" + ratingCategory + '\'' +
                ", anonymous=" + anonymous +
                '}';
    }
}
