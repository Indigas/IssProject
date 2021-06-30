package sk.durovic.services.data;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import sk.durovic.model.Company;
import sk.durovic.model.CompanyCredentials;
import sk.durovic.repositories.CompanyCredentialsRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;

@ExtendWith(MockitoExtension.class)
class CompanyCredentialsServiceImplTest {

    @Mock
    private CompanyCredentialsRepository companyCredentialsRepository;

    @InjectMocks
    private CompanyCredentialsServiceImpl companyCredentialsService;

    private CompanyCredentials companyCredentials;

    @BeforeEach
    void setUp() {
        companyCredentials = new CompanyCredentials("test", "pass");

    }

    @Test
    void findByUsername() {
        Mockito.when(companyCredentialsRepository.findByUsername("test"))
                .thenReturn(Optional.of(companyCredentials));

        CompanyCredentials cc = companyCredentialsService.findByUsername("test").orElse(null);

        Mockito.verify(companyCredentialsRepository, Mockito.atMostOnce()).findByUsername("test");
        assertThat(cc, Matchers.notNullValue());
        assertThat(cc, Matchers.is(companyCredentials));
    }

    @Test
    void save() {
        Mockito.when(companyCredentialsRepository.save(companyCredentials))
                .thenReturn(companyCredentials);

        CompanyCredentials saved = companyCredentialsService.save(companyCredentials);

        Mockito.verify(companyCredentialsRepository, Mockito.atMostOnce())
                .save(companyCredentials);
        assertThat(saved, Matchers.notNullValue());
        assertThat(saved, Matchers.is(companyCredentials));
    }

    @Test
    void delete() {
        Mockito.doNothing().when(companyCredentialsRepository).delete(companyCredentials);

        companyCredentialsService.delete(companyCredentials);

        Mockito.verify(companyCredentialsRepository, Mockito.atMostOnce())
                .delete(companyCredentials);
    }

    @Test
    void deleteById() {
        Mockito.doNothing().when(companyCredentialsRepository).deleteById(1L);

        companyCredentialsService.deleteById(1L);

        Mockito.verify(companyCredentialsRepository, Mockito.atMostOnce())
                .deleteById(1L);
    }
}