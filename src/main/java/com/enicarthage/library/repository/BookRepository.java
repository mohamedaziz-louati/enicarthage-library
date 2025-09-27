package com.enicarthage.library.repository;

import com.enicarthage.library.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    
    List<Book> findByTitleContainingIgnoreCase(String title);
    
    List<Book> findByAuthorContainingIgnoreCase(String author);
    
    Optional<Book> findByIsbn(String isbn);
    
    @Query("SELECT b FROM Book b WHERE b.title LIKE %:searchTerm% OR b.author LIKE %:searchTerm% OR b.isbn LIKE %:searchTerm%")
    List<Book> findBySearchTerm(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT b FROM Book b WHERE b.availableCopies > 0")
    List<Book> findAvailableBooks();
    
    @Query("SELECT b FROM Book b WHERE b.publicationYear = :year")
    List<Book> findByPublicationYear(@Param("year") Integer year);
    
    @Query("SELECT b FROM Book b WHERE b.publicationYear BETWEEN :startYear AND :endYear")
    List<Book> findByPublicationYearRange(@Param("startYear") Integer startYear, @Param("endYear") Integer endYear);
    
    @Query("SELECT b FROM Book b WHERE b.language = :language")
    List<Book> findByLanguage(@Param("language") String language);
    
    @Query("SELECT b FROM Book b WHERE b.category = :category AND b.availableCopies > 0")
    List<Book> findAvailableBooksByCategory(@Param("category") Book.BookCategory category);
    
    @Query("SELECT b FROM Book b WHERE b.status = :status")
    List<Book> findByStatus(@Param("status") Book.BookStatus status);
    
    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    Page<Book> findByAuthorContainingIgnoreCase(String author, Pageable pageable);
    
    Page<Book> findByCategory(Book.BookCategory category, Pageable pageable);
    
    List<Book> findByCategory(Book.BookCategory category);
    
    Page<Book> findByStatus(Book.BookStatus status, Pageable pageable);
    
    Page<Book> findByStatus(String status, Pageable pageable);
    
    @Query("SELECT b FROM Book b WHERE (b.title LIKE %:searchTerm% OR b.author LIKE %:searchTerm% OR b.isbn LIKE %:searchTerm%)")
    Page<Book> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT b FROM Book b WHERE (b.title LIKE %:searchTerm% OR b.author LIKE %:searchTerm% OR b.isbn LIKE %:searchTerm%)")
    Page<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrIsbnContainingIgnoreCase(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT DISTINCT b.category FROM Book b")
    List<String> findAllCategories();
}