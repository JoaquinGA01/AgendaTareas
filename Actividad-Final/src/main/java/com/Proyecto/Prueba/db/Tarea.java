package com.Proyecto.Prueba.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Tarea {
    private String idTarea;
    private String Titulo;
    private String Prioridad;
    private String Descripcion;
    private String ImagenPath;
    private String Correo;
    private byte[] foto;
    
    public Tarea(){

    }
    public Tarea(String idTarea, String titulo, String prioridad, String descripcion, String correo) {
        this.idTarea = idTarea;
        Titulo = titulo;
        Prioridad = prioridad;
        Descripcion = descripcion;
        Correo = correo;
    }
    
    public Tarea(String idTarea, String titulo, String prioridad, String descripcion, byte[] imagen,
            String correo) {
        this.idTarea = idTarea;
        Titulo = titulo;
        Prioridad = prioridad;
        Descripcion = descripcion;
        Correo = correo;
        foto = imagen;
    }

    
    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }

    public String getImagenPath() {
        return ImagenPath;
    }

    public void setImagenPath(String imagenPath) {
        ImagenPath = imagenPath;
    }

    public String getIdTarea() {
        return idTarea;
    }
    public void setIdTarea(String idTarea) {
        this.idTarea = idTarea;
    }
    public String getTitulo() {
        return Titulo;
    }
    public void setTitulo(String titulo) {
        Titulo = titulo;
    }
    public String getPrioridad() {
        return Prioridad;
    }
    public void setPrioridad(String prioridad) {
        Prioridad = prioridad;
    }
    public String getDescripcion() {
        return Descripcion;
    }
    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }
    
    public String getCorreo() {
        return Correo;
    }
    public void setCorreo(String correo) {
        Correo = correo;
    }
    @Override
    public String toString() {
        return "Tarea [Correo=" + Correo + ", Descripcion=" + Descripcion + ", Imagen=" +  ", Prioridad="
                + Prioridad + ", Titulo=" + Titulo + ", idTarea=" + idTarea + "]";
    }

    

}
