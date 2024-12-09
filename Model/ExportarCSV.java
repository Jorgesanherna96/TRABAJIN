package model;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ExportarCSV implements IExporter {

    @Override
    public void exportTasks(List<Task> tasks, String filePath) throws ExporterException {
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
    public List<Task> importTasks(String filePath) throws ExporterException {
        List<Task> tasks = new ArrayList<>();
        
        try {
            // Leemos todas las líneas del archivo
            List<String> lines = Files.readAllLines(Path.of(filePath));
            
            // Procesamos cada línea y creamos una tarea
            for (String line : lines) {
                Task task = Task.getTaskFromDelimitedString(line, ","); // Usamos coma como delimitador
                if (task != null) {
                    tasks.add(task); // Si la tarea es válida, la agregamos a la lista
                }
            }
        } catch (IOException e) {
            // Si ocurre un error leyendo el archivo, lanzamos una excepción
            throw new ExporterException("Error al importar las tareas", e);
        }
        
        return tasks;
    }
}