package sk.durovic.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import sk.durovic.api.dto.CompanyDto;
import sk.durovic.model.Company;

@Mapper
public interface CompanyMapper {
    CompanyMapper INSTANCE = Mappers.getMapper(CompanyMapper.class);

    CompanyDto toDto(Company company);

    @Mapping(target = "listOfCars", ignore = true)
    Company dtoToCompany(CompanyDto companyDto);
}
