package com.ry.repository;

import com.ry.model.Note;
import com.ry.model.Category;
import com.ry.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class NoteRepositoryTest {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByArchivedAndCategoryNameAndUser_shouldReturnCorrectNotes() {
        User user = new User(null, "user", "pass");
        user = userRepository.save(user);

        Note note1 = new Note();
        note1.setTitle("Note 1");
        note1.setArchived(false);
        note1.setUser(user);
        noteRepository.save(note1);

        Note note2 = new Note();
        note2.setTitle("Note 2");
        note2.setArchived(true);
        note2.setUser(user);
        noteRepository.save(note2);

        List<Note> active = noteRepository.findByArchivedAndCategoryNameAndUser(false, null, user);
        assertThat(active).hasSize(1);
        assertThat(active.get(0).getTitle()).isEqualTo("Note 1");

        List<Note> archived = noteRepository.findByArchivedAndCategoryNameAndUser(true, null, user);
        assertThat(archived).hasSize(1);
        assertThat(archived.get(0).getTitle()).isEqualTo("Note 2");
    }

    @Test
    void findByArchivedAndCategoryNameAndUser_withCategory_shouldReturnCorrectNotes() {
        User user = new User(null, "user", "pass");
        user = userRepository.save(user);

        Category category = new Category(null, "Work", null, user);
        category = categoryRepository.save(category);

        Note note = new Note();
        note.setTitle("Work Note");
        note.setArchived(false);
        note.setCategories(Set.of(category));
        note.setUser(user);
        noteRepository.save(note);

        List<Note> result = noteRepository.findByArchivedAndCategoryNameAndUser(false, "Work", user);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Work Note");

        List<Note> resultEmpty = noteRepository.findByArchivedAndCategoryNameAndUser(false, "Home", user);
        assertThat(resultEmpty).isEmpty();
    }
}
