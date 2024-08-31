package umg.programacion2.service;

import umg.programacion2.dao.RespuestaDao;
import umg.programacion2.model.Respuesta;

import java.sql.SQLException;
import java.util.List;

public class RespuestaService {
    private RespuestaDao respuestaDao;

    // Constructor que inicializa el DAO
    public RespuestaService() {
        this.respuestaDao = new RespuestaDao();
    }

    // Método para guardar una respuesta
    public void saveRespuesta(Respuesta respuesta) {
        try {
            respuestaDao.save(respuesta);
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar el error
        }
    }






    // Método para obtener una respuesta por su ID
    public Respuesta getRespuestaById(int id) {
        try {
            return respuestaDao.getById(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para obtener todas las respuestas
    public List<Respuesta> getAllRespuestas() {
        try {
            return respuestaDao.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para actualizar una respuesta
    public void updateRespuesta(Respuesta respuesta) {
        try {
            respuestaDao.update(respuesta);
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar el error
        }
    }

    // Método para eliminar una respuesta por su ID
    public void deleteRespuesta(int id) {
        try {
            respuestaDao.delete(id);
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar el error
        }
    }
}