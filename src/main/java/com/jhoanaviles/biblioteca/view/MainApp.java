package com.jhoanaviles.biblioteca.view;

import com.jhoanaviles.biblioteca.controller.MainController;
import com.jhoanaviles.biblioteca.model.EstadoLibro;
import com.jhoanaviles.biblioteca.model.Libro;
import com.jhoanaviles.biblioteca.model.Prestamo;
import com.jhoanaviles.biblioteca.singleton.ConfiguracionBiblioteca;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

/**
 * Vista principal de la aplicación.
 *
 * Utiliza JavaFX y forma parte de la arquitectura MVC.
 */
public class MainApp extends Application {

    private MainController controller;

    // Lista de registros realizados durante la sesión.
    private ListView<String> listaRegistroView;

    // Listas de libros separadas por estado.
    private ListView<String> listaDisponiblesView;
    private ListView<String> listaPrestadosView;

    // Información de configuración.
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
                crearTabRegistro()
        );

        tabPane.getTabs().add(
                crearTabListaLibros()
        );

        tabPane.getTabs().add(
                crearTabModificacion()
        );

        tabPane.getTabs().add(
                crearTabPrestamos()
        );

        Scene scene =
                new Scene(tabPane, 950, 650);

        primaryStage.setScene(scene);
        primaryStage.show();

        actualizarTodasLasListas();
    }

    /**
     * Crea un ScrollPane para una sección de la interfaz.
     */
    private ScrollPane crearScrollPane(VBox contenido) {

        ScrollPane scrollPane =
                new ScrollPane(contenido);

        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);

        scrollPane.setHbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        scrollPane.setVbarPolicy(
                ScrollPane.ScrollBarPolicy.AS_NEEDED
        );

        return scrollPane;
    }

    /**
     * Pestaña de configuración general.
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

        TextField txtPorcentaje =
                new TextField(
                        String.valueOf(
                                config.getPorcentajeMulta()
                        )
                );

        TextField txtValorBase =
                new TextField(
                        String.valueOf(
                                config.getValorBaseMultaDia()
                        )
                );

        Button btnGuardar =
                new Button(
                        "Guardar configuración"
                );

        btnGuardar.setOnAction(event -> {

            try {

                double porcentaje =
                        Double.parseDouble(
                                txtPorcentaje.getText()
                        );

                double valorBase =
                        Double.parseDouble(
                                txtValorBase.getText()
                        );

                if (porcentaje < 0) {
                    throw new IllegalArgumentException(
                            "El porcentaje no puede ser negativo."
                    );
                }

                if (valorBase < 0) {
                    throw new IllegalArgumentException(
                            "El valor base no puede ser negativo."
                    );
                }

                controller.actualizarConfiguracion(
                        txtNombre.getText(),
                        txtDireccion.getText(),
                        valorBase,
                        porcentaje
                );

                actualizarLabelConfig();

                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Configuración",
                        "La configuración fue actualizada correctamente."
                );

            } catch (NumberFormatException e) {

                mostrarAlerta(
                        Alert.AlertType.ERROR,
                        "Error",
                        "El porcentaje y el valor base deben ser números."
                );

            } catch (Exception e) {

                mostrarAlerta(
                        Alert.AlertType.ERROR,
                        "Error",
                        e.getMessage()
                );
            }
        });

        Label lblFormula =
                new Label(
                        "Información sobre la multa\n\n"
                                + "Fórmula:\n"
                                + "Multa = días de retraso × valor base diario "
                                + "× (porcentaje / 100)\n\n"
                                + "El porcentaje general y el valor base diario "
                                + "configurados se aplican normalmente a todos "
                                + "los préstamos con devolución tardía.\n\n"
                                + "Durante una devolución se puede activar un "
                                + "porcentaje especial. En ese caso, dicho "
                                + "porcentaje solo afecta esa devolución y no "
                                + "modifica la configuración general."
                );

        lblFormula.setWrapText(true);

        layout.getChildren().addAll(

                new Label(
                        "Configuración general de la biblioteca"
                ),

                lblInfoConfig,

                new Label("Nombre de la biblioteca:"),
                txtNombre,

                new Label("Dirección:"),
                txtDireccion,

                new Label("Porcentaje de multa general (%):"),
                txtPorcentaje,

                new Label("Valor base de multa por día:"),
                txtValorBase,

                btnGuardar,

                new Separator(),

                lblFormula
        );

        tab.setContent(
                crearScrollPane(layout)
        );

        return tab;
    }

    /**
     * Pestaña para registrar libros y utilizar Builder.
     */
    private Tab crearTabRegistro() {

        Tab tab =
                new Tab("Registro");

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

                actualizarTodasLasListas();

                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Registro",
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

                actualizarTodasLasListas();

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

        listaRegistroView =
                new ListView<>();

        lista.getChildren().addAll(

                new Label(
                        "Lista de registro por sesión"
                ),

                listaRegistroView
        );

        mainLayout.getChildren().addAll(
                formulario,
                lista
        );

        VBox contenido =
                new VBox(mainLayout);

        tab.setContent(
                crearScrollPane(contenido)
        );

        return tab;
    }

    /**
     * Pestaña que muestra los libros separados
     * entre disponibles y prestados.
     */
    private Tab crearTabListaLibros() {

        Tab tab =
                new Tab("Lista Libros");

        tab.setClosable(false);

        VBox layout =
                new VBox(12);

        layout.setPadding(
                new Insets(15)
        );

        Button btnActualizar =
                new Button(
                        "Actualizar lista"
                );

        btnActualizar.setOnAction(
                event -> actualizarListaLibros()
        );

        listaDisponiblesView =
                new ListView<>();

        listaPrestadosView =
                new ListView<>();

        layout.getChildren().addAll(

                new Label(
                        "Lista de libros"
                ),

                btnActualizar,

                new Label(
                        "LIBROS DISPONIBLES"
                ),

                listaDisponiblesView,

                new Separator(),

                new Label(
                        "LIBROS PRESTADOS"
                ),

                listaPrestadosView
        );

        tab.setContent(
                crearScrollPane(layout)
        );

        return tab;
    }

    /**
     * Pestaña para modificar datos de un libro.
     *
     * El estado no se modifica manualmente.
     */
    private Tab crearTabModificacion() {

        Tab tab =
                new Tab("Modificación");

        tab.setClosable(false);

        VBox layout =
                new VBox(10);

        layout.setPadding(
                new Insets(15)
        );

        TextField txtCodigoActual =
                new TextField();

        txtCodigoActual.setPromptText(
                "Código actual del libro"
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

        TextField txtNuevoAutor =
                new TextField();

        txtNuevoAutor.setPromptText(
                "Nuevo autor"
        );

        TextField txtNuevaCategoria =
                new TextField();

        txtNuevaCategoria.setPromptText(
                "Nueva categoría"
        );

        Button btnModificar =
                new Button(
                        "Guardar cambios"
                );

        btnModificar.setOnAction(event -> {

            try {

                controller.modificarLibro(
                        txtCodigoActual.getText(),
                        txtNuevoCodigo.getText(),
                        txtNuevoTitulo.getText(),
                        txtNuevoAutor.getText(),
                        txtNuevaCategoria.getText()
                );

                txtCodigoActual.clear();
                txtNuevoCodigo.clear();
                txtNuevoTitulo.clear();
                txtNuevoAutor.clear();
                txtNuevaCategoria.clear();

                actualizarTodasLasListas();

                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Modificación",
                        "Los datos del libro fueron actualizados."
                );

            } catch (Exception e) {

                mostrarAlerta(
                        Alert.AlertType.ERROR,
                        "Error",
                        e.getMessage()
                );
            }
        });

        Label lblAviso =
                new Label(
                        "Nota: el estado del libro no se modifica "
                                + "manualmente. El sistema lo cambia "
                                + "automáticamente mediante el préstamo "
                                + "y la devolución."
                );

        lblAviso.setWrapText(true);

        layout.getChildren().addAll(

                new Label(
                        "Modificar datos de un libro"
                ),

                new Label(
                        "Código actual:"
                ),
                txtCodigoActual,

                new Label(
                        "Nuevo código:"
                ),
                txtNuevoCodigo,

                new Label(
                        "Nuevo título:"
                ),
                txtNuevoTitulo,

                new Label(
                        "Nuevo autor:"
                ),
                txtNuevoAutor,

                new Label(
                        "Nueva categoría:"
                ),
                txtNuevaCategoria,

                btnModificar,

                new Separator(),

                lblAviso
        );

        tab.setContent(
                crearScrollPane(layout)
        );

        return tab;
    }

    /**
     * Pestaña de préstamos y devoluciones.
     */
    private Tab crearTabPrestamos() {

        Tab tab =
                new Tab(
                        "Préstamos"
                );

        tab.setClosable(false);

        VBox layout =
                new VBox(10);

        layout.setPadding(
                new Insets(15)
        );

        // --------------------------
        // REALIZAR PRÉSTAMO
        // --------------------------

        TextField txtCodigoLibro =
                new TextField();

        txtCodigoLibro.setPromptText(
                "Código del libro"
        );

        DatePicker fechaEstimada =
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
                        fechaEstimada.getValue()
                );

                txtCodigoLibro.clear();

                actualizarTodasLasListas();

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

        // --------------------------
        // DEVOLUCIÓN
        // --------------------------

        Separator separator =
                new Separator();

        TextField txtCodigoPrestamo =
                new TextField();

        txtCodigoPrestamo.setPromptText(
                "Número del préstamo"
        );

        DatePicker fechaReal =
                new DatePicker(
                        LocalDate.now()
                );

        CheckBox checkEspecial =
                new CheckBox(
                        "Aplicar porcentaje especial"
                );

        TextField txtPorcentajeEspecial =
                new TextField();

        txtPorcentajeEspecial.setPromptText(
                "Porcentaje especial (%)"
        );

        txtPorcentajeEspecial.setDisable(
                true
        );

        checkEspecial.setOnAction(
                event -> txtPorcentajeEspecial.setDisable(
                        !checkEspecial.isSelected()
                )
        );

        Label lblConfigMulta =
                new Label();

        actualizarInfoMulta(
                lblConfigMulta
        );

        Button btnDevolver =
                new Button(
                        "Registrar devolución"
                );

        btnDevolver.setOnAction(event -> {

            try {

                boolean usarEspecial =
                        checkEspecial.isSelected();

                double porcentajeEspecial = 0.0;

                if (usarEspecial) {

                    porcentajeEspecial =
                            Double.parseDouble(
                                    txtPorcentajeEspecial.getText()
                            );
                }

                double multa =
                        controller.registrarDevolucion(
                                txtCodigoPrestamo.getText(),
                                fechaReal.getValue(),
                                usarEspecial,
                                porcentajeEspecial
                        );

                txtCodigoPrestamo.clear();
                txtPorcentajeEspecial.clear();

                checkEspecial.setSelected(false);
                txtPorcentajeEspecial.setDisable(true);

                actualizarTodasLasListas();

                mostrarAlerta(
                        Alert.AlertType.INFORMATION,
                        "Devolución",
                        String.format(
                                "Libro devuelto correctamente.%n"
                                        + "Multa calculada: $%.2f",
                                multa
                        )
                );

                actualizarInfoMulta(
                        lblConfigMulta
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
                        "REALIZAR PRÉSTAMO"
                ),

                new Label(
                        "Código del libro:"
                ),
                txtCodigoLibro,

                new Label(
                        "Fecha de devolución:"
                ),
                fechaEstimada,

                btnPrestar,

                separator,

                new Label(
                        "REGISTRAR DEVOLUCIÓN"
                ),

                new Label(
                        "Número del préstamo:"
                ),
                txtCodigoPrestamo,

                new Label(
                        "Fecha real de devolución:"
                ),
                fechaReal,

                lblConfigMulta,

                checkEspecial,

                new Label(
                        "Porcentaje especial:"
                ),
                txtPorcentajeEspecial,

                btnDevolver
        );

        tab.setContent(
                crearScrollPane(layout)
        );

        return tab;
    }

    /**
     * Actualiza la información global de multa.
     */
    private void actualizarInfoMulta(
            Label label) {

        ConfiguracionBiblioteca config =
                controller.getConfiguracion();

        label.setText(
                "Configuración general actual:\n"
                        + "Porcentaje: "
                        + config.getPorcentajeMulta()
                        + "%\n"
                        + "Valor base diario: $"
                        + String.format(
                        "%.2f",
                        config.getValorBaseMultaDia()
                )
        );
    }

    /**
     * Actualiza el texto de configuración.
     */
    private void actualizarLabelConfig() {

        if (lblInfoConfig == null) {
            return;
        }

        ConfiguracionBiblioteca config =
                controller.getConfiguracion();

        lblInfoConfig.setText(

                "Configuración actual:\n"
                        + "Biblioteca: "
                        + config.getNombreBiblioteca()
                        + "\nDirección: "
                        + config.getDireccion()
                        + "\nPorcentaje general: "
                        + config.getPorcentajeMulta()
                        + "%"
                        + "\nValor base diario: $"
                        + String.format(
                        "%.2f",
                        config.getValorBaseMultaDia()
                )
        );
    }

    /**
     * Actualiza la lista de registros de la sesión.
     */
    private void actualizarListaRegistro() {

        if (listaRegistroView == null) {
            return;
        }

        listaRegistroView.setItems(
                FXCollections.observableArrayList()
        );

        for (Libro libro :
                controller.obtenerLibros()) {

            listaRegistroView.getItems().add(
                    libro.toString()
            );
        }
    }

    /**
     * Actualiza la lista de libros disponibles
     * y prestados.
     */
    private void actualizarListaLibros() {

        javafx.collections.ObservableList<String> disponibles =
                javafx.collections.FXCollections.observableArrayList();

        javafx.collections.ObservableList<String> prestados =
                javafx.collections.FXCollections.observableArrayList();

        for (Libro libro : controller.obtenerLibros()) {

            if (libro.getEstado() == EstadoLibro.DISPONIBLE) {

                // Se conserva toda la información descriptiva del libro
                disponibles.add(libro.toString());

            } else {

                for (Prestamo prestamo : controller.obtenerPrestamos()) {

                    if (prestamo.getLibro() == libro
                            && prestamo.getFechaDevolucionReal() == null) {

                        // Se muestra el código del préstamo
                        // junto con toda la información del libro
                        prestados.add(
                                "Código préstamo: ["
                                        + prestamo.getCodigoPrestamo()
                                        + "] | "
                                        + libro.toString()
                        );

                        break;
                    }
                }
            }
        }

        listaDisponiblesView.setItems(disponibles);
        listaPrestadosView.setItems(prestados);
    }

    /**
     * Actualiza todas las listas de la interfaz.
     */
    private void actualizarTodasLasListas() {

        actualizarListaRegistro();
        actualizarListaLibros();
    }

    /**
     * Limpia varios campos de texto.
     */
    private void limpiarCampos(
            TextField... campos) {

        for (TextField campo : campos) {
            campo.clear();
        }
    }

    /**
     * Muestra un mensaje.
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