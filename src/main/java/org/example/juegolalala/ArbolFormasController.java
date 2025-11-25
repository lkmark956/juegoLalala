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

    // FIGURAS
    @FXML private ImageView circulo;
    @FXML private ImageView corazon;
    @FXML private ImageView cuadrado;
    @FXML private ImageView estrella;
    @FXML private ImageView triangulo;

    // SOMBRAS
    @FXML private ImageView sombra1; // circulo
    @FXML private ImageView sombra2; // corazon
    @FXML private ImageView sombra3; // cuadrado
    @FXML private ImageView sombra4; // estrella
    @FXML private ImageView sombra5; // triangulo

    private ImageView formaSeleccionada = null;

    private final Map<ImageView, ImageView> formaASombra = new HashMap<>();
    private final Map<ImageView, Boolean> formasColocadas = new HashMap<>();

    private final DropShadow efectoSeleccion = new DropShadow();

    @FXML
    public void initialize() {

        // Efecto visual al seleccionar
        efectoSeleccion.setColor(Color.YELLOW);
        efectoSeleccion.setRadius(20);
        efectoSeleccion.setSpread(0.7);

        // Mapear figuras → sombras correctas
        formaASombra.put(circulo, sombra1);
        formaASombra.put(corazon, sombra2);
        formaASombra.put(cuadrado, sombra3);
        formaASombra.put(estrella, sombra4);
        formaASombra.put(triangulo, sombra5);

        // Estado inicial (ninguna colocada)
        formasColocadas.put(circulo, false);
        formasColocadas.put(corazon, false);
        formasColocadas.put(cuadrado, false);
        formasColocadas.put(estrella, false);
        formasColocadas.put(triangulo, false);

        // Configurar clic en formas
        configurarSeleccion(circulo);
        configurarSeleccion(corazon);
        configurarSeleccion(cuadrado);
        configurarSeleccion(estrella);
        configurarSeleccion(triangulo);

        // Configurar clic en sombras (por si se quiere usar)
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

    // -------------------------------
    // 🔵 Lógica unificada para sombras
    // -------------------------------

    private void manejarClickEnSombra(ImageView sombra) {

        if (formaSeleccionada == null)
            return;

        ImageView sombraCorrecta = formaASombra.get(formaSeleccionada);

        if (sombra == sombraCorrecta) {

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


    // ----------------------------
    // 🔵 Métodos llamados por FXML
    // ----------------------------

    @FXML private void clickSombra1() { manejarClickEnSombra(sombra1); }
    @FXML private void clickSombra2() { manejarClickEnSombra(sombra2); }
    @FXML private void clickSombra3() { manejarClickEnSombra(sombra3); }
    @FXML private void clickSombra4() { manejarClickEnSombra(sombra4); }
    @FXML private void clickSombra5() { manejarClickEnSombra(sombra5); }


    private void verificarJuegoCompletado() {
        boolean completo = formasColocadas.values().stream().allMatch(v -> v);

        if (completo) {
            Timeline t = new Timeline(new KeyFrame(Duration.seconds(3), e -> mostrarMensajeVictoria()));
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
