package sk.durovic.database;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import sk.durovic.model.CompanyCredentials;
import sk.durovic.repositories.CompanyCredentialsRepository;

import static org.hamcrest.MatcherAssert.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CompanyCredentialsRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private CompanyCredentialsRepository companyCredentialsRepository;

    private CompanyCredentials companyCredentials;

    @BeforeEach
    void setUp() {
        companyCredentials = new CompanyCredentials("User", "Password");
    }

    @Test
    void findByUsername(){
        testEntityManager.persist(companyCredentials);

        CompanyCredentials cc = companyCredentialsRepository.findByUsername(companyCredentials
                .getUsername()).orElse(null);

        assertThat(cc, Matchers.samePropertyValuesAs(companyCredentials));
    }

    @Test
    void notFoundUsername(){
        testEntityManager.persist(companyCredentials);

        CompanyCredentials cc = companyCredentialsRepository.findByUsername("Mock").orElse(null);

        assertThat(cc, Matchers.nullValue());

    }

    @Test
    void save(){
        long id = companyCredentialsRepository.save(companyCredentials).getId();

        CompanyCredentials found = testEntityManager.find(CompanyCredentials.class, id);

        assertThat(found, Matchers.samePropertyValuesAs(companyCredentials));
    }

    @Test
    void delete(){
        CompanyCredentials saved = testEntityManager.persist(companyCredentials);

        companyCredentialsRepository.delete(saved);

        CompanyCredentials found = testEntityManager.find(CompanyCredentials.class, saved.getId());

        assertThat(found, Matchers.nullValue());
    }

    @Test
    void notDelete(){
        long id = testEntityManager.persistAndGetId(companyCredentials, Long.class);
        CompanyCredentials saved = new CompanyCredentials("test", "pass");

        companyCredentialsRepository.delete(saved);

        CompanyCredentials found = testEntityManager.find(CompanyCredentials.class, id);

        assertThat(found, Matchers.samePropertyValuesAs(companyCredentials));
    }

    @Test
    void deleteById(){
        long id = testEntityManager.persistAndGetId(companyCredentials, Long.class);

        companyCredentialsRepository.deleteById(id);

        CompanyCredentials found = testEntityManager.find(CompanyCredentials.class, id);

        assertThat(found, Matchers.nullValue());
    }

    @Test
    void notDeleteById(){
        long id = testEntityManager.persistAndGetId(companyCredentials, Long.class);

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            companyCredentialsRepository.deleteById(2000L);
                });
    }
}
