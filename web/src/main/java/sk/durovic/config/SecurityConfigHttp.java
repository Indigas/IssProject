package sk.durovic.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import sk.durovic.security.JwtAuthenticationFilter;
import sk.durovic.security.JwtUsernamePasswordAuthFilter;

@Configuration
@Order(2)
public class SecurityConfigHttp extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
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
                            .antMatchers("/companies/**").permitAll();


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
}
