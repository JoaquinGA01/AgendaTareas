package com.Proyecto.Prueba;

import static spark.Spark.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.Proyecto.Prueba.db.Conexion;
import com.Proyecto.Prueba.db.Tarea;
import com.Proyecto.Prueba.db.TareaDAO;
import com.Proyecto.Prueba.db.Usuario;
import com.Proyecto.Prueba.db.UsuarioDAO;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.apache.commons.io.IOUtils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import spark.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static spark.debug.DebugScreen.*;
import java.util.UUID;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        staticFiles.externalLocation("/");

        get("/", (rq, rs) -> {
            rs.redirect("app\\target\\classes\\index.html");
            return null;
        });

        post("/Ingresar", (rq, rs) -> {
            Usuario usuario;
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            usuario = usuarioDAO.BuscarUsuario(rq.queryParams("email"), rq.queryParams("pass"));
            if (usuario != null) {
                String respuesta = obtenerTareas(rq, usuario.getCorreo(), usuario.getPass(), usuario.getNombre());

                return respuesta;
            } else {
                Map<String, Object> variables = new HashMap<>();
                variables.put("Mensaje", "No Existe este Usuario");
                IContext context = new Context(rq.raw().getLocale(), variables);
                String out = ThymeleafUtil.getTemplateEngine().process("../index.html", context);
                return out;
            }
        });

        post("/Registrar", (rq, rs) -> {
            String Correo = rq.queryParams("email");
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            if (usuarioDAO.BuscarUsuario(Correo)) {
                Map<String, Object> variables = new HashMap<>();
                variables.put("Mensaje", "Este Usuario Ya Existe");
                IContext context = new Context(rq.raw().getLocale(), variables);
                String out = ThymeleafUtil.getTemplateEngine().process("../index.html", context);
                return out;
            }
            String Pass = rq.queryParams("pass");
            String Nombre = rq.queryParams("Nombre");
            String Apellido = rq.queryParams("Apellido");
            usuarioDAO.guardarUsuario(new Usuario(Correo, Pass, Nombre, Apellido));
            String respuesta = obtenerTareas(rq, Correo, Apellido, Nombre);
            return respuesta;
        });

        post("/GuardarTarea", (rq, rs) -> {
            rq.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
            String NombreTarea = convertInputStreamToString(rq.raw().getPart("NombreTarea").getInputStream());
            String Prioridad = convertInputStreamToString(rq.raw().getPart("Prioridad").getInputStream());
            String DescripcionTarea = convertInputStreamToString(rq.raw().getPart("Descripcion").getInputStream());
            byte[] Imagen = convertInputStreamToString(rq.raw().getPart("Imagen").getInputStream()).getBytes();
            String Correo = convertInputStreamToString(rq.raw().getPart("Correo").getInputStream());
            String Pass = convertInputStreamToString(rq.raw().getPart("Pass").getInputStream());
            String id = UUID.randomUUID().toString();
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario usuario = usuarioDAO.BuscarUsuario(Correo, Pass);
            Tarea tarea = new Tarea(id, NombreTarea, Prioridad, DescripcionTarea, Imagen, Correo);
            tarea.setImagenPath(Correo + NombreTarea + ".jpg");
            crearFile(tarea, rq);
            TareaDAO tDao = new TareaDAO();

            System.out.println(tDao.GuardarTarea(tarea, tarea.getImagenPath()));
            String respuesta = obtenerTareas(rq, Correo, Pass, usuario.getNombre());
            return respuesta;            
        });
    }

    private static String obtenerTareas(Request rq, String Correo, String Pass, String Nombre) {
        TareaDAO tareaDAO = new TareaDAO();
        ArrayList<Tarea> tareas = tareaDAO.BuscarTareas(Correo);
        Map<String, Object> variables = new HashMap<>();
        variables.put("Correo", Correo);
        variables.put("Pass", Pass);
        variables.put("Nombre", Nombre);
        for (int i = 0; i < tareas.size(); i++) {
            recuperarFile(tareas.get(i), rq);
        }
        if (tareas.size() > 0) {
            variables.put("Existen", "Si");
            variables.put("Tareas", tareas);
            variables.put("TotalTareas", tareas.size());

        } else {
            variables.put("Existen", "No");
            variables.put("TotalTareas", "No tienes Tareas Registradas");
        }

        IContext context = new Context(rq.raw().getLocale(), variables);
        String out = ThymeleafUtil.getTemplateEngine().process("Tareas/index.html", context);
        
        return out;
    }

    private static void crearFile(Tarea tarea, Request rq) {
        File fichero = new File(tarea.getImagenPath());
        if (!fichero.exists()) {

            try {
                File uploadDir = new File("/tmp/");
                uploadDir.mkdir();
                String nombredoc = tarea.getCorreo() + tarea.getTitulo();
                Path tempFile = Files.createTempFile(uploadDir.toPath(), nombredoc, ".jpg");
                rq.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
                try (InputStream input = rq.raw().getPart("Imagen").getInputStream()) {
                    Files.copy(input, tempFile, StandardCopyOption.REPLACE_EXISTING);
                }

                File file = logInfo(rq, tempFile);
                File fileRename = new File("/tmp/" + nombredoc
                        + ".jpg");
                file.renameTo(fileRename);
            } catch (IOException | ServletException e) {
                System.out.println(e);
            }

        }
        // return null;
    }

    private static void recuperarFile(Tarea tarea, Request rq) {

        if (!new File( ("/tmp/" + tarea.getImagenPath())) .exists()) {

            PreparedStatement stmt = null;
            InputStream input = null;
            FileOutputStream output = null;
            Conexion conexion = new Conexion();
            Connection conn = null;
            conn = conexion.getConnection();
            try {
                String sql = "SELECT Imagen FROM listatareas where CorreoUsuario = ?;";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, tarea.getCorreo());
                ResultSet rs = stmt.executeQuery();

                File file = new File( "/tmp/"+  tarea.getImagenPath());
                output = new FileOutputStream(file);

                if (rs.next()) {
                    input = rs.getBinaryStream("Imagen");
                    byte[] buffer = new byte[1024];
                    while (input.read(buffer) > 0) {
                        output.write(buffer);
                    }
                    File fileRename = new File("/tmp/" + tarea.getImagenPath()
                        );

                    file.renameTo(fileRename);
                }
            } catch (SQLException | IOException ex) {
                System.err.println(ex.getMessage());
            } finally {
                try {
                    if (input != null) {
                        input.close();
                    }
                    if (output != null) {
                        output.close();
                    }
                    if (stmt != null) {
                        stmt.close();
                    }
                } catch (IOException | SQLException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }

    private static File logInfo(Request req, Path tempFile) throws IOException, ServletException {
        File file = tempFile.toFile();
        return file;
    }

    private static String convertInputStreamToString(InputStream is) throws IOException {
        return IOUtils.toString(is, StandardCharsets.UTF_8);
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 1234; // return default port if heroku-port isn't set (i.e. on localhost)
    }
}
