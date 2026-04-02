package com.ry.backend.repository;

import com.ry.backend.model.Category;
import com.ry.backend.model.Note;
import com.ry.backend.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class NoteRepositoryImpl implements NoteRepositoryCustom {

    private static final String PROP_CATEGORIES = "categories";
    private static final String PROP_ARCHIVED = "archived";
    private static final String PROP_NAME = "name";
    private static final String PROP_USER = "user";

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Note> findByArchivedAndCategoryNameAndUser(boolean archived, String categoryName, User user) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Note> query = cb.createQuery(Note.class);
        Root<Note> note = query.from(Note.class);

        // Eager load categories to avoid N+1
        note.fetch(PROP_CATEGORIES, JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(note.get(PROP_ARCHIVED), archived));
        predicates.add(cb.equal(note.get(PROP_USER), user));

        if (categoryName != null && !categoryName.isEmpty()) {
            Join<Note, Category> categories = note.join(PROP_CATEGORIES);
            predicates.add(cb.equal(categories.get(PROP_NAME), categoryName));
        }

        query.where(cb.and(predicates.toArray(new Predicate[0])));
        query.distinct(true);

        return entityManager.createQuery(query).getResultList();
    }
}
