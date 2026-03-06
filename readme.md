# Spring Boot — Proyecto Educativo EAFIT

![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-brightgreen?logo=springboot)
![Maven](https://img.shields.io/badge/Maven-3.6%2B-blue?logo=apachemaven)
![H2](https://img.shields.io/badge/Database-H2-lightblue)
![License](https://img.shields.io/badge/License-MIT-green)

Repositorio de referencia para el curso de **Spring Boot** de la Universidad EAFIT. Cubre los conceptos fundamentales del desarrollo de aplicaciones web empresariales con Spring Boot, incluyendo persistencia de datos, seguridad, migraciones de base de datos y principios de diseño.

---

## 📚 Objetivos de Aprendizaje

Al completar este curso, los estudiantes serán capaces de:

1. Configurar un proyecto Spring Boot desde cero con Maven.
2. Modelar entidades de dominio con JPA/Hibernate y gestionarlas con Spring Data.
3. Aplicar migraciones de esquema de base de datos con Flyway.
4. Implementar autenticación y autorización con Spring Security.
5. Construir interfaces de usuario con Thymeleaf y plantillas reutilizables (fragments).
6. Aplicar el principio de Inversión de Dependencias (DIP) y la Inyección de Dependencias (DI).
7. Gestionar estado de sesión del usuario (ej.: carrito de compras).
8. Cargar datos de prueba con DataFaker.
9. Subir y servir archivos estáticos (imágenes).

---

## 🛠️ Prerrequisitos

| Herramienta | Versión mínima | Descripción |
|-------------|---------------|-------------|
| **Java (JDK)** | 17 | Lenguaje de programación principal |
| **Maven** | 3.6 | Gestión de dependencias y construcción (incluido como `mvnw`) |
| **IDE** | — | IntelliJ IDEA, VS Code + Extension Pack for Java, o Eclipse |
| **Git** | — | Control de versiones |

> **Nota:** No necesitas instalar Maven por separado. El proyecto incluye el Maven Wrapper (`mvnw` / `mvnw.cmd`) que descarga automáticamente la versión correcta.

---

## 📁 Estructura del Proyecto

```
springboot-curso-eafit/
├── src/
│   ├── main/
│   │   ├── java/com/docencia/clase10/
│   │   │   ├── bootstrap/          # Carga inicial de datos (DataLoader)
│   │   │   ├── config/             # Configuración de seguridad y recursos estáticos
│   │   │   ├── controllers/        # Controladores MVC
│   │   │   ├── DTOs/               # Objetos de Transferencia de Datos (formularios)
│   │   │   ├── interfaces/         # Interfaces para abstracción (DIP)
│   │   │   ├── models/             # Entidades JPA (Alumno, Curso, Product, Comment, Usuario)
│   │   │   ├── repositories/       # Repositorios Spring Data JPA
│   │   │   ├── services/           # Servicios de negocio
│   │   │   ├── util/               # Utilidades (ej.: ImageLocalStorage)
│   │   │   └── clase10Application.java
│   │   └── resources/
│   │       ├── db/migration/       # Scripts SQL de Flyway (V1__, V2__, ...)
│   │       ├── templates/          # Plantillas Thymeleaf
│   │       └── application.properties
│   └── test/                       # Pruebas unitarias e integración
├── uploads/                        # Archivos subidos (imágenes, DB H2)
├── pom.xml                         # Configuración Maven con dependencias
└── mvnw / mvnw.cmd                 # Maven Wrapper
```

---

## ⚡ Inicio Rápido

### 1. Clonar el repositorio

```bash
git clone https://github.com/seagomezar/springboot-curso-eafit.git
cd springboot-curso-eafit
```

### 2. Compilar y ejecutar

**En Linux/macOS:**
```bash
./mvnw spring-boot:run
```

**En Windows:**
```cmd
mvnw.cmd spring-boot:run
```

### 3. Acceder a la aplicación

| URL | Descripción |
|-----|-------------|
| `http://localhost:8080/registro` | Registrar un nuevo usuario |
| `http://localhost:8080/login` | Iniciar sesión |
| `http://localhost:8080/products` | Lista de productos (requiere autenticación) |
| `http://localhost:8080/cart` | Carrito de compras |
| `http://localhost:8080/image` | Subir imagen (con DI) |
| `http://localhost:8080/image-not-di` | Subir imagen (sin DI) |
| `http://localhost:8080/h2-console` | Consola H2 (base de datos) |

> **Primer uso:** Registra un usuario en `/registro` y luego inicia sesión. Para acceder a `/alumnos/**` necesitas un usuario con rol `ROLE_ADMIN` (asignable directamente en la base de datos H2).

---

## 🗃️ Configuración de la Base de Datos (H2)

El proyecto usa **H2** como base de datos embebida en modo archivo, lo que significa que los datos persisten entre reinicios en la carpeta `uploads/h2db/`.

### application.properties

```properties
# Datasource
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.url=jdbc:h2:file:./uploads/h2db/clase10;MODE=PostgreSQL
spring.datasource.username=sa
spring.datasource.password=

# Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update

# Consola H2 (solo para desarrollo)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

### Acceso a la Consola H2

1. Navega a `http://localhost:8080/h2-console`
2. Usa la URL JDBC: `jdbc:h2:file:./uploads/h2db/clase10`
3. Usuario: `sa`, Contraseña: (vacía)

---

## 🗄️ JPA & Hibernate — Modelos y Repositorios

JPA (Java Persistence API) es el estándar de Java para el mapeo objeto-relacional (ORM). **Hibernate** es la implementación más popular de JPA que Spring Boot usa por defecto.

### ¿Cómo funciona?

1. Defines una clase Java con la anotación `@Entity`.
2. Hibernate crea automáticamente la tabla en la base de datos.
3. Usas un `JpaRepository` para operaciones CRUD sin escribir SQL.

### Entidades del Proyecto

#### Product (con relación OneToMany a Comment)

```java
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer price;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SUBSELECT)
    private List<Comment> comments = new ArrayList<>();

    // Constructores, getters y setters...
}
```

#### Comment (con relación ManyToOne a Product)

```java
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    // Constructores, getters y setters...
}
```

### Tipos de Relaciones JPA

| Anotación | Descripción | Ejemplo |
|-----------|-------------|---------|
| `@OneToMany` | Un registro tiene muchos relacionados | Un `Product` tiene muchos `Comment` |
| `@ManyToOne` | Muchos registros pertenecen a uno | Un `Comment` pertenece a un `Product` |
| `@OneToOne` | Relación uno a uno | Un `Usuario` tiene un `Perfil` |
| `@ManyToMany` | Muchos a muchos | Un `Alumno` toma muchos `Curso` |

### Repositorios

Spring Data JPA proporciona operaciones CRUD automáticas al extender `JpaRepository`:

```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Spring genera automáticamente: findAll(), findById(), save(), delete(), count(), ...
    // También puedes declarar métodos personalizados:
    List<Product> findByName(String name);
    List<Product> findByPriceGreaterThan(int price);
}
```

---

## 🔄 Flyway — Migraciones de Base de Datos

Flyway gestiona el versionado del esquema de la base de datos con archivos SQL numerados. Cada vez que arranca la aplicación, Flyway verifica qué migraciones pendientes hay y las ejecuta en orden.

### Convención de Nombres

```
V{versión}__{descripción}.sql
```

- `V1__Create_person_table.sql` — Primera migración
- `V2__Add_email_to_users.sql` — Segunda migración

### Ubicación de Migraciones

```
src/main/resources/db/migration/
└── V1__Create_person_table.sql
```

### Ejemplo de Migración

```sql
-- V1__Create_person_table.sql
CREATE TABLE IF NOT EXISTS PERSON (
    ID   INT PRIMARY KEY GENERATED BY DEFAULT AS IDENTITY,
    NAME VARCHAR(100) NOT NULL
);
```

### Configuración

```properties
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
```

> **Importante:** Nunca modifiques una migración ya aplicada. Si necesitas cambiar el esquema, crea una nueva migración (`V2__...sql`).

---

## 🎲 DataFaker — Generación de Datos de Prueba

El proyecto usa [DataFaker](https://www.datafaker.net/) (evolución moderna de JavaFaker) para generar datos de prueba realistas al arrancar la aplicación por primera vez.

### DataLoader

```java
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
        // Solo carga datos si la base de datos está vacía
        if (productRepository.count() > 0) {
            return;
        }

        Faker faker = new Faker(new Locale("es"));
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            String productName = faker.commerce().productName();
            int price = random.nextInt(500) + 50;
            Product product = new Product(productName, price);

            int numComments = random.nextInt(3) + 1;
            for (int j = 0; j < numComments; j++) {
                String description = faker.lorem().sentence();
                Comment comment = new Comment(description, product);
                product.getComments().add(comment);
            }
            productRepository.save(product);
        }
    }
}
```

**Conceptos clave:**
- `CommandLineRunner`: interfaz de Spring Boot que ejecuta código al arrancar la aplicación.
- La verificación `productRepository.count() > 0` evita insertar datos duplicados en reinicios.
- `CascadeType.ALL` en la entidad `Product` permite guardar los comentarios al guardar el producto.

---

## 🔒 Spring Security — Autenticación y Autorización

Spring Security protege las rutas de la aplicación y gestiona el ciclo de vida de la sesión de usuario.

### Flujo de Autenticación

```
1. Usuario visita una ruta protegida
2. Spring Security redirige a /login
3. Usuario ingresa credenciales
4. Spring verifica contra la base de datos (UserDetailsService)
5. Si es correcto → redirige a /products
6. Si falla → muestra error en /login
```

### Roles de Usuario

| Rol | Acceso |
|-----|--------|
| `ROLE_USER` | `/products/**`, `/cursos/**`, `/cart`, `/image/**` |
| `ROLE_ADMIN` | Todo lo anterior + `/alumnos/**` |

### SecurityConfig

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                // Rutas públicas
                .requestMatchers("/login", "/registro", "/css/**", "/js/**", "/h2-console/**").permitAll()
                // Rutas protegidas (solo accesibles para usuarios autenticados)
                .requestMatchers("/products/**").authenticated()
                // Rutas específicas según roles
                .requestMatchers("/alumnos/**").hasRole("ADMIN")
                .requestMatchers("/cursos/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/products", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            // ⚠️ SOLO PARA DESARROLLO: CSRF deshabilitado para simplificar el ejemplo.
            // En producción, habilita CSRF y configura las excepciones necesarias.
            .csrf(csrf -> csrf.disable())
            // Necesario para la consola H2 (carga en un iframe)
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### Registro de Usuarios

El endpoint `/registro` permite crear nuevas cuentas. Las contraseñas se almacenan usando **BCrypt** (hash seguro):

```java
usuario.setPassword(passwordEncoder.encode(registroForm.getPassword()));
```

---

## 🛒 Carrito de Compras — Gestión de Sesión

Ejemplo de cómo usar `HttpSession` de Jakarta EE para mantener estado entre peticiones HTTP.

### ¿Por qué usar sesión?

HTTP es un protocolo sin estado (stateless). La sesión permite recordar información del usuario entre peticiones, como los productos que ha agregado al carrito.

### Controller

```java
@Controller
@RequestMapping("/cart")
public class CartController {

    @GetMapping("/add/{id}")
    public String add(@PathVariable Integer id, HttpSession session) {
        Map<Integer, Integer> cartProductData =
            (Map<Integer, Integer>) session.getAttribute("cart_product_data");
        if (cartProductData == null) {
            cartProductData = new HashMap<>();
        }
        cartProductData.put(id, id);
        session.setAttribute("cart_product_data", cartProductData);
        return "redirect:/cart";
    }

    @GetMapping("/removeAll")
    public String removeAll(HttpSession session) {
        session.removeAttribute("cart_product_data");
        return "redirect:/cart";
    }
}
```

### Vista Thymeleaf (cart/index.html)

```html
<h2>Productos disponibles</h2>
<ul>
  <li th:each="entry : ${products}">
    <span th:text="${entry.value.name}">Producto</span> -
    <span th:text="${entry.value.price}">0</span>
    <a th:href="@{'/cart/add/' + ${entry.key}}">Agregar al carrito</a>
  </li>
</ul>

<h2>Mi Carrito</h2>
<ul>
  <li th:each="entry : ${cartProducts}">
    <span th:text="${entry.value.name}">Producto</span>
  </li>
</ul>
<a th:href="@{/cart/removeAll}">Vaciar carrito</a>
```

---

## 🖼️ Almacenamiento de Imágenes — Inversión de Dependencias (DIP)

Este módulo ilustra el **Principio de Inversión de Dependencias** (uno de los principios SOLID):
- Las clases de alto nivel no deben depender de clases de bajo nivel.
- Ambas deben depender de **abstracciones** (interfaces).

### Diagrama de Dependencias

```
                  ┌─────────────────────┐
                  │   ImageController   │  (alto nivel)
                  └──────────┬──────────┘
                             │ depende de
                             ▼
                  ┌─────────────────────┐
                  │   ImageStorage      │  (abstracción / interfaz)
                  └──────────┬──────────┘
                             │ implementada por
                             ▼
                  ┌─────────────────────┐
                  │ ImageLocalStorage   │  (bajo nivel)
                  └─────────────────────┘
```

### Interface

```java
public interface ImageStorage {
    void store(MultipartFile file);
}
```

### Implementación

```java
@Component
public class ImageLocalStorage implements ImageStorage {
    private static final String STORAGE_DIR = "uploads/";

    @Override
    public void store(MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                // Crear el directorio si no existe
                Path storageDir = Paths.get(STORAGE_DIR);
                if (!Files.exists(storageDir)) {
                    Files.createDirectories(storageDir);
                }
                // Guardar la imagen con el nombre "test.png"
                Path destination = storageDir.resolve("test.png").normalize().toAbsolutePath();
                Files.copy(file.getInputStream(), destination);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```

### Con DI (recomendado)

```java
@Controller
@RequestMapping("/image")
public class ImageController {

    private final ImageStorage imageStorage; // Depende de la interfaz, no de la implementación

    @Autowired
    public ImageController(ImageStorage imageStorage) {
        this.imageStorage = imageStorage;
    }

    @PostMapping("/save")
    public String save(@RequestParam("profile_image") MultipartFile profileImage,
                       RedirectAttributes redirectAttributes) {
        imageStorage.store(profileImage);
        redirectAttributes.addFlashAttribute("message", "Image uploaded successfully!");
        return "redirect:/image";
    }
}
```

### Sin DI (no recomendado — acoplamiento fuerte)

```java
@PostMapping("/save")
public String save(@RequestParam("profile_image") MultipartFile profileImage) {
    ImageLocalStorage storage = new ImageLocalStorage(); // Acoplado a la implementación concreta
    storage.store(profileImage);
    return "redirect:/image";
}
```

> **¿Por qué es mejor con DI?** Si mañana quieres guardar imágenes en AWS S3 o Google Cloud Storage, solo cambias la implementación sin tocar `ImageController`. Con DI, el controlador está desacoplado del mecanismo de almacenamiento.

---

## 🌿 Thymeleaf — Motor de Plantillas

Thymeleaf es el motor de plantillas HTML que Spring Boot usa por defecto. Permite renderizar páginas HTML dinámicas en el servidor.

### Características Clave

- **Fragmentos reutilizables:** `fragments/header.html`, `fragments/footer.html`
- **Iteración:** `th:each` para recorrer listas y mapas
- **Condiciones:** `th:if` y `th:unless`
- **Enlace de formularios:** `th:object` y `th:field`
- **URLs dinámicas:** `th:href="@{/ruta}"`

### Ejemplo de Fragmento (header.html)

```html
<!-- templates/fragments/header.html -->
<nav th:fragment="header">
  <a href="/products">Productos</a>
  <a href="/cart">Carrito</a>
  <a href="/logout">Cerrar sesión</a>
</nav>
```

**Uso en otra plantilla:**
```html
<div th:replace="~{fragments/header :: header}"></div>
```

---

## 📦 Dependencias Principales

```xml
<!-- Spring Boot Parent (gestiona versiones de todas las dependencias) -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.5.3</version>
</parent>

<!-- Web MVC + Tomcat embebido -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- JPA / Hibernate -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Spring Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Motor de plantillas Thymeleaf -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>

<!-- Base de datos H2 embebida (en memoria / archivo) -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- DataFaker: generación de datos de prueba -->
<dependency>
    <groupId>net.datafaker</groupId>
    <artifactId>datafaker</artifactId>
    <version>2.4.3</version>
</dependency>
```

---

## 🚀 Construir un JAR Ejecutable

Para generar un JAR que puedas distribuir o desplegar:

```bash
./mvnw clean package
java -jar target/clase10-0.0.1-SNAPSHOT.jar
```

---

## 🐛 Resolución de Problemas Comunes

| Problema | Causa | Solución |
|----------|-------|----------|
| `HTTP 413 — Payload Too Large` | Archivo demasiado grande | Ajusta `spring.servlet.multipart.max-file-size=10MB` |
| La imagen no aparece | Archivo no servido | Verifica `StaticResourceConfiguration` y que el archivo exista en `uploads/` |
| Error al iniciar: `Flyway` | Script SQL inválido | Revisa la sintaxis SQL en `db/migration/` y que el nombre siga el patrón `V{n}__*.sql` |
| `Access Denied` en H2 Console | CSRF o headers | Verifica que `csrf().disable()` y `frameOptions().sameOrigin()` estén configurados |
| `UsernameNotFoundException` | Usuario no registrado | Regístrate primero en `/registro` |
| Los datos se duplican al reiniciar | Falta chequeo de existencia | El `DataLoader` ya incluye `if (productRepository.count() > 0) return;` |

---

## 📖 Recursos Adicionales

- [Documentación oficial de Spring Boot](https://docs.spring.io/spring-boot/index.html)
- [Guía de Spring Security](https://docs.spring.io/spring-security/reference/index.html)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/index.html)
- [Thymeleaf Tutorial](https://www.thymeleaf.org/doc/tutorials/3.1/usingthymeleaf.html)
- [DataFaker](https://www.datafaker.net/documentation/getting-started/)
- [Flyway Documentation](https://documentation.red-gate.com/flyway)

---

## 👩‍💻 Cómo Contribuir

Este es un repositorio educativo. Si encuentras un error o tienes una mejora:

1. Haz un fork del repositorio.
2. Crea una rama: `git checkout -b mejora/descripcion`.
3. Realiza tus cambios y haz commit: `git commit -m "Describe el cambio"`.
4. Abre un Pull Request.

---

*Desarrollado para el curso de Spring Boot — Universidad EAFIT*

