package org.medilabo.notes.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.medilabo.notes.model.Note;
import org.medilabo.notes.model.dto.NoteDTO;

@Mapper(componentModel = "spring")
public interface NoteMapper {

    NoteDTO noteToDto(Note note);

    Note dtoToNote(NoteDTO noteDTO);

    @Mapping(target = "id", ignore = true)
    void updateNoteFromDto(NoteDTO noteDTO, @MappingTarget Note note);
}
