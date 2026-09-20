package com.jhoanaviles.biblioteca.service;

import com.jhoanaviles.biblioteca.model.EstadoLibro;
import com.jhoanaviles.biblioteca.model.Libro;
import com.jhoanaviles.biblioteca.model.Prestamo;
import com.jhoanaviles.biblioteca.singleton.ConfiguracionBiblioteca;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Operaciones principales de la biblioteca.
 */
public class BibliotecaService {

    private final List<Libro> libros = new ArrayList<>();
    private final List<Prestamo> prestamos = new ArrayList<>();

    /**
     * Registrar un nuevo libro.
     */
    public void registrarLibro(Libro libro) {

        if (libro == null) {
            throw new IllegalArgumentException(
                    "El libro no puede ser nulo."
            );
        }

        if (buscarLibroPorCodigo(libro.getCodigo()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un libro con ese código."
            );
        }

        libros.add(libro);
    }

    /**
     * Clona un libro existente utilizando Prototype.
     */
    public Libro clonarLibro(
            String codigoOriginal,
            String nuevoCodigo,
            String nuevoTitulo) {

        Libro original =
                buscarLibroPorCodigo(codigoOriginal);

        if (original == null) {
            throw new IllegalArgumentException(
                    "El libro original no existe."
            );
        }

        if (nuevoCodigo == null
                || nuevoCodigo.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El nuevo código es obligatorio."
            );
        }

        if (nuevoTitulo == null
                || nuevoTitulo.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El nuevo título es obligatorio."
            );
        }

        if (buscarLibroPorCodigo(nuevoCodigo) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un libro con ese código."
            );
        }

        Libro clon = original.clonar();

        clon.setCodigo(nuevoCodigo.trim());
        clon.setTitulo(nuevoTitulo.trim());

        // El nuevo ejemplar comienza disponible.
        clon.setEstado(EstadoLibro.DISPONIBLE);

        libros.add(clon);

        return clon;
    }

    /**
     * Realiza un préstamo.
     */
    public Prestamo realizarPrestamo(
            String codigoLibro,
            LocalDate fechaEstimada) {

        Libro libro =
                buscarLibroPorCodigo(codigoLibro);

        if (libro == null) {
            throw new IllegalArgumentException(
                    "El libro no existe."
            );
        }

        if (fechaEstimada == null) {
            throw new IllegalArgumentException(
                    "La fecha estimada es obligatoria."
            );
        }

        if (libro.getEstado() != EstadoLibro.DISPONIBLE) {
            throw new IllegalStateException(
                    "El libro ya se encuentra PRESTADO."
            );
        }

        libro.setEstado(EstadoLibro.PRESTADO);

        String codigoPrestamo =
                "PRES-" + (prestamos.size() + 1);

        Prestamo prestamo =
                new Prestamo(
                        codigoPrestamo,
                        libro,
                        LocalDate.now(),
                        fechaEstimada
                );

        prestamos.add(prestamo);

        return prestamo;
    }

    /**
     * La devolución.
     * La tasa se obtiene de la única instancia de ConfiguracionBiblioteca.
     */
    public double registrarDevolucion(
            String codigoPrestamo,
            LocalDate fechaReal,
            double valorBaseDia) {

        Prestamo prestamo =
                buscarPrestamoPorCodigo(codigoPrestamo);

        if (prestamo == null) {
            throw new IllegalArgumentException(
                    "El préstamo no existe."
            );
        }

        ConfiguracionBiblioteca configuracion =
                ConfiguracionBiblioteca.getInstancia();

        return prestamo.registrarDevolucion(
                fechaReal,
                configuracion.getPorcentajeMulta(),
                valorBaseDia
        );
    }

    /**
     * Busca un libro por su código.
     */
    public Libro buscarLibroPorCodigo(String codigo) {

        if (codigo == null) {
            return null;
        }

        for (Libro libro : libros) {

            if (libro.getCodigo()
                    .equalsIgnoreCase(codigo)) {

                return libro;
            }
        }

        return null;
    }

    /**
     * Busca un préstamo por su código.
     **/
    public Prestamo buscarPrestamoPorCodigo(
            String codigoPrestamo) {

        if (codigoPrestamo == null) {
            return null;
        }

        for (Prestamo prestamo : prestamos) {

            if (prestamo.getCodigoPrestamo()
                    .equalsIgnoreCase(codigoPrestamo)) {

                return prestamo;
            }
        }

        return null;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }
}