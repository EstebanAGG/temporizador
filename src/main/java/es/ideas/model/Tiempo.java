/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ideas.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Esta clase modela un objeto de tipo tiempo actual del temporizador. Guarda
 * las horas, los minutos y los segundos que restan hasta que el
 * temporizador llegue a cero.
 * 
 * @since 1.0
 * @author Esteban A. Giménez
 * @see <a href="https://github.com/EstebanAGG">Cuenta de GitHub</a> 
 */
public class Tiempo {
    //Atributos de la clase
    /**
     * Guarda las horas del temporizador
     */
    private int hora;
    /**
     * Guarda los minutos del temporizador
     */
    private int minutos;
    /**
     * Guarda los minutos del temporizador
     */
    private int segundos;
    
    /**
     * Vale true cuando se ha establecido un valor para el temporizador.
     * Se utiliza para habilitar o deshabilitar botones.
     */
    private BooleanProperty establecido= new SimpleBooleanProperty();
    
    /**
     * Crea una instancia de tiempo, estableciendo los atributos hora, minutos y
     * segundos a cero y establecido a false
     */
    public Tiempo(){
        hora = minutos = segundos = 0;
        establecido.set(false);
    }
    
    /**
     * Crea una instancia de Tiempo, estableciendo los atributos a los valores 
     * pasados como parámetros.<p>
     * El atributo <b>establecido</b> se establece a false.
     * 
     * @param horas del temporizador
     * @param minutos del temporizador
     * @param segundos del temporizador
     */
    public Tiempo(int horas, int minutos, int segundos){
        this.hora = horas;
        this.minutos = minutos;
        this.segundos = segundos;
        establecido.set(true);
    }
    
    //Métodos
    //Getter y Setters

    /**
     *
     * @return
     */
    public int getHora() {
        return hora;
    }

    /**
     *
     * @param hora
     */
    public void setHora(int hora) {
        this.hora = hora;
    }

    /**
     *
     * @return
     */
    public int getMinutos() {
        return minutos;
    }

    /**
     *
     * @param minutos
     */
    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    /**
     *
     * @return
     */
    public int getSegundos() {
        return segundos;
    }

    /**
     *
     * @param segundos
     */
    public void setSegundos(int segundos) {
        this.segundos = segundos;
    }
    
    /**
     *
     * @param b
     */
    public void setEstablecido(boolean b){
        establecido.set(b);
    }
    
    /**
     *
     * @return
     */
    public boolean getEstablecido(){
        return establecido.get();
    }
    
    /**
     *
     * @return
     */
    public BooleanProperty establecidoProperty(){
        return establecido;
    }
    
    /**
     * Restar un segundo
     * return: true Si es igual a cero
     * return: false Si no es igual a cero
     * 
     * @return 
    **/
    public boolean restaUnSegundo(){
        if (segundos > 0)
            segundos--;
        else if (minutos > 0){
            minutos--;
            segundos = 59;
        }else if (hora > 0){
            hora--;
            minutos = 59;
            segundos = 59;
        }else{
            return true;
        }            
        return false;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        String horaAux, minutosAux, segundosAux;
               
        if (hora < 10)
            horaAux = "0"+hora;
        else
            horaAux = "" + hora;
        
        if (minutos < 10)
            minutosAux = "0" + minutos;
        else
            minutosAux = "" + minutos;
        if (segundos < 10)
            segundosAux = "0" + segundos;
        else
            segundosAux = "" + segundos;
        
        return (horaAux + ":" + minutosAux + ":" + segundosAux);
    }    
}
