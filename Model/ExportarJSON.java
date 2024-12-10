package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

public class ExportarJSON implements IExporter {
    private final Gson gson;
    private final String filePath;

    public ExportarJSON() {
        String userHome = System.getProperty("user.home");
        this.filePath = userHome + File.separator + "output.json";

        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    @Override
    public void exportTasks(List<Task> tasks) throws ExporterException {
        try {
            String json = gson.toJson(tasks); 
            Files.writeString(Path.of(filePath), json); 
        } catch (IOException e) {
            throw new ExporterException("Error al exportar las tareas a JSON", e);
        }
    }

    @Override
    public List<Task> importTasks() throws ExporterException {
        try {
            String json = Files.readString(Path.of(filePath)); 
            Task[] tasksArray = gson.fromJson(json, Task[].class); 
            List <Task> importedTasks = new ArrayList<>(List.of(tasksArray));
            importedTasks.sort(Comparator.comparing(Task::getFecha).reversed());

            return importedTasks;
        } catch (IOException e) {
            throw new ExporterException("Error al leer el archivo JSON", e);
        } catch (Exception e) {
            throw new ExporterException("Error al importar las tareas desde JSON", e);
        }
    }
}


