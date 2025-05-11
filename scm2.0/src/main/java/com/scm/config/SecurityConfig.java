package com.scm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.scm.services.impl.SecurityCustomUserDetailService;

@Configuration
public class SecurityConfig {

    // user create and login using java code with in memory service

    // @Bean
    // public UserDetailsService userDetailsService() {

    // UserDetails user1 = User
    // .withDefaultPasswordEncoder()
    // .username("admin123")
    // .password("admin123")
    // .roles("ADMIN", "USER")
    // .build();

    // UserDetails user2 =User
    // .withDefaultPasswordEncoder()
    // .username("user123")
    // .password("user123")
    // .build();

    // var inMemoryUserDetailsManager = new InMemoryUserDetailsManager(user1 ,
    // user2);
    // return inMemoryUserDetailsManager;
    // }

    @Autowired
    private OAuthAuthenticationSuccessHandler handler;
    @Autowired
    private SecurityCustomUserDetailService securityCustomUserDetailService;

    // Configuration Of Authentication Provider

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // User Detail Service ka object
        daoAuthenticationProvider.setUserDetailsService(securityCustomUserDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // Confuguration

        // url ko configure kiya hai kaun se public rahenge aur kaun se private
        httpSecurity
                .authorizeHttpRequests(authorize -> {
                    // authorize.requestMatchers("/home","/services","/register").permitAll();
                    authorize.requestMatchers("/user/**").authenticated();
                    authorize.anyRequest().permitAll();

                });

        // form default login (kyuki maine user ke saare route ko authenticate kr diya
        // ab login form nahi load hoga by default)
        // agar hume kuch change krna hua form login se related toh hum yaha aayenge

        httpSecurity.formLogin(formLogin -> {

            // custom login page
            formLogin.loginPage("/login");
            formLogin.loginProcessingUrl("/authenticate");
            formLogin.successForwardUrl("/user/profile");
            // formLogin.failureForwardUrl("/login?error=true");
            formLogin.usernameParameter("email"); // to pass the name in login.html otherwise by default we had to user
                                                  // username
            formLogin.passwordParameter("password");
            // formLogin.failureHandler(new AuthenticationFailureHandler() {

            /*
             * 
             * @Override
             * public void onAuthenticationFailure(HttpServletRequest request,
             * HttpServletResponse response,
             * AuthenticationException exception) throws IOException, ServletException {
             * // TODO Auto-generated method stub
             * throw new
             * UnsupportedOperationException("Unimplemented method 'onAuthenticationFailure'"
             * );
             * }
             * 
             * });
             * 
             * formLogin.successHandler(new AuthenticationSuccessHandler() {
             * 
             * @Override
             * public void onAuthenticationSuccess(HttpServletRequest request,
             * HttpServletResponse response,
             * Authentication authentication) throws IOException, ServletException {
             * // TODO Auto-generated method stub
             * throw new
             * UnsupportedOperationException("Unimplemented method 'onAuthenticationSuccess'"
             * );
             * }
             * 
             * });
             * 
             */

        });

        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.logout(logoutForm -> {
            logoutForm.logoutUrl("/logout");
            logoutForm.logoutSuccessUrl("/login?logout=true");
        });

        // oAuth2 Configuration

        httpSecurity.oauth2Login(oauth -> {
            oauth.loginPage("/login");
            oauth.successHandler(handler);
        });

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
