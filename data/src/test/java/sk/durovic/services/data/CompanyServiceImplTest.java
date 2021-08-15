package sk.durovic.services.data;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import sk.durovic.model.Company;
import sk.durovic.repositories.CompanyRepository;
import sk.durovic.services.CompanyService;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;

    private CompanyService companyService;

    private Company company;

    @BeforeEach
    void setUp() {
        companyService = new CompanyServiceImpl(companyRepository);
        company = new Company();
        company.setId(1L);
        company.setName("testing");
    }

    @Test
    void findAll() {
        Set<Company> companies = Set.of(company, new Company());
        Mockito.when(companyRepository.findAll()).thenReturn(companies);

        Set<Company> companySet = companyService.findAll();

        assertThat(companySet, Matchers.hasSize(2));
        assertThat(companySet, Matchers.hasItem(company));
    }


    @Test
    void save() {
        Mockito.when(companyRepository.save(Mockito.any())).thenReturn(company);

        Company saved = companyService.save(company);

        Mockito.verify(companyRepository, Mockito.atMostOnce()).save(company);
        assertThat(saved, Matchers.is(company));
    }

    @Test
    void delete() {
        Mockito.doNothing().when(companyRepository).delete(company);

        companyService.delete(company);

        Mockito.verify(companyRepository, Mockito.atMostOnce()).delete(company);
    }

    @Test
    void deleteById() {
        long id=1L;
        Mockito.doNothing().when(companyRepository).deleteById(id);

        companyService.deleteById(id);

        Mockito.verify(companyRepository, Mockito.atMostOnce()).deleteById(id);

    }

    @Nested
    class findByEmailMethods {
        @Test
        void findByEmail() {
            String email = company.getEmail();
            Mockito.when(companyRepository.findByEmail(email)).thenReturn(Optional.ofNullable(company));

            Company found = companyService.findByEmail(email).orElse(null);

            assertThat(found, Matchers.notNullValue());
            assertThat(found, Matchers.is(company));
        }

        @Test
        void notFindByEmail(){
            String email = "nothing";
            Mockito.when(companyRepository.findByEmail(email)).thenReturn(Optional.empty());

            Company found = companyService.findByEmail(email).orElse(null);

            assertThat(found, Matchers.nullValue());
        }

    }

    @Nested
    class FindByIdMethods {

        @Test
        void findById() {
            long id = company.getId();
            Mockito.when(companyRepository.findById(Mockito.any())).thenReturn(Optional.ofNullable(company));

            Company found = companyService.findById(id);

            Mockito.verify(companyRepository, Mockito.atMostOnce())
                    .findById(id);

            assertThat(found, Matchers.notNullValue());
            assertThat(found, Matchers.samePropertyValuesAs(company));
        }

        @Test
        void notFindById(){
            long id = 5000L;
            Mockito.when(companyRepository.findById(id)).thenReturn(Optional.empty());

            Company found = companyService.findById(id);

            Mockito.verify(companyRepository, Mockito.atMostOnce())
                    .findById(5000L);
            assertThat(found, Matchers.nullValue());
        }
    }
}