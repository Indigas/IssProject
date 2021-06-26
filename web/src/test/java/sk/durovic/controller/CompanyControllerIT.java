package sk.durovic.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sk.durovic.model.Company;
import sk.durovic.services.CompanyCredentialsService;
import sk.durovic.services.CompanyService;

import static org.hamcrest.MatcherAssert.*;

import java.util.Set;

@WebMvcTest(CompanyController.class)
class CompanyControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyService companyService;
    @MockBean
    private CompanyCredentialsService companyCredentialsService;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {

    }

    @Test
    void registerCompany() {
    }

    @Test
    void getAllCompanies() throws Exception {
        Company company = new Company();
        company.setName("marek");
        company.setId(1L);

        Mockito.when(companyService.findAll()).thenReturn(Set.of(company));
        mockMvc.perform(MockMvcRequestBuilders.get("/listcompanies"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("companies"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("companies"));


    }
}