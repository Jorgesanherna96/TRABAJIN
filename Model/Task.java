package Model;

import java.util.Date;

public class Task {
    private int identificador;
    private String titulo;
    private Date fecha; // Usar java.util.Date
    private String contenido;
    private int prioridad; // Rango entre 1 y 5
    private int duracionEstimada; // En minutos
    private boolean completada;

    // Constructor
    public Task(int identificador, String titulo, Date fecha, String contenido, int prioridad, int duracionEstimada, boolean completada) {
        this.identificador = identificador;
        this.titulo = titulo;
        this.fecha = fecha;
        this.contenido = contenido;
        setPrioridad(prioridad); // Validación en el setter
        this.duracionEstimada = duracionEstimada;
        this.completada = completada;
    }

    // Getters y Setters
    public int getIdentificador() {
        return identificador;
    }

    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        if (prioridad < 1 || prioridad > 5) {
            throw new IllegalArgumentException("La prioridad debe estar entre 1 y 5.");
        }
        this.prioridad = prioridad;
    }

    public int getDuracionEstimada() {
        return duracionEstimada;
    }

    public void setDuracionEstimada(int duracionEstimada) {
        this.duracionEstimada = duracionEstimada;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    // Método toString
    @Override
    public String toString() {
        return "Task{" +
                "identificador=" + identificador +
                ", titulo='" + titulo + '\'' +
                ", fecha=" + fecha +
                ", contenido='" + contenido + '\'' +
                ", prioridad=" + prioridad +
                ", duracionEstimada=" + duracionEstimada +
                ", completada=" + completada +
                '}';
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + identificador;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Task other = (Task) obj;
        if (identificador != other.identificador)
            return false;
        return true;
    }

    
}
