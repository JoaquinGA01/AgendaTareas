package com.Proyecto.Prueba.db;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TareaDAO {
    private Conexion conexion = new Conexion();

    public ArrayList<Tarea> BuscarTareas(String correo){
        ArrayList<Tarea> tareas = new ArrayList<Tarea>();

        String  Correo = correo;
        PreparedStatement stm = null;
        ResultSet rs = null;
        Connection conn = null;
        conn = conexion.getConnection();
        try{
            String sql = "SELECT * FROM listatareas WHERE CorreoUsuario = ?";
            
            stm = conn.prepareStatement(sql);
            stm.setString(1, Correo);
            rs = stm.executeQuery();
            while (rs.next()){
                Tarea t = new Tarea();
                t.setIdTarea(rs.getString("idTarea"));
                t.setTitulo(rs.getString("NombreTarea"));
                t.setPrioridad(rs.getString("Prioridad"));
                t.setDescripcion(rs.getString("Descricpcion"));
                t.setFoto(rs.getBytes("Imagen"));
                t.setCorreo(rs.getString("CorreoUsuario"));
                t.setImagenPath(rs.getString("CorreoUsuario") + rs.getString("NombreTarea")  + ".jpg");
                tareas.add(t);
            }
        }catch(Exception e){
            System.out.println(e);
        }
        return tareas;

    }

    public String GuardarTarea(Tarea t, String file){
        Tarea tarea = t;
        PreparedStatement stm = null;
        ResultSet rs = null;
        Connection conn = null;
        FileInputStream input = null;
        conn = conexion.getConnection();

        try {
            String sql = "INSERT INTO listatareas (idTarea, NombreTarea, Prioridad, Descricpcion, Imagen, CorreoUsuario) VALUE (?,?,?,?,?,?)";
            stm = conn.prepareStatement(sql);
            stm.setString(1, tarea.getIdTarea());
            stm.setString(2, tarea.getTitulo());
            stm.setString(3, tarea.getPrioridad());
            stm.setString(4, tarea.getDescripcion());
            input = new FileInputStream(new File("/tmp/" + file));
            stm.setBinaryStream(5, input);
            stm.setString(6, tarea.getCorreo());

            if (stm.executeUpdate() >0) 
                return "Tarea Agregada" ;
            else
                return "No se Agrego la Tarea";

            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    
}
