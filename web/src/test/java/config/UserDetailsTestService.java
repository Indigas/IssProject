package config;

import helper.CompanyServiceHelper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import sk.durovic.model.CompanyCredentials;
import sk.durovic.model.UserDetailImpl;

@TestConfiguration
public class UserDetailsTestService {

    @Bean
    @Primary
    public UserDetailsService getService(){
        return new MyUserDetails();
    }

    private class MyUserDetails implements UserDetailsService {
        @Override
        public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
            String pass = new BCryptPasswordEncoder().encode("pass");
            CompanyCredentials companyCredentials =
                    new CompanyCredentials("testUser", pass);

            UserDetails userDetails = new UserDetailImpl(companyCredentials,
                    new CompanyServiceHelper(1L, companyCredentials.getUsername()));

            return userDetails;
        }
    }
}
