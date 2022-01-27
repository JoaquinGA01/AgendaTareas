package com.Proyecto.Prueba.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
    private Conexion conexion = new Conexion();

    public boolean BuscarUsuario(String correo){
        String  Correo = correo;
        PreparedStatement stm = null;
        ResultSet rs = null;
        Connection conn = null;
        conn = conexion.getConnection();
        try{
            String sql = "SELECT * FROM usuarios WHERE Correo = ?";
            
            stm = conn.prepareStatement(sql);
            stm.setString(1, Correo);
            rs = stm.executeQuery();
            while (rs.next()){
                return true;
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return false;
    }
    public String guardarUsuario(Usuario u){
        Usuario usuario = u;
        PreparedStatement stm = null;
        ResultSet rs = null;
        Connection conn = null;
        conn = conexion.getConnection();

        try {
            String sql = "INSERT INTO usuarios (Correo, pass, Nombre, Apellido) VALUE (?,?,?,?)";
            stm = conn.prepareStatement(sql);
            stm.setString(1, usuario.getCorreo());
            stm.setString(2, usuario.getPass());
            stm.setString(3, usuario.getNombre() );
            stm.setString(4, usuario.getApellido());

            if (stm.executeUpdate() >0) 
                return "Usuario agregado";
            else
                return "No se agregó el usuario";

            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Listo";
    }
    public Usuario BuscarUsuario(String email, String pass){
        Usuario usuario = null; 
        PreparedStatement stm = null;
        ResultSet rs = null;
        Connection conn = null;
        conn = conexion.getConnection();
        try {
            String sql = "SELECT * FROM usuarios WHERE Correo = ? and pass = ?";
            stm = conn.prepareStatement(sql);
            stm.setString(1,email);
            stm.setString(2, pass);

            rs = stm.executeQuery();
            if(rs.next() != false){
                String Correo =  rs.getString("Correo");
                String Pass =  rs.getString("pass");
                String Nombre =  rs.getString("Nombre");
                String Apellido =  rs.getString("Apellido");
                usuario = new Usuario(Correo, Pass, Nombre, Apellido);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuario;
    }

}
