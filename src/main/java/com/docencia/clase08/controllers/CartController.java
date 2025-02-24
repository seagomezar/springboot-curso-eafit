package com.docencia.clase08.controllers;

import com.docencia.clase08.models.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    // Simula la "base de datos" de productos
    private final Map<Integer, Product> products;

    public CartController() {
        products = new HashMap<>();
        products.put(121, new Product( "MacBook Pro", 3000));
        products.put(122, new Product("iPad", 1000));
    }

    @GetMapping
    public String index(HttpSession session, Model model) {
        // Obtener productos del carrito almacenados en la sesión
        Map<Integer, Integer> cartProductData = (Map<Integer, Integer>) session.getAttribute("cart_product_data");
        Map<Integer, Product> cartProducts = new HashMap<>();

        if (cartProductData != null) {
            // Agrega a cartProducts los productos presentes en el carrito
            for (Integer id : cartProductData.keySet()) {
                if (products.containsKey(id)) {
                    cartProducts.put(id, products.get(id));
                }
            }
        }

        model.addAttribute("title", "Cart - Online Store");
        model.addAttribute("subtitle", "Shopping Cart");
        model.addAttribute("products", products);
        model.addAttribute("cartProducts", cartProducts);
        return "cart/index";
    }

    @GetMapping("/add/{id}")
    public String add(@PathVariable Integer id, HttpSession session) {
        // Recuperar el carrito de la sesión o crear uno nuevo si es nulo
        Map<Integer, Integer> cartProductData = (Map<Integer, Integer>) session.getAttribute("cart_product_data");
        if (cartProductData == null) {
            cartProductData = new HashMap<>();
        }
        // Agrega el producto al carrito (aquí se usa el id como clave y valor)
        cartProductData.put(id, id);
        session.setAttribute("cart_product_data", cartProductData);
        return "redirect:/cart";
    }

    @GetMapping("/removeAll")
    public String removeAll(HttpSession session) {
        // Elimina el atributo del carrito de la sesión
        session.removeAttribute("cart_product_data");
        return "redirect:/cart";
    }
}
