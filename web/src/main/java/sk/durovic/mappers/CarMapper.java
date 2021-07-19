package sk.durovic.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import sk.durovic.api.dto.CarDto;
import sk.durovic.model.Car;

@Mapper
public interface CarMapper {
    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    CarDto toDto(Car car);
}
