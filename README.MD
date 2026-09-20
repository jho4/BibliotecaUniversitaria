# Biblioteca Universitaria

Sistema de gestión de biblioteca desarrollado para la asignatura **Programación II**.
Profe, fue desarrollao en linux, Kubuntu 24.04, use openjdk 21, pero por lo que entendí, no hay incovenientes si ustedes usan sdk de Oracle Java

## Descripción

El sistema permite:

* Administrar la configuración general de la biblioteca.
* Registrar libros.
* Construir libros mediante el patrón Builder.
* Clonar libros mediante el patrón Prototype.
* Registrar préstamos.
* Registrar devoluciones.
* Calcular multas por retraso.
* Modificar la información de los libros.
* Consultar libros disponibles y prestados.
* Gestionar la aplicación mediante una interfaz gráfica JavaFX.

## Tecnologías

* Java 21
* Maven
* JavaFX
* IntelliJ IDEA
* Git y GitHub

## Patrones de diseño

El proyecto implementa los tres patrones creacionales trabajados en la asignatura:

### Singleton

Aplicado en `ConfiguracionBiblioteca`, para mantener una única instancia de la configuración general del sistema.

### Builder

Aplicado mediante `LibroBuilder`, permitiendo construir objetos `Libro` con información obligatoria y opcional.

### Prototype

Aplicado mediante `Prototipo<T>` y `Libro`, permitiendo clonar libros existentes.

## Arquitectura

El proyecto utiliza una organización basada en MVC:

* `model`: clases principales del dominio.
* `view`: interfaz gráfica desarrollada con JavaFX.
* `controller`: comunicación entre la interfaz y la lógica del sistema.
* `service`: operaciones y reglas de negocio.

## Estructura principal

```text
com.jhoanaviles.biblioteca
├── builder
│   └── LibroBuilder.java
├── controller
│   └── MainController.java
├── model
│   ├── EstadoLibro.java
│   ├── Libro.java
│   └── Prestamo.java
├── prototype
│   └── Prototipo.java
├── service
│   └── BibliotecaService.java
├── singleton
│   └── ConfiguracionBiblioteca.java
├── view
│   └── MainApp.java
└── PruebaBiblioteca.java
```

## Requisitos

Para ejecutar el proyecto se necesita:

* Java 21
* Maven

## Ejecución de la aplicación

Desde la raíz del proyecto:

```bash
mvn clean javafx:run
```

## Prueba interna

El proyecto incluye la clase:

```text
PruebaBiblioteca.java
```

Esta clase permite comprobar desde consola algunas de las operaciones principales, como:

* configuración;
* registro de libros;
* Builder;
* Prototype;
* préstamos;
* devoluciones;
* cálculo de multas;
* modificación de libros.

Puede ejecutarse directamente desde IntelliJ IDEA mediante:

**Run → PruebaBiblioteca.main()**

## Diagrama de clases

El diagrama de clases utilizado en el proyecto se encuentra en:

```text
docs/UML.svg
```
