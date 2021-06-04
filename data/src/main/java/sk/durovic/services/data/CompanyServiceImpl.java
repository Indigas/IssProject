package sk.durovic.services.data;

import org.springframework.stereotype.Service;
import sk.durovic.model.Company;
import sk.durovic.repositories.CompanyRepository;
import sk.durovic.services.CompanyService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Set<Company> findAll() {
        Set<Company> list = new HashSet<>();
        companyRepository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public Company findById(Long aLong) {
        return companyRepository.findById(aLong).orElse(null);
    }

    @Override
    public Company save(Company object) {
        return companyRepository.save(object);
    }

    @Override
    public void delete(Company object) {
        companyRepository.delete(object);
    }

    @Override
    public void deleteById(Long aLong) {
        companyRepository.deleteById(aLong);
    }

    @Override
    public Optional<Company> findByEmail(String email) {
        return companyRepository.findByEmail(email);
    }
}
