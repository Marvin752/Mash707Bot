package umg.programacion2.dao;

import umg.programacion2.db.DatabaseConnection;
import umg.programacion2.model.Respuesta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RespuestaDao {

    // Método para insertar una nueva respuesta
    public void save(Respuesta respuesta) throws SQLException {
        String sql = "INSERT INTO tb_respuestas (seccion, telegram_id, pregunta_id, respuesta_texto, fecha_respuesta) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, respuesta.getSeccion());
            statement.setLong(2, respuesta.getTelegramId());
            statement.setInt(3, respuesta.getPreguntaId());
            statement.setString(4, respuesta.getRespuestaTexto());
            statement.setTimestamp(5, respuesta.getFechaRespuesta());
            statement.executeUpdate();
        }
    }

    //hacer metodo para getRespuestaByTelegramId
    public Respuesta getRespuestaByTelegramId(long telegramId) throws SQLException {
        String sql = "SELECT * FROM tb_respuestas WHERE telegram_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, telegramId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToRespuesta(resultSet);
            }
        }
        return null;
    }




    // Método para obtener una respuesta por su ID
    public Respuesta getById(int id) throws SQLException {
        String sql = "SELECT * FROM tb_respuestas WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return mapResultSetToRespuesta(resultSet);
            }
        }
        return null;
    }

    // Método para obtener todas las respuestas
    public List<Respuesta> getAll() throws SQLException {
        String sql = "SELECT * FROM tb_respuestas";
        List<Respuesta> respuestas = new ArrayList<>();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                respuestas.add(mapResultSetToRespuesta(resultSet));
            }
        }
        return respuestas;
    }

    // Método para actualizar una respuesta existente
    public void update(Respuesta respuesta) throws SQLException {
        String sql = "UPDATE tb_respuestas SET seccion = ?, telegram_id = ?, pregunta_id = ?, respuesta_texto = ?, fecha_respuesta = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, respuesta.getSeccion());
            statement.setLong(2, respuesta.getTelegramId());
            statement.setInt(3, respuesta.getPreguntaId());
            statement.setString(4, respuesta.getRespuestaTexto());
            statement.setTimestamp(5, respuesta.getFechaRespuesta());
            statement.setInt(6, respuesta.getId());
            statement.executeUpdate();
        }
    }

    // Método para eliminar una respuesta por su ID
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM tb_respuestas WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
    // Método para mapear un ResultSet a un objeto Respuesta
    private Respuesta mapResultSetToRespuesta(ResultSet resultSet) throws SQLException {
        Respuesta respuesta = new Respuesta();
        respuesta.setId(resultSet.getInt("id"));
        respuesta.setSeccion(resultSet.getString("seccion"));
        respuesta.setTelegramId(resultSet.getLong("telegram_id"));
        respuesta.setPreguntaId(resultSet.getInt("pregunta_id"));
        respuesta.setRespuestaTexto(resultSet.getString("respuesta_texto"));
        respuesta.setFechaRespuesta(resultSet.getTimestamp("fecha_respuesta"));
        return respuesta;
    }
}