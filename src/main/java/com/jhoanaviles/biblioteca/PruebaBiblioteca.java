package com.jhoanaviles.biblioteca;

import com.jhoanaviles.biblioteca.controller.MainController;
import com.jhoanaviles.biblioteca.model.EstadoLibro;
import com.jhoanaviles.biblioteca.model.Libro;
import com.jhoanaviles.biblioteca.model.Prestamo;

import java.time.LocalDate;

public class PruebaBiblioteca {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("   PRUEBA INTERNA BIBLIOTECA");
        System.out.println("======================================");

        MainController controller = new MainController();

        try {

            System.out.println("\n1. PROBANDO CONFIGURACION...");

            controller.actualizarConfiguracion(
                    "Biblioteca Universitaria",
                    "Universidad",
                    2000.0,
                    5.0
            );

            System.out.println("[OK] Configuracion actualizada.");


            System.out.println("\n2. PROBANDO BUILDER...");

            controller.registrarLibro(
                    "001",
                    "Java desde cero",
                    "Autor Java",
                    "Programacion"
            );

            controller.registrarLibro(
                    "002",
                    "Programacion II",
                    "",
                    ""
            );

            verificar(
                    controller.obtenerLibros().size() == 2,
                    "Se registraron 2 libros."
            );


            System.out.println("\n3. PROBANDO PROTOTYPE...");

            Libro clon = controller.clonarLibro(
                    "001",
                    "003",
                    "Java avanzado"
            );

            verificar(
                    clon.getCodigo().equals("003"),
                    "El clon tiene el nuevo codigo."
            );

            verificar(
                    clon.getTitulo().equals("Java avanzado"),
                    "El clon tiene el nuevo titulo."
            );

            verificar(
                    clon.getAutor().equals("Autor Java"),
                    "El clon conservo el autor."
            );

            verificar(
                    clon.getCategoria().equals("Programacion"),
                    "El clon conservo la categoria."
            );

            verificar(
                    clon.getEstado() == EstadoLibro.DISPONIBLE,
                    "El clon quedo disponible."
            );


            System.out.println("\n4. PROBANDO PRESTAMO...");

            Prestamo prestamo = controller.realizarPrestamo(
                    "001",
                    LocalDate.now().plusDays(7)
            );

            verificar(
                    prestamo.getCodigoPrestamo().equals("1"),
                    "El prestamo recibio el codigo 1."
            );

            verificar(
                    prestamo.getLibro().getEstado()
                            == EstadoLibro.PRESTADO,
                    "El libro paso a PRESTADO."
            );


            System.out.println("\n5. PROBANDO DEVOLUCION Y MULTA...");

            double multa = controller.registrarDevolucion(
                    "1",
                    LocalDate.now().plusDays(9),
                    false,
                    0
            );

            verificar(
                    prestamo.getFechaDevolucionReal() != null,
                    "La devolucion fue registrada."
            );

            verificar(
                    prestamo.getLibro().getEstado()
                            == EstadoLibro.DISPONIBLE,
                    "El libro volvio a DISPONIBLE."
            );

            verificar(
                    multa > 0,
                    "La multa fue calculada."
            );


            System.out.println("\n6. PROBANDO MODIFICACION...");

            controller.modificarLibro(
                    "002",
                    "004",
                    "Programacion II - Modificado",
                    "Nuevo autor",
                    "Ingenieria"
            );

            Libro modificado =
                    controller.buscarLibroPorCodigo("004");

            verificar(
                    modificado != null,
                    "El libro fue encontrado con el nuevo codigo."
            );

            verificar(
                    modificado.getTitulo()
                            .equals("Programacion II - Modificado"),
                    "El titulo fue modificado."
            );

            verificar(
                    modificado.getEstado()
                            == EstadoLibro.DISPONIBLE,
                    "La modificacion no cambio el estado."
            );


            System.out.println();
            System.out.println("======================================");
            System.out.println("   TODAS LAS PRUEBAS FUERON EXITOSAS");
            System.out.println("======================================");

        } catch (Exception e) {

            System.out.println();
            System.out.println("======================================");
            System.out.println("   PRUEBA FALLIDA");
            System.out.println("======================================");

            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void verificar(
            boolean condicion,
            String mensaje) {

        if (!condicion) {
            throw new AssertionError(mensaje);
        }

        System.out.println("[OK] " + mensaje);
    }
}