package sk.durovic.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import config.WithMockCustomUser;
import helper.CompanyBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sk.durovic.model.Company;
import sk.durovic.services.CompanyService;
import sk.durovic.services.JwtTokenService;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(AccountApiHandler.class)
class AccountApiHandlerTest {
    @MockBean
    private CompanyService companyService;

    @MockBean
    private JwtTokenService jwtTokenService;

    @Autowired
    private MockMvc mockMvc;

    private Company company;
    private ObjectMapper jsonData;

    @BeforeEach
    void setUp() {
        company = CompanyBuilder.getCompanyForTest();
        company.setId(null);

        jsonData = new ObjectMapper();
    }

    @Test
    @WithMockCustomUser
    void updateDetails() throws Exception{
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/account")
                .with(SecurityMockMvcRequestPostProcessors.user(user))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData.writeValueAsString(company)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Mockito.verify(companyService, Mockito.atMostOnce()).save(company);
    }
}