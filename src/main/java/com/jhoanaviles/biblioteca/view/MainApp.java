package com.jhoanaviles.biblioteca.view;

import com.jhoanaviles.biblioteca.controller.MainController;
import com.jhoanaviles.biblioteca.singleton.ConfiguracionBiblioteca;
import com.jhoanaviles.biblioteca.model.Libro;
import com.jhoanaviles.biblioteca.model.Prestamo;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Vista principal de la aplicación JavaFX.
 *
 * Contiene una interfaz gráfica sencilla
 * para demostrar las funcionalidades principales.
 */
public class MainApp extends Application {

    private MainController controller;

    private ListView<String> listaLibrosView;
    private ListView<String> listaPrestamosView;

    private Label lblInfoConfig;

    @Override
    public void start(Stage primaryStage) {

        controller = new MainController();

        primaryStage.setTitle(
                "Sistema de Gestión de Biblioteca"
        );

        TabPane tabPane = new TabPane();

        tabPane.getTabs().add(
                crearTabConfiguracion()
        );

        tabPane.getTabs().add(
                crearTabLibros()
        );

        tabPane.getTabs().add(
                crearTabPrestamos()
        );

        Scene scene =
                new Scene(tabPane, 850, 550);

        primaryStage.setScene(scene);
        primaryStage.show();

        actualizarListas();
    }

    /**
     * Pestaña de configuración.
     */
    private Tab crearTabConfiguracion() {

        Tab tab =
                new Tab("Configuración");

        tab.setClosable(false);

        VBox layout =
                new VBox(10);

        layout.setPadding(
                new Insets(15)
        );

        ConfiguracionBiblioteca config =
                controller.getConfiguracion();

        lblInfoConfig =
                new Label();

        actualizarLabelConfig();

        TextField txtNombre =
                new TextField(
                        config.getNombreBiblioteca()
                );

        TextField txtDireccion =
                new TextField(
                        config.getDireccion()
                );

        TextField txtMulta =
                new TextField(
                        String.valueOf(
                                config.getPorcentajeMulta()
                        )
                );

        Button btnGuardar =
                new Button(
                        "Guardar configuración"
                );

        btnGuardar.setOnAction(event -> {

            try {

                double multa =
                        Double.parseDouble(
                                txtMulta.getText()
                        );

                controller.actualizarConfiguracion(
                        txtNombre.getText(),
                        txtDireccion.getText(),
                        multa
                );

                actualizarLabelConfig();

                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Configuración",
                        "Configuración actualizada correctamente."
                );

            } catch (Exception e) {

                mostrarAlerta(
                        Alert.AlertType.ERROR,
                        "Error",
                        e.getMessage()
                );
            }
        });

        layout.getChildren().addAll(

                new Label(
                        "Configuración general de la biblioteca"
                ),

                lblInfoConfig,

                new Label("Nombre:"),
                txtNombre,

                new Label("Dirección:"),
                txtDireccion,

                new Label("Porcentaje de multa:"),
                txtMulta,

                btnGuardar
        );

        tab.setContent(layout);

