/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.ideas.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 *
 * @author deymian
 */
public class Tiempo {
    //Atributos de la clase
    private int hora;
    private int minutos;
    private int segundos;
    private BooleanProperty establecido= new SimpleBooleanProperty();
    
    public Tiempo(){
        hora = minutos = segundos = 0;
        establecido.set(false);
    }
    
    public Tiempo(int horas, int minutos, int segundos){
        this.hora = horas;
        this.minutos = minutos;
        this.segundos = segundos;
        establecido.set(true);
    }
    
    //Métodos
    //Getter y Setters
    public int getHora() {
        return hora;
    }

    public void setHora(int hora) {
        this.hora = hora;
    }

    public int getMinutos() {
        return minutos;
    }

    public void setMinutos(int minutos) {
        this.minutos = minutos;
    }

    public int getSegundos() {
        return segundos;
    }

    public void setSegundos(int segundos) {
        this.segundos = segundos;
    }
    
    public void setEstablecido(boolean b){
        establecido.set(b);
    }
    
    public boolean getEstablecido(){
        return establecido.get();
    }
    
    public BooleanProperty establecidoProperty(){
        return establecido;
    }
    
    /**
     * Restar un segundo
     * return: true Si es igual a cero
     * return: false Si no es igual a cero
     * 
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
