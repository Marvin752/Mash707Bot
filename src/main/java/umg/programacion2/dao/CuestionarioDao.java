package umg.programacion2.dao;

import umg.programacion2.db.DatabaseConnection;
import umg.programacion2.model.Cuestionario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class CuestionarioDao {

    private final Connection connection;

    public CuestionarioDao() throws SQLException {
        this.connection = DatabaseConnection.getConnection() ;
    }

    // Método para insertar un cuestionario
    public void insertCuestionario(Cuestionario cuestionario) throws SQLException {
        String query = "INSERT INTO cuestionarios (nombre, descripcion, fecha_creacion) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, cuestionario.getNombre());
            ps.setString(2, cuestionario.getDescripcion());
            ps.setTimestamp(3, cuestionario.getFechaCreacion());
            ps.executeUpdate();
        }
    }

    // Método para obtener todos los cuestionarios
    public List<Cuestionario> getAllCuestionarios() throws SQLException {
        List<Cuestionario> cuestionarios = new ArrayList<>();
        String query = "SELECT * FROM cuestionarios";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Cuestionario cuestionario = new Cuestionario();
                cuestionario.setId(rs.getInt("id"));
                cuestionario.setNombre(rs.getString("nombre"));
                cuestionario.setDescripcion(rs.getString("descripcion"));
                cuestionario.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                cuestionarios.add(cuestionario);
            }
        }
        return cuestionarios;
    }

    // Método para obtener un cuestionario por su ID
    public Cuestionario getCuestionarioById(int id) throws SQLException {
        String query = "SELECT * FROM cuestionarios WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cuestionario cuestionario = new Cuestionario();
                    cuestionario.setId(rs.getInt("id"));
                    cuestionario.setNombre(rs.getString("nombre"));
                    cuestionario.setDescripcion(rs.getString("descripcion"));
                    cuestionario.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                    return cuestionario;
                }
            }
        }
        return null;
    }

    // Método para actualizar un cuestionario
    public void updateCuestionario(Cuestionario cuestionario) throws SQLException {
        String query = "UPDATE cuestionarios SET nombre = ?, descripcion = ?, fecha_creacion = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, cuestionario.getNombre());
            ps.setString(2, cuestionario.getDescripcion());
            ps.setTimestamp(3, cuestionario.getFechaCreacion());
            ps.setInt(4, cuestionario.getId());
            ps.executeUpdate();
        }
    }

    // Método para eliminar un cuestionario por su ID
    public void deleteCuestionario(int id) throws SQLException {
        String query = "DELETE FROM cuestionarios WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
