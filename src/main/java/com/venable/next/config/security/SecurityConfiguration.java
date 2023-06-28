package com.venable.next.config.security;

import com.venable.next.config.ApplicationProperties;
import com.venable.next.web.filter.SpaWebFilter;
import com.venable.next.security.AuthoritiesConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    private ApplicationProperties applicationProperties;

    public SecurityConfiguration(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**").disable())
                .addFilterAfter(new SpaWebFilter(), BasicAuthenticationFilter.class)
                .headers(headers ->
                        headers.contentSecurityPolicy(csp -> csp.policyDirectives(applicationProperties.getSecurity().getContentSecurityPolicy()))
                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                                .referrerPolicy(referrer -> referrer.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN))
                                .permissionsPolicy(permissions -> permissions.policy(applicationProperties.getSecurity().getPermissionsPolicy()))
                )
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/", "/index.html", "/*.js", "/*.map", "/*.css").permitAll()
                                .requestMatchers("/*.ico", "/*.png", "/*.svg", "/*.webapp").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/swagger-resources/**").permitAll()
                                .requestMatchers("/webjars/**").permitAll()
                                .requestMatchers("/static/**").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/authenticate").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/authenticate").permitAll()
                                .requestMatchers("/api/register").permitAll()
                                .requestMatchers("/api/activate").permitAll()
                                .requestMatchers("/api/account/reset-password/init").permitAll()
                                .requestMatchers("/api/account/reset-password/finish").permitAll()
                                .requestMatchers("/api/admin/**").hasAuthority(AuthoritiesConstants.ADMIN)
                                .requestMatchers("/api/**").authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exceptions ->
                        exceptions
                                .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                                .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(null)));

        return http.build();
    }
}
