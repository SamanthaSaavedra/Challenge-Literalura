package com.saidavaas.literalura.principal;

import com.saidavaas.literalura.service.CatalogoService;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Scanner;

@Component
public class MenuPrincipal {

    private final CatalogoService catalogoService;
    private final Scanner scanner = new Scanner(System.in);

    public MenuPrincipal(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    private static final String BANNER = """
            ╔══════════════════════════════════════════╗
            ║            L I T E R A L U R A           ║
            ║         Catálogo de Libros Gutendex      ║
            ╚══════════════════════════════════════════╝
            """;

    private static final String MENU = """
            ┌──────────────────────────────────────────┐
            │  1 - Buscar libro por título             │
            │  2 - Listar libros registrados           │
            │  3 - Listar autores registrados          │
            │  4 - Listar libros por idioma            │
            │  5 - Autores vivos en un año             │
            │  0 - Salir                               │
            └──────────────────────────────────────────┘
            Elige una opción:\s""";

    public void iniciar() {
        System.out.println(BANNER);
        int opcion = -1;

        while (opcion != 0) {
            System.out.print(MENU);
            try {
                opcion = scanner.nextInt();
                scanner.nextLine();
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Opción inválida. Ingresa un número del menú.\n");
                continue;
            }

            switch (opcion) {
                case 1 -> opcionBuscarLibro();
                case 2 -> catalogoService.listarLibros();
                case 3 -> catalogoService.listarAutores();
                case 4 -> opcionListarPorIdioma();
                case 5 -> opcionAutoresVivos();
                case 0 -> System.out.println("\n¡Hasta pronto!\n");
                default -> System.out.println("Opción no reconocida. Intenta de nuevo.\n");
            }
        }
    }

    private void opcionBuscarLibro() {
        System.out.print("\nIngresa el título del libro: ");
        String titulo = scanner.nextLine().trim();
        if (titulo.isBlank()) {
            System.out.println("El título no puede estar vacío.");
            return;
        }
        catalogoService.buscarYGuardarLibro(titulo);
    }

    private void opcionListarPorIdioma() {
        System.out.print("\nIngresa el código de idioma (ej: en, es, fr): ");
        String idioma = scanner.nextLine().trim();
        if (idioma.isBlank()) {
            System.out.println("El idioma no puede estar vacío.");
            return;
        }
        catalogoService.listarPorIdioma(idioma);
    }

    private void opcionAutoresVivos() {
        System.out.print("\nIngresa el año: ");
        try {
            int anio = scanner.nextInt();
            scanner.nextLine();
            catalogoService.autoresVivosEnAnio(anio);
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("Debes ingresar un año válido (número entero).\n");
        }
    }
}
