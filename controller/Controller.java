package controller;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import model.ExporterException;
import model.Model;
import model.RepositoryException;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        // Convertir la cadena de fecha a LocalDate
        LocalDate fechaTarea = LocalDate.parse(fecha, formatter);
        // Crear la nueva tarea
        Task nuevaTarea = new Task(id, titulo, fechaTarea, contenido, prioridad, duracion, false);
        model.agregarTarea(nuevaTarea);

        view.showMessage("Tarea creada con éxito.");
    } 
    catch (RepositoryException e) {
        view.showErrorMessage("Error en el repositorio: " + e.getMessage());
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
        } catch (RepositoryException e) {
            view.showErrorMessage("Error en el repositorio: " + e.getMessage());
        }
    }

    public void modificarTarea(Task tarea) {
        try {
            model.modificarTarea(tarea.getIdentificador(), tarea.getTitulo(), tarea.getContenido(), tarea.getPrioridad());
            view.showMessage("Tarea modificada con éxito.");
        } catch (RepositoryException e) {
            view.showErrorMessage("Error en el repositorio: " + e.getMessage());
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

    

    public void exportarTareas(String formato) {
        try {
            model.setExporter(formato); // Usamos la Factory para configurar el exportador
            model.exportTasks();
            view.showMessage("Exportación a " + formato + " completada con éxito.");
        } catch (ExporterException e) {
            view.showErrorMessage("Error en la exportación: " + e.getMessage());
        } catch (Exception e) {
            view.showErrorMessage("Error inesperado al exportar: " + e.getMessage());
        }
    }

    public void importarTareas(String formato) {
        try {
            // Configuramos el exportador en el modelo
            model.setExporter(formato); 
            
            // Usamos el exportador para importar las tareas
            var tareasImportadas = model.importTasks();
            
            // Agregamos las tareas importadas al repositorio
            if (tareasImportadas.isEmpty()) {
                view.showMessage("No se han encontrado tareas para importar.");
            } else {
                for (Task tarea : tareasImportadas) {
                    model.agregarTarea(tarea); // Agrega la tarea al repositorio
                }
                view.showMessage("Importación desde " + formato + " completada con éxito.");
            }
            
        } catch (ExporterException e) {
            throw new ExporterException("Error al leer el archivo CSV: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new ExporterException("Error al importar las tareas: " + e.getMessage(), e);
        }
    }
    

    
}
