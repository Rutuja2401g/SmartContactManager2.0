package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.scm.services.EmailService;

@Controller
public class EmailController {

    @Autowired
    private EmailService emailService;

    // Serve the email form page
    @GetMapping("/email-form")
    public String showEmailForm() {
        return "email_form"; // Loads email_form.html
    }

    // Handle email submission with attachment
    @PostMapping("/send-email")
    public String sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body,
            @RequestParam("file") MultipartFile file,
            Model model) {

        boolean isSent = emailService.sendEmailWithAttachment(to, subject, body, file);
        if (isSent) {
            model.addAttribute("message", "Email sent successfully with attachment!");
        } else {
            model.addAttribute("message", "Failed to send email.");
        }
        return "email_form"; // Reloads the email page with a success/error message
    }
}
