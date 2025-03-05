package com.scm.config;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


import com.scm.services.Implmentation.SecurityCustomerUserDetailService;


@Configuration
public class SecurityConfig {
    @Autowired
    private SecurityCustomerUserDetailService userDetailService;
    @Autowired
    private OAuthenticationSuccessHandler handler;
    @Bean
    public AuthenticationProvider authenticationProvider(){
 DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
 //object of userdetail service
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        //object of password encoder
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;

    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        //configuration
        httpSecurity.authorizeHttpRequests(authorize->{
            //authorize.requestMatchers("/home","/register","/services","").permitAll();
            authorize.requestMatchers("/user/**").authenticated();
            authorize.anyRequest().permitAll();

        });
        httpSecurity.formLogin(formLogin->{
            formLogin.loginPage("/login");
           formLogin.loginProcessingUrl("/authenticate");
          formLogin.successForwardUrl("/user/profile");
          //formLogin.failureForwardUrl("/login?error=true");
          formLogin.usernameParameter("email");
          formLogin.passwordParameter("password");
          
        //   formLogin.failureHandler(new AuthenticationFailureHandler() {

        //     @Override
        //     public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
        //             AuthenticationException exception) throws IOException, ServletException {
        //       
        //         throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationFailure'");
        //     }
            
        //   });


        });
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logoutForm->{
            logoutForm.logoutUrl("/do-logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");


        });
        // oauth configurations
        httpSecurity.oauth2Login(oauth->{
            oauth.loginPage("/login");
            oauth.successHandler(handler);
        });
        return httpSecurity.build();

    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
}