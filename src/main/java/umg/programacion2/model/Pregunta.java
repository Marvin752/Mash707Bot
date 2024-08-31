package umg.programacion2.model;

public class Pregunta {
    private int id;
    private int cuestionarioId;
    private String preguntaTexto;

    // Constructor por defecto
    public Pregunta() {}

    // Constructor con parámetros
    public Pregunta(int id, int cuestionarioId, String preguntaTexto) {
        this.id = id;
        this.cuestionarioId = cuestionarioId;
        this.preguntaTexto = preguntaTexto;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCuestionarioId() {
        return cuestionarioId;
    }

    public void setCuestionarioId(int cuestionarioId) {
        this.cuestionarioId = cuestionarioId;
    }

    public String getPreguntaTexto() {
        return preguntaTexto;
    }

    public void setPreguntaTexto(String preguntaTexto) {
        this.preguntaTexto = preguntaTexto;
    }

    @Override
    public String toString() {
        return "Pregunta{" +
                "id=" + id +
                ", cuestionarioId=" + cuestionarioId +
                ", preguntaTexto='" + preguntaTexto + '\'' +
                '}';
    }
}