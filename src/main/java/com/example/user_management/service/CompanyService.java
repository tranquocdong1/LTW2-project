package com.example.user_management.service;

import com.example.user_management.entity.Company;
import com.example.user_management.entity.User;
import com.example.user_management.repository.CompanyRepository;
import com.example.user_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    // them cong ty
    public Company addCompany(Company company) {
        // kiem tra ten cong ty da ton tai chua
        if (companyRepository.findByName(company.getName()).isPresent()) {
            throw new IllegalArgumentException("Company name already exists: " + company.getName());
        }
        return companyRepository.save(company);
    }

    // lay danh sach cong ty
    @Transactional(readOnly = true)
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    // lay cong ty theo ID
    @Transactional(readOnly = true)
    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    // cap nhat cong ty
    public Company updateCompany(Company company) {
        if (company.getId() == null) {
            throw new IllegalArgumentException("Company ID cannot be null for update");
        }

        // kiem tra cong ty co ton tai khong
        Company existingCompany = companyRepository.findById(company.getId())
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + company.getId()));

        // kiem tra ten company conflict (neu ten thay doi)
        if (!existingCompany.getName().equals(company.getName())) {
            if (companyRepository.findByName(company.getName()).isPresent()) {
                throw new IllegalArgumentException("Company name already exists: " + company.getName());
            }
        }

        return companyRepository.save(company);
    }

    // xoa cong ty
    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + id));

        //kiem tra cong ty co users khong
        List<User> users = userRepository.findByCompanyId(id);
        if (!users.isEmpty()) {
            // dua ra exception
            throw new IllegalStateException("Cannot delete company with existing users. Please reassign or delete users first.");
        }

        companyRepository.deleteById(id);
    }

    // them user vao cong ty
    public Company addUserToCompany(Long companyId, User user) {
        Company company = getCompanyById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + companyId));

        user.setCompany(company);
        userRepository.save(user);

        return company;
    }

    // xoa user khoi cong ty
    public Company removeUserFromCompany(Long companyId, Long userId) {
        Company company = getCompanyById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + companyId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        if (!user.getCompany().getId().equals(companyId)) {
            throw new IllegalArgumentException("User does not belong to this company");
        }

        user.setCompany(null);
        userRepository.save(user);

        return company;
    }

}