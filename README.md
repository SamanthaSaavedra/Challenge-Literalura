# 📚 LiteraLura — Catálogo de Libros

Aplicación de consola en **Java + Spring Boot** que permite buscar, registrar y consultar libros usando la API pública de [Gutendex](https://gutendex.com/), con persistencia en **PostgreSQL**.

---

## 🚀 Funcionalidades

| Opción | Descripción |
|--------|-------------|
| 1 | Buscar libro por título (llama a la API y lo guarda) |
| 2 | Listar todos los libros registrados |
| 3 | Listar todos los autores registrados |
| 4 | Filtrar libros por idioma (ej: `en`, `es`) |
| 5 | Listar autores vivos en un año determinado |

---

## 🛠️ Tecnologías

- Java 21
- Spring Boot 3.2
- Spring Data JPA
- PostgreSQL
- Jackson (JSON)
- Lombok

---

## ⚙️ Configuración

### 1. Base de datos

Crea la base de datos en PostgreSQL:

```sql
CREATE DATABASE literalura;
```

### 2. Variables de entorno

Copia el archivo de ejemplo y rellena tus datos:

```bash
cp .env.example .env
```

Edita `.env`:

```env
DB_URL=jdbc:postgresql://localhost:5432/literalura
DB_USERNAME=tu_usuario
DB_PASSWORD=tu_contraseña
```

> ⚠️ **Nunca subas el archivo `.env` a Git.** Ya está incluido en `.gitignore`.

### 3. Ejecutar la aplicación

Exporta las variables de entorno y lanza con Maven:

**Linux / macOS:**
```bash
export DB_USERNAME=tu_usuario
export DB_PASSWORD=tu_contraseña
./mvnw spring-boot:run
```

**Windows (PowerShell):**
```powershell
$env:DB_USERNAME="tu_usuario"
$env:DB_PASSWORD="tu_contraseña"
.\mvnw.cmd spring-boot:run
```

**Con IntelliJ IDEA:**  
Ve a `Run > Edit Configurations > Environment Variables` y agrega `DB_USERNAME` y `DB_PASSWORD`.

---

## 📁 Estructura del proyecto

```
src/main/java/com/tuapp/literalura/
├── LiteraluraApplication.java      # Punto de entrada
├── principal/
│   └── MenuPrincipal.java          # Menú interactivo de consola
├── model/
│   ├── Autor.java                  # Entidad Autor
│   └── Libro.java                  # Entidad Libro
├── dto/
│   ├── RespuestaApiDto.java        # DTO raíz de la API
│   ├── LibroApiDto.java            # DTO de libro
│   └── AutorApiDto.java            # DTO de autor
├── repository/
│   ├── AutorRepository.java        # Repositorio JPA de autores
│   └── LibroRepository.java        # Repositorio JPA de libros
└── service/
    ├── GutendexClient.java         # Cliente HTTP para Gutendex
    └── CatalogoService.java        # Lógica de negocio
```
