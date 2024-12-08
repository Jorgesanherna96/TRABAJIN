import controller.Controller;
import model.BinaryRepository;
import model.Model;
import view.InteractiveView;

public class App {
    public static void main(String[] args) {
        // Configurar la ruta del archivo de datos
        String filePath = ".tareas.bin";

        // Crear los componentes principales
        BinaryRepository repository = new BinaryRepository();
        Model modelo = new Model(repository);

        // Crear el controlador junto con la vista
        InteractiveView vista = new InteractiveView(null); // Vista sin controlador inicial
        Controller controller = new Controller(modelo, vista); // El controlador asigna la vista

        // Iniciar la aplicación
        controller.start();
    }
}
