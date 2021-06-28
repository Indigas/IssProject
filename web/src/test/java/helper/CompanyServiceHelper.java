package helper;

import sk.durovic.model.Company;
import sk.durovic.services.CompanyService;

import java.util.Optional;
import java.util.Set;

public class CompanyServiceHelper implements CompanyService {

    private Long id;
    private String email;

    public CompanyServiceHelper(Long id, String email) {
        this.id = id;
        this.email = email;
    }

    public Optional<Company> getCompanyForTest(Long id, String email){
        return Optional.of(new CompanyBuilder().setId(id).setEmail(email).setName("CompanyTest").build());
    }

    @Override
    public Optional<Company> findByEmail(String email) {
        return getCompanyForTest(this.id, email);
    }

    @Override
    public Set<Company> findAll() {
        return null;
    }

    @Override
    public Company findById(Long aLong) {
        return new CompanyBuilder().setId(aLong).setName(this.email).build();
    }

    @Override
    public Company save(Company object) {
        return new CompanyBuilder().setId(object.getId()).setName(object.getName()).build();
    }

    @Override
    public void delete(Company object) {

    }

    @Override
    public void deleteById(Long aLong) {

    }
}
