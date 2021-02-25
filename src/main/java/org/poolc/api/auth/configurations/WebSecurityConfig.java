package org.poolc.api.auth.configurations;

import lombok.RequiredArgsConstructor;
import org.poolc.api.auth.domain.JwtAuthenticationFilter;
import org.poolc.api.auth.infra.JwtTokenProvider;
import org.poolc.api.member.domain.MemberRole;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Argon2PasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/board").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.PUT, "/board/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers(HttpMethod.DELETE, "/board/*").hasAuthority(MemberRole.ADMIN.name())
                .antMatchers("/board/**").permitAll()
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
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class);
    }
}
