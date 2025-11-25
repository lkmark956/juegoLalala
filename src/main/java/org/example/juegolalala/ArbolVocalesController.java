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

public class ArbolVocalesController {
    
    @FXML
    private Pane rootPane;
    
    @FXML
    private ImageView letraA;
    
    @FXML
    private ImageView letraE;
    
    @FXML
    private ImageView letraI;
    
    @FXML
    private ImageView letraO;
    
    @FXML
    private ImageView letraU;
    
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
    
    private ImageView letraSeleccionada = null;
    private final Map<ImageView, ImageView> letraASombra = new HashMap<>();
    private final Map<ImageView, Boolean> letrasColocadas = new HashMap<>();
    private final DropShadow efectoSeleccion = new DropShadow();
    
    @FXML
    public void initialize() {
        // Configurar efecto de selección
        efectoSeleccion.setColor(Color.YELLOW);
        efectoSeleccion.setRadius(20);
        efectoSeleccion.setSpread(0.7);
        
        // Mapear cada letra con su sombra correspondiente
        letraASombra.put(letraA, sombra1);
        letraASombra.put(letraE, sombra2);
        letraASombra.put(letraI, sombra3);
        letraASombra.put(letraO, sombra4);
        letraASombra.put(letraU, sombra5);
        
        // Inicializar estado de letras
        letrasColocadas.put(letraA, false);
        letrasColocadas.put(letraE, false);
        letrasColocadas.put(letraI, false);
        letrasColocadas.put(letraO, false);
        letrasColocadas.put(letraU, false);
        
        // Configurar eventos de clic para cada letra
        configurarSeleccion(letraA);
        configurarSeleccion(letraE);
        configurarSeleccion(letraI);
        configurarSeleccion(letraO);
        configurarSeleccion(letraU);
        
        // Configurar eventos de clic para cada sombra
        configurarSombra(sombra1);
        configurarSombra(sombra2);
        configurarSombra(sombra3);
        configurarSombra(sombra4);
        configurarSombra(sombra5);
    }
    
    private void configurarSeleccion(ImageView letra) {
        letra.setOnMouseClicked(event -> {
            // Solo permitir seleccionar si no está ya colocada
            if (!letrasColocadas.get(letra)) {
                // Deseleccionar la anterior
                if (letraSeleccionada != null) {
                    letraSeleccionada.setEffect(null);
                }
                
                // Seleccionar la nueva
                letraSeleccionada = letra;
                letraSeleccionada.setEffect(efectoSeleccion);
            }
        });
        letra.setStyle("-fx-cursor: hand;");
    }
    
    private void configurarSombra(ImageView sombra) {
        sombra.setOnMouseClicked(event -> {
            if (letraSeleccionada != null) {
                // Verificar si la sombra corresponde a la letra seleccionada
                ImageView sombraCorrecta = letraASombra.get(letraSeleccionada);
                
                if (sombra == sombraCorrecta) {
                    // ¡Correcto! Colocar la letra sobre la sombra
                    letraSeleccionada.setLayoutX(sombra.getLayoutX());
                    letraSeleccionada.setLayoutY(sombra.getLayoutY());
                    letraSeleccionada.setFitWidth(sombra.getFitWidth());
                    letraSeleccionada.setFitHeight(sombra.getFitHeight());
                    
                    // Remover efecto de selección
                    letraSeleccionada.setEffect(null);
                    
                    // Marcar como colocada
                    letrasColocadas.put(letraSeleccionada, true);
                    letraSeleccionada.setStyle("-fx-cursor: default;");
                    
                    // Deseleccionar
                    letraSeleccionada = null;
                    
                    // Verificar si todas las letras están colocadas
                    verificarJuegoCompletado();
                } else {
                    // Incorrecto - hacer un efecto de "error"
                    Glow errorEffect = new Glow(0.8);
                    sombra.setEffect(errorEffect);
                    
                    // Remover el efecto después de 500ms
                    new Thread(() -> {
                        try {
                            Thread.sleep(500);
                            javafx.application.Platform.runLater(() -> sombra.setEffect(null));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();
                }
            }
        });
        sombra.setStyle("-fx-cursor: hand;");
        sombra.setOpacity(0.3);
        sombra.setPickOnBounds(true);
    }
    
    private void verificarJuegoCompletado() {
        boolean todasColocadas = letrasColocadas.values().stream().allMatch(colocada -> colocada);
        
        if (todasColocadas) {
            // Iniciar contador de 5 segundos invisible
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
                mostrarMensajeVictoria();
            }));
            timeline.play();
        }
    }
    
    private void mostrarMensajeVictoria() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("victoria-view.fxml"));
            Pane mensajePane = loader.load();
            
            // Agregar el mensaje encima del contenido actual
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
