package com.spring.reactive.example1.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(final Model model) {
        return "index";
    }

    @GetMapping("/with")
    public String withError(final Model model) {
        return "withError";
    }

}
