package model;

import java.util.List;

public class Model {
    private final IRepository repository;
    private IExporter exporter;
    // Constructor que recibe el repositorio
    public Model(IRepository repository) {
        this.repository = repository;
    }

    public void setExporter(String exporter) {
        this.exporter = ExporterFactory.getExporter(exporter);
    }

    // Métodos para gestionar tareas
    public Task agregarTarea(Task t) {
        return repository.addTask(t);
    }

    public void eliminarTarea(Task t) {
        repository.removeTask(t);
    }

    public void eliminarTareaPorId(int id) {
        Task tarea = obtenerTareaPorId(id);
        if (tarea != null) {
            repository.removeTask(tarea);
        } else {
            throw new IllegalArgumentException("No se encontró la tarea con el ID proporcionado.");
        }
    }

    public void modificarTarea(int id, String nuevoTitulo, String nuevoContenido, int nuevaPrioridad) {
        Task tarea = obtenerTareaPorId(id);
        if (tarea != null) {
            tarea.setTitulo(nuevoTitulo);
            tarea.setContenido(nuevoContenido);
            tarea.setPrioridad(nuevaPrioridad);
            repository.modifyTask(tarea);
        } else {
            throw new IllegalArgumentException("No se encontró la tarea con el ID proporcionado.");
        }
    }

    public Task obtenerTareaPorId(int id) {
        List<Task> tareas = repository.getAllTasks();
        for (Task tarea : tareas) {
            if (tarea.getIdentificador() == id) {
                return tarea;
            }
        }
        return null;
    }

    public List<Task> obtenerTareas() {
        return repository.getAllTasks();
    }

    public List<Task> importTasks() {
        if (exporter == null) {
            throw new ExporterException("Exportador no configurado.");
        }
        return exporter.importTasks();
    }

    public void exportTasks() {
        if (exporter == null) {
            throw new ExporterException("Exportador no configurado.");
        }
        exporter.exportTasks(repository.getAllTasks());
    }
}