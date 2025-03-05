package com.scm.controllers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entities.User;
import com.scm.entities.contact;
import com.scm.forms.ContactForm;
import com.scm.forms.ContactSearchForm;
import com.scm.helpers.AppConstants;
import com.scm.helpers.Helper;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.ContactService;
import com.scm.services.ImageService;
import com.scm.services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("user/contacts")
public class ContactController {

    private Logger logger = org.slf4j.LoggerFactory.getLogger(ContactController.class);

    @Autowired
    private ContactService contactService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private UserService userService;

    // Add Contact View
    @RequestMapping("/add")
    public String addContactView(Model model) {
        ContactForm contactForm = new ContactForm();
        contactForm.setFavorite(false);
        model.addAttribute("contactForm", contactForm);
        return "user/add_contact";
    }

    // Save Contact
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveContact(@Valid @ModelAttribute ContactForm contactForm, BindingResult result,
                              Authentication authentication, HttpSession session) {

        if (result.hasErrors()) {
            session.setAttribute("message", Message.builder()
                    .content("Please correct the following errors")
                    .type(MessageType.red)
                    .build());
            return "user/add_contact";
        }

        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

        // Process the contact
        String fileURL = imageService.uploadImage(contactForm.getContactImage());
        contact contact = new contact();
        contact.setName(contactForm.getName());
        contact.setFavorite(contactForm.isFavorite());
        contact.setEmail(contactForm.getEmail());
        contact.setPhoneNumber(contactForm.getPhoneNumber());
        contact.setAddress(contactForm.getAddress());
        contact.setDescription(contactForm.getDescription());
        contact.setUser(user);
        contact.setLinkedInLink(contactForm.getLinkedlnLink());
        contact.setWebsiteLink(contactForm.getWebsiteLink());
        contact.setPicture(fileURL);

        contactService.save(contact);

        session.setAttribute("message", Message.builder()
                .content("Contact Successfully added")
                .type(MessageType.green)
                .build());

        return "redirect:/user/contacts/add";
    }

    // View Contacts (Showing all contacts with pagination)
    @RequestMapping
    public String viewContacts(@RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue ="5") int size,
                               @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
                               @RequestParam(value = "direction", defaultValue = "asc") String direction,
                               Model model, Authentication authentication) {

        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

        // Fetch contacts with pagination
        Page<contact> pageContact = contactService.getByUser(user, page, size, sortBy, direction);
        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE); // Constant page size if needed
        model.addAttribute("contactSearchForm", new ContactSearchForm()); // Empty search form for initial load
        return "user/contacts";
    }

    // Search Handler (With pagination and filtering)
    @RequestMapping("/search")
    public String searchHandler(
            @ModelAttribute ContactSearchForm contactSearchForm,
            @RequestParam(value = "size", defaultValue = AppConstants.PAGE_SIZE + "") int size,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            Model model,
            Authentication authentication) {

        logger.info("field {} keyword {}", contactSearchForm.getField(), contactSearchForm.getValue());

        var user = userService.getUserByEmail(Helper.getEmailOfLoggedInUser(authentication));

        Page<contact> pageContact = null;

        // Check if there's a search value and field
        if (contactSearchForm.getField() != null && !contactSearchForm.getField().isEmpty()) {
            if (contactSearchForm.getField().equalsIgnoreCase("name")) {
                pageContact = contactService.searchByName(contactSearchForm.getValue(), size, page, sortBy, direction, user);
            } else if (contactSearchForm.getField().equalsIgnoreCase("email")) {
                pageContact = contactService.searchByEmail(contactSearchForm.getValue(), size, page, sortBy, direction, user);
            } else if (contactSearchForm.getField().equalsIgnoreCase("phone")) {
                pageContact = contactService.searchByPhoneNumber(contactSearchForm.getValue(), size, page, sortBy, direction, user);
            }
        }

        // If no search form is submitted, show all contacts
        if (pageContact == null) {
            pageContact = contactService.getByUser(user, page, size, sortBy, direction);
        }

        model.addAttribute("contactSearchForm", contactSearchForm);
        model.addAttribute("pageContact", pageContact);
        model.addAttribute("pageSize", AppConstants.PAGE_SIZE);

        return "user/contacts"; // Use the contacts view instead of search view
    }
    //delete
    // detete contact
    // @RequestMapping("/delete/{contactId}")
    // public String deleteContact(@PathVariable("contactId") String contactId, HttpSession session) {
    //     try {
    //         contactService.delete(contactId); // Calls the delete method
    //         logger.info("contactId {} deleted", contactId);
    
    //         session.setAttribute("message",
    //                 Message.builder()
    //                         .content("Contact is Deleted successfully !! ")
    //                         .type(MessageType.green)
    //                         .build()
    //         );
    //     } catch (Exception e) {
    //         logger.error("Error deleting contact with id {}: {}", contactId, e.getMessage());
    //         session.setAttribute("message", Message.builder()
    //                 .content("Failed to delete the contact!")
    //                 .type(MessageType.red)
    //                 .build());
    //     }
    
    //     return "redirect:/user/contacts";
    // }
    
    @RequestMapping("/delete/{contactId}")
    public String deleteContact(@PathVariable("contactId") String contactId, HttpSession session) {
        try {
            contactService.delete(contactId); // Delete contact
            logger.info("contactId {} deleted", contactId);
    
            session.setAttribute("message",
                Message.builder()
                    .content("Contact is Deleted successfully !! ")
                    .type(MessageType.green)
                    .build()
            );
        } catch (Exception e) {
            logger.error("Error deleting contact with id {}: {}", contactId, e.getMessage());
            session.setAttribute("message", Message.builder()
                .content("Failed to delete the contact!")
                .type(MessageType.red)
                .build());
        }
    
        return "redirect:/user/contacts"; // Redirect after deletion
    }
    
    // update contact from view
    
    // update contact form view
    @GetMapping("/view/{contactId}")
