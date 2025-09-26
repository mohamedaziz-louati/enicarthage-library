package com.enicarthage.library.config;

import com.enicarthage.library.entity.User;
import com.enicarthage.library.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeUsers();
    }

    private void initializeUsers() {
        // Check if users already exist
        if (userRepository.count() == 0) {
            // Create admin user
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@enicarthage.tn");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setRole(User.Role.ADMIN);
            admin.setStatus(User.UserStatus.ACTIVE);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setLastLogin(LocalDateTime.now());
            userRepository.save(admin);

            // Create librarian user
            User librarian = new User();
            librarian.setUsername("librarian");
            librarian.setEmail("librarian@enicarthage.tn");
            librarian.setPassword(passwordEncoder.encode("librarian123"));
            librarian.setFirstName("Librarian");
            librarian.setLastName("User");
            librarian.setRole(User.Role.LIBRARIAN);
            librarian.setStatus(User.UserStatus.ACTIVE);
            librarian.setCreatedAt(LocalDateTime.now());
            librarian.setLastLogin(LocalDateTime.now());
            userRepository.save(librarian);

            // Create student user
            User student = new User();
            student.setUsername("student");
            student.setEmail("student@enicarthage.tn");
            student.setPassword(passwordEncoder.encode("student123"));
            student.setFirstName("Student");
            student.setLastName("User");
            student.setRole(User.Role.STUDENT);
            student.setStatus(User.UserStatus.ACTIVE);
            student.setStudentId("STU001");
            student.setPhoneNumber("+21612345678");
            student.setCreatedAt(LocalDateTime.now());
            student.setLastLogin(LocalDateTime.now());
            userRepository.save(student);

            System.out.println("=== Default users created ===");
            System.out.println("Admin: admin / admin123");
            System.out.println("Librarian: librarian / librarian123");
            System.out.println("Student: student / student123");
        }
    }
}
