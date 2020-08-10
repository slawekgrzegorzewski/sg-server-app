package pl.sg.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomWebAuthenticationDetailsSource authenticationDetailsSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests().antMatchers("/", "/index.html", "/login.html").permitAll().anyRequest().authenticated()
                .and().httpBasic().authenticationDetailsSource(authenticationDetailsSource)
                .and().logout().logoutUrl("/logout").deleteCookies("JSESSIONID").permitAll()
                .and().csrf().disable();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        for (ApplicationUser user : applicationUserRepository.findAll()) {
//            auth.apply(new InMemoryUserDetailsManagerConfigurer<>())
////            auth.inMemoryAuthentication()
//                    .withUser(user.getLogin())
//                    .password(user.getPassword())
//                    .roles(user.getRoles().toArray(new String[0]));
//        }
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public UserDetailsService userDetailsService(ApplicationUserRepository applicationUserRepository) {
        return username -> {
            ApplicationUser applicationUser = applicationUserRepository.findFirstByLogin(username).get();
            return new User(
                    applicationUser.getLogin(),
                    applicationUser.getPassword(),
                    applicationUser.getRoles().stream().map(role -> (GrantedAuthority) () -> role).collect(Collectors.toList())
            );
        };
    }

    @Bean
    public DaoAuthenticationProvider authProvider(PasswordEncoder encoder, UserDetailsService userDetailsService) {
        final CustomAuthenticationProvider authProvider = new CustomAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return authProvider;
    }
}
