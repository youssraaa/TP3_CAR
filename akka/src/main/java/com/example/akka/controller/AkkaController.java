package com.example.akka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.akka.service.AkkaService;

@Controller
@RequestMapping("/akka")
public class AkkaController {

    @Autowired
    private AkkaService akkaService;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @PostMapping("/init")
    public String initializeAkka(Model model) {
        akkaService.initializeActors();
        return "home";
    }
}


