package sk.durovic.database;

import config.JwtTokenTestService;
import helper.CompanyBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import sk.durovic.model.Company;
import sk.durovic.repositories.CompanyRepository;
import sk.durovic.services.CompanyService;
import sk.durovic.services.data.CompanyServiceImpl;

import java.util.Set;
import java.util.TreeSet;

import static org.hamcrest.MatcherAssert.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CompanyRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CompanyRepository companyRepository;

    private Company company;

    @BeforeEach
    void setUp() {
        company = CompanyBuilder.getCompanyForTest();
        company.setId(null);
    }

    @Test
    public void findAll(){
        testEntityManager.persist(company);

        Set<Company> companies = new TreeSet<>();
        companyRepository.findAll().forEach(companies::add);

        assertThat(companies, Matchers.hasSize(1));
        assertThat(companies, Matchers.contains(company));
    }

    @Test
    public void findById(){
        long id = testEntityManager.persistAndGetId(company, Long.class);

        Company found = companyRepository.findById(id).orElse(null);

        assertThat(found, Matchers.samePropertyValuesAs(company, "id"));
    }

    @Test
    public void saveCompany(){
        Company saved = companyRepository.save(company);

        Company found = testEntityManager.find(Company.class, saved.getId());

        assertThat(found, Matchers.samePropertyValuesAs(company, "id"));
    }

    @Test
    public void deleteCompany(){
        Company saved = testEntityManager.persist(company);

        companyRepository.delete(saved);

        Company found = testEntityManager.find(Company.class, saved.getId());

        assertThat(found, Matchers.nullValue());
    }

    @Test
    public void notDeleteCompany(){
        long id = testEntityManager.persistAndGetId(company, Long.class);
        Company saved = CompanyBuilder.getCompanyForTest();
        saved.setId(2L);

        companyRepository.delete(saved);

        Company found = testEntityManager.find(Company.class, id);

        assertThat(found, Matchers.samePropertyValuesAs(company, "id"));
    }

    @Test
    public void deleteByIdCompany(){
        long id = testEntityManager.persistAndGetId(company, Long.class);

        companyRepository.deleteById(id);

        Company found = testEntityManager.find(Company.class, id);

        assertThat(found, Matchers.nullValue());
    }

    @Test
    public void notDeleteByIdCompany(){
        long id = testEntityManager.persistAndGetId(company, Long.class);


            Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
                companyRepository.deleteById(2000L);
            });
    }

    @Test
    public void findByEmail(){
        testEntityManager.persist(company);

        Company found = companyRepository.findByEmail(company.getEmail()).orElse(null);

        assertThat(found, Matchers.samePropertyValuesAs(company, "id"));
    }

    @Test
    public void notFoundByEmail(){
        testEntityManager.persist(company);

        Company found = companyRepository.findByEmail("notfound").orElse(null);

        assertThat(found, Matchers.nullValue());
    }
}
