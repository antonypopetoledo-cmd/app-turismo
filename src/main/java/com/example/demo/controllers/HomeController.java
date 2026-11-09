package com.example.demo.controllers;

import com.example.demo.models.User;
import com.example.demo.services.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    private final UserService userService;
    
    public HomeController(UserService userService) {
        this.userService = userService;
    }
    
    private void addUserData(Authentication auth, Model model) {
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            
            String email = auth.getName();
            User user = this.userService.getByEmail(email);
            
            model.addAttribute("userName", user.getFirstname());
            model.addAttribute("logout", "Cerrar Sesión");
        } else {
            model.addAttribute("login", "Iniciar Sesión");
        }
    }
    
    @GetMapping("/")
    public String inicio(Authentication auth, Model model) {
        this.addUserData(auth, model);
        return "index";
    }
    
    @GetMapping("/destinos")
    public String destinos(Authentication auth, Model model) {
        this.addUserData(auth, model);        
        return "destinos";
    }
    
    @GetMapping("/landing-page-test")
    public String landingPageTest() {
        return "testingViews/landingPageTest";
    }
}
