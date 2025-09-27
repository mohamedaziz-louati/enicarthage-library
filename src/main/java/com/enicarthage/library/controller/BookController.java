package com.enicarthage.library.controller;

import com.enicarthage.library.entity.Book;
import com.enicarthage.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4201"})
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String category) {
        
        try {
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            
            Page<Book> booksPage;
            
            if (search != null && !search.trim().isEmpty()) {
                booksPage = bookRepository.findBySearchTerm(search, pageable);
            } else if (status != null && !status.trim().isEmpty()) {
                booksPage = bookRepository.findByStatus(status, pageable);
            } else if (category != null && !category.trim().isEmpty()) {
                booksPage = bookRepository.findByCategory(Book.BookCategory.valueOf(category), pageable);
            } else {
                booksPage = bookRepository.findAll(pageable);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("content", booksPage.getContent());
            response.put("totalElements", booksPage.getTotalElements());
            response.put("totalPages", booksPage.getTotalPages());
            response.put("number", booksPage.getNumber());
            response.put("size", booksPage.getSize());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to retrieve books: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody Book book) {
        try {
            System.out.println("=== Creating Book ===");
            System.out.println("Title: " + book.getTitle());
            System.out.println("Author: " + book.getAuthor());
            System.out.println("ISBN: " + book.getIsbn());
            
            // Check if book with same ISBN already exists
            if (book.getIsbn() != null && bookRepository.findByIsbn(book.getIsbn()).isPresent()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Book with this ISBN already exists");
                return ResponseEntity.badRequest().body(error);
            }
            
            // Set default status if not provided
            if (book.getStatus() == null) {
                book.setStatus(Book.BookStatus.AVAILABLE);
            }
            
            Book savedBook = bookRepository.save(book);
            System.out.println("SUCCESS: Book created with ID: " + savedBook.getId());
            
            return ResponseEntity.ok(savedBook);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to create book: " + e.getMessage());
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        try {
            return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(bookDetails.getTitle());
                    book.setAuthor(bookDetails.getAuthor());
                    book.setIsbn(bookDetails.getIsbn());
                    book.setPublisher(bookDetails.getPublisher());
                    book.setDescription(bookDetails.getDescription());
                    book.setCategory(bookDetails.getCategory());
                    book.setStatus(bookDetails.getStatus());
                    book.setPublicationYear(bookDetails.getPublicationYear());
                    book.setTotalCopies(bookDetails.getTotalCopies());
                    book.setAvailableCopies(bookDetails.getAvailableCopies());
                    
                    Book updatedBook = bookRepository.save(book);
                    return ResponseEntity.ok(updatedBook);
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update book: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        try {
            return bookRepository.findById(id)
                .map(book -> {
                    bookRepository.delete(book);
                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Book deleted successfully");
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete book: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Book>> searchBooks(@RequestParam String query) {
        List<Book> books = bookRepository.findBySearchTerm(query);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<String>> getAllCategories() {
        List<String> categories = bookRepository.findAllCategories();
        return ResponseEntity.ok(categories);
    }
}
