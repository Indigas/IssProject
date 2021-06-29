package config;

import helper.CompanyServiceHelper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import sk.durovic.model.CompanyCredentials;
import sk.durovic.model.UserDetailImpl;

@TestConfiguration
public class UserDetailsTestService {

    @Bean
    public UserDetailsService getService(){
        return new MyUserDetails();
    }

    private class MyUserDetails implements UserDetailsService {
        @Override
        public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
            CompanyCredentials companyCredentials = new CompanyCredentials("abc@abc.com", "pass");

            UserDetails userDetails = new UserDetailImpl(companyCredentials, new CompanyServiceHelper(1L, "abc@abc.com"));
            return userDetails;
        }
    }
}
