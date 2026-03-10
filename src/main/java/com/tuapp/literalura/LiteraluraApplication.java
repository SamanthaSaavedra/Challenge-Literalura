package com.tuapp.literalura;

import com.tuapp.literalura.principal.MenuPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class LiteraluraApplication implements CommandLineRunner {

    private final MenuPrincipal menuPrincipal;

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Override
    public void run(String... args) {
        menuPrincipal.iniciar();
    }
}
