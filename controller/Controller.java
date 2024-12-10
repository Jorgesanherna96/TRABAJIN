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

    
    public Controller(Model model, BaseView view) {
        this.model = model;
        this.view = view;
        view.setController(this); 
    }

     
    
    public void start() {
        view.init(); 
    }

    public void end() {
        view.end(); 
    }

    public void crearTarea(int id, String titulo, String contenido, int prioridad, int duracion, String fecha) {
    try {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        LocalDate fechaTarea = LocalDate.parse(fecha, formato);

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
            .filter(t -> !t.isCompletada()) 
            .sorted((t1, t2) -> Integer.compare(t2.getPrioridad(), t1.getPrioridad())) 
            .forEach(t -> view.showMessage(t.toString())); 
    }

    

    public void exportarTareas(String formato) {
        try {
            model.setExporter(formato); 
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
            model.setExporter(formato); 
            
            var tareasImportadas = model.importTasks();
            
            if (tareasImportadas.isEmpty()) {
                view.showMessage("No se han encontrado tareas para importar.");
            } else {
                for (Task tarea : tareasImportadas) {
                    model.agregarTarea(tarea); 
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
