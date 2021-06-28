package sk.durovic.model;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import sk.durovic.services.CompanyService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class UserDetailImpl implements UserDetails {

    private String userName;
    private String password;
    private boolean isEnabled;
    private Company company;
    private final List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();

    public UserDetailImpl(CompanyCredentials companyCredentials, CompanyService companyService) {
        this.userName = companyCredentials.getIdCompany();
        this.password = companyCredentials.getPassword();
        this.isEnabled = true;

        this.company = companyService.findByEmail(userName).get();
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return grantedAuthorityList;
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
