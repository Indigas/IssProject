package sk.durovic.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sk.durovic.commands.CompanyCommand;
import sk.durovic.model.Company;
import sk.durovic.model.CompanyCredentials;
import sk.durovic.services.CompanyCredentialsService;
import sk.durovic.services.CompanyService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Set;

@WebMvcTest(CompanyController.class)
class CompanyControllerWebIT {

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
    void registerCompany() throws Exception {

        CompanyCredentials companyCredentials = new CompanyCredentials();
        companyCredentials.setUsername("Marek");
        CompanyCommand cc = new CompanyCommand();
        cc.setName("AAA");

        Mockito.when(companyService.save(Mockito.any())).thenReturn(new Company());
        Mockito.when(passwordEncoder.encode(Mockito.any())).thenReturn(Mockito.any());
        Mockito.when(companyCredentialsService.save(companyCredentials)).thenReturn(companyCredentials);

        mockMvc.perform(MockMvcRequestBuilders.post("/register/new")
                .requestAttr("companyCommand", cc)
                .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/register?successfull"));
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