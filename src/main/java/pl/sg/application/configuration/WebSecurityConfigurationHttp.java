package pl.sg.application.configuration;

import com.google.gson.Gson;
import jakarta.persistence.EntityManager;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.sg.application.security.annotations.*;
import pl.sg.application.service.AuthorizationService;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true)
@EnableScheduling
@Profile("http")
public class WebSecurityConfigurationHttp {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz.anyRequest().permitAll())
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }


    @Bean
    public WebMvcConfigurer corsConfigurer(AuthorizationService authorizationService, Gson gson,
                                           EntityManager entityManager, ModelMapper modelMapper,
                                           AddMDCRequestInterceptor addMDCRequestInterceptor,
                                           SavingRequestsInterceptor savingRequestsInterceptor) {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NotNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("OPTIONS", "HEAD", "GET", "PUT", "POST", "DELETE", "PATCH");
            }

            @Override
            public void addArgumentResolvers(@NotNull List<HandlerMethodArgumentResolver> argumentResolvers) {
                argumentResolvers.add(new RequestUserResolver(authorizationService));
                argumentResolvers.add(new RequestDomainResolver(entityManager));
                argumentResolvers.add(new PathVariableWithDomainResolver(authorizationService, entityManager));
                argumentResolvers.add(new RequestBodyWithDomainResolver(authorizationService, entityManager, modelMapper, gson));
                argumentResolvers.add(new RequestBodyIdWithDomainResolver(authorizationService, entityManager, modelMapper, gson));
                argumentResolvers.add(new MapRequestBodyResolver(modelMapper, gson));
            }

            @Override
            public void addInterceptors(@NotNull InterceptorRegistry registry) {
                registry.addWebRequestInterceptor(addMDCRequestInterceptor);
                registry.addWebRequestInterceptor(savingRequestsInterceptor);
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }
}
