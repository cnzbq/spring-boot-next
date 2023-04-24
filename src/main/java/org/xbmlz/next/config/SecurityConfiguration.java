package org.xbmlz.next.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CorsFilter;
import org.xbmlz.next.common.config.ApplicationProperties;
import org.xbmlz.next.security.jwt.JWTConfigurer;
import org.xbmlz.next.security.jwt.TokenProvider;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    private final ApplicationProperties applicationProperties;

    private final TokenProvider tokenProvider;

    private final CorsFilter corsFilter;

    public SecurityConfiguration(
            TokenProvider tokenProvider,
            CorsFilter corsFilter,
            ApplicationProperties applicationProperties
    ) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.applicationProperties = applicationProperties;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // we don't need CSRF because our token is invulnerable
                .csrf()
                .disable()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                // content security policy (see https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Content-Security-Policy)
                .and()
                .headers()
                .contentSecurityPolicy(applicationProperties.getSecurity().getContentSecurityPolicy())
                // referrer policy (see https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Referrer-Policy)
                .and()
                .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                // feature policy (see https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Feature-Policy)
                .and()
                .permissionsPolicy().policy(applicationProperties.getSecurity().getPermissionsPolicy())
                // same origin (see https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/X-Frame-Options)
                .and()
                .frameOptions().sameOrigin()
                // X-XSS-Protection (see https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/X-XSS-Protection)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // X-Content-Type-Options (see https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/X-Content-Type-Options)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(new AntPathRequestMatcher("/h2-console/**")).permitAll()
                .requestMatchers("/api/login").permitAll()
                .requestMatchers("/api/**").authenticated()
                // allow anonymous resource requests
                .and()
                .httpBasic()
                .and()
                .apply(securityConfigurerAdapter());
        return http.build();
    }

    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }
}
