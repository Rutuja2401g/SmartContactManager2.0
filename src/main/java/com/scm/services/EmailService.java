package com.scm.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.web.multipart.MultipartFile;



@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public boolean sendEmailWithAttachment(String to, String subject, String body, MultipartFile file) {
        try {
            // Create a MIME message
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Set email properties
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);
            helper.setFrom("garjerutuja5@gmail.com"); // Must match SMTP username

            // Add attachment if file is provided
            if (file != null && !file.isEmpty()) {
                helper.addAttachment(file.getOriginalFilename(), file);
            }

            // Send email
            mailSender.send(message);
            System.out.println("Email sent successfully with attachment!");
            return true;
        } catch (MessagingException e) {  // ✅ Only catch MessagingException
            e.printStackTrace();
            System.out.println("Failed to send email with attachment.");
            return false;
        }
        
    }
}
