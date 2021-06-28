package sk.durovic.controller;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import sk.durovic.commands.CompanyCommand;
import sk.durovic.converters.CompanyCommandToCompany;
import sk.durovic.model.Company;
import sk.durovic.model.CompanyCredentials;
import sk.durovic.services.CompanyCredentialsService;
import sk.durovic.services.CompanyService;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.hamcrest.MatcherAssert.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CompanyControllerIT {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyCredentialsService companyCredentialsService;

    @Autowired
    private MockMvc mockMvc;

    private CompanyCommand companyCommand;

    @BeforeEach
    void setUp() {
        companyCommand = new CompanyCommand();
        companyCommand.setName("user");
        companyCommand.setPassword("pass");
        companyCommand.setAddress("Poharyho 5");
        companyCommand.setCity("banska bystrica");
        companyCommand.setEmail("abc@abc.com");
        companyCommand.setPhone("555-555-555");
    }

    @Test
    void registerCompany() throws Exception {
        Company company = new CompanyCommandToCompany().convert(companyCommand);
        CompanyCredentials companyCredentials = new CompanyCommandToCompany()
                .convertToCredentials(companyCommand);


        mockMvc.perform(MockMvcRequestBuilders.post("/register/new")
                .with(csrf())
                .flashAttr("companyCommand", companyCommand))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/register?successfull"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());

        Company found = companyService.findByEmail(companyCommand.getEmail()).orElse(new Company());

        assertThat(company, Matchers.notNullValue());
        assertThat(company, Matchers.samePropertyValuesAs(found, "id","listOfCars"));
        assertThat(companyCredentials, Matchers.samePropertyValuesAs(companyCredentialsService
                .findByIdCompany(company.getEmail())
                .orElse(new CompanyCredentials()), "id", "password"));
    }

    @Test
    void getAllCompanies() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/listcompanies"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("companies"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("companies"));
    }
}