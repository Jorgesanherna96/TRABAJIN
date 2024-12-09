package model;
import java.util.*;


public interface IExporter {
    void exportTasks(List<Task> tasks, String filePath) throws ExporterException;
    List<Task> importTasks(String filePath) throws ExporterException;
}
