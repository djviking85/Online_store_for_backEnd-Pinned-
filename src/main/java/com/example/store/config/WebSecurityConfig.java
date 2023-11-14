package com.example.store.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true)

public class WebSecurityConfig {
    private final UserContextService service;
    /**
     * List of URLs available to all (unauthorized) users
     */
    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v3/api-docs",
            "/webjars/**",
            "/login",
            "/register",
            "/images/**",
            "users/me/**"
    };

    public WebSecurityConfig(UserContextService service) {
        this.service = service;
    }

    /**
     * A method for adding users from a database in the context of a spring
     * @param passwordEncoder {@link PasswordEncoder}
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
        List<UserDetails> userDetailsList = service.doAllUsersToContext();
        return new InMemoryUserDetailsManager(userDetailsList);
    }

    /**
     * Bean for configuring user security and accessibility
     * @param http {@link HttpSecurity}
     */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests(
                        authorization ->
                                authorization
                                        .mvcMatchers(AUTH_WHITELIST)
                                        .permitAll()
                                        .mvcMatchers(HttpMethod.GET,"/ads")
                                        .permitAll()
                                        .mvcMatchers("/ads/**", "/users/**")
                                        .authenticated())
                .cors()
                .and()
                .httpBasic(withDefaults());
        return http.build();
    }

    /**
     * Bean for storing passwords in encoded form
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
