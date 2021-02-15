package sk.durovic.service;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;
import sk.durovic.model.Employee;

@Service
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
}
