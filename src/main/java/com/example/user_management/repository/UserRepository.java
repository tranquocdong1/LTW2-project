package com.example.user_management.repository;

import com.example.user_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Tìm user theo email
    Optional<User> findByEmail(String email);

    // Tìm users theo company ID
    List<User> findByCompanyId(Long companyId);

    // Tìm users không thuộc company nào
    @Query("SELECT u FROM User u WHERE u.company IS NULL")
    List<User> findUsersWithoutCompany();
}