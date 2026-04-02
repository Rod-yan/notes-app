package com.ensolvers.backend.repository;

import com.ensolvers.backend.model.Note;
import com.ensolvers.backend.model.User;
import java.util.List;

public interface NoteRepositoryCustom {
    List<Note> findByArchivedAndCategoryNameAndUser(boolean archived, String categoryName, User user);
}
