package com.scm.controllers;

import com.scm.entities.*;
import com.scm.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @RequestMapping("/feedback")
    public String showFeedbackForm() {
        return "feedback"; // Loads feedback.html
    }

    @PostMapping("/submit-feedback")
    public String submitFeedback(
        @RequestParam String name,
        @RequestParam String email,
        @RequestParam String message,
        @RequestParam int rating,
        Model model) {
        
        // Save feedback to MySQL
        Feedback feedback = new Feedback(name, email, message, rating);
        feedbackRepository.save(feedback);

        // Add success message
        model.addAttribute("success", true);
        return "feedback";
    }
}
