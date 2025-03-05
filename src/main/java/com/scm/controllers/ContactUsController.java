package com.scm.controllers;

import com.scm.entities.ContactMessage;
import com.scm.repositories.ContactMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ContactUsController {

    @Autowired
    private ContactMessageRepository contactMessageRepository;

    @RequestMapping("/contact")
    public String showContactForm() {
        return "contact"; // Loads contact.html
    }

    @PostMapping("/submit-contact")
    public String submitContactForm(
        @RequestParam String name,
        @RequestParam String email,
        @RequestParam String message,
        Model model) {

        // Save contact message to database
        ContactMessage contactMessage = new ContactMessage(name, email, message);
        contactMessageRepository.save(contactMessage);

        // Add success message
        model.addAttribute("success", true);
        return "contact"; // Reloads contact page with success message
    }
}
