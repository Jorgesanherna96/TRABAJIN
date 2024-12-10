package model;

import java.util.Arrays;
import java.util.Locale;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;



public class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private int identificador;
    private String titulo;
    private LocalDate fecha; 
    private String contenido;
    private int prioridad; 
    private int duracionEstimada; 
    private boolean completada;

    // Constructor
    public Task(int identificador, String titulo, LocalDate fecha, String contenido, int prioridad, int duracionEstimada, boolean completada) {
        this.identificador = identificador;
        this.titulo = titulo;
        this.fecha = fecha;
        this.contenido = contenido;
        setPrioridad(prioridad); 
        this.duracionEstimada = duracionEstimada;
        this.completada = completada;
    }

    
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
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

    @Override
    public String toString() {
        DateTimeFormatter dateFormat=DateTimeFormatter.ofPattern("dd/MM/yyyy");
         String fechaFormateada = (fecha != null) ? dateFormat.format(fecha) : "Sin fecha";
        return "Task{" +
                "identificador=" + identificador +
                ", titulo='" + titulo + '\'' +
                ", fecha=" + fechaFormateada +
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

    public String getInstanceAsDelimitedString(String delimiter) {
        DateTimeFormatter formato=DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateFormato=fecha.format(formato);
        return String.format(Locale.ENGLISH, "%d%s%s%s%s%s%d%s%d%s%b%s%s",
                identificador, delimiter, titulo, delimiter, contenido, delimiter, prioridad,
                delimiter, duracionEstimada, delimiter, completada, delimiter, dateFormato);
    }
    

    public static Task getTaskFromDelimitedString(String delimitedString, String delimiter) {
        String[] trozos = delimitedString.split(delimiter);
   
        if (trozos.length != 7) {
            
            return null;
        }
    
        try {
            int identificador = Integer.parseInt(trozos[0]);
            String titulo = trozos[1];
            String contenido = trozos[2];
            int prioridad = Integer.parseInt(trozos[3]);
            int duracionEstimada = Integer.parseInt(trozos[4]);
            boolean completada = Boolean.parseBoolean(trozos[5]);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fecha = LocalDate.parse(trozos[6], formatter); 
    
            return new Task(identificador, titulo, fecha, contenido, prioridad, duracionEstimada, completada);
        } catch (Exception e) {
           
            return null;
        }
    }
    
    

    
}
