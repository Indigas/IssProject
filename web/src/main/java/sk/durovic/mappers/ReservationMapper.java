package sk.durovic.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import sk.durovic.api.dto.ReservationDto;
import sk.durovic.model.Reservation;

@Mapper
public interface ReservationMapper {
    ReservationMapper INSTANCE = Mappers.getMapper(ReservationMapper.class);

    ReservationDto toDto(Reservation reservation);
}
