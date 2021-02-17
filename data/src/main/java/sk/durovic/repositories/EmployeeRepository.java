package sk.durovic.repositories;

import org.springframework.data.repository.CrudRepository;
import sk.durovic.model.Employee;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {
}
