/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package es.ideas;

import es.ideas.model.Tiempo;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * FXML Controller class
 *
 * @author deymian
 */
public class TemporizadorUIController implements Initializable {

    //Variables de la vista
    @FXML
    private Label lbContador;
    @FXML
    private ToggleButton btnIniciar;
    @FXML
    private Button btnReset;
    @FXML
    private TextField txtHoras;
    @FXML
    private TextField txtMinutos;
    @FXML
    private TextField txtSegundos;
    @FXML
    private Button btnLoad;

    //Atributo del modelo de datos para llevar el valor del temporizador
    private Tiempo tiempoTemporizador;
    //Línea de tiempo para poder ir actualizando el temporizador.
    private Timeline tl;
    //Propiedad para controlar cuándo se ha modificado alguno de los valores
    private BooleanProperty valorModificado;
    //Atributo para el sonido
    private Clip soundClip;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Inicialización de atributos
        valorModificado = new SimpleBooleanProperty(false);
        tiempoTemporizador = new Tiempo();

        //Creación de una nueva línea de tiempo para poder actualizar el temporizador
        tl = new Timeline();
        tl.setCycleCount(Timeline.INDEFINITE);
        //Keyframe para indicar lo que se debe hacer cada cierto tiempo
        final KeyFrame kf = new KeyFrame(Duration.seconds(1), (event) -> {
            //String hora= LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            if (!tiempoTemporizador.restaUnSegundo()) {
                lbContador.textProperty().set(tiempoTemporizador.toString());
            } else {
                tl.stop();
                tiempoTemporizador.setEstablecido(false);
                reproducirSonido();
                ImageView iv = (ImageView) btnIniciar.getChildrenUnmodifiable().get(0);
                iv.setImage(new Image(TemporizadorUIController.class
                        .getResourceAsStream("pictures/play.png")));
            }
        });
        //Añadir los keyFrames a la línea de tiempo
        tl.getKeyFrames().add(kf);
        //------------------------------------------------------------
        //BINDS para control de habilitar/deshabilitar de los botones 
        //------------------------------------------------------------
        //Botón iniciar
        btnIniciar.disableProperty().bind(tiempoTemporizador.establecidoProperty().not());
        //Botón reset
        btnReset.disableProperty().bind(valorModificado.not().or(
                txtHoras.textProperty().isEqualTo("00").
                        and(txtMinutos.textProperty().isEqualTo("00").
                                and(txtSegundos.textProperty().isEqualTo("00")).
                                and(lbContador.textProperty().isEqualTo("00:00:00")))));
        //Botón cargar datos en temporizador
        btnLoad.disableProperty().bind(txtHoras.textProperty().isEqualTo("00")
                .and(txtMinutos.textProperty().isEqualTo("00")
                        .and(txtSegundos.textProperty().isEqualTo("00"))));
        //----------------------------------------------------------------
        //LISTENERS
        //----------------------------------------------------------------
        txtHoras.textProperty().addListener((obs, viejo, nuevo) -> {
//            System.out.println("Estoy comprobando si hay más de dos dígitos");
            valorModificado.set(true);
            try {
                if (nuevo.length() > 2) {
                    txtHoras.setText(viejo);
                } else if ((nuevo.length() != 0)
                        && ((Integer.decode(nuevo) > 23) || (Integer.decode(nuevo) < 0))) {
                    txtHoras.setText("00");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Excepción de formato de número");
                txtHoras.setText("00");
            }
        });
        txtMinutos.textProperty().addListener((obs, viejo, nuevo) -> {
//            System.out.println("Estoy comprobando si hay más de dos dígitos");
            valorModificado.set(true);
            try {
                if (nuevo.length() > 2) {
                    txtMinutos.setText(viejo);
                } else if ((nuevo.length() != 0)
                        && ((Integer.decode(nuevo) > 59) || (Integer.decode(nuevo) < 0))) {
                    txtMinutos.setText("00");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Excepción de formato de número");
                txtMinutos.setText("00");
            }
        });
        txtSegundos.textProperty().addListener((obs, viejo, nuevo) -> {
//            System.out.println("Estoy comprobando si hay más de dos dígitos");
            valorModificado.set(true);
            try {
                if (nuevo.length() > 2) {
                    txtSegundos.setText(viejo);
                } else if ((nuevo.length() != 0)
                        && ((Integer.decode(nuevo) > 59) || (Integer.decode(nuevo) < 0))) {
                    txtSegundos.setText("00");
                }
            } catch (NumberFormatException nfe) {
//                System.out.println("Excepción de formato de número");
                txtSegundos.setText("00");
            }
        });
        txtHoras.focusedProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (!newValue && txtHoras.getText().isEmpty()) {
                        txtHoras.setText("00");
                    } else {
                        if (Integer.decode(txtHoras.getText()) < 10) {
                            txtHoras.setText("0" + txtHoras.getText());
                        }
                    }
                });
        txtMinutos.focusedProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (!newValue && txtMinutos.getText().isEmpty()) {
                        txtMinutos.setText("00");
                    } else {
                        if (Integer.decode(txtMinutos.getText()) < 10) {
                            txtMinutos.setText("0" + txtMinutos.getText());
                        }
                    }
                });
        txtSegundos.focusedProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (!newValue && txtSegundos.getText().isEmpty()) {
                        txtSegundos.setText("00");
                    } else {
                        if (Integer.decode(txtSegundos.getText()) < 10) {
                            txtSegundos.setText("0" + txtSegundos.getText());
                        }
                    }
                });
    }

    /**
     * Reproduce el sonido por defecto
     * En este caso de una sirena.
     * TODO: reproducir sonido incluido en resources/sounds
     */
    private void reproducirSonido() {
        try {
            //AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
            //      getClass().getResourceAsStream("sounds/sirena.wav"));

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    new File("sirena.wav").getAbsoluteFile());
            soundClip = AudioSystem.getClip();
            soundClip.open(audioInputStream);
            soundClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            System.out.println("Error cargando sonido: " + ex);
        }
    }
    