public String updateContactFormView(
        @PathVariable("contactId") String contactId,
        Model model) {

    var contact = contactService.getById(contactId);
    ContactForm contactForm = new ContactForm();
    contactForm.setName(contact.getName());
    contactForm.setEmail(contact.getEmail());
    contactForm.setPhoneNumber(contact.getPhoneNumber());
    contactForm.setAddress(contact.getAddress());
    contactForm.setDescription(contact.getDescription());
    contactForm.setFavorite(contact.isFavorite());
    contactForm.setWebsiteLink(contact.getWebsiteLink());
    contactForm.setLinkedlnLink(contact.getLinkedInLink());
    contactForm.setPicture(contact.getPicture());

    model.addAttribute("contactForm", contactForm);
    model.addAttribute("contactId", contactId);

    return "user/update_contact_view";
}

@RequestMapping(value = "/update/{contactId}", method = RequestMethod.POST)
public String updateContact(@PathVariable("contactId") String contactId, 
                            @Valid @ModelAttribute ContactForm contactForm, 
                            BindingResult bindingResult, 
                            Model model) {

    // Get the existing contact to retain the current image if no new image is uploaded
    var existingContact = contactService.getById(contactId);
    
    // Default to the existing image if no new image is provided
    String fileURL = existingContact.getPicture();
    
    // Check if a new image is uploaded
    if (contactForm.getContactImage() != null && !contactForm.getContactImage().isEmpty()) {
        fileURL = imageService.uploadImage(contactForm.getContactImage());  // Upload new image
    }

    // Create the updated contact object
    var con = new contact();
    con.setId(contactId);
    con.setName(contactForm.getName());
    con.setEmail(contactForm.getEmail());
    con.setPhoneNumber(contactForm.getPhoneNumber());
    con.setAddress(contactForm.getAddress());
    con.setDescription(contactForm.getDescription());
    con.setFavorite(contactForm.isFavorite());
    con.setWebsiteLink(contactForm.getWebsiteLink());
    con.setLinkedInLink(contactForm.getLinkedlnLink());
    con.setPicture(fileURL);  // Use the correct image URL (either new or existing)

    // Update the contact in the service
    var updatedContact = contactService.update(con);
    logger.info("Updated contact: {}", updatedContact);

    // Add success message to the model
    model.addAttribute("message", Message.builder()
            .content("Contact updated successfully.")
            .type(MessageType.green)
            .build());

    // Clear the contact form data (if needed, but it's not essential here)
    contactForm = new ContactForm();

    // Redirect to the contacts list page after successful update
    return "redirect:/user/contacts";

}
}