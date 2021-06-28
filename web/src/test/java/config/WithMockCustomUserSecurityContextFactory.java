package config;

import helper.CompanyBuilder;
import helper.CompanyServiceHelper;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import sk.durovic.model.Company;
import sk.durovic.model.CompanyCredentials;
import sk.durovic.model.UserDetailImpl;
import sk.durovic.services.CompanyService;

import java.util.Optional;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser withMockCustomUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        CompanyCredentials companyCredentials = new CompanyCredentials("abc@abc.com", "password");
        CompanyService companyService= new CompanyServiceHelper(1L, "abc@abc.com");

        UserDetailImpl userDetails = new UserDetailImpl(companyCredentials, companyService);

        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword());


        context.setAuthentication(auth);

        return context;

    }
}
