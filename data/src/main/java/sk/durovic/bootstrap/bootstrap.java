package sk.durovic.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sk.durovic.model.Employee;
import sk.durovic.repositories.EmployeeRepository;

@Component
public class bootstrap implements CommandLineRunner {

    private EmployeeRepository employeeRepository;

    public bootstrap(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Employee marek = new Employee();
        marek.setFirstName("Marek");
        employeeRepository.save(marek);


        System.out.println("Started in bootstrap");
        System.out.println(employeeRepository.count());
    }
}
