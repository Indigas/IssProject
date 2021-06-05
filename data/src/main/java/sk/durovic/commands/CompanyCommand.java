package sk.durovic.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyCommand {

    private String name;
    private String address;
    private String city;
    private String phone;
    private String email;
    private String password;


}
