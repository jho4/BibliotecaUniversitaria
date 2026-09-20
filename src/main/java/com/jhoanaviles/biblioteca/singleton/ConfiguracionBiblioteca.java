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
     * se agrega nueva variable, para colocar valor de base diario
     */
    private double valorBaseMultaDia;

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
        /*
        *Se agrega al constructor,
        *valor para tener en cuenta
        *base de multa diaria.
        *ahora tanto el valor de 5 como 2000, es de ejemplo
         **/
        valorBaseMultaDia = 2000.0;
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

    /*
    * Se agrega para que el valor del base diaría de multa, sea persistente a todo el programa
     **/
    public double getValorBaseMultaDia() {
        return valorBaseMultaDia;
    }

    public void setValorBaseMultaDia(double valorBaseMultaDia) {
        this.valorBaseMultaDia = valorBaseMultaDia;
    }
}