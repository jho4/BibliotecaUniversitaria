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
    /*se agrega un contador para identificar los prestamos más fácilmente
    **/
    private int siguienteCodigoPrestamo = 1;
    /*
     * Registrar un nuevo libro.
     **/
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

    public void modificarLibro(
            String codigoOriginal,
            String nuevoCodigo,
            String nuevoTitulo,
            String nuevoAutor,
            String nuevaCategoria) {
        Libro libro = buscarLibroPorCodigo(codigoOriginal);

        if (libro == null) {
            throw new IllegalArgumentException(
                    "El libro no existe."
            );
        }

        if (nuevoCodigo == null ||
                nuevoCodigo.trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El código es obligatorio."
            );

        }
        Libro libroConNuevoCodigo =
                buscarLibroPorCodigo(nuevoCodigo);

        if (libroConNuevoCodigo != null &&
                libroConNuevoCodigo != libro) {

            throw new IllegalArgumentException(
                    "Ya existe otro libro con ese código."
            );
        }
        libro.setCodigo(nuevoCodigo.trim());
        libro.setTitulo(nuevoTitulo.trim());
        libro.setAutor(nuevoAutor);
        libro.setCategoria(nuevaCategoria);
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
/*
*profe, la verdad, es identificador esta suave, más bien solo número y ya
 **/
/*        String codigoPrestamo =
                "PRES-" + (prestamos.size() + 1);
            se procede a tomar el valor del contador como String, y no se cambia a int, para no modificar más código
**/

        String codigoPrestamo =
                String.valueOf(siguienteCodigoPrestamo);

        siguienteCodigoPrestamo++;

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
            boolean usarPorcentajeEspecial,
            /*no lo pienso eliminar, hasta probar
            *double valorBaseDia
             double valorBaseDia
             * */
            double porcentajeEspecial) {

        Prestamo prestamo =
                buscarPrestamoPorCodigo(codigoPrestamo);

        if (prestamo == null) {
            throw new IllegalArgumentException(
                    "El préstamo no existe."
            );
        }

        ConfiguracionBiblioteca configuracion =
                ConfiguracionBiblioteca.getInstancia();
        double porcentaje =
                configuracion.getPorcentajeMulta();

        if (usarPorcentajeEspecial) {

            if (porcentajeEspecial <= porcentaje) {
                throw new IllegalArgumentException(
                        "El porcentaje especial debe ser mayor al porcentaje general."
                );
            }

            porcentaje = porcentajeEspecial;
        }

        double valorBase = configuracion.getValorBaseMultaDia();
        return prestamo.registrarDevolucion(
                fechaReal,
                configuracion.getPorcentajeMulta(),
                valorBase
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