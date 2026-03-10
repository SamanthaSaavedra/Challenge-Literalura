package com.tuapp.literalura.repository;

import com.tuapp.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {

    Optional<Autor> findByNombreIgnoreCase(String nombre);

    @Query("""
            SELECT a FROM Autor a
            WHERE a.anioNacimiento <= :anio
            AND (a.anioFallecimiento IS NULL OR a.anioFallecimiento >= :anio)
            ORDER BY a.nombre
            """)
    List<Autor> findAutoresVivosEnAnio(@Param("anio") Integer anio);
}
