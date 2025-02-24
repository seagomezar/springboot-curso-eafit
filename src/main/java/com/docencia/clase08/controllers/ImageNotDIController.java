package com.docencia.clase08.controllers;

import com.docencia.clase08.util.ImageLocalStorage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/image-not-di")
public class ImageNotDIController {

    @GetMapping
    public String index(Model model) {
        return "imagenotdi/index";
    }

    @PostMapping("/save")
    public String save(@RequestParam("profile_image") MultipartFile profileImage,
                       RedirectAttributes redirectAttributes) {
        // Se crea la instancia manualmente, sin usar el contenedor de Spring
        ImageLocalStorage storage = new ImageLocalStorage();
        storage.store(profileImage);
        redirectAttributes.addFlashAttribute("message", "Image uploaded successfully (not DI)!");
        return "redirect:/image-not-di";
    }
}
