package com.ry.repository;

import com.ry.model.Category;
import com.ry.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNameAndUser(String name, User user);
    List<Category> findByUser(User user);
}
