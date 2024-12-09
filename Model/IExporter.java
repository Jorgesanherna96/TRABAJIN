package model;
import java.util.*;


public interface IExporter {
    void exportTasks(List<Task> tasks) throws ExporterException;
    List<Task> importTasks() throws ExporterException;
}
