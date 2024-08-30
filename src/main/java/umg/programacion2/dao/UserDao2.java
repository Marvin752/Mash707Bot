package umg.programacion2.dao;

import umg.programacion2.db.DatabaseConnection;
import umg.programacion2.model.User2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDao2 {


    //Metodo para insertar las respuestas

    public void InsertarRespuesta(User2 user2) throws SQLException {
        String sql = "INSERT INTO tb_respuestas (seccion, telegram_id, pregunta_id, respuesta_texto) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user2.getSeccion());
            preparedStatement.setLong(2, user2.getTelegramId());
            preparedStatement.setInt(3, user2.getPreguntaId());
            preparedStatement.setString(4, user2.getRespuestaTexto());

            preparedStatement.executeUpdate();
        }
    }

    public void ObtenerRespuestas(User2 user2) throws SQLException {

    }
}