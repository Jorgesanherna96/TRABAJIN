package controller;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import model.Model;
import model.Task;
import view.BaseView;

public class Controller {
    private final Model model;
    private final BaseView view;

    // Constructor que recibe el modelo y la vista
    public Controller(Model model, BaseView view) {
        this.model = model;
        this.view = view;
        view.setController(this); // Vincula la vista con el controlador
    }

     
    
    // Método para iniciar la aplicación
    public void start() {
        view.init(); // Inicia la vista
    }

    // Método para finalizar la aplicación
    public void end() {
       // model.saveData(); // Guarda los datos antes de finalizar
        view.end(); // Finaliza la vista
    }

    // Métodos básicos que gestionan el modelo y la vista
    public void crearTarea(int id, String titulo, String contenido, int prioridad, int duracion, String fecha) {
    try {
        // Convertir la fecha ingresada por el usuario a un objeto Date
        Date fechaTarea = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);

        // Crear la nueva tarea
        Task nuevaTarea = new Task(id, titulo, fechaTarea, contenido, prioridad, duracion, false);
        model.agregarTarea(nuevaTarea);

        view.showMessage("Tarea creada con éxito.");
    } catch (ParseException e) {
        view.showErrorMessage("Formato de fecha inválido. Use el formato dd/MM/yyyy.");
    } catch (IllegalArgumentException e) {
        view.showErrorMessage("Error al crear la tarea: " + e.getMessage());
    }
}


    public void listarTareas() {
        var tareas = model.obtenerTareas();
        if (tareas.isEmpty()) {
            view.showMessage("No hay tareas registradas.");
        } else {
            for (var tarea : tareas) {
                view.showMessage(tarea.toString());
            }
        }
    }

    public void eliminarTarea(int id) {
        try {
            model.eliminarTareaPorId(id);
            view.showMessage("Tarea eliminada con éxito.");
        } catch (Exception e) {
            view.showErrorMessage("Error al eliminar la tarea: " + e.getMessage());
        }
    }

    public void modificarTarea(Task tarea) {
        try {
            model.modificarTarea(tarea.getIdentificador(), tarea.getTitulo(), tarea.getContenido(), tarea.getPrioridad());
            view.showMessage("Tarea modificada con éxito.");
        } catch (Exception e) {
            view.showErrorMessage("Error al modificar la tarea: " + e.getMessage());
        }
    }

    public Task obtenerTareaPorId(int id) {
        return model.obtenerTareaPorId(id);
    }

    public void listarTareasIncompletas() {
        var tareas = model.obtenerTareas();
        tareas.stream()
            .filter(t -> !t.isCompletada()) // Filtrar tareas incompletas
            .sorted((t1, t2) -> Integer.compare(t2.getPrioridad(), t1.getPrioridad())) // Ordenar por prioridad
            .forEach(t -> view.showMessage(t.toString())); // Mostrar las tareas
    }

    

    
}
