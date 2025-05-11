package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model) {
        System.out.println("this is home handler");
        model.addAttribute("Name", "Anurag Pandey");
        model.addAttribute("youtubeChannel", "Coding");
        model.addAttribute("LinkedIn", "https://www.linkedin.com/in/anurag-pandey6222/");
        return "home";
    }

    // about route

    @RequestMapping("/about")
    public String aboutPage(Model model) {
        System.out.println("About page loading");
        return "about";
    }

    // services

    @RequestMapping("/services")
    public String servicesPage() {
        System.out.println("services page loading");
        return "services";
    }

    @GetMapping("/contact")
    public String contactPage() {
        System.out.println("services page loading");
        return "contact";
    }

    // login view
    @GetMapping("/login")
    public String loginPage() {
        System.out.println("Login page loading");
        return "login";
    }

    // register view
    @GetMapping("/register")
    public String register(Model model) {
        UserForm userform = new UserForm();
        // userform.setName("Anurag");
        // userform.setEmail("anuragp6222@gmail.com");
        // yaha pe hum default value bhi daal skte hai
        model.addAttribute("userForm", userform);// userform ko key userForm se access karenge,register.html me

        return "register";
    }

    // processing form(Processing register)
    @RequestMapping(value = "/do-register", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userform, BindingResult rBindingResult,
            HttpSession session) {
        // 1.fetch form data
        // UserFrom (Data ko fetch krne ke liye, is class ke object ke andar puri data
        // store karenge)
        System.out.println(userform);

        // 2.validate form data

        if (rBindingResult.hasErrors()) {
            return "register";
        }

        // 3.Save it to Database
        // DB me save krne k liye userService ka class banaunga usme woh saare methos
        // hai joh user ki buisness logic excute karenge

        // userform se data fetch krke user object me daal denge
        // >>>builder nahi use karenge kyuki default value nahi aa rahi thi<<<
        // User user = User.builder()
        // .name(userform.getName())
        // .email(userform.getEmail())
        // .password(userform.getPassword())
        // .about(userform.getAbout())
        // .phoneNumber(userform.getPhoneNumber())
        // .profilePic("")
        // .build();

        User user = new User();
        user.setName(userform.getName());
        user.setEmail(userform.getEmail());
        user.setPassword(userform.getPassword());
        user.setAbout(userform.getAbout());
        user.setPhoneNumber(userform.getPhoneNumber());
        user.setProfilePic("");

        User saveUser = userService.saveUser(user);
        System.out.println("user saved" + saveUser);
        // 4.message = "Successfully Registered"

        // add the message

        Message message = Message.builder().content("Registration Successful").type(MessageType.green).build();
        session.setAttribute("message", message);
        // 5.redirect to login page
        return "redirect:/register";
    }
}
