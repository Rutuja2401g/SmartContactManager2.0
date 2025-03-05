package com.scm.controllers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entities.User;
import com.scm.helpers.Helper;
import com.scm.services.UserService;

@ControllerAdvice
public class RootController {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addLoggedInUserInformation(Model model, Authentication authentication) {
        if (authentication == null) {
            return;
        }
    
        System.out.println("Adding logged in user information to the model");
    
        String username = Helper.getEmailOfLoggedInUser(authentication);
        logger.info("User logged in: {}", username);
    
        // Try to fetch user from database
        User user = userService.getUserByEmail(username);
    
        // Check alternative email if not found
        if (user == null && username.endsWith("@users.noreply.github.com")) {
            String alternativeEmail = username.replace("@users.noreply.github.com", "@gmail.com");
            user = userService.getUserByEmail(alternativeEmail);
        }
    
        // Handle case where user is still null
        if (user == null) {
            logger.error("User not found for email: {}", username);
            return; // Don't proceed if user is not found
        }
    
        logger.info("User found: {} ({})", user.getName(), user.getEmail());
    
        // Add user to the model
        model.addAttribute("loggedInUser", user);
    }
    
}
