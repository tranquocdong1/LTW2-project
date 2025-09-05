package com.example.user_management.controller;

import com.example.user_management.entity.Company;
import com.example.user_management.entity.User;
import com.example.user_management.service.CompanyService;
import com.example.user_management.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import java.util.List;

@RestController
@RequestMapping("/api/companies")
public class CompanyRestController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    // Lấy danh sách tất cả công ty
    @GetMapping
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();
        return ResponseEntity.ok(companies);
    }

    // Lấy thông tin chi tiết công ty theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        Company company = companyService.getCompanyById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + id));
        return ResponseEntity.ok(company);
    }

    // Thêm công ty mới
    @PostMapping
    public ResponseEntity<?> addCompany(@Valid @RequestBody Company company, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        try {
            Company savedCompany = companyService.addCompany(company);
            return ResponseEntity.ok(savedCompany);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred: " + e.getMessage());
        }
    }

    // Cập nhật thông tin công ty
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCompany(@PathVariable Long id, @Valid @RequestBody Company company, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        try {
            company.setId(id);
            Company updatedCompany = companyService.updateCompany(company);
            return ResponseEntity.ok(updatedCompany);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred: " + e.getMessage());
        }
    }

    // Xóa công ty
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCompany(@PathVariable Long id) {
        try {
            companyService.deleteCompany(id);
            return ResponseEntity.ok("Company deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred: " + e.getMessage());
        }
    }

    // Xem chi tiết công ty và danh sách nhân viên
    @GetMapping("/{id}/detail")
    public ResponseEntity<Company> viewCompanyDetail(@PathVariable Long id) {
        Company company = companyService.getCompanyById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + id));
        return ResponseEntity.ok(company);
    }

    // Thêm người dùng vào công ty
    @PostMapping("/{companyId}/add-user")
    public ResponseEntity<?> addUserToCompany(@PathVariable Long companyId, @RequestParam Long userId) {
        try {
            User user = userService.getUserById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
            companyService.addUserToCompany(companyId, user);
            return ResponseEntity.ok("User added to company successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred: " + e.getMessage());
        }
    }

    // Xóa người dùng khỏi công ty
    @DeleteMapping("/{companyId}/remove-user/{userId}")
    public ResponseEntity<?> removeUserFromCompany(@PathVariable Long companyId, @PathVariable Long userId) {
        try {
            companyService.removeUserFromCompany(companyId, userId);
            return ResponseEntity.ok("User removed from company successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("An unexpected error occurred: " + e.getMessage());
        }
    }
}