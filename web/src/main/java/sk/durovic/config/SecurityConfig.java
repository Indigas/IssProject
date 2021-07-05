package sk.durovic.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import sk.durovic.security.JwtAuthenticationFilter;
import sk.durovic.security.JwtAuthenticationProvider;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new JwtAuthenticationFilter(authenticationManagerBean()), UsernamePasswordAuthenticationFilter.class);

        http
                .csrf(csrf -> csrf.csrfTokenRepository(new HttpSessionCsrfTokenRepository())
                                .ignoringAntMatchers("/api/**")
                            )
                .authorizeRequests(authorize -> {
                    authorize.antMatchers("/account", "/account/**").authenticated()
                            .antMatchers("/api/reservation/create").permitAll()
                            .antMatchers("/*", "/assets/**", "/dist/**", "/images/**").permitAll()
                            .antMatchers("/register/new").permitAll()
                            .antMatchers("/list/**").permitAll()
                            .antMatchers("/api/list").permitAll()
                            .antMatchers("/api/list/**").permitAll()
                            .antMatchers("/companies/**").permitAll()
                            .antMatchers("/api/login").permitAll();

                })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin(loginConfigurer -> {
                    loginConfigurer.loginPage("/register")
                            .loginProcessingUrl("/login")
                            .defaultSuccessUrl("/", true)
                            .failureUrl("/register?login=failed");
                })
                .logout(logoutConfigurer -> {
                    logoutConfigurer.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                            .logoutSuccessUrl("/register?logout")
                            .permitAll();
                });

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(new JwtAuthenticationProvider(userDetailService))
                .userDetailsService(userDetailService);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
}