//-----------------------------------------------------------------------------
// ACCIONES DE LOS BOTONES
//-----------------------------------------------------------------------------
    /**
     * Inicia la cuenta atrás de temporizador o la pausa
     * Establecer el icono de los botones por código mediante un imageView
     *
     * @param event
     */
    @FXML
    private void btnIniciarAccion(ActionEvent event) {
//        System.out.println("Botón play");
        //Obtener el hijo 0 del botón (imageView)
        ImageView iv = (ImageView) btnIniciar.getChildrenUnmodifiable().get(0);
        if (btnIniciar.isSelected()) {
            iv.setImage(new Image(TemporizadorUIController.class
                    .getResourceAsStream("pictures/pause.png")));
            tl.play();
        } else {
            iv.setImage(new Image(TemporizadorUIController.class
                    .getResourceAsStream("pictures/play.png")));

            tl.pause();
        }
    }
    
    /**
     * Establece el valor puesto en los configuradores en el temporizador.
     * @param event 
     */
    @FXML
    private void btnLoadAction(ActionEvent event) {
        tiempoTemporizador.setHora(Integer.decode(txtHoras.getText()));
        tiempoTemporizador.setMinutos(Integer.decode(txtMinutos.getText()));
        tiempoTemporizador.setSegundos(Integer.decode(txtSegundos.getText()));
        tiempoTemporizador.setEstablecido(true);
        lbContador.setText(tiempoTemporizador.toString());
    }

    /**
     * Resetea todos los valores a 0, tanto de configuradores 
     *   como del temporizador
     * @param event 
     */
    @FXML
    private void btnResetAction(ActionEvent event) {
//        System.out.println("Botón Reset");
        txtHoras.setText("00");
        txtMinutos.setText("00");
        txtSegundos.setText("00");
        if (tl.getStatus() == tl.getStatus().RUNNING) {
            btnIniciar.selectedProperty().set(false);
            btnIniciar.fireEvent(event);
        }
        lbContador.setText("00:00:00");
        tiempoTemporizador.setEstablecido(false);
        valorModificado.set(false);
    }
}
