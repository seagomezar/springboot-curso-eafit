package com.docencia.clase08.controllers;

import com.docencia.clase08.interfaces.ImageStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/image")
public class ImageController {

    private final ImageStorage imageStorage;

    @Autowired
    public ImageController(ImageStorage imageStorage) {
        this.imageStorage = imageStorage;
    }

    @GetMapping
    public String index(Model model) {
        // Muestra la vista de carga de imagen
        return "image/index";
    }

    @PostMapping("/save")
    public String save(@RequestParam("profile_image") MultipartFile profileImage,
                       RedirectAttributes redirectAttributes) {
        imageStorage.store(profileImage);
        redirectAttributes.addFlashAttribute("message", "Image uploaded successfully!");
        return "redirect:/image";
    }
}
