package sk.durovic.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import sk.durovic.model.Company;
import sk.durovic.services.CompanyCredentialsService;
import sk.durovic.services.CompanyService;

import static org.hamcrest.MatcherAssert.*;

import java.util.Set;

@ExtendWith(MockitoExtension.class)
public class CompanyControllerTest {
    @Mock
    private CompanyService companyService;
    @Mock
    private CompanyCredentialsService companyCredentialsService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Model model;

    @InjectMocks
    private CompanyController companyController;

    private Company company;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setId(1L);
        company.setName("Marek");
    }

    @Test
    public void getAllCompaniesTest(){
        Company a = new Company();
        a.setId(2L);
        a.setName("Indigo");
        Mockito.when(companyService.findAll()).thenReturn(Set.of(company, a));

        ArgumentCaptor<Set<Company>> argumentCaptor = ArgumentCaptor.forClass(Set.class);

        String viewName = companyController.getAllCompanies(model);

        assertThat(viewName, Matchers.equalTo("companies"));

        Mockito.verify(companyService, Mockito.atMostOnce()).findAll();
        Mockito.verify(model, Mockito.atMostOnce())
                .addAttribute(ArgumentMatchers.eq("companies"), argumentCaptor.capture());
        Set<Company> companies = argumentCaptor.getValue();
        assertThat(companies, Matchers.hasSize(2));

    }
}
