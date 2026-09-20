package com.jhoanaviles.biblioteca.builder;

import com.jhoanaviles.biblioteca.model.EstadoLibro;
import com.jhoanaviles.biblioteca.model.Libro;

/**
 * Patrón creacional Builder.
 *
 * Código y título son obligatorios.
 * Autor, categoría y estado son opcionales.
 */
public class LibroBuilder {

    private String codigo;
    private String titulo;
    private String autor;
    private String categoria;

    private EstadoLibro estado = EstadoLibro.DISPONIBLE;

    public LibroBuilder conCodigo(String codigo) {

        this.codigo = codigo;

        return this;
    }

    public LibroBuilder conTitulo(String titulo) {

        this.titulo = titulo;

        return this;
    }

    public LibroBuilder conAutor(String autor) {

        this.autor = autor;

        return this;
    }

    public LibroBuilder conCategoria(String categoria) {

        this.categoria = categoria;

        return this;
    }

    public LibroBuilder conEstado(EstadoLibro estado) {

        if (estado != null) {
            this.estado = estado;
        }

        return this;
    }

    /**
     * Crea finalmente el objeto Libro.
     */
    public Libro build() {

        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El código del libro es obligatorio."
            );
        }

        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El título del libro es obligatorio."
            );
        }

        return new Libro(
                codigo.trim(),
                titulo.trim(),
                autor,
                categoria,
                estado
        );
    }
}