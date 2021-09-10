package recipes.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.util.logging.Logger;

@EnableWebSecurity
@Configuration
public class WebSecurityConfigureImpl extends WebSecurityConfigurerAdapter {
    private static final Logger log = Logger.getLogger(WebSecurityConfigureImpl.class.getName());
    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";
    private final DataSource dataSource;
    private final UserDetailsService userDetailsService;

    @Autowired
    public WebSecurityConfigureImpl(UserDetailsService userDetailsService, DataSource dataSource) {
        this.userDetailsService = userDetailsService;
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("nam@gmail.com")
                .password(getPasswordEncoder().encode("123"))
                .roles(ADMIN);
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
        auth.authenticationProvider(authenticationProvider());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().httpBasic().and().csrf().disable();
        http.authorizeRequests().mvcMatchers("/actuator/shutdown").permitAll();
        http.authorizeRequests()
                .mvcMatchers("/api/register")
                .permitAll()
                .and()
                .formLogin()
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated();
        http.headers().frameOptions().disable();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService());
        auth.setPasswordEncoder(getPasswordEncoder());
        return auth;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        DelegatingPasswordEncoder delegatePassEncoder = (DelegatingPasswordEncoder) PasswordEncoderFactories.createDelegatingPasswordEncoder();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        delegatePassEncoder.setDefaultPasswordEncoderForMatches(bCryptPasswordEncoder);
        return delegatePassEncoder;
    }

}
