package com.ry.backend.repository;

import com.ry.backend.model.Note;
import com.ry.backend.model.User;
import java.util.List;

public interface NoteRepositoryCustom {
    List<Note> findByArchivedAndCategoryNameAndUser(boolean archived, String categoryName, User user);
}
