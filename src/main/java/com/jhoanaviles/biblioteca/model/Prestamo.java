package com.jhoanaviles.biblioteca.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Representación de préstamo libro - biblioteca.
 */
public class Prestamo {

    private String codigoPrestamo;
    private Libro libro;

    private LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionEstimada;
    private LocalDate fechaDevolucionReal;

    private double valorMulta;

    public Prestamo(
            String codigoPrestamo,
            Libro libro,
            LocalDate fechaPrestamo,
            LocalDate fechaDevolucionEstimada) {

        this.codigoPrestamo = codigoPrestamo;
        this.libro = libro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionEstimada = fechaDevolucionEstimada;
        this.valorMulta = 0.0;
    }

    /**
     * Registra la devolución del libro.
     */
    public double registrarDevolucion(
            LocalDate fechaReal,
            double porcentajeMulta,
            double valorBaseDia) {

        if (fechaReal == null) {
            throw new IllegalArgumentException(
                    "La fecha de devolución es obligatoria."
            );
        }

        if (fechaDevolucionReal != null) {
            throw new IllegalStateException(
                    "Este préstamo ya fue devuelto."
            );
        }

        fechaDevolucionReal = fechaReal;

        valorMulta = calcularMulta(
                porcentajeMulta,
                valorBaseDia
        );

        libro.setEstado(EstadoLibro.DISPONIBLE);

        return valorMulta;
    }

    /**
     * Calcula la multa cuando existe retraso.
     *
     * Fórmula utilizada en nuestra propuesta:
     *
     * días de retraso
     * × valor base por día
     * × porcentaje de multa / 100
     */
    public double calcularMulta(
            double porcentajeMulta,
            double valorBaseDia) {

        if (fechaDevolucionReal == null) {
            return 0.0;
        }

        if (!fechaDevolucionReal.isAfter(fechaDevolucionEstimada)) {
            return 0.0;
        }

        long diasRetraso =
                ChronoUnit.DAYS.between(
                        fechaDevolucionEstimada,
                        fechaDevolucionReal
                );

        return diasRetraso
                * valorBaseDia
                * (porcentajeMulta / 100.0);
    }

    public String getCodigoPrestamo() {
        return codigoPrestamo;
    }

    public Libro getLibro() {
        return libro;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDate getFechaDevolucionEstimada() {
        return fechaDevolucionEstimada;
    }

    public LocalDate getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public double getValorMulta() {
        return valorMulta;
    }

    @Override
    public String toString() {

        String estado;

        if (fechaDevolucionReal == null) {
            estado = "PENDIENTE";
        } else {
            estado = "DEVUELTO";
        }

        return "[" + codigoPrestamo + "] "
                + libro.getTitulo()
                + " | Fecha límite: "
                + fechaDevolucionEstimada
                + " | Estado: "
                + estado
                + " | Multa: $"
                + String.format("%.2f", valorMulta);
    }
}