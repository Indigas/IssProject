package sk.durovic.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sk.durovic.services.CompanyCredentialsService;
import sk.durovic.services.CompanyService;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    CompanyCredentialsService companyCredentialsService;

    @Autowired
    CompanyService companyService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<Company> company = companyService.findByEmail(s);

        company.orElseThrow(()-> new UsernameNotFoundException("Not found"));

        Optional<CompanyCredentials> companyCredentials = companyCredentialsService.findByIdCompany(company.get().getEmail());

        return companyCredentials.map(UserDetailImpl::new).get();
    }
}
