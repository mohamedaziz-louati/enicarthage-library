package com.enicarthage.library.advanced.dto;

import java.time.LocalDateTime;
import java.util.List;

public class AdvancedBookSearchDTO {
    
    private String keyword;
    private String author;
    private String category;
    private String language;
    private Integer minYear;
    private Integer maxYear;
    private Double minPrice;
    private Double maxPrice;
    private Integer minPages;
    private Integer maxPages;
    private Boolean availableOnly;
    private String sortBy;
    private String sortOrder;
    private Integer page;
    private Integer size;
    private LocalDateTime searchDate;
    private String userId;
    private List<String> excludeCategories;
    private List<String> includeCategories;
    private Boolean hasCoverImage;
    private Boolean hasDescription;
    private String publisher;
    private String shelfLocation;
    
    public AdvancedBookSearchDTO() {
        this.searchDate = LocalDateTime.now();
        this.page = 0;
        this.size = 20;
        this.sortOrder = "asc";
        this.availableOnly = false;
    }
    
    // Getters and Setters
    public String getKeyword() {
        return keyword;
    }
    
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public Integer getMinYear() {
        return minYear;
    }
    
    public void setMinYear(Integer minYear) {
        this.minYear = minYear;
    }
    
    public Integer getMaxYear() {
        return maxYear;
    }
    
    public void setMaxYear(Integer maxYear) {
        this.maxYear = maxYear;
    }
    
    public Double getMinPrice() {
        return minPrice;
    }
    
    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }
    
    public Double getMaxPrice() {
        return maxPrice;
    }
    
    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }
    
    public Integer getMinPages() {
        return minPages;
    }
    
    public void setMinPages(Integer minPages) {
        this.minPages = minPages;
    }
    
    public Integer getMaxPages() {
        return maxPages;
    }
    
    public void setMaxPages(Integer maxPages) {
        this.maxPages = maxPages;
    }
    
    public Boolean getAvailableOnly() {
        return availableOnly;
    }
    
    public void setAvailableOnly(Boolean availableOnly) {
        this.availableOnly = availableOnly;
    }
    
    public String getSortBy() {
        return sortBy;
    }
    
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }
    
    public String getSortOrder() {
        return sortOrder;
    }
    
    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public Integer getPage() {
        return page;
    }
    
    public void setPage(Integer page) {
        this.page = page;
    }
    
    public Integer getSize() {
        return size;
    }
    
    public void setSize(Integer size) {
        this.size = size;
    }
    
    public LocalDateTime getSearchDate() {
        return searchDate;
    }
    
    public void setSearchDate(LocalDateTime searchDate) {
        this.searchDate = searchDate;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public List<String> getExcludeCategories() {
        return excludeCategories;
    }
    
    public void setExcludeCategories(List<String> excludeCategories) {
        this.excludeCategories = excludeCategories;
    }
    
    public List<String> getIncludeCategories() {
        return includeCategories;
    }
    
    public void setIncludeCategories(List<String> includeCategories) {
        this.includeCategories = includeCategories;
    }
    
    public Boolean getHasCoverImage() {
        return hasCoverImage;
    }
    
    public void setHasCoverImage(Boolean hasCoverImage) {
        this.hasCoverImage = hasCoverImage;
    }
    
    public Boolean getHasDescription() {
        return hasDescription;
    }
    
    public void setHasDescription(Boolean hasDescription) {
        this.hasDescription = hasDescription;
    }
    
    public String getPublisher() {
        return publisher;
    }
    
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    
    public String getShelfLocation() {
        return shelfLocation;
    }
    
    public void setShelfLocation(String shelfLocation) {
        this.shelfLocation = shelfLocation;
    }
    
    @Override
    public String toString() {
        return "AdvancedBookSearchDTO{" +
                "keyword='" + keyword + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", language='" + language + '\'' +
                ", minYear=" + minYear +
                ", maxYear=" + maxYear +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", minPages=" + minPages +
                ", maxPages=" + maxPages +
                ", availableOnly=" + availableOnly +
                ", sortBy='" + sortBy + '\'' +
                ", sortOrder='" + sortOrder + '\'' +
                ", page=" + page +
                ", size=" + size +
                ", searchDate=" + searchDate +
                ", userId='" + userId + '\'' +
                ", excludeCategories=" + excludeCategories +
                ", includeCategories=" + includeCategories +
                ", hasCoverImage=" + hasCoverImage +
                ", hasDescription=" + hasDescription +
                ", publisher='" + publisher + '\'' +
                ", shelfLocation='" + shelfLocation + '\'' +
                '}';
    }
}
