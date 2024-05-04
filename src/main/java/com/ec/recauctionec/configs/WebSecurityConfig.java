package com.ec.recauctionec.configs;

import com.ec.recauctionec.data.repositories.UserRepo;
import com.ec.recauctionec.services.impl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
    @Bean
    public UserDetailsService userDetailsService(UserRepo userRepo) {
        return new UserDetailsServiceImpl(userRepo);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,
                                                     UserDetailsService userDetailService) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers("/resources/**");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/",
                        "/dang-ky",
                        "/dang-nhap",
                        "/trang-chu",
                        "/danh-muc",
                        "/tat-ca-phien",
                        "/san-pham").permitAll()
                .antMatchers("/tai-khoan/**",
                        "/dau-gia/**",
                        "/don-hang/**",
                        "/thanh-toan/**",
                        "/api/v1/supplier/**",
                        "/api/v1/admin/**").authenticated()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/dang-xuat"))
                .logoutSuccessUrl("/trang-chu")
                .permitAll();
        http.formLogin().loginPage("/dang-nhap")
                .usernameParameter("email")
                .passwordParameter("password");
        http.formLogin()
                .defaultSuccessUrl("/")
                .failureUrl("/dang-nhap?error=true");
        http.csrf().disable();

        //authorize
        //admin
        http.authorizeRequests()
                .antMatchers("/admin/**",
                        "/api/v1/admin/**")
                .access("hasRole('ADMIN')");
        //supplier
        http.authorizeRequests()
                .antMatchers("/supplier/**",
                        "/api/v1/supplier/**")
                .access("hasRole('SUPPLIER')");
        //handle when user not have permission
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

        return http.build();
    }

    /*protected void configure(HttpSecurity http) throws Exception {
        //authentication
        http.authorizeRequests()
                .antMatchers("/",
                        "/dang-ky",
                        "/dang-nhap",
                        "/trang-chu",
                        "/danh-muc",
                        "/tat-ca-phien",
                        "/san-pham").permitAll()
                .antMatchers("/tai-khoan/**",
                        "/dau-gia/**",
                        "/don-hang/**",
                        "/thanh-toan/**",
                        "/api/v1/supplier/**",
                        "/api/v1/admin/**").authenticated()
                .and()
                .logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/dang-xuat"))
                .logoutSuccessUrl("/trang-chu")
                .permitAll();
        http.formLogin().loginPage("/dang-nhap")
                .usernameParameter("email")
                .passwordParameter("password");
        http.formLogin()
                .defaultSuccessUrl("/")
                .failureUrl("/dang-nhap?error=true");
        http.csrf().disable();


        //authorize
        //admin
        http.authorizeRequests()
                .antMatchers("/admin/**",
                        "/api/v1/admin/**")
                .access("hasRole('ADMIN')");
        //supplier
        http.authorizeRequests()
                .antMatchers("/supplier/**",
                        "/api/v1/supplier/**")
                .access("hasRole('SUPPLIER')");
        //handle when user not have permission
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

    }*/

}
