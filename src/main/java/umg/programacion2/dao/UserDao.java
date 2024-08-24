package umg.programacion2.dao;

import umg.programacion2.db.DatabaseConnection;
import umg.programacion2.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    public void deleteUserByEmail(String email) throws SQLException {
        String query = "DELETE FROM  tb_usuarios WHERE correo = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            statement.executeUpdate();
        }
    }




    public void updateUser(User user) throws SQLException {
        String query = "UPDATE tb_usuarios SET carne = ?, nombre = ?, correo = ?, seccion = ?, telegramid = ?, activo = ? WHERE idusuario = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getCarne());
            statement.setString(2, user.getNombre());
            statement.setString(3, user.getCorreo());
            statement.setString(4, user.getSeccion());
            statement.setLong(5, user.getTelegramid());
            statement.setString(6, user.getActivo());
            statement.setInt(7, user.getId());
            statement.executeUpdate();
        }
    }



    public void insertUser(User user) throws SQLException {
        String query = "INSERT INTO tb_usuarios (carne, nombre, correo, seccion, telegramid, activo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getCarne());
            statement.setString(2, user.getNombre());
            statement.setString(3, user.getCorreo());
            statement.setString(4, user.getSeccion());
            statement.setLong(5, user.getTelegramid());
            statement.setString(6, user.getActivo());
            statement.executeUpdate();
        }
    }

    public User getUserByTelegramId(long telegramid) throws SQLException {
        String query = "SELECT * FROM tb_usuarios WHERE telegramid = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, telegramid);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("idusuario"));
                user.setCarne(resultSet.getString("carne"));
                user.setNombre(resultSet.getString("nombre"));
                user.setCorreo(resultSet.getString("correo"));
                user.setSeccion(resultSet.getString("seccion"));
                user.setTelegramid(resultSet.getLong("telegramid"));
                user.setActivo(resultSet.getString("activo"));
                return user;
            }
        }
        return null;
    }


    public User getUserByEmail(String Email) throws SQLException {
        String query = "SELECT * FROM tb_usuarios WHERE correo = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, Email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("idusuario"));
                user.setCarne(resultSet.getString("carne"));
                user.setNombre(resultSet.getString("nombre"));
                user.setCorreo(resultSet.getString("correo"));
                user.setSeccion(resultSet.getString("seccion"));
                user.setTelegramid(resultSet.getLong("telegramid"));
                user.setActivo(resultSet.getString("activo"));
                return user;
            }
        }
        return null;
    }

    public User getUserByidusuario(int idusuario) throws SQLException {
        String query = "SELECT * FROM tb_usuarios WHERE idusuario = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idusuario);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("idusuario"));
                user.setCarne(resultSet.getString("carne"));
                user.setNombre(resultSet.getString("nombre"));
                user.setCorreo(resultSet.getString("correo"));
                user.setSeccion(resultSet.getString("seccion"));
                user.setTelegramid(resultSet.getLong("telegramid"));
                user.setActivo(resultSet.getString("activo"));
                return user;
            }
        }
        return null;
    }

    public User getUserBycarnet(String carnet) throws SQLException {
        String query = "SELECT * FROM tb_usuarios WHERE carne = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, carnet);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("idusuario"));
                user.setCarne(resultSet.getString("carne"));
                user.setNombre(resultSet.getString("nombre"));
                user.setCorreo(resultSet.getString("correo"));
                user.setSeccion(resultSet.getString("seccion"));
                user.setTelegramid(resultSet.getLong("telegramid"));
                user.setActivo(resultSet.getString("activo"));
                return user;
            }
        }
        return null;
    }


}