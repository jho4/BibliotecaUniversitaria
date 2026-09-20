package com.jhoanaviles.biblioteca.model;

import com.jhoanaviles.biblioteca.prototype.Prototipo;

/**
 * Representa un libro de la biblioteca.
 * Se implementa Prototype para poder crear copias del libro.
 */
public class Libro implements Prototipo<Libro> {

    private String codigo;
    private String titulo;
    private String autor;
    private String categoria;
    private EstadoLibro estado;

    public Libro(
            String codigo,
            String titulo,
            String autor,
            String categoria,
            EstadoLibro estado) {

        this.codigo = codigo;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;

        if (estado == null) {
            this.estado = EstadoLibro.DISPONIBLE;
        } else {
            this.estado = estado;
        }
    }

    /**
     * Implementación del patrón Prototype.
     *
     * Crea un nuevo objeto copiando los datos
     * del libro actual.
     */
    @Override
    public Libro clonar() {

        return new Libro(
                codigo,
                titulo,
                autor,
                categoria,
                estado
        );
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public EstadoLibro getEstado() {
        return estado;
    }

    public void setEstado(EstadoLibro estado) {

        if (estado == null) {
            this.estado = EstadoLibro.DISPONIBLE;
        } else {
            this.estado = estado;
        }
    }

    @Override
    public String toString() {

        String autorMostrar =
                autor == null || autor.trim().isEmpty()
                        ? "Sin autor"
                        : autor;

        String categoriaMostrar =
                categoria == null || categoria.trim().isEmpty()
                        ? "Sin categoría"
                        : categoria;

        return "[" + codigo + "] "
                + titulo
                + " | Autor: " + autorMostrar
                + " | Categoría: " + categoriaMostrar
                + " | Estado: " + estado;
    }
}