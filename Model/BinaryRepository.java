package model;
import java.util.*;
import java.io.*;


public class BinaryRepository implements IRepository {
    private ArrayList<Task> tareas;
    private final String filePath;

    public BinaryRepository() {
        String userHome = System.getProperty("user.home");
        this.filePath = userHome + File.separator + "tareas.bin";
        this.tareas = new ArrayList<>();
        cargarTareas();
    }
    private void cargarTareas() {
        File file = new File(filePath);
        if (!file.exists() || file.length() == 0) {
            tareas = new ArrayList<>();
            return;
        }
    
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
           Object objeto= ois.readObject();

           if(objeto instanceof ArrayList<?>){
                ArrayList<?> listaTemporal =(ArrayList<?>) objeto;

                for(Object i: listaTemporal) {

                    if(i instanceof Task){

                        Task task=(Task) i;
                        tareas.add(task);
                    }

                    else{

                        throw new RepositoryException("Hay un objeto no valido");
                    }
                }

           }
        } catch (EOFException e) {
            tareas = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            throw new RepositoryException("Error al cargar las tareas desde el archivo binario", e);
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
            tareas.set(i, t); 
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
            oos.writeObject(tareas); 
        } catch (IOException e) {
            throw new RepositoryException("Error al guardar tareas en tareas.bin",e);
        }
    }
    

}
