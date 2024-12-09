package model;
import java.util.*;
import java.io.*;

public class BinaryRepository implements IRepository {
    private ArrayList<Task> tareas;
    private final String filePath;

    public BinaryRepository() {
        String userHome = System.getProperty("user.home");
        this.filePath = userHome + File.separator + "tareas.bin"; // Ruta completa al archivo
        this.tareas = new ArrayList<>();
        cargarTareas();
    }

    private void cargarTareas(){

        File file=new File(filePath);
            if(!file.exists()){

                return;
            }
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                tareas = (ArrayList<Task>) ois.readObject(); // Cargar las tareas
            } catch (IOException | ClassNotFoundException e) {
                throw new RepositoryException("Error al cargar las tareas desde el tareas.bin",e);
                
            }
        
    }

    @Override
    public Task addTask(Task t) {
       

        for(Task tarea:tareas){

            if(tarea.equals(t)){

                throw new RepositoryException("Ya existe una tarea con este identificador.");
            }
        }
        tareas.add(t);
        guardarTareas();
        return t;

       
    }
    
    @Override
    public void removeTask(Task t) {
        if (!tareas.remove(t)) {
            throw new RepositoryException("No se encontró la tarea para eliminar.");
        }
        guardarTareas();
    }
    
    @Override
    public void modifyTask(Task t) {
        for (int i = 0; i < tareas.size(); i++) {
        if(tareas.get(i).equals(t)){
            //Aqui se modifica
            tareas.set(i, t); // Reemplazar la tarea
            guardarTareas(); 
            return;
        }
        }
        
        throw new RepositoryException("No se encontró la tarea para modificar.");
        
    }
    
    @Override
    public List<Task> getAllTasks(){

        return  tareas;
    }
    
    private void guardarTareas() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(tareas); // Guardar la lista de tareas en el archivo
        } catch (IOException e) {
            throw new RepositoryException("Error al guardar tareas en tareas.bin",e);
        }
    }
    

}
