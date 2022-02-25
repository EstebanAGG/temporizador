/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package es.ideas;

import es.ideas.model.Tiempo;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.animation.Animation;
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
 * Clase controladora de temporizadorUI.fxml
 *
 * @since 1.0
 * @author Esteban A. Giménez
 * @see <a href="https://github.com/EstebanAGG">Cuenta de GitHub</a> 
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
    @FXML
    private Label lblHora;

    /**
     * Objeto del modelo de datos para llevar el valor del temporizador
    */
    public Tiempo tiempoTemporizador;
    /**
     * Línea de tiempo para poder ir actualizando el temporizador.
    */
    private Timeline tl;
    /**
     * Línea de tiempo para actualizar el reloj (*comentar en clase*)
    */
    private Timeline tlRelojPrincipal, tlRelojSecundaria;
    /**
     * Propiedad para controlar cuándo se ha modificado alguno de los valores
    */
    private BooleanProperty valorModificado;
    /**
     * Atributo para el sonido
    */
    private Clip soundClip;

    /**
     * Inicialización de la clase controladora. 
     * <p>
     * Se crea un TimeLine para calcular la desviación de 1 seg en el que 
     * nos encontramos. <p> 
     *    |---- 1 seg ----||--u--inicioTimeLine--deltaU--|  <p>
     *   u        = tiempoMilisegundos % 1000               <p>
     * deltaU     = 1000 - u                                <p>
     * u + deltaU = 1                                       <p>
     * Posteriormente se lanza el segundo TimeLine que se ejecuta
     *  cada segundo exactamente y va actualizando el reloj
     *
     * @param url Url del fichero fxml
     * @param rb Fichero de recursos (para internacionalización)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Inicialización de atributos
        valorModificado = new SimpleBooleanProperty(false);
        tiempoTemporizador = new Tiempo();

        /*
         * Se crea un TimeLine para calcular la desviación de 1 seg en el que 
         * nos encontramos. 
         *    |---- 1 seg ----||--u--inicioTimeLine--deltaU--|
         *   u        = tiempoMilisegundos % 1000
         * deltaU     = 1000 - u
         * u + deltaU = 1
         * Posteriormente se lanza el segundo TimeLine que se ejecuta
         *  cada segundo exactamente y va actualizando el reloj
         * 
         */
        //Clase para dar el formato adecuado a la hora actual
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");

        tlRelojPrincipal = new Timeline();
        tlRelojSecundaria = new Timeline();
        //Para que se repita indefinidamente
        tlRelojSecundaria.setCycleCount(Animation.INDEFINITE);
        //Key Frame Línea de Tiempo Secundaria
        KeyFrame kfRelojSecundaria = new KeyFrame(
                Duration.seconds(1),
                event -> {
                    lblHora.setText(formatoHora.format(System.currentTimeMillis()));
                }
        );
        tlRelojSecundaria.getKeyFrames().add(kfRelojSecundaria);
        //Key Frame Línea de Tiempo Primaria
        KeyFrame kfRelojPrincipal = new KeyFrame(
                new Duration(1000 - (System.currentTimeMillis() % 1000)),
                (event) -> {
                    lblHora.setText(formatoHora.format(System.currentTimeMillis()));
                    tlRelojSecundaria.play();
                }
        );
        tlRelojPrincipal.getKeyFrames().add(kfRelojPrincipal);
        //Inicio del TimeLine principal
        tlRelojPrincipal.play();

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
                btnIniciar.selectedProperty().set(false);
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
                        && ((Integer.parseInt(nuevo) > 23) || (Integer.parseInt(nuevo) < 0))) {
                    txtHoras.setText("00");
                }
            } catch (NumberFormatException nfe) {
                System.out.println("Excepción de formato de número");
                txtHoras.setText("00");
            }
        });
        txtMinutos.textProperty().addListener((obs, viejo, nuevo) -> {
//            System.out.println("Estoy en los minutos -- " + nuevo);
            valorModificado.set(true);
            try {
//                System.out.println("Valor: " + Integer.parseInt(nuevo));
                if (nuevo.length() > 2) {
                    txtMinutos.setText(viejo);
                } else if ((nuevo.length() != 0)
                        && (Integer.parseInt(nuevo) > 59)) {
                    txtMinutos.setText("00");
                }
            } catch (NumberFormatException nfe) {
                txtMinutos.setText("00");
            }
        });
        txtSegundos.textProperty().addListener((obs, viejo, nuevo) -> {
//            System.out.println("Estoy comprobando si hay más de dos dígitos");
            valorModificado.set(true);
            try {
                if (nuevo.length() > 2) {
                    txtSegundos.setText(viejo);
                } else if ((nuevo.length() != 0) && (Integer.parseInt(nuevo) > 59)) {
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
                        if (Integer.parseInt(txtHoras.getText()) < 10) {
                            txtHoras.setText("0" + txtHoras.getText());
                        }
                    }
                });
        txtMinutos.focusedProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (!newValue && txtMinutos.getText().isEmpty()) {
                        //foco ganado
                        //System.out.println("Foco ganado");
                        txtMinutos.setText("00");
                    } else {
                        //Foco perdido
                        //System.out.println("Foco perdido");
                        if (Integer.parseInt(txtMinutos.getText()) < 10) {
                            txtMinutos.setText("0" + txtMinutos.getText());
                        }
                    }
                });
        txtSegundos.focusedProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (!newValue && txtSegundos.getText().isEmpty()) {
                        txtSegundos.setText("00");
                    } else {
                        if (Integer.parseInt(txtSegundos.getText()) < 10) {
                            txtSegundos.setText("0" + txtSegundos.getText());
                        }
                    }
                });
    }

    /**
     * Reproduce el sonido por defecto En este caso de una sirena. TODO:
     * reproducir sonido incluido en resources/sounds
     */
    private void reproducirSonido() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                    getClass().getResource("sounds/sirena.wav"));
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
     * Inicia la cuenta atrás de temporizador o la pausa Establecer el icono de
     * los botones por código mediante un imageView
     *
     * @param event
     */
    @FXML
    private void btnIniciarAccion(ActionEvent event) {
//        System.out.println("Botón play");
        //Obtener el hijo 0 del botón (imageView)
        //TODO: Se puede poner un id al ImageView del botón y acceder directamente
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
     *
     * @param event
     */
    @FXML
    private void btnLoadAction(ActionEvent event) {
        tiempoTemporizador.setHora(Integer.parseInt(txtHoras.getText()));
        tiempoTemporizador.setMinutos(Integer.parseInt(txtMinutos.getText()));
        tiempoTemporizador.setSegundos(Integer.parseInt(txtSegundos.getText()));
        tiempoTemporizador.setEstablecido(true);
        lbContador.setText(tiempoTemporizador.toString());
    }

    /**
     * Resetea todos los valores a 0, tanto de configuradores como del
     * temporizador
     *
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