        return tab;
    }

    /**
     * Pestaña de libros.
     */
    private Tab crearTabLibros() {

        Tab tab =
                new Tab("Libros");

        tab.setClosable(false);

        HBox mainLayout =
                new HBox(15);

        mainLayout.setPadding(
                new Insets(15)
        );

        VBox formulario =
                new VBox(8);

        formulario.setPrefWidth(350);

        TextField txtCodigo =
                new TextField();

        txtCodigo.setPromptText(
                "Código obligatorio"
        );

        TextField txtTitulo =
                new TextField();

        txtTitulo.setPromptText(
                "Título obligatorio"
        );

        TextField txtAutor =
                new TextField();

        txtAutor.setPromptText(
                "Autor opcional"
        );

        TextField txtCategoria =
                new TextField();

        txtCategoria.setPromptText(
                "Categoría opcional"
        );

        Button btnRegistrar =
                new Button(
                        "Registrar libro"
                );

        btnRegistrar.setOnAction(event -> {

            try {

                controller.registrarLibro(
                        txtCodigo.getText(),
                        txtTitulo.getText(),
                        txtAutor.getText(),
                        txtCategoria.getText()
                );

                limpiarCampos(
                        txtCodigo,
                        txtTitulo,
                        txtAutor,
                        txtCategoria
                );

                actualizarListas();

                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Libro",
                        "Libro registrado correctamente."
                );

            } catch (Exception e) {

                mostrarAlerta(
                        Alert.AlertType.ERROR,
                        "Error",
                        e.getMessage()
                );
            }
        });

        Separator separator =
                new Separator();

        TextField txtCodigoOriginal =
                new TextField();

        txtCodigoOriginal.setPromptText(
                "Código del libro original"
        );

        TextField txtNuevoCodigo =
                new TextField();

        txtNuevoCodigo.setPromptText(
                "Nuevo código"
        );

        TextField txtNuevoTitulo =
                new TextField();

        txtNuevoTitulo.setPromptText(
                "Nuevo título"
        );

        Button btnClonar =
                new Button(
                        "Clonar libro"
                );

        btnClonar.setOnAction(event -> {

            try {

                controller.clonarLibro(
                        txtCodigoOriginal.getText(),
                        txtNuevoCodigo.getText(),
                        txtNuevoTitulo.getText()
                );

                txtCodigoOriginal.clear();
                txtNuevoCodigo.clear();
                txtNuevoTitulo.clear();

                actualizarListas();

                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Prototype",
                        "Libro clonado correctamente."
                );

            } catch (Exception e) {

                mostrarAlerta(
                        Alert.AlertType.ERROR,
                        "Error",
                        e.getMessage()
                );
            }
        });

        formulario.getChildren().addAll(

                new Label(
                        "Registrar libro - Builder"
                ),

                new Label("Código:"),
                txtCodigo,

                new Label("Título:"),
                txtTitulo,

                new Label("Autor:"),
                txtAutor,

                new Label("Categoría:"),
                txtCategoria,

                btnRegistrar,

                separator,

                new Label(
                        "Clonar libro - Prototype"
                ),

                new Label("Código original:"),
                txtCodigoOriginal,

                new Label("Nuevo código:"),
                txtNuevoCodigo,

                new Label("Nuevo título:"),
                txtNuevoTitulo,

                btnClonar
        );

        VBox lista =
                new VBox(8);

        listaLibrosView =
                new ListView<>();

        lista.getChildren().addAll(

                new Label(
                        "Catálogo de libros"
                ),

                listaLibrosView
        );

        mainLayout.getChildren().addAll(
                formulario,
                lista
        );

        tab.setContent(mainLayout);

        return tab;
    }

    /**
     * Pestaña de préstamos y devoluciones.
     */
    private Tab crearTabPrestamos() {

        Tab tab =
                new Tab(
                        "Préstamos y devoluciones"
                );

        tab.setClosable(false);

        HBox mainLayout =
                new HBox(15);

        mainLayout.setPadding(
                new Insets(15)
        );

        VBox formulario =
                new VBox(8);

        formulario.setPrefWidth(350);

        TextField txtCodigoLibro =
                new TextField();

        txtCodigoLibro.setPromptText(
                "Código del libro"
        );

        DatePicker fechaDevolucion =
                new DatePicker(
                        LocalDate.now().plusDays(3)
                );

        Button btnPrestar =
                new Button(
                        "Realizar préstamo"
                );

        btnPrestar.setOnAction(event -> {

            try {

                controller.realizarPrestamo(
                        txtCodigoLibro.getText(),
                        fechaDevolucion.getValue()
                );

                txtCodigoLibro.clear();

                actualizarListas();

                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Préstamo",
                        "Préstamo realizado correctamente."
                );

            } catch (Exception e) {

                mostrarAlerta(
                        Alert.AlertType.ERROR,
                        "Error",
                        e.getMessage()
                );
            }
        });

        Separator separator =
                new Separator();

        TextField txtCodigoPrestamo =
                new TextField();

        txtCodigoPrestamo.setPromptText(
                "Ejemplo: PRES-1"
        );

        DatePicker fechaReal =
                new DatePicker(
                        LocalDate.now()
                );

        TextField txtValorBase =
                new TextField();

        txtValorBase.setPromptText(
                "Valor base por día"
        );

        Button btnDevolver =
                new Button(
                        "Registrar devolución"
                );

        btnDevolver.setOnAction(event -> {

            try {

                double valorBase =
                        Double.parseDouble(
                                txtValorBase.getText()
                        );

                double multa =
                        controller.registrarDevolucion(
                                txtCodigoPrestamo.getText(),
                                fechaReal.getValue(),
                                valorBase
                        );

                txtCodigoPrestamo.clear();

                actualizarListas();

                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Devolución",
                        String.format(
                                "Libro devuelto correctamente.%nMulta: $%.2f",
                                multa
                        )
                );

            } catch (Exception e) {

                mostrarAlerta(
                        Alert.AlertType.ERROR,
                        "Error",
                        e.getMessage()
                );
            }
        });

        formulario.getChildren().addAll(

                new Label(
                        "Realizar préstamo"
                ),

                new Label("Código del libro:"),
                txtCodigoLibro,

                new Label(
                        "Fecha de devolución:"
                ),
                fechaDevolucion,

                btnPrestar,

                separator,

                new Label(
                        "Registrar devolución"
                ),

                new Label(
                        "Código del préstamo:"
                ),
                txtCodigoPrestamo,

                new Label(
                        "Fecha real:"
                ),
                fechaReal,

                new Label(
                        "Valor base por día:"
                ),
                txtValorBase,

                btnDevolver
        );

        VBox lista =
                new VBox(8);

        listaPrestamosView =
                new ListView<>();

        lista.getChildren().addAll(

                new Label(
                        "Préstamos"
                ),

                listaPrestamosView
        );

        mainLayout.getChildren().addAll(
                formulario,
                lista
        );

        tab.setContent(mainLayout);

        return tab;
    }

    /**
     * Actualiza las listas mostradas en pantalla.
     */
    private void actualizarListas() {

        if (listaLibrosView != null) {

            listaLibrosView.setItems(
                    FXCollections.observableArrayList()
            );

            for (Libro libro :
                    controller.obtenerLibros()) {

                listaLibrosView.getItems().add(
                        libro.toString()
                );
            }
        }

        if (listaPrestamosView != null) {

            listaPrestamosView.setItems(
                    FXCollections.observableArrayList()
            );

            for (Prestamo prestamo :
                    controller.obtenerPrestamos()) {

                listaPrestamosView.getItems().add(
                        prestamo.toString()
                );
            }
        }
    }

    /**
     * Actualiza el texto de la configuración.
     */
    private void actualizarLabelConfig() {

        ConfiguracionBiblioteca config =
                controller.getConfiguracion();

        lblInfoConfig.setText(

                "Actual: "
                        + config.getNombreBiblioteca()
                        + " | "
                        + config.getDireccion()
                        + " | Multa: "
                        + config.getPorcentajeMulta()
                        + "%"
        );
    }

    private void limpiarCampos(
            TextField... campos) {

        for (TextField campo : campos) {
            campo.clear();
        }
    }

    /**
     * Muestra una ventana sencilla con un mensaje.
     */
    private void mostrarAlerta(
            Alert.AlertType tipo,
            String titulo,
            String mensaje) {

        Alert alert =
                new Alert(tipo);

        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        alert.showAndWait();
    }

    public static void main(String[] args) {

        launch(args);
    }
}