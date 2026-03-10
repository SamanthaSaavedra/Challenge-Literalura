package com.saidavaas.literalura.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saidavaas.literalura.dto.LibroApiDto;
import com.saidavaas.literalura.dto.RespuestaApiDto;
import com.saidavaas.literalura.model.Autor;
import com.saidavaas.literalura.model.Libro;
import com.saidavaas.literalura.repository.AutorRepository;
import com.saidavaas.literalura.repository.LibroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogoService {

    private final GutendexClient gutendexClient;
    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String SEPARADOR = "─".repeat(45);

    // ──────────────────────────────────────────────
    // BUSCAR Y GUARDAR LIBRO
    // ──────────────────────────────────────────────
    @Transactional
    public void buscarYGuardarLibro(String titulo) {
        try {
            String json = gutendexClient.buscarPorTitulo(titulo);
            RespuestaApiDto respuesta = objectMapper.readValue(json, RespuestaApiDto.class);

            if (respuesta.resultados() == null || respuesta.resultados().isEmpty()) {
                System.out.println("\nNo se encontraron resultados para: \"" + titulo + "\"");
                return;
            }

            LibroApiDto datos = respuesta.resultados().getFirst();

            if (libroRepository.existsByTituloIgnoreCase(datos.titulo())) {
                System.out.println("\nEl libro \"" + datos.titulo() + "\" ya está registrado en el catálogo.");
                return;
            }

            // Obtener o crear autor
            String nombreAutor = datos.autores().isEmpty() ? "Desconocido" : datos.autores().getFirst().nombre();
            Autor autor = autorRepository.findByNombreIgnoreCase(nombreAutor)
                    .orElseGet(() -> {
                        Autor nuevo = new Autor();
                        nuevo.setNombre(nombreAutor);
                        if (!datos.autores().isEmpty()) {
                            nuevo.setAnioNacimiento(datos.autores().getFirst().anioNacimiento());
                            nuevo.setAnioFallecimiento(datos.autores().getFirst().anioFallecimiento());
                        }
                        return autorRepository.save(nuevo);
                    });

            Libro libro = new Libro();
            libro.setTitulo(datos.titulo());
            libro.setIdioma(datos.idiomas().isEmpty() ? "desconocido" : datos.idiomas().getFirst());
            libro.setNumeroDescargas(datos.descargas());
            libro.setAutor(autor);
            libroRepository.save(libro);

            imprimirLibro(libro);
            System.out.println("Libro guardado correctamente.\n");

        } catch (Exception e) {
            System.out.println("\nError al buscar el libro: " + e.getMessage());
        }
    }

    // ──────────────────────────────────────────────
    // LISTAR LIBROS
    // ──────────────────────────────────────────────
    @Transactional(readOnly = true)
    public void listarLibros() {
        List<Libro> libros = libroRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("\nNo hay libros registrados aún.");
            return;
        }

        System.out.println("\nLIBROS EN EL CATÁLOGO (" + libros.size() + ")\n" + SEPARADOR);
        libros.forEach(this::imprimirLibro);
    }

    // ──────────────────────────────────────────────
    // LISTAR AUTORES
    // ──────────────────────────────────────────────
    @Transactional(readOnly = true)
    public void listarAutores() {
        List<Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("\nNo hay autores registrados aún.");
            return;
        }

        System.out.println("\nAUTORES REGISTRADOS (" + autores.size() + ")\n" + SEPARADOR);
        autores.forEach(this::imprimirAutor);
    }

    // ──────────────────────────────────────────────
    // LISTAR POR IDIOMA
    // ──────────────────────────────────────────────
    @Transactional(readOnly = true)
    public void listarPorIdioma(String idioma) {
        List<Libro> libros = libroRepository.findByIdiomaIgnoreCase(idioma.trim());

        if (libros.isEmpty()) {
            System.out.println("\n️No hay libros registrados en el idioma: \"" + idioma + "\"");
            return;
        }

        System.out.println("\nLIBROS EN IDIOMA '" + idioma.toUpperCase() + "' (" + libros.size() + ")\n" + SEPARADOR);
        libros.forEach(l -> System.out.printf("%-40s  (Autor: %s)%n",
                l.getTitulo(), l.getAutor().getNombre()));
        System.out.println(SEPARADOR + "\n");
    }

    // ──────────────────────────────────────────────
    // AUTORES VIVOS EN UN AÑO
    // ──────────────────────────────────────────────
    @Transactional(readOnly = true)
    public void autoresVivosEnAnio(Integer anio) {
        List<Autor> autores = autorRepository.findAutoresVivosEnAnio(anio);

        if (autores.isEmpty()) {
            System.out.printf("%n No hay autores vivos registrados en el año %d.%n", anio);
            return;
        }

        System.out.println("\nAUTORES VIVOS EN " + anio + " (" + autores.size() + ")\n" + SEPARADOR);
        autores.forEach(this::imprimirAutor);
        System.out.println(SEPARADOR + "\n");
    }

    // ──────────────────────────────────────────────
    // HELPERS DE IMPRESIÓN
    // ──────────────────────────────────────────────
    private void imprimirLibro(Libro l) {
        System.out.printf("""
                %s
                    Título    : %s
                    Autor     : %s
                    Idioma    : %s
                    Descargas : %.0f
                """,
                SEPARADOR,
                l.getTitulo(),
                l.getAutor() != null ? l.getAutor().getNombre() : "Desconocido",
                l.getIdioma(),
                l.getNumeroDescargas() != null ? l.getNumeroDescargas() : 0.0);
    }

    private void imprimirAutor(Autor a) {
        String fallecimiento = a.getAnioFallecimiento() != null
                ? a.getAnioFallecimiento().toString()
                : "Actualidad";
        System.out.printf("%-40s  (%s – %s)%n",
                a.getNombre(),
                a.getAnioNacimiento() != null ? a.getAnioNacimiento() : "?",
                fallecimiento);
    }
}
