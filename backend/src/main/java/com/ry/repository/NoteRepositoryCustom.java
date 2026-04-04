package com.ry.repository;

import com.ry.model.Note;
import com.ry.model.User;
import java.util.List;

public interface NoteRepositoryCustom {
    List<Note> findByArchivedAndCategoryNameAndUser(boolean archived, String categoryName, User user);
}
