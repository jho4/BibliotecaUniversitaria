package com.jhoanaviles.biblioteca.singleton;

/**
 * Patrón Singleton.
 * Una única instancia compartida de la configuración de la biblioteca.
 */
public class ConfiguracionBiblioteca {

    private static ConfiguracionBiblioteca instancia;

    private String nombreBiblioteca;
    private String direccion;
    private double porcentajeMulta;

    /**
     * Constructor privado.
     * Evita que otras clases hagan:
     *
     * new ConfiguracionBiblioteca()
     */
    private ConfiguracionBiblioteca() {

        nombreBiblioteca = "Biblioteca Universitaria";
        direccion = "Dirección no especificada";
        porcentajeMulta = 5.0;
    }

    /**
     * Devuelve la única instancia de la configuración.
     */
    public static ConfiguracionBiblioteca getInstancia() {

        if (instancia == null) {
            instancia = new ConfiguracionBiblioteca();
        }

        return instancia;
    }

    public String getNombreBiblioteca() {
        return nombreBiblioteca;
    }

    public void setNombreBiblioteca(String nombreBiblioteca) {
        this.nombreBiblioteca = nombreBiblioteca;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public double getPorcentajeMulta() {
        return porcentajeMulta;
    }

    public void setPorcentajeMulta(double porcentajeMulta) {
        this.porcentajeMulta = porcentajeMulta;
    }
}