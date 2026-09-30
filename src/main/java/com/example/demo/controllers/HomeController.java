package com.example.demo.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String inicio(HttpSession session, Model model) {
        String userName = (String) session.getAttribute("userName");

        if (userName == null) {
            model.addAttribute("login", "Iniciar Sesión");
        } else {
            model.addAttribute("logout", "Cerrar Sesión");
            model.addAttribute("userName", userName);
        }

        return "index";
    }

    @GetMapping("/destinos")
    public String destinos(HttpSession session, Model model) {

        String userName = (String) session.getAttribute("userName");

        if (userName == null) {
            model.addAttribute("login", "Iniciar Sesión");
        } else {
            model.addAttribute("logout", "Cerrar Sesión");
            model.addAttribute("userName", userName);
        }
        return "destinos";
    }

    @GetMapping("/landing-page-test")
    public String landingPageTest() {
        return "testingViews/landingPageTest";
    }
}
