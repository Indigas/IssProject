package sk.durovic.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CompanyDto {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String phone;
    private String email;
}
