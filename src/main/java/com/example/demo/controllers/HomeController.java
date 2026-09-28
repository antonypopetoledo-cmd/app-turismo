package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    @GetMapping("/destinos")
    public String destinos() {
        return "destinos";
    }

    @GetMapping("/landing-page-test")
    public String landingPageTest() {
        return "testingViews/landingPageTest";
    }
}
