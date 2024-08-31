package umg.programacion2.service;

import umg.programacion2.dao.CuestionarioDao;
import umg.programacion2.model.Cuestionario;

import java.sql.SQLException;
import java.util.List;

public class CuestionarioService {

    private CuestionarioDao cuestionarioDao;


    public CuestionarioService() {
        try{
            this.cuestionarioDao = new CuestionarioDao();
        } catch (SQLException e) {
            System.out.println("Error al inicializar el DAO   "+e.getMessage());
            e.printStackTrace();
        }

    }



    // Método para agregar un nuevo cuestionario
    public void addCuestionario(Cuestionario cuestionario) {
        try {
            cuestionarioDao.insertCuestionario(cuestionario);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener todos los cuestionarios
    public List<Cuestionario> getAllCuestionarios() {
        try {
            return cuestionarioDao.getAllCuestionarios();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para obtener un cuestionario por su ID
    public Cuestionario getCuestionarioById(int id) {
        try {
            return cuestionarioDao.getCuestionarioById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Método para actualizar un cuestionario existente
    public void updateCuestionario(Cuestionario cuestionario) {
        try {
            cuestionarioDao.updateCuestionario(cuestionario);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar un cuestionario por su ID
    public void deleteCuestionario(int id) {
        try {
            cuestionarioDao.deleteCuestionario(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
