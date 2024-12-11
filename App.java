import controller.Controller;
import model.BinaryRepository;
import model.Model;
import model.NotionRepository;
import model.IRepository;
import view.InteractiveView;

public class App {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("[ERROR] Debe especificar el repositorio con el argumento --repository.");
            return;
        }

        // Parseamos los argumentos
        String repositoryType = null;
        String apiKey = null;
        String databaseId = null;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--repository":
                    if (i + 1 < args.length) {
                        repositoryType = args[++i];
                    } else {
                        System.err.println("[ERROR] Debe especificar el tipo de repositorio después de --repository.");
                        return;
                    }
                    break;
                default:
                    // Manejar posibles valores para Notion
                    if (repositoryType != null && repositoryType.equalsIgnoreCase("notion")) {
                        if (apiKey == null) {
                            apiKey = args[i];
                        } else if (databaseId == null) {
                            databaseId = args[i];
                        }
                    }
                    break;
            }
        }

        // Validar el tipo de repositorio
        if (repositoryType == null) {
            System.err.println("[ERROR] El argumento --repository es obligatorio.");
            return;
        }

        IRepository repository;
        try {
            switch (repositoryType.toLowerCase()) {
                case "bin":
                    System.out.println("[INFO] Usando BinaryRepository.");
                    repository = new BinaryRepository();
                    break;

                case "notion":
                    if (apiKey == null || databaseId == null) {
                        System.err.println("[ERROR] Debe proporcionar API_KEY y DATABASE_ID para el repositorio Notion.");
                        return;
                    }
                    System.out.println("[INFO] Usando NotionRepository.");
                    repository = new NotionRepository(apiKey, databaseId);
                    break;

                default:
                    System.err.println("[ERROR] Tipo de repositorio no reconocido: " + repositoryType);
                    return;
            }

            // Crear el modelo con el repositorio configurado
            Model model = new Model(repository);

            // Crear la vista y el controlador
            InteractiveView view = new InteractiveView(null);
            Controller controller = new Controller(model, view);

            // Iniciar la aplicación
            controller.start();

        } catch (Exception e) {
            System.err.println("[ERROR] Ocurrió un error al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
