package com.scm.config;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.entities.Providers;
import com.scm.entities.User;
import com.scm.helper.AppConstants;
import com.scm.repositories.UserRepo;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    Logger logger = org.slf4j.LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    @Autowired
    private UserRepo userRepo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        logger.info("OAuthAuthenticationSuccessHandler");
            
        //Identify login provider
        var oAuth2AuthenticationToken=(OAuth2AuthenticationToken)authentication;
        String authorizedClientRegistrationId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        logger.info(authorizedClientRegistrationId);

        var oAuthUser = (DefaultOAuth2User) authentication.getPrincipal();
        
        oAuthUser.getAttributes().forEach((key, value) -> {
        logger.info(key + ":" + value);
        
        });

        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setRoleList(List.of(AppConstants.ROLE_USER));
        user.setEmailVerified(true);
        user.setEnabled(true);

        if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {
            //Google
            user.setEmail(oAuthUser.getAttribute("email").toString());
            user.setProfilePic(oAuthUser.getAttribute("picture").toString());
            user.setName(oAuthUser.getAttribute("name").toString());
            user.setProviderUserId(oAuthUser.getName());
            user.setProvider(Providers.GOOGLE);
            user.setAbout("This account is created using google");


        }else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {
            //GitHub
            String email = oAuthUser.getAttribute("email") != null ? oAuthUser.getAttribute("email").toString()
                    : oAuthUser.getAttribute("login").toString() + "gmail.com";
            String picture = oAuthUser.getAttribute("avatar_url").toString();
            String name = oAuthUser.getAttribute("login").toString();
            String providerUserId = oAuthUser.getName();

            user.setEmail(email);
            user.setProfilePic(picture);
            user.setName(name);
            user.setProviderUserId(providerUserId);
            user.setProvider(Providers.GITHUB);
            user.setAbout("This Account is created using GitHUb");
            
        } else {

            logger.info("OAuthAuthenticationSuccessHandler: Unknown Provider");
        }



        //Facebook

        // logger.info(user.getName());
        //         user.getAttributes().forEach((key , value) ->{
        //             logger.info("{} => {}",key , value);
        //         });
        // logger.info(user.getAuthorities().toString());




        //create user and save to database

            User user2 = userRepo.findByEmail(user.getEmail()).orElse(null);
            if (user2 == null) {
            
                userRepo.save(user);
                logger.info("user saved" );
            }

            new DefaultRedirectStrategy().sendRedirect(request, response, "/user/profile");
 
            }

}
