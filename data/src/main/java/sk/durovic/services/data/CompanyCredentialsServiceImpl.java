package sk.durovic.services.data;

import org.springframework.stereotype.Service;
import sk.durovic.model.CompanyCredentials;
import sk.durovic.repositories.CompanyCredentialsRepository;
import sk.durovic.services.CompanyCredentialsService;

import java.util.Optional;

@Service
public class CompanyCredentialsServiceImpl implements CompanyCredentialsService {

    private final CompanyCredentialsRepository companyCredentialsRepository;

    public CompanyCredentialsServiceImpl(CompanyCredentialsRepository companyCredentialsRepository) {
        this.companyCredentialsRepository = companyCredentialsRepository;
    }

    @Override
    public Optional<CompanyCredentials> findByUsername(String username) {
        return companyCredentialsRepository.findByUsername(username);
    }

    @Override
    public CompanyCredentials save(CompanyCredentials object) {
        return companyCredentialsRepository.save(object);
    }

    @Override
    public void delete(CompanyCredentials object) {
        companyCredentialsRepository.delete(object);
    }

    @Override
    public void deleteById(Long id) {
        companyCredentialsRepository.deleteById(id);
    }
}
