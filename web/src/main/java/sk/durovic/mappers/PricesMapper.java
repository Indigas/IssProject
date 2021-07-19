package sk.durovic.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import sk.durovic.api.dto.PricesDto;
import sk.durovic.model.Prices;

@Mapper
public interface PricesMapper {
    PricesMapper INSTANCE = Mappers.getMapper(PricesMapper.class);

    PricesDto toDto(Prices prices);
}
