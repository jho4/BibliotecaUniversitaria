package com.jhoanaviles.biblioteca.controller;

import com.jhoanaviles.biblioteca.builder.LibroBuilder;
import com.jhoanaviles.biblioteca.model.Libro;
import com.jhoanaviles.biblioteca.model.Prestamo;
import com.jhoanaviles.biblioteca.service.BibliotecaService;
import com.jhoanaviles.biblioteca.singleton.ConfiguracionBiblioteca;

import java.time.LocalDate;
import java.util.List;

/**
 * Controlador de la aplicación.
 * Es el puente entre la interfaz gráfica
 * y la lógica de la biblioteca, criterio mvc
 **/
public class MainController {

    private final BibliotecaService service;

    public MainController() {
        service = new BibliotecaService();
    }

    public ConfiguracionBiblioteca getConfiguracion() {

        return ConfiguracionBiblioteca.getInstancia();
    }

    public void actualizarConfiguracion(
            String nombre,
            String direccion,
            double valorBaseMultaDia,
            double porcentajeMulta) {

        ConfiguracionBiblioteca configuracion =
                getConfiguracion();

        configuracion.setNombreBiblioteca(nombre);
        configuracion.setDireccion(direccion);
        configuracion.setPorcentajeMulta(porcentajeMulta);
        configuracion.setValorBaseMultaDia(valorBaseMultaDia);

    }

    /**
     * Crea un libro utilizando Builder.
     **/
    public void registrarLibro(
            String codigo,
            String titulo,
            String autor,
            String categoria) {

        Libro libro =
                new LibroBuilder()
                        .conCodigo(codigo)
                        .conTitulo(titulo)
                        .conAutor(autor)
                        .conCategoria(categoria)
                        .build();

        service.registrarLibro(libro);
    }

    public void modificarLibro(
            String codigoOriginal,
            String nuevoCodigo,
            String nuevoTitulo,
            String nuevoAutor,
            String nuevaCategoria) {

        service.modificarLibro(
                codigoOriginal,
                nuevoCodigo,
                nuevoTitulo,
                nuevoAutor,
                nuevaCategoria
        );
    }

    /**
     * Clona un libro utilizando Prototype.
     **/
    public Libro clonarLibro(
            String codigoOriginal,
            String nuevoCodigo,
            String nuevoTitulo) {

        return service.clonarLibro(
                codigoOriginal,
                nuevoCodigo,
                nuevoTitulo
        );
    }

    /**
     * Realiza un préstamo.
     **/
    public Prestamo realizarPrestamo(
            String codigoLibro,
            LocalDate fechaEstimada) {

        return service.realizarPrestamo(
                codigoLibro,
                fechaEstimada
        );
    }

    /**
     * Registra una devolución.
     **/
    public double registrarDevolucion(
            String codigoPrestamo,
            LocalDate fechaReal,
            boolean usarPorcentajeEspecial,
            double porcentajeEspecial) {

        return service.registrarDevolucion(
                codigoPrestamo,
                fechaReal,
                usarPorcentajeEspecial,
                porcentajeEspecial
        );
    }

    public List<Libro> obtenerLibros() {

        return service.getLibros();
    }

    public List<Prestamo> obtenerPrestamos() {

        return service.getPrestamos();
    }
    public Libro buscarLibroPorCodigo(String codigo) {
        return service.buscarLibroPorCodigo(codigo);
    }
}