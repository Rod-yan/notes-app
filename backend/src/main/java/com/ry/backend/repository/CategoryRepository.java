package com.ry.backend.repository;

import com.ry.backend.model.Category;
import com.ry.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNameAndUser(String name, User user);
    List<Category> findByUser(User user);
}
