package com.ensolvers.backend.controller;

import com.ensolvers.backend.dto.CategoryResponse;
import com.ensolvers.backend.model.User;
import com.ensolvers.backend.repository.UserRepository;
import com.ensolvers.backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(CategoryController.API_PATH)
@RequiredArgsConstructor
public class CategoryController {
    public static final String API_PATH = "/api/categories";

    private final CategoryService categoryService;
    private final UserRepository userRepository;

    @GetMapping
    public List<CategoryResponse> getAllCategories(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return categoryService.getAllCategories(user);
    }
}
