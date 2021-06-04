package sk.durovic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
               /* .authorizeRequests(authorize -> {
                    authorize.antMatchers("/").permitAll()
                            .antMatchers("/assets/**", "/dist/**", "/images/**").permitAll()
                            .antMatchers("/index", "/login").permitAll()
                            .antMatchers("/companies/**").permitAll()
                            .antMatchers("/list", "/list/car-grid").permitAll()
                            .antMatchers("/about", "/register", "/reservation"
                                    , "/contact", "/checkout", "/shopping", "/news", "/news-details").permitAll();
                })*/
               // .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .csrf(csrf -> csrf.csrfTokenRepository(new HttpSessionCsrfTokenRepository()))
                .authorizeRequests(authorize -> {
                    authorize.antMatchers("/*", "/assets/**", "/dist/**", "/images/**").permitAll();
                })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().and()
                .httpBasic();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //web.ignoring().antMatchers("/*", "/assets/**", "/dist/**", "/images/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("marek")
                .password("{noop}spring")
                .roles("USER");

    }




}
