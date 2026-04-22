package org.medilabo.patient.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.medilabo.patient.model.Patient;
import org.medilabo.patient.model.dto.PatientDTO;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientDTO entityToDto(Patient patient);

    Patient dtoToEntity(PatientDTO patientDTO);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDto(PatientDTO patientDTO, @MappingTarget Patient patient);
}
