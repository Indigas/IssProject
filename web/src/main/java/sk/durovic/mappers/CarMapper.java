package sk.durovic.mappers;

import lombok.extern.slf4j.Slf4j;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.control.MappingControl;
import org.mapstruct.control.MappingControls;
import org.mapstruct.factory.Mappers;
import sk.durovic.api.dto.CarDto;
import sk.durovic.model.Car;
import sk.durovic.model.Company;
import sk.durovic.services.FileStorageService;
import sk.durovic.services.data.FileStorageServiceImpl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

@Mapper
@Slf4j
public abstract class CarMapper {
    public static CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    public abstract CarDto toDto(Car car);

    public abstract Car dtoToCar(CarDto carDto);

    @AfterMapping
    protected void updateImagesPath(@MappingTarget CarDto carDto) {
        Company company = CompanyMapper.INSTANCE.dtoToCompany(carDto.getCompany());
        Car car = CarMapper.INSTANCE.dtoToCar(carDto);
        FileStorageService fss = new FileStorageServiceImpl(company);
        try {
            Stream<Path> paths = fss.loadAll(car);

            String[] images = paths.map(Path::toString).toArray(String[]::new);

            carDto.setImages(images);
        } catch (IOException e){
            log.error("Cannot update images in carDto");
        }
    }
}
