package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.file.*;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

public class ExportarJSON implements IExporter {
    private final Gson gson;
    private final String filePath;

    public ExportarJSON() {
        String userHome = System.getProperty("user.home");
        this.filePath = userHome + File.separator + "output.json";

        // Configuramos Gson con el adaptador para LocalDate
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
    }

    @Override
    public void exportTasks(List<Task> tasks) throws ExporterException {
        try {
            String json = gson.toJson(tasks); // Serializamos la lista de tareas
            Files.writeString(Path.of(filePath), json); // Guardamos en un archivo
        } catch (IOException e) {
            throw new ExporterException("Error al exportar las tareas a JSON", e);
        }
    }

    @Override
    public List<Task> importTasks() throws ExporterException {
        try {
            String json = Files.readString(Path.of(filePath)); // Leemos el contenido del archivo
            Task[] tasksArray = gson.fromJson(json, Task[].class); // Deserializamos el JSON
            return List.of(tasksArray); // Convertimos a una lista
        } catch (IOException e) {
            throw new ExporterException("Error al leer el archivo JSON", e);
        } catch (Exception e) {
            throw new ExporterException("Error al importar las tareas desde JSON", e);
        }
    }
}
