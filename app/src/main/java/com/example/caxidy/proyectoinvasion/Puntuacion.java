package com.example.caxidy.proyectoinvasion;

public class Puntuacion {
    int id,puntuacion;
    String nombre;

    public Puntuacion(){
        id=0;
        nombre="";
        puntuacion=-1;
    }

    public Puntuacion(int id, String nombre, int puntuacion){
        this.id=id;
        this.nombre=nombre;
        this.puntuacion=puntuacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
