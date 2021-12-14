/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package es.ideas;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author deymian
 */
public class TemporizadorUIController implements Initializable {
    private Timeline tl;
    @FXML
    private Label lbContador;
    @FXML
    private ToggleButton btnIniciar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tl = new Timeline();
        tl.setCycleCount(Timeline.INDEFINITE);
        
        final KeyFrame kf= new KeyFrame(Duration.seconds(1),(event) -> {
            String hora= LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            lbContador.textProperty().set(hora);
        });
        tl.getKeyFrames().add(kf);       
    }    

    /**
     * Establecer el icono de los botones por código mediante un imageView
     * @param event 
     */
    @FXML
    private void btnIniciarAccion(ActionEvent event) {
        //Obtener el hijo 0 del botón (imageView)
        ImageView iv = (ImageView)btnIniciar.getChildrenUnmodifiable().get(0);
        if (btnIniciar.isSelected()){
            iv.setImage(new Image(TemporizadorUIController.class
                    .getResourceAsStream("pictures/pause.png")));
            tl.play();
        }else{
            iv.setImage(new Image(TemporizadorUIController.class
                    .getResourceAsStream("pictures/play.png")));
            
            tl.pause();
        }
    }    
}
