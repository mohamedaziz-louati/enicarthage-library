package com.enicarthage.library.repository;

import com.enicarthage.library.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByStudentId(String studentId);
    
    List<User> findByRole(User.Role role);
    
    Page<User> findByRole(String role, Pageable pageable);
    
    List<User> findByStatus(User.UserStatus status);
    
    Page<User> findByStatus(String status, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.firstName LIKE %:name% OR u.lastName LIKE %:name%")
    List<User> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT u FROM User u WHERE u.firstName LIKE %:name% OR u.lastName LIKE %:name%")
    Page<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.firstName LIKE %:searchTerm% OR u.lastName LIKE %:searchTerm% OR u.username LIKE %:searchTerm% OR u.email LIKE %:searchTerm%")
    Page<User> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.firstName LIKE %:searchTerm% OR u.lastName LIKE %:searchTerm% OR u.username LIKE %:searchTerm% OR u.email LIKE %:searchTerm%")
    Page<User> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.firstName LIKE %:searchTerm% OR u.lastName LIKE %:searchTerm% OR u.username LIKE %:searchTerm% OR u.email LIKE %:searchTerm%")
    List<User> findBySearchTerm(@Param("searchTerm") String searchTerm);
    
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.status = :status")
    List<User> findByRoleAndStatus(@Param("role") User.Role role, @Param("status") User.UserStatus status);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    boolean existsByStudentId(String studentId);
}