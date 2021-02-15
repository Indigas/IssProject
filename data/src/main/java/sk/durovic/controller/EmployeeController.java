package sk.durovic.controller;

import org.springframework.stereotype.Controller;
import sk.durovic.service.EmployeeRepository;

@Controller
public class EmployeeController {

    private EmployeeRepository employeeRepository;

    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }


}
