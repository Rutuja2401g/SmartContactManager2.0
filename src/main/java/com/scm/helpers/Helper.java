package com.scm.helpers;



import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {

    public static String getEmailOfLoggedInUser(Authentication authentication) {

    if (authentication instanceof OAuth2AuthenticationToken) {

        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
        String clientId = oauth2Token.getAuthorizedClientRegistrationId();

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String username = null;

        if (clientId.equalsIgnoreCase("google")) {
            // Sign in with Google
            System.out.println("Getting email from Google");
            username = oauth2User.getAttribute("email"); // Null check is inherent here, but good to confirm
        } else if (clientId.equalsIgnoreCase("github")) {
            // Sign in with GitHub
            System.out.println("Getting email from GitHub");
            username = oauth2User.getAttribute("email") != null
                    ? oauth2User.getAttribute("email")
                    : oauth2User.getAttribute("login") + "@users.noreply.github.com"; // Avoid hardcoding domains
        }

        return username != null ? username : "No email found";
    } else {
        // Handle local database login (email/password)
        System.out.println("Getting data from local database");
        return authentication.getName();
    }
}


}
