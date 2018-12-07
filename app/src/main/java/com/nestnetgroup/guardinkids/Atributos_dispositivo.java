package com.nestnetgroup.guardinkids;

/**
 * Created by NestNet on 28/04/2018.
 */

public class Atributos_dispositivo {

    String id_dispositivo;
    String nombre_dispositivo;
    String restante;

    boolean estado;

    public Atributos_dispositivo(String id_dispositivo, String nombre_dispositivo,String restante,boolean estado) {
        this.id_dispositivo = id_dispositivo;
        this.nombre_dispositivo = nombre_dispositivo;
        this.restante = restante;
        this.estado=estado;
    }

    public String getRestante() {
        return restante;
    }

    public void setRestante(String restante) {
        this.restante = restante;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getId_dispositivo() {
        return id_dispositivo;
    }

    public void setId_dispositivo(String id_dispositivo) {
        this.id_dispositivo = id_dispositivo;
    }

    public String getNombre_dispositivo() {
        return nombre_dispositivo;
    }

    public void setNombre_dispositivo(String nombre_dispositivo) {
        this.nombre_dispositivo = nombre_dispositivo;
    }


}
