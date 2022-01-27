package com.Proyecto.Prueba.db;

public class Usuario {
    private String Correo;
    private String Pass;
    private String Nombre;
    private String Apellido;
    public Usuario(String correo, String pass, String nombre, String apellido) {
        Correo = correo;
        Pass = pass;
        Nombre = nombre;
        Apellido = apellido;
    }
    public String getCorreo() {
        return Correo;
    }
    public void setCorreo(String correo) {
        Correo = correo;
    }
    public String getPass() {
        return Pass;
    }
    public void setPass(String pass) {
        Pass = pass;
    }
    public String getNombre() {
        return Nombre;
    }
    public void setNombre(String nombre) {
        Nombre = nombre;
    }
    public String getApellido() {
        return Apellido;
    }
    public void setApellido(String apellido) {
        Apellido = apellido;
    }
    @Override
    public String toString() {
        return "Usuario [Apellido=" + Apellido + ", Correo=" + Correo + ", Nombre=" + Nombre + ", Pass=" + Pass + "]";
    }

    
}
