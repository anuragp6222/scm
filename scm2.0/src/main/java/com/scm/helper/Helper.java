package com.scm.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {


    public static String getEmailOfLoggedInUser(Authentication authentication){

         if (authentication instanceof OAuth2AuthenticationToken) {

            var roAuth2AuthenticationToken =(OAuth2AuthenticationToken)authentication;
            var clientId=roAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            var oauth2User = (OAuth2User)authentication.getPrincipal();
            String username="";
            if(clientId.equalsIgnoreCase("google")){
                //Sign-in With Google
                System.out.println("Signed in with google");
               username=oauth2User.getAttribute("email").toString();
            }
            else if(clientId.equalsIgnoreCase("github")){

                //sign-in with github
                System.out.println("signed in with github");
                username =oauth2User.getAttribute("email") != null ? oauth2User.getAttribute("email").toString()
                : oauth2User.getAttribute("login").toString() + "gmail.com";
            }
            return username;

            
         }
         else{
            System.out.println("getting data from the local databse ");
            return authentication.getName();
        }


    }
}
