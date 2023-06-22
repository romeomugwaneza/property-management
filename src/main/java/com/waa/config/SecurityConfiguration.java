package com.waa.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf()
                .disable()
                .authorizeHttpRequests(auth ->{
                    auth.requestMatchers(AntPathRequestMatcher.antMatcher("/api/v1/auth/test/**")).permitAll();
                    auth.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.POST,"/api/v1/properties")).hasAuthority("OWNER");
                    auth.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.DELETE,"/api/v1/properties/{id}")).hasAuthority("OWNER");
                    auth.requestMatchers(AntPathRequestMatcher.antMatcher(HttpMethod.PUT,"/api/v1/properties/{id}")).hasAuthority("OWNER");
                    auth.requestMatchers("/api/v1/auth/**").permitAll();
                    auth.anyRequest().authenticated();
                })
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/v1/auth/logout");

        return httpSecurity.build();
    }
}