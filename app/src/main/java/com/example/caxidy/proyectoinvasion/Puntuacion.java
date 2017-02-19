package com.example.caxidy.proyectoinvasion;

public class Puntuacion {
    int puntuacion;
    String nombre;

    public Puntuacion(){
        nombre="";
        puntuacion=-1;
    }

    public Puntuacion(String nombre, int puntuacion){
        this.nombre=nombre;
        this.puntuacion=puntuacion;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
