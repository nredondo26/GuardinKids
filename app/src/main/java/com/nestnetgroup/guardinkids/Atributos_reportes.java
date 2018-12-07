package com.nestnetgroup.guardinkids;

/**
 * Created by NestNet on 4/05/2018.
 */

public class Atributos_reportes {

    String Nombre_dispositivo;
    String Fecha_reporte;
    String imagen;

    public Atributos_reportes(String nombre_dispositivo, String fecha_reporte, String imagen) {
        Nombre_dispositivo = nombre_dispositivo;
        Fecha_reporte = fecha_reporte;
        this.imagen = imagen;
    }


    public String getNombre_dispositivo() {
        return Nombre_dispositivo;
    }

    public void setNombre_dispositivo(String nombre_dispositivo) {
        Nombre_dispositivo = nombre_dispositivo;
    }

    public String getFecha_reporte() {
        return Fecha_reporte;
    }

    public void setFecha_reporte(String fecha_reporte) {
        Fecha_reporte = fecha_reporte;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
