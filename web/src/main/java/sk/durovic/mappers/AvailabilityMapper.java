package sk.durovic.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import sk.durovic.api.dto.AvailabilityDto;
import sk.durovic.model.Availability;

@Mapper
public interface AvailabilityMapper {
    AvailabilityMapper INSTANCE = Mappers.getMapper(AvailabilityMapper.class);

    AvailabilityDto toDto(Availability availability);
}
