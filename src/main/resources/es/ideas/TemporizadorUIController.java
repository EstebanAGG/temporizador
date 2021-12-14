/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package es.ideas;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.StackPane;
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
    @FXML
    private Button btnLoad;
    @FXML
    private Button btnReset;
    
    //Propiedad para controlar cuándo se ha modificado alguno de los valores
    private SimpleBooleanProperty valorModificado;
    @FXML
    private StackPane lblContador;
    @FXML
    private TextField txtHoras;
    @FXML
    private TextField txtMinutos;
    @FXML
    private TextField txtSegundos;
    
    private ChangeListener<String> dosDigitos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        valorModificado = new SimpleBooleanProperty(false);
        
        tl = new Timeline();
        tl.setCycleCount(Timeline.INDEFINITE);
        
        final KeyFrame kf= new KeyFrame(Duration.seconds(1),(event) -> {
            String hora= LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            lbContador.textProperty().set(hora);
        });
        tl.getKeyFrames().add(kf);       
        
        txtHoras.textProperty().addListener(
                dosDigitos = (obs,viejo,nuevo) -> {
                    System.out.println("Estoy comprobando si hay más de dos dígitos");
                    try{
                        if (nuevo.length() > 2) {
                            txtHoras.setText(viejo);                                              
                        }else if ((nuevo.length() != 0)&&
                                ((Integer.decode(nuevo) > 23) || (Integer.decode(nuevo) < 0))
                                ){
                                    txtHoras.setText("00");
                        }
                    }catch(NumberFormatException nfe){
                        System.out.println("Excepción de formato de número");
                        txtHoras.setText("00");
                    }
                });
        txtMinutos.textProperty().addListener(
                dosDigitos = (obs,viejo,nuevo) -> {
                    System.out.println("Estoy comprobando si hay más de dos dígitos");
                    try{
                        if (nuevo.length() > 2) {
                            txtMinutos.setText(viejo);                                              
                        }else if ((nuevo.length() != 0)&&
                                ((Integer.decode(nuevo) > 59) || (Integer.decode(nuevo) < 0))
                                ){
                                    txtMinutos.setText("00");
                        }
                    }catch(NumberFormatException nfe){
                        System.out.println("Excepción de formato de número");
                        txtMinutos.setText("00");
                    }
                });
        txtSegundos.textProperty().addListener(
                dosDigitos = (obs,viejo,nuevo) -> {
                    System.out.println("Estoy comprobando si hay más de dos dígitos");
                    try{
                        if (nuevo.length() > 2) {
                         txtSegundos.setText(viejo);                                              
                        }else if ((nuevo.length() != 0)&&
                                ((Integer.decode(nuevo) > 59) || (Integer.decode(nuevo) < 0))
                                ){
                                 txtSegundos.setText("00");
                        }
                    }catch(NumberFormatException nfe){
                        System.out.println("Excepción de formato de número");
                     txtSegundos.setText("00");
                    }
                });
        txtHoras.focusedProperty()
                .addListener((observable, oldValue, newValue)-> {
                    if (!newValue && txtHoras.getText().isEmpty()){
                        txtHoras.setText("00");
                    }
        });
        txtMinutos.focusedProperty()
                .addListener((observable, oldValue, newValue)-> {
                    if (!newValue && txtMinutos.getText().isEmpty()){
                        txtMinutos.setText("00");
                    }
        });
        txtSegundos.focusedProperty()
                .addListener((observable, oldValue, newValue)-> {
                    if (!newValue && txtSegundos.getText().isEmpty()){
                        txtSegundos.setText("00");
                    }
        });
    }
    
    /**
     * Establecer el icono de los botones por código mediante un imageView
     * @param event 
     */
    @FXML
    private void btnIniciarAccion(ActionEvent event) {
        System.out.println("Botón play");
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

    @FXML
    private void btnLoadAction(ActionEvent event) {
        System.out.println("Botón Cargar");
    }

    @FXML
    private void btnResetAction(ActionEvent event) {
        System.out.println("Botón Reset");
        txtHoras.setText("00");
        txtMinutos.setText("00");
        txtSegundos.setText("00");
        if (tl.getStatus() == tl.getStatus().RUNNING ){
            btnIniciar.selectedProperty().set(false);
            btnIniciar.fireEvent(event);
        }
        lbContador.setText("00:00:00");
        
    }

}
