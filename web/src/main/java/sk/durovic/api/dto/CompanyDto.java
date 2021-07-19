package sk.durovic.api.dto;

import java.util.Set;

public class CompanyDto {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String phone;
    private String email;

    private Set<CarDto> listOfCars;
}
