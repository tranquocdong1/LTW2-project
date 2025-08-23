package com.example.user_management.repository;

import com.example.user_management.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    // Tìm company theo tên
    Optional<Company> findByName(String name);

}