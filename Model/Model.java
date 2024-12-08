package model;

import java.util.List;

public class Model {
    private final IRepository repository;

    // Constructor que recibe el repositorio
    public Model(IRepository repository) {
        this.repository = repository;
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

    /*public void saveData() {
        repository.saveData();
    }

    public void loadData() {
        repository.loadData();
    }*/
}