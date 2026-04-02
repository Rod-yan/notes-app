package com.ensolvers.backend.service;

import com.ensolvers.backend.dto.CategoryResponse;
import com.ensolvers.backend.model.Category;
import com.ensolvers.backend.model.User;
import com.ensolvers.backend.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(1L, "user", "pass");
    }

    @Test
    void getAllCategories_shouldReturnList() {
        Category category = new Category(1L, "Work", null, user);
        when(categoryRepository.findByUser(user)).thenReturn(List.of(category));

        List<CategoryResponse> result = categoryService.getAllCategories(user);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Work");
    }

    @Test
    void getOrCreate_shouldReturnExisting_whenExists() {
        Category category = new Category(1L, "Work", null, user);
        when(categoryRepository.findByNameAndUser("Work", user)).thenReturn(Optional.of(category));

        Category result = categoryService.getOrCreate("Work", user);

        assertThat(result.getName()).isEqualTo("Work");
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void getOrCreate_shouldCreate_whenNotExists() {
        Category category = new Category(1L, "Work", null, user);
        when(categoryRepository.findByNameAndUser("Work", user)).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.getOrCreate("Work", user);

        assertThat(result.getName()).isEqualTo("Work");
        verify(categoryRepository).save(any(Category.class));
    }
}
