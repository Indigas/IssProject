package sk.durovic.model;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import sk.durovic.services.CompanyService;

import java.util.Collection;

@Slf4j
public class UserDetailImpl implements UserDetails {

    private String userName;
    private String password;
    private boolean isEnabled;
    private Company company;

    public UserDetailImpl(CompanyCredentials companyCredentials, CompanyService companyService) {
        this.userName = companyCredentials.getIdCompany();
        this.password = companyCredentials.getPassword();
        this.isEnabled = true;

        log.error("chyba v hladani company: " + userName + " :: " + companyService);
        this.company = companyService.findByEmail(userName).get();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public Company getCompany(){
        return this.company;
    }
}
