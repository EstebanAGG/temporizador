package es.ideas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Clase principal, utilizando JavaFX, que crea un Temporizador para explicación
 * en las clases de 2º de Desarrollo de Aplicaciones Multiplataforma.
 * 
 * @since 1.0
 * @author Esteban A. Giménez
 * @see <a href="https://github.com/EstebanAGG">Cuenta de GitHub</a> 
 */
public class Temporizador extends Application {

    private static Scene scene;

    /**
     * Método principal de una aplicación JavaFX
     * 
     * @param stage Escenario de la aplicación
     * @throws IOException Lanza la excepción si no encuentra el fichero fxml el
     *          método loadFXML
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("temporizadorUI"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Temporizador.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Método principal que lanza la aplicación, no es necesario en una
     *  aplicación de JavaFX, pero se puede poner para evitar algún error de
     *  compatibilidad.
     * 
     * @param args Argumentos de línea de comandos para la aplicación.
     */
    public static void main(String[] args) {
        launch();
    }

}