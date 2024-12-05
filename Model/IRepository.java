package Model;
import java.util.List;

public interface IRepository {
Task addTask(Task t);

void removeTask(Task t);

void modifyTask(Task t);

List<Task> getAllTasks();
}
