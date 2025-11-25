package org.example.juegolalala;

import java.io.IOException;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MenuController {
    
    @FXML
    private ImageView lunaNumeros;
    
    @FXML
    private ImageView lunaVocales;
    
    @FXML
    private ImageView lunaAnimales;
    
    @FXML
    private ImageView lunaFormas;
    
    @FXML
    private ImageView lunaNavidad;
    
    @FXML
    public void initialize() {
        // Reproducir música del menú principal
        MusicManager.playMusic("sounds/background_music/Cosmic Christmas Lights 2.mp3");
        
        // Animar cada luna con un movimiento vertical simple
        animarLuna(lunaNumeros, 20, 2.5);
        animarLuna(lunaVocales, 25, 3.0);
        animarLuna(lunaAnimales, 18, 2.8);
        animarLuna(lunaFormas, 22, 3.2);
        animarLuna(lunaNavidad, 20, 2.6);
    }
    
    private void animarLuna(ImageView luna, double distancia, double duracion) {
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(luna);
        transition.setDuration(Duration.seconds(duracion));
        transition.setByY(distancia);
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        transition.setAutoReverse(true);
        transition.play();
    }
    
    @FXML
    private void abrirMinijuegoNumeros() {
        SoundEffectManager.playCorrectSound();
        abrirMinijuego("arbol-numeros-view.fxml", "Árbol de Números");
    }
    
    @FXML
    private void abrirMinijuegoVocales() {
        SoundEffectManager.playCorrectSound();
        abrirMinijuego("arbol-vocales-view.fxml", "Árbol de Vocales");
    }
    
    @FXML
    private void abrirMinijuegoAnimales() {
        SoundEffectManager.playCorrectSound();
        abrirMinijuego("arbol-animales-view.fxml", "Árbol de animales");
    }
    
    @FXML
    private void abrirMinijuegoFormas() {
        SoundEffectManager.playCorrectSound();
        abrirMinijuego("arbol-formas-view.fxml", "Árbol de Formas");
    }
    
    @FXML
    private void abrirMinijuegoNavidad() {
        SoundEffectManager.playCorrectSound();
        abrirMinijuego("arbol-navidad-view.fxml", "Árbol de Navidad");
    }
    
    private void abrirMinijuego(String fxmlFile, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) lunaNumeros.getScene().getWindow();
            stage.setTitle(titulo);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
