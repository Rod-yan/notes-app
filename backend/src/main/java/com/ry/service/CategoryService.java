package com.ry.service;

import com.ry.dto.CategoryResponse;
import com.ry.model.Category;
import com.ry.model.User;
import com.ry.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> getAllCategories(User user) {
        return categoryRepository.findByUser(user).stream()
                .map(c -> {
                    CategoryResponse res = new CategoryResponse();
                    res.setId(c.getId());
                    res.setName(c.getName());
                    return res;
                })
                .collect(Collectors.toList());
    }

    public Category getOrCreate(String name, User user) {
        return categoryRepository.findByNameAndUser(name, user)
                .orElseGet(() -> {
                    Category newCategory = new Category();
                    newCategory.setName(name);
                    newCategory.setUser(user);
                    return categoryRepository.save(newCategory);
                });
    }
}
