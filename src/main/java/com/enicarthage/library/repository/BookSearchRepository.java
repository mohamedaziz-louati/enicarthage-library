package com.enicarthage.library.repository;

import com.enicarthage.library.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookSearchRepository extends JpaRepository<Book, Long> {
    
    @Query("SELECT b FROM Book b WHERE b.title LIKE %:keyword% OR b.author LIKE %:keyword% OR b.description LIKE %:keyword%")
    List<Book> findByKeywordInTitleAuthorDescription(@Param("keyword") String keyword);
    
    @Query("SELECT b FROM Book b WHERE b.price BETWEEN :minPrice AND :maxPrice")
    List<Book> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
    
    @Query("SELECT b FROM Book b WHERE b.publicationYear BETWEEN :startYear AND :endYear ORDER BY b.publicationYear DESC")
    List<Book> findByPublicationYearRange(@Param("startYear") Integer startYear, @Param("endYear") Integer endYear);
    
    @Query("SELECT b FROM Book b WHERE b.availableCopies > 0 AND b.totalCopies > :minTotalCopies")
    List<Book> findAvailableBooksWithMinimumTotalCopies(@Param("minTotalCopies") Integer minTotalCopies);
    
    @Query("SELECT b FROM Book b WHERE b.createdAt >= :startDate AND b.createdAt <= :endDate")
    List<Book> findBooksCreatedBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT COUNT(b) FROM Book b WHERE b.category = :category")
    Long countBooksByCategory(@Param("category") Book.BookCategory category);
    
    @Query("SELECT DISTINCT b.author FROM Book b ORDER BY b.author")
    List<String> findAllDistinctAuthors();
    
    @Query("SELECT b FROM Book b WHERE b.language = :language AND b.availableCopies > 0")
    List<Book> findAvailableBooksByLanguage(@Param("language") String language);
    
    @Query("SELECT b FROM Book b WHERE b.pages BETWEEN :minPages AND :maxPages ORDER BY b.pages ASC")
    List<Book> findBooksByPageRange(@Param("minPages") Integer minPages, @Param("maxPages") Integer maxPages);
    
    @Query("SELECT b FROM Book b WHERE b.shelfLocation LIKE %:location%")
    List<Book> findBooksByShelfLocation(@Param("location") String location);
    
    @Query(value = "SELECT * FROM books ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Book> findRandomBooks(@Param("limit") Integer limit);
    
    @Query("SELECT b FROM Book b WHERE b.title LIKE %:title% AND b.author LIKE %:author%")
    List<Book> findByTitleAndAuthorContaining(@Param("title") String title, @Param("author") String author);
    
    @Query("SELECT b FROM Book b WHERE b.price > :averagePrice")
    List<Book> findBooksAboveAveragePrice(@Param("averagePrice") Double averagePrice);
    
    @Query("SELECT b FROM Book b WHERE b.description IS NOT NULL AND b.description != ''")
    List<Book> findBooksWithDescriptions();
    
    @Query("SELECT b FROM Book b WHERE b.coverImageUrl IS NOT NULL AND b.coverImageUrl != ''")
    List<Book> findBooksWithCoverImages();
}
