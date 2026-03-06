package com.docencia.clase10.bootstrap;

import com.docencia.clase10.models.Product;
import com.docencia.clase10.models.Comment;
import com.docencia.clase10.repositories.ProductRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Random;

@Component
public class DataLoader implements CommandLineRunner {

    private final ProductRepository productRepository;

    public DataLoader(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Solo insertar datos si la base de datos está vacía para evitar duplicados
        if (productRepository.count() > 0) {
            System.out.println("La base de datos ya tiene datos. Omitiendo la carga inicial.");
            return;
        }

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
            }

            // Guardar el producto (y sus comentarios via cascade)
            productRepository.save(product);
        }

        System.out.println("Productos y comentarios generados.");
    }
}
