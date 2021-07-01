package sk.durovic.model;

import helper.CompanyServiceHelper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import sk.durovic.services.CompanyCredentialsService;
import sk.durovic.services.CompanyService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;

@ExtendWith(MockitoExtension.class)
class UserDetailServiceImplTest {

    @Mock
    private CompanyCredentialsService companyCredentialsService;

    private CompanyService companyService;
    private CompanyCredentials companyCredentials;

    @BeforeEach
    void setUp() {
        companyService = new CompanyServiceHelper(1L, "test@test.com");

        companyCredentials = new CompanyCredentials("test@test.com", "pass");
    }

    @Test
    void getUserSuccess(){
        Mockito.when(companyCredentialsService.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(companyCredentials));

        UserDetailsService userDetailsService =
                new UserDetailServiceImpl(companyCredentialsService, companyService);
        UserDetails user = userDetailsService.loadUserByUsername("test@test.com");

        Mockito.verify(companyCredentialsService, Mockito.atMostOnce())
                .findByUsername("test@test.com");

        assertThat(user, Matchers.notNullValue());
        assertThat(user.getUsername(), Matchers.equalTo("test@test.com"));
    }

    @Test
    void getUserThrowsException(){
        Mockito.when(companyCredentialsService.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.empty());

        UserDetailsService userDetailsService =
                new UserDetailServiceImpl(companyCredentialsService, companyService);



        assertThrows(UsernameNotFoundException.class, () ->
            userDetailsService.loadUserByUsername("error")
        );
        Mockito.verify(companyCredentialsService, Mockito.atMostOnce())
                .findByUsername("error");

    }
}