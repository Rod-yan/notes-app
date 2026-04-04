package com.ry.controller;

import com.ry.dto.CategoryResponse;
import com.ry.model.User;
import com.ry.repository.UserRepository;
import com.ry.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
