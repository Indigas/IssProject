package sk.durovic.api.login;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.UserDetailsTestService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import sk.durovic.model.JwtToken;
import sk.durovic.repositories.JwtTokenRepository;
import sk.durovic.security.JwtUtil;
import sk.durovic.services.JwtTokenService;
import sk.durovic.services.data.JwtTokenServiceImpl;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;

@WebMvcTest(JwtAuthentication.class)
@Import(UserDetailsTestService.class)
class JwtAuthenticationTest {
    @MockBean
    private JwtTokenService jwtTokenService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper jsonData = new ObjectMapper();

    @BeforeEach
    void setUp() {
    }

    @Test
    void login() throws Exception {
        Mockito.when(jwtTokenService.createToken(Mockito.any(), Mockito.any()))
                .thenReturn(null);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/data/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentialsToSend()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Map<String, String> map = (Map<String, String>) jsonData.readValue(content, Map.class);


        Mockito.verify(jwtTokenService, Mockito.atMostOnce())
                .createToken(argumentCaptor.capture(), Mockito.any());
        assertThat(map.get("token"), Matchers.is(argumentCaptor.getValue()));
    }

    @Test
    void getCurrentUser() throws Exception {
        Mockito.when(jwtTokenService.isValid(Mockito.any())).thenReturn(true);
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        String token = JwtUtil.createJWTtoken("testUser");

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/data/login/me")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+token))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Map<String, String> map = (Map<String, String>) jsonData.readValue(content, Map.class);

        Mockito.verify(jwtTokenService,Mockito.atMostOnce()).isValid(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), Matchers.is(token));
        assertThat(map.get("user"), Matchers.is("testUser"));
    }

    private String credentialsToSend() throws JsonProcessingException {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "testUser");
        credentials.put("password", "pass");

        return jsonData.writeValueAsString(credentials);
    }
}