package com.ry.backend.mapper;

import com.ry.backend.dto.CategoryResponse;
import com.ry.backend.dto.NoteRequest;
import com.ry.backend.dto.NoteResponse;
import com.ry.backend.model.Category;
import com.ry.backend.model.Note;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NoteMapper {
    @Mapping(target = "categories", ignore = true)
    Note toEntity(NoteRequest request);

    NoteResponse toResponse(Note note);

    List<NoteResponse> toResponseList(List<Note> notes);

    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(NoteRequest request, @MappingTarget Note note);

    CategoryResponse toCategoryResponse(Category category);
}
