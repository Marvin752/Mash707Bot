package umg.programacion2.service;

import umg.programacion2.dao.UserDao2;
import umg.programacion2.db.DatabaseConnection;
import umg.programacion2.db.TransactionManager;

import umg.programacion2.model.User2;

import java.sql.Connection;
import java.sql.SQLException;

////////////

public class UserService2 {

    private UserDao2 userDao2 = new UserDao2();

    public void createUser(User2 user2) throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection()) {
            TransactionManager tm = new TransactionManager(connection);
            tm.beginTransaction();
            try {
                userDao2.InsertarRespuesta(user2);
                tm.commit();
            } catch (SQLException e) {
                tm.rollback();
                throw e;
            }
        }
    }
}
