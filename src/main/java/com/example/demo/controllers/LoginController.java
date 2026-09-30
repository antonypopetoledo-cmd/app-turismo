package com.example.demo.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final String TEST_USER_EMAIL = "jhon@gmail.com";
    private final String TEST_USER_PASSWORD = "12345";
    private final String TEST_USER_NAME = "Jhon Traveler";

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @PostMapping("/submit")
    public String successfulLogin(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {
        if (this.TEST_USER_EMAIL.equals(email) && this.TEST_USER_PASSWORD.equals(password)) {
            session.setAttribute("userName", TEST_USER_NAME);
            return "redirect:/destinos";
        } else {
            model.addAttribute("error", "Correo o contraseña incorrectos");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
