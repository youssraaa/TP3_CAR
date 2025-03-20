package com.example.akka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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
    public String initializeAkka() {
        akkaService.initializeActors();
        System.out.println("Actors initialized successfully!");
        return "home";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {

        if (file.isEmpty()) {
            System.out.println("Aucun fichier sélectionné !");
            return "home";
        }

        try {
            System.out.println("Submit : " + file.getOriginalFilename()); 
            akkaService.processFile(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "home";
    }

    @GetMapping("/search")
    public String searchWord(@RequestParam("mot") String mot, Model model) {
        int occurrences = akkaService.getWordOccurrences(mot);
        model.addAttribute("mot", mot);
        model.addAttribute("occurrences", occurrences);
        return "home";
    }
}
