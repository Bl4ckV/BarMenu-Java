package org.openjfx.javaapi;

public class Bebida {
    
    protected String nombre, alcoholica, fecha;
    
    public Bebida() {
    }

    public String getNombre() {
        return nombre;
    }

    public String getAlcoholica() {
        return alcoholica;
    }

    public String getFecha() {
        return fecha;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setAlcoholica(String alcoholica) {
        this.alcoholica = alcoholica;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }  
}
