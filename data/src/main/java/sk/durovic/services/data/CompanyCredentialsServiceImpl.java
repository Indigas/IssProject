package sk.durovic.services.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sk.durovic.model.CompanyCredentials;
import sk.durovic.repositories.CompanyCredentialsRepository;
import sk.durovic.services.CompanyCredentialsService;

import javax.transaction.NotSupportedException;
import java.util.Optional;
import java.util.Set;

@Service
public class CompanyCredentialsServiceImpl implements CompanyCredentialsService {

    private final CompanyCredentialsRepository companyCredentialsRepository;

    public CompanyCredentialsServiceImpl(CompanyCredentialsRepository companyCredentialsRepository) {
        this.companyCredentialsRepository = companyCredentialsRepository;
    }

    @Override
    public Optional<CompanyCredentials> findByIdCompany(String idCompany) {
        return companyCredentialsRepository.findByIdCompany(idCompany);
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
