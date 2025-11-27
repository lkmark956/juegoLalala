package org.example.juegolalala;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ArbolFormasController {

    @FXML
    private Pane rootPane;

    @FXML
    private ImageView cuadrado;

    @FXML
    private ImageView circulo;

    @FXML
    private ImageView estrella;

    @FXML
    private ImageView triangulo;

    @FXML
    private ImageView corazon;

    @FXML
    private ImageView sombra1;

    @FXML
    private ImageView sombra2;

    @FXML
    private ImageView sombra3;

    @FXML
    private ImageView sombra4;

    @FXML
    private ImageView sombra5;

    private ImageView formaSeleccionada = null;
    private final Map<ImageView, ImageView> formaASombra = new HashMap<>();
    private final Map<ImageView, Boolean> formasColocadas = new HashMap<>();
    private final DropShadow efectoSeleccion = new DropShadow();

    @FXML
    public void initialize() {
        // Reproducir música del minijuego
        MusicManager.playMusic("sounds/background_music/Cosmic Christmas Lights.mp3");
        
        // Configurar efecto de selección
        efectoSeleccion.setColor(Color.YELLOW);
        efectoSeleccion.setRadius(20);
        efectoSeleccion.setSpread(0.7);

        // Mapear cada forma con su sombra correspondiente
        formaASombra.put(cuadrado, sombra1);
        formaASombra.put(circulo, sombra2);
        formaASombra.put(estrella, sombra3);
        formaASombra.put(triangulo, sombra4);
        formaASombra.put(corazon, sombra5);

        // Inicializar estado de formas
        formasColocadas.put(cuadrado, false);
        formasColocadas.put(circulo, false);
        formasColocadas.put(estrella, false);
        formasColocadas.put(triangulo, false);
        formasColocadas.put(corazon, false);

        // Configurar eventos de selección de formas
        configurarSeleccion(cuadrado);
        configurarSeleccion(circulo);
        configurarSeleccion(estrella);
        configurarSeleccion(triangulo);
        configurarSeleccion(corazon);

        // Configurar eventos directos sobre sombras (opcional)
        configurarSombra(sombra1);
        configurarSombra(sombra2);
        configurarSombra(sombra3);
        configurarSombra(sombra4);
        configurarSombra(sombra5);
    }

    private void configurarSeleccion(ImageView forma) {
        forma.setOnMouseClicked(event -> {
            if (!formasColocadas.get(forma)) {

                if (formaSeleccionada != null)
                    formaSeleccionada.setEffect(null);

                formaSeleccionada = forma;
                formaSeleccionada.setEffect(efectoSeleccion);
            }
        });

        forma.setStyle("-fx-cursor: hand;");
    }


    private void configurarSombra(ImageView sombra) {
        sombra.setOnMouseClicked(event -> manejarClickEnSombra(sombra));
        sombra.setStyle("-fx-cursor: hand;");
        sombra.setOpacity(0.3);
        sombra.setPickOnBounds(true);
    }

    // 🔵 LÓGICA CENTRALIZADA DE CLIC EN SOMBRA (botón invisible o sombra real)
    private void manejarClickEnSombra(ImageView sombra) {

        if (formaSeleccionada != null) {

            ImageView sombraCorrecta = formaASombra.get(formaSeleccionada);

            if (sombra == sombraCorrecta) {
                // ¡Correcto! Reproducir sonido de acierto
                SoundEffectManager.playCorrectSound();
                
                // Colocar forma correctamente
                formaSeleccionada.setLayoutX(sombra.getLayoutX());
                formaSeleccionada.setLayoutY(sombra.getLayoutY());
                formaSeleccionada.setFitWidth(sombra.getFitWidth());
                formaSeleccionada.setFitHeight(sombra.getFitHeight());

                formaSeleccionada.setEffect(null);
                formasColocadas.put(formaSeleccionada, true);
                formaSeleccionada.setStyle("-fx-cursor: default;");
                formaSeleccionada = null;

                verificarJuegoCompletado();

            } else {
                // Incorrecto - reproducir sonido de error y hacer efecto visual
                SoundEffectManager.playErrorSound();
                
                Glow glow = new Glow(0.8);
                sombra.setEffect(glow);

                new Thread(() -> {
                    try {
                        Thread.sleep(500);
                        javafx.application.Platform.runLater(() -> sombra.setEffect(null));
                    } catch (Exception e) {}
                }).start();
            }
        }
    }


    // 🔵 Botones invisibles en FXML llaman a estos métodos:
    @FXML private void clickSombra1() { manejarClickEnSombra(sombra1); }
    @FXML private void clickSombra2() { manejarClickEnSombra(sombra2); }
    @FXML private void clickSombra3() { manejarClickEnSombra(sombra3); }
    @FXML private void clickSombra4() { manejarClickEnSombra(sombra4); }
    @FXML private void clickSombra5() { manejarClickEnSombra(sombra5); }

    private void verificarJuegoCompletado() {
        boolean todas = formasColocadas.values().stream().allMatch(v -> v);

        if (todas) {
            // Esperar 3 segundos antes de mostrar victoria
            Timeline t = new Timeline(
                new KeyFrame(Duration.seconds(3), e -> mostrarMensajeVictoria())
            );
            t.play();
        }
    }

    private void mostrarMensajeVictoria() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("victoria-view.fxml"));
            Pane mensajePane = loader.load();
            rootPane.getChildren().add(mensajePane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void volverAlMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("menu-view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setTitle("Menú Principal");
            stage.setScene(scene);
            // La música del menú se cambiará automáticamente en MenuController.initialize()
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
