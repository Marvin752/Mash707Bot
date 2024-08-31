package umg.programacion2.dao;

import umg.programacion2.db.DatabaseConnection;
import umg.programacion2.model.Pregunta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PreguntaDao {
    private final Connection connection;

    public PreguntaDao() throws SQLException {
        this.connection = DatabaseConnection.getConnection() ;
    }

    // Método para insertar una pregunta
    public void insertPregunta(Pregunta pregunta) throws SQLException {
        String query = "INSERT INTO preguntas (cuestionario_id, pregunta_texto) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, pregunta.getCuestionarioId());
            ps.setString(2, pregunta.getPreguntaTexto());
            ps.executeUpdate();
        }
    }

    // Método para obtener todas las preguntas de un cuestionario específico
    public List<Pregunta> getPreguntasByCuestionarioId(int cuestionarioId) throws SQLException {
        List<Pregunta> preguntas = new ArrayList<>();
        String query = "SELECT * FROM preguntas WHERE cuestionario_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, cuestionarioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pregunta pregunta = new Pregunta();
                    pregunta.setId(rs.getInt("id"));
                    pregunta.setCuestionarioId(rs.getInt("cuestionario_id"));
                    pregunta.setPreguntaTexto(rs.getString("pregunta_texto"));
                    preguntas.add(pregunta);
                }
            }
        }
        return preguntas;
    }

    // Método para obtener una pregunta por su ID
    public Pregunta getPreguntaById(int id) throws SQLException {
        String query = "SELECT * FROM preguntas WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Pregunta pregunta = new Pregunta();
                    pregunta.setId(rs.getInt("id"));
                    pregunta.setCuestionarioId(rs.getInt("cuestionario_id"));
                    pregunta.setPreguntaTexto(rs.getString("pregunta_texto"));
                    return pregunta;
                }
            }
        }
        return null;
    }

    // Método para actualizar una pregunta
    public void updatePregunta(Pregunta pregunta) throws SQLException {
        String query = "UPDATE preguntas SET cuestionario_id = ?, pregunta_texto = ? WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, pregunta.getCuestionarioId());
            ps.setString(2, pregunta.getPreguntaTexto());
            ps.setInt(3, pregunta.getId());
            ps.executeUpdate();
        }
    }

    // Método para eliminar una pregunta por su ID
    public void deletePregunta(int id) throws SQLException {
        String query = "DELETE FROM preguntas WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}