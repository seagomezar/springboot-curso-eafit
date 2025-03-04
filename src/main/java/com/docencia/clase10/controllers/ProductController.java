package com.docencia.clase10.controllers;

import com.docencia.clase10.models.Comment;
import com.docencia.clase10.models.Product;
import com.docencia.clase10.repositories.CommentRepository;
import com.docencia.clase10.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/products")
    public String index(Model model) {
        List<Product> products = productRepository.findAll();
        model.addAttribute("title", "Products - Online Store");
        model.addAttribute("subtitle", "List of products");
        model.addAttribute("products", products);
        return "product/index"; // Retorna la vista product/index.html (Thymeleaf)
    }

    @GetMapping("/products/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("title", product.getName() + " - Online Store");
        model.addAttribute("subtitle", product.getName() + " - Product information");
        model.addAttribute("product", product);
        return "product/show"; // Retorna la vista product/show.html (Thymeleaf)
    }

    @GetMapping("/products/create")
    public String createProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "product/create";
    }

    @PostMapping("/products")
    public String save(Product product) {
        // Validaciones mínimas
        if (product.getName() == null || product.getName().isEmpty() ||
                product.getPrice() == null) {
            throw new RuntimeException("Name and Price are required");
        }
        productRepository.save(product);
        return "redirect:/products";
    }

    @PostMapping("/products/{id}/comments")
    public String addComment(@PathVariable("id") Long id, @RequestParam("description") String description) {
        // Buscar el producto por su id
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Crear y asociar el comentario al producto
        Comment comment = new Comment();
        comment.setDescription(description);
        comment.setProduct(product);

        // Guardar el comentario
        commentRepository.save(comment);

        // Redireccionar a la vista del producto
        return "redirect:/products/" + id;
    }

}