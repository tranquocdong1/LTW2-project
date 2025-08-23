package com.example.user_management.controller;

import com.example.user_management.entity.Company;
import com.example.user_management.entity.User;
import com.example.user_management.service.CompanyService;
import com.example.user_management.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserService userService;

    // Hiển thị danh sách công ty
    @GetMapping
    public String showCompanyList(Model model) {
        model.addAttribute("companies", companyService.getAllCompanies());
        return "company-list";
    }

    // Hiển thị form thêm công ty
    @GetMapping("/add")
    public String showAddCompanyForm(Model model) {
        model.addAttribute("company", new Company());
        return "add-company";
    }

    // Xử lý thêm công ty
    @PostMapping("/add")
    public String addCompany(@ModelAttribute Company company) {
        companyService.addCompany(company);
        return "redirect:/companies";
    }

    // Hiển thị form sửa công ty
    @GetMapping("/edit/{id}")
    public String showEditCompanyForm(@PathVariable Long id, Model model) {
        Company company = companyService.getCompanyById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + id));
        model.addAttribute("company", company);
        return "edit-company";
    }

    // Xử lý cập nhật công ty
    @PostMapping("/edit/{id}")
    public String updateCompany(@PathVariable Long id, @ModelAttribute Company company) {
        company.setId(id);
        companyService.updateCompany(company);
        return "redirect:/companies";
    }

    // Xem chi tiết công ty và danh sách nhân viên
    @GetMapping("/view/{id}")
    public String viewCompany(@PathVariable Long id, Model model) {
        Company company = companyService.getCompanyById(id)
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + id));
        model.addAttribute("company", company);
        model.addAttribute("users", company.getUsers());
        model.addAttribute("availableUsers", userService.getUsersWithoutCompany());
        return "company-detail";
    }

    // Xử lý thêm người dùng vào công ty
    @PostMapping("/{companyId}/add-user")
    public String addUserToCompany(@PathVariable Long companyId, @RequestParam Long userId) {
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        companyService.addUserToCompany(companyId, user);
        return "redirect:/companies/view/" + companyId;
    }

    // Xóa user khỏi công ty
    @PostMapping("/{companyId}/remove-user/{userId}")
    public String removeUserFromCompany(@PathVariable Long companyId, @PathVariable Long userId) {
        companyService.removeUserFromCompany(companyId, userId);
        return "redirect:/companies/view/" + companyId;
    }

    // Xóa công ty
    @GetMapping("/delete/{id}")
    public String deleteCompany(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            companyService.deleteCompany(id);
            redirectAttributes.addFlashAttribute("successMessage", "Company deleted successfully!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Company not found with id: " + id);
        }
        return "redirect:/companies";
    }
}