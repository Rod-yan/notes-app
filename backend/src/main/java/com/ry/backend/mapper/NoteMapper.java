package com.ensolvers.backend.mapper;

import com.ensolvers.backend.dto.CategoryResponse;
import com.ensolvers.backend.dto.NoteRequest;
import com.ensolvers.backend.dto.NoteResponse;
import com.ensolvers.backend.model.Category;
import com.ensolvers.backend.model.Note;
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
