package com.fastcart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.fastcart.service.impl.UserDetailsServiceImpl;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    private final UserDetailsServiceImpl userDetailsService;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    
    @SuppressWarnings("removal")
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector, AuthenticationManager authenticationManager) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        http
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.disable())) // Permitir iframes (para H2)
            .authorizeHttpRequests(authorize -> 
                authorize
                    .requestMatchers(toH2Console()).permitAll()
                    .requestMatchers(
                        mvcMatcherBuilder.pattern("/"),
                        mvcMatcherBuilder.pattern("/index.html"),
                        mvcMatcherBuilder.pattern("/h2-console/**"),
                        mvcMatcherBuilder.pattern("/css/**"),
                        mvcMatcherBuilder.pattern("/products"),
                        mvcMatcherBuilder.pattern("/user/register"),
                        mvcMatcherBuilder.pattern("/register"),
                        mvcMatcherBuilder.pattern("/error"),
                        mvcMatcherBuilder.pattern("/favicon.ico"),
                        mvcMatcherBuilder.pattern("/api/login"),
                        mvcMatcherBuilder.pattern("/uploads/**"),
                        mvcMatcherBuilder.pattern("/api/products"),
                        mvcMatcherBuilder.pattern("products/searchAndFilter")
                    ).permitAll()
                    .requestMatchers(mvcMatcherBuilder.pattern("/admin/**"), mvcMatcherBuilder.pattern("/api/admin/**")).hasRole("ADMIN")
                    .requestMatchers(mvcMatcherBuilder.pattern("/cart/**")).authenticated()
                    .anyRequest().authenticated()) // Requiere autenticación para todo lo demás
                    .formLogin(formLogin -> formLogin.loginPage("/login").defaultSuccessUrl("/index.html", true).permitAll())
                    .logout(logout -> logout.permitAll())
                    .exceptionHandling(exceptionHandling -> exceptionHandling
                            .defaultAuthenticationEntryPointFor(
                                new CustomAuthenticationEntryPoint(), 
                                mvcMatcherBuilder.pattern("/api/**") // Aplica a todas las rutas API
                            )
                        )
                    .addFilterBefore(customApiLoginFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                    .csrf(csrf -> csrf.disable()) // CSRF desactivado para simplificar desarrollo
                    ;
        	
	        http.sessionManagement()
	        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS); // Asegura que se cree la sesión si es necesario
        return http.build();
    }
    
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CustomApiLoginFilter customApiLoginFilter(AuthenticationManager authenticationManager) {
        return new CustomApiLoginFilter(authenticationManager);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}
