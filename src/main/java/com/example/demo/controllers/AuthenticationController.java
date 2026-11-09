package com.example.demo.controllers;

import com.example.demo.dtos.RegisterSupplierDTO;
import com.example.demo.dtos.RegisterTouristDTO;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthenticationController {

    private final UserService userService;

    private final List<String> countries = List.of("Perú", "Chile", "Argentina", "USA", "Canada", "United Kingdom", "France", "Spain", "Germany", "Italy");

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registro")
    public String register(Model model) {

        model.addAttribute("countries", this.countries);

        return "registro";
    }

    @PostMapping("/registro/turista")
    public String registerTourist(@Valid @ModelAttribute RegisterTouristDTO dto, BindingResult result, Model model, RedirectAttributes redirectAttributes) {

        System.out.println("POST REGISTRO TURISTA RECIBIDO");
        System.out.println(dto.toString());        

        if (result.hasErrors()) {
            model.addAttribute("countries", this.countries);

            model.addAttribute("errores", result.getFieldErrors().stream().map(error -> error.getField() + ": " + error.getDefaultMessage()).toList());
            System.out.println("Errores: " + result.getAllErrors());

            return "registro";
        }

        String message = this.userService.registerTourist(dto);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/login";
    }

    @PostMapping("/registro/proveedor")
    public String registerSupplier(@Valid @ModelAttribute RegisterSupplierDTO dto, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        
        System.out.println("POST REGISTRO PROVEEDOR RECIBIDO");
        System.out.println(dto.toString());

        if (result.hasErrors()) {
            model.addAttribute("countries", this.countries);

            model.addAttribute("errores", result.getFieldErrors().stream().map(error -> error.getField() + ": " + error.getDefaultMessage()).toList());
            System.out.println("Errores: " + result.getAllErrors());

            return "registro";
        }

        String message = this.userService.registerSupplier(dto);
        redirectAttributes.addFlashAttribute("message", message);        
        return "redirect:/login";
    }

    /*
    @GetMapping("/registro")
    public String register(Model model) {
        model.addAttribute("user", new RegisterUserDTO());
        return "registro";
    }
     */
 /*
    private final String TEST_USER_EMAIL = "jhon@gmail.com";
    private final String TEST_USER_PASSWORD = "12345";
    private final String TEST_USER_NAME = "Jhon Traveler";
     */
 /*
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
     */
}
