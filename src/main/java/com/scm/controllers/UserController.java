package com.scm.controllers;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scm.entities.User;
import com.scm.services.ContactService;
import com.scm.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    
    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @RequestMapping(value = "/dashboard")
    public String dashboard(Model model, Principal principal) {
        // Retrieve the logged in user's email
        String email = principal.getName();
        logger.info("Authenticated email: " + email);
        User user = userService.getUserByEmail(email);
        
        // Check if the user is null to avoid NullPointerException
        if (user == null) {
            logger.error("User not found for email: " + email);
            // Redirect to login page or an error page as appropriate
            return "redirect:/login?error=UserNotFound";
        }
        
        // Get the user's id (stored in userId)
        String userId = user.getUserId();
        
        // Retrieve the contact counts from the service layer
        int totalContacts = contactService.countTotalContacts(userId);
        int favoriteContacts = contactService.countFavoriteContacts(userId);
        
        // Add attributes to the model for the view
        model.addAttribute("loggedInUser", user);
        model.addAttribute("totalContacts", totalContacts);
        model.addAttribute("totalFavContacts", favoriteContacts);
        
        // Return the dashboard view
        return "user/dashboard";
    }

    // user profile page
    @RequestMapping(value = "/profile")
    public String userProfile(Model model, Authentication authentication) {
        return "user/profile"; // Returns the profile view
    }
}
