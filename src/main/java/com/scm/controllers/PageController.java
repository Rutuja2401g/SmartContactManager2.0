package com.scm.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class PageController {

    @Autowired
    private UserService userService;  // Inject UserService
    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }
    

    @RequestMapping("/home")
    public String home(Model model) {
        System.out.println("Home");
        model.addAttribute("name", "substring ");
        return "home";
    }

    // about
    @RequestMapping("/about")
    public String aboutpage() {
        return "about";
    }

    @RequestMapping("/services")
    public String servicepage() {
        return "services";
    }

    // contact
    @GetMapping("/contact")
    public String contactpage() {
        return "contact";
    }
    //this is login page
    // Login
    @GetMapping("/login")
    public String loginpage() {
        return "login";
    }
    // do process registratiion
    // register
    @GetMapping("/register")
    public String registerpage(Model model) {
        UserForm userForm = new UserForm();
        model.addAttribute("user", userForm);
        return "register";
    }

    // processing register
    @RequestMapping(value = "/do-register", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute("user") UserForm userForm, 
    BindingResult bindingResult, 
    Model model, 
    HttpSession session) {
if (bindingResult.hasErrors()) {
return "register"; // Return to the registration page to show errors
}
        // Save the user in the database using the injected UserService
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());

        user.setProfilePic("");
        User savedUser = userService.saveUser(user);
        System.out.println("User Saved: " + savedUser);
// add the pop up message
Message message=Message.builder().content("registration Sucessful").type(MessageType.green).build();
session.setAttribute("message",message);
        // Redirect to register page after successful registration
        return "redirect:/register";
    }
}
