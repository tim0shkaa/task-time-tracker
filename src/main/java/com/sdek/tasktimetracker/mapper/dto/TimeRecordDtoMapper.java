package com.sdek.tasktimetracker.mapper.dto;

import com.sdek.tasktimetracker.model.dto.request.TimeRecordCreateRequest;
import com.sdek.tasktimetracker.model.dto.response.TimeRecordResponse;
import com.sdek.tasktimetracker.model.entity.TimeRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TimeRecordDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employeeId", source = "employeeId")
    TimeRecord toEntity(TimeRecordCreateRequest request, Long employeeId);

    TimeRecordResponse toResponse(TimeRecord record);
}
