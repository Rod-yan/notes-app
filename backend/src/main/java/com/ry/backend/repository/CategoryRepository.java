package com.ensolvers.backend.repository;

import com.ensolvers.backend.model.Category;
import com.ensolvers.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByNameAndUser(String name, User user);
    List<Category> findByUser(User user);
}
