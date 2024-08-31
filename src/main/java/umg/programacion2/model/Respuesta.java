package umg.programacion2.model;


import java.sql.Timestamp;

public class Respuesta {
    private int id;
    private String seccion;
    private long telegramId;
    private int preguntaId;
    private String respuestaTexto;
    private Timestamp fechaRespuesta;

    // Constructor vacío
    public Respuesta() {}

    // Constructor con parámetros
    public Respuesta(int id, String seccion, long telegramId, int preguntaId, String respuestaTexto, Timestamp fechaRespuesta) {
        this.id = id;
        this.seccion = seccion;
        this.telegramId = telegramId;
        this.preguntaId = preguntaId;
        this.respuestaTexto = respuestaTexto;
        this.fechaRespuesta = fechaRespuesta;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public long getTelegramId() {
        return telegramId;
    }

    public void setTelegramId(long telegramId) {
        this.telegramId = telegramId;
    }

    public int getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(int preguntaId) {
        this.preguntaId = preguntaId;
    }

    public String getRespuestaTexto() {
        return respuestaTexto;
    }

    public void setRespuestaTexto(String respuestaTexto) {
        this.respuestaTexto = respuestaTexto;
    }

    public Timestamp getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(Timestamp fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }

    @Override
    public String toString() {
        return "Respuesta{" +
                "id=" + id +
                ", seccion='" + seccion + '\'' +
                ", telegramId=" + telegramId +
                ", preguntaId=" + preguntaId +
                ", respuestaTexto='" + respuestaTexto + '\'' +
                ", fechaRespuesta=" + fechaRespuesta +
                '}';
    }
}
