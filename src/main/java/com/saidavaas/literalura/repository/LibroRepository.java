package com.saidavaas.literalura.repository;

import com.saidavaas.literalura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    boolean existsByTituloIgnoreCase(String titulo);

    List<Libro> findByIdiomaIgnoreCase(String idioma);
}
