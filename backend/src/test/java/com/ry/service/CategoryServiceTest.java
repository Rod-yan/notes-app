package com.ry.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ry.dto.CategoryResponse;
import com.ry.model.Category;
import com.ry.model.User;
import com.ry.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
