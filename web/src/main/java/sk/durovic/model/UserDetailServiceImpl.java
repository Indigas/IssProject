package sk.durovic.model;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sk.durovic.services.CompanyCredentialsService;
import sk.durovic.services.CompanyService;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private CompanyCredentialsService companyCredentialsService;

    @Autowired
    private CompanyService companyService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Optional<CompanyCredentials> companyCredentials = companyCredentialsService.findByUsername(s);

        companyCredentials.orElseThrow(() -> new UsernameNotFoundException("Not found"));

        return new UserDetailImpl(companyCredentials.get(), companyService);
    }
}
