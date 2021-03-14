package org.poolc.api.auth.configurations;

import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.domain.JwtAuthenticationFilter;
import org.poolc.api.auth.infra.JwtTokenProvider;
import org.poolc.api.member.domain.MemberRole;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("https://haribo.poolc.org",
                "https://poolc.org", "https://beta.poolc.org", "http://localhost:3000"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control",
                "Content-Type", "Accept", "Content-Length", "Accept-Encoding", "X-Requested-With"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(HttpMethod.POST, "/project").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/project/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/project/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers("/project/**").permitAll()

                .antMatchers(HttpMethod.POST, "/board").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/board/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/board/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers("/board/**").permitAll()

                .antMatchers(HttpMethod.GET, "/comment").permitAll()
                .antMatchers(HttpMethod.GET, "/comment/post/*").permitAll()
                .antMatchers(HttpMethod.GET, "/comment/*").permitAll()
                .antMatchers("/comment/**").hasAuthority(MemberRole.MEMBER.name())

                .antMatchers(HttpMethod.GET, "/post").permitAll()
                .antMatchers(HttpMethod.GET, "/post/board/*").permitAll()
                .antMatchers(HttpMethod.GET, "/post/*").permitAll()
                .antMatchers("/post/**").hasAuthority(MemberRole.MEMBER.name())

                .antMatchers(HttpMethod.GET, "/book").permitAll()
                .antMatchers(HttpMethod.GET, "/book/*").permitAll()
                .antMatchers(HttpMethod.POST, "/book").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/book/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/book/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/book/borrow/*").hasAuthority(MemberRole.MEMBER.name())
                .antMatchers(HttpMethod.PUT, "/book/return/*").hasAuthority(MemberRole.MEMBER.name())
                .antMatchers("/book/**").hasAuthority(MemberRole.MEMBER.name())

                .antMatchers(HttpMethod.POST, "/member").permitAll()
                .antMatchers(HttpMethod.GET, "/member").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/member/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers("/member/activate/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers("/member/admin/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers("/member/**").hasAuthority(MemberRole.MEMBER.name())

                .antMatchers(HttpMethod.POST, "/poolc").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/poolc").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers("/poolc/**").permitAll()

                .antMatchers(HttpMethod.GET, "/activity").permitAll()
                .antMatchers(HttpMethod.GET, "/activity/*").permitAll()
                .antMatchers(HttpMethod.GET, "/activity/session/activity/*").permitAll()
                .antMatchers(HttpMethod.GET, "/activity/session/*").permitAll()
                .antMatchers(HttpMethod.PUT, "/activity/open/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/activity/close/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.GET, "/activity/check/*").permitAll()
                .antMatchers(HttpMethod.GET, "/activity/member/*").hasAuthority(MemberRole.MEMBER.name())
                .antMatchers(HttpMethod.POST, "/activity").hasAuthority(MemberRole.MEMBER.name())
                .antMatchers(HttpMethod.POST, "/activity/session").hasAuthority(MemberRole.MEMBER.name())
                .antMatchers(HttpMethod.POST, "/activity/apply/*").hasAuthority(MemberRole.MEMBER.name())
                .antMatchers(HttpMethod.POST, "/activity/check/*").hasAuthority(MemberRole.MEMBER.name())
                .antMatchers(HttpMethod.PUT, "/activity/*").hasAuthority(MemberRole.MEMBER.name())
                .antMatchers(HttpMethod.PUT, "/activity/session/*").hasAuthority(MemberRole.MEMBER.name())
                .antMatchers(HttpMethod.DELETE, "/activity/*").hasAuthority(MemberRole.MEMBER.name())
                .antMatchers("/file").hasAuthority(MemberRole.MEMBER.name())
                .antMatchers("/**").permitAll()


                .anyRequest().authenticated().and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class);
    }
}
