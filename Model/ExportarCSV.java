package model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.List;


public class ExportarCSV implements IExporter {


    private  String filePath;
    @Override
    public void exportTasks(List<Task> tasks) throws ExporterException {

        String userHome = System.getProperty("user.home");
        this.filePath = userHome + File.separator + "output.csv";
        List<String> lines = new ArrayList<>();
        
        // Convertimos cada tarea a una cadena delimitada por comas
        for (Task task : tasks) {
            String line = task.getInstanceAsDelimitedString(",");
            lines.add(line); // Añadimos cada línea a la lista
        }
        
        try {
            // Escribimos todas las líneas en el archivo
            Files.write(Path.of(filePath), lines);
        } catch (IOException e) {
            // Si ocurre un error escribiendo el archivo, lanzamos una excepción
            throw new ExporterException("Error al exportar las tareas", e);
        }
    }

    @Override
    public List<Task> importTasks() throws ExporterException {

        
        String userHome = System.getProperty("user.home");
        this.filePath = userHome + File.separator + "output.csv";
       
        List<Task> tasks = new ArrayList<>();
        
        try {
            // Verifica que el archivo exista
            if (!Files.exists(Path.of(filePath))) {
                throw new ExporterException("El archivo CSV no existe.");
            }
            
            // Leemos todas las líneas del archivo
            List<String> lines = Files.readAllLines(Path.of(filePath));
      

            
            if (lines.isEmpty()) {
                throw new ExporterException("El archivo CSV está vacío.");
            }
    
            // Procesamos cada línea y creamos una tarea
            for (String line : lines) {
                Task task = Task.getTaskFromDelimitedString(line, ",");
                if (task != null) {
                    tasks.add(task); // Si la tarea es válida, la agregamos a la lista
                }
            }
        } catch (IOException e) {
            throw new ExporterException("Error al leer el archivo CSV: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new ExporterException("Error al importar las tareas: " + e.getMessage(), e);
        }
        
        return tasks;
    }
    

  
}