package com.ec.recauctionec.configs;

import com.ec.recauctionec.services.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService())
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //authentication
        http.authorizeRequests()
                .antMatchers("/",
                        "/dang-ky",
                        "/dang-nhap",
                        "/trang-chu",
                        "/doanh-muc",
                        "/tat-ca-phien",
                        "/san-pham").permitAll()
                .antMatchers("/tai-khoan/**",
                        "/dau-gia/**",
                        "/don-hang/**",
                        "/thanh-toan/**").authenticated()
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
                .antMatchers("/admin/**")
                .access("hasRole('ADMIN')");
        //supplier
        http.authorizeRequests()
                .antMatchers("/supplier/**")
                .access("hasRole('SUPPLIER')");
        //handle when user not have permission
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");

    }
}
