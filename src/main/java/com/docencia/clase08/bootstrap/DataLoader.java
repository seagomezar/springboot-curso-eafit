package com.docencia.clase08.bootstrap;

import com.docencia.clase08.models.Product;
import com.docencia.clase08.models.Comment;
import com.docencia.clase08.repositories.ProductRepository;
import com.docencia.clase08.repositories.CommentRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CommentRepository commentRepository;

    public DataLoader(ProductRepository productRepository, CommentRepository commentRepository) {
        this.productRepository = productRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker(new Locale("es"));
        Random random = new Random();

        // Insertar 5 productos
        for (int i = 0; i < 5; i++) {
            String productName = faker.commerce().productName();
            // Generar un precio aleatorio entre 50 y 550
            int price = random.nextInt(500) + 50;
            Product product = new Product(productName, price);

            // Insertar entre 1 y 3 comentarios para cada producto
            int numComments = random.nextInt(3) + 1;
            for (int j = 0; j < numComments; j++) {
                String description = faker.lorem().sentence();
                Comment comment = new Comment(description, product);
                // Agregar el comentario a la lista del producto
                product.getComments().add(comment);
                // Opcional: si no tienes configurado el cascade en Product, también podrías guardar el comentario
                // commentRepository.save(comment);
            }

            // Guardar el producto (y sus comentarios, si se usa cascade)
            productRepository.save(product);
        }

        System.out.println("Productos y comentarios generados.");
    }
}
