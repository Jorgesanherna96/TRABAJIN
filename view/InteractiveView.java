package view;

import controller.Controller;
import model.Task;

import java.util.Scanner;

public class InteractiveView extends BaseView {
    private final Scanner scanner;

    public InteractiveView(Controller controller) {
        super(controller);
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void init() {
        showMessage("Bienvenido al gestor de tareas.");
        while (true) {
            showMenuPrincipal();
            int opcion = leerOpcion();
            if (opcion == 0) { // Salir
                end();
                break;
            }
            procesarOpcionMenuPrincipal(opcion);
        }
    }

    @Override
    public void showMessage(String message) {
        System.out.println("[INFO] " + message);
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        System.err.println("[ERROR] " + errorMessage);
    }

    @Override
    public void end() {
        showMessage("Gracias por usar el gestor de tareas. ¡Hasta pronto!");
        scanner.close();
    }

    private void showMenuPrincipal() {
        System.out.println("\n--- Menú Principal ---");
        System.out.println("1. Operaciones CRUD");
        System.out.println("2. Exportación/Importación de datos");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private void showMenuCRUD() {
        System.out.println("\n--- Menú CRUD ---");
        System.out.println("1. Crear nueva tarea");
        System.out.println("2. Listar tareas incompletas (ordenadas por prioridad)");
        System.out.println("3. Listar historial completo de tareas");
        System.out.println("4. Ver detalle de una tarea");
        System.out.println("0. Volver al menú principal");
        System.out.print("Seleccione una opción: ");
    }

    private void showMenuDetalleCompletada() {
        System.out.println("\n--- Detalle de la Tarea ---");
        System.out.println("1. Marcar como pendiente");
        System.out.println("2. Modificar tarea");
        System.out.println("3. Eliminar tarea");
        System.out.println("0. Volver al menú CRUD");
        System.out.print("Seleccione una opción: ");
    }

    private void showMenuDetallePendiente() {
        System.out.println("\n--- Detalle de la Tarea ---");
        System.out.println("1. Marcar como completada");
        System.out.println("2. Modificar tarea");
        System.out.println("3. Eliminar tarea");
        System.out.println("0. Volver al menú CRUD");
        System.out.print("Seleccione una opción: ");
    }

    private void showMenuExportImport() {
        System.out.println("\n--- Exportación/Importación ---");
        System.out.println("1. Exportar tareas a JSON");
        System.out.println("2. Exportar tareas a CSV");
        System.out.println("3. Importar tareas desde JSON");
        System.out.println("4. Importar tareas desde CSV");
        System.out.println("0. Volver al menú principal");
        System.out.print("Seleccione una opción: ");
    }

    private int leerOpcion() {
        while (!scanner.hasNextInt()) {
            scanner.next();
            showErrorMessage("Por favor, introduzca un número válido.");
        }
        return scanner.nextInt();
    }

    private void procesarOpcionMenuPrincipal(int opcion) {
        switch (opcion) {
            case 1:
                ejecutarCRUD();
                break;
            case 2:
                ejecutarExportacionImportacion();
                break;
            default:
                showErrorMessage("Opción no válida.");
        }
    }

    private void ejecutarCRUD() {
        while (true) {
            showMenuCRUD();
            int opcion = leerOpcion();
            if (opcion == 0) return; // Volver al menú principal
            procesarOpcionCRUD(opcion);
        }
    }

    private void procesarOpcionCRUD(int opcion) {
        switch (opcion) {
            case 1:
                crearTarea();
                break;
            case 2:
                listarTareasIncompletas();
                break;
            case 3:
                listarHistorialCompleto();
                break;
            case 4:
                gestionarDetalleTarea();
                break;
            default:
                showErrorMessage("Opción no válida.");
        }
    }

    private void gestionarDetalleTarea() {
       try {
        System.out.print("ID de la tarea: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        // Obtener la tarea desde el controlador
        Task tarea = controller.obtenerTareaPorId(id);
        if (tarea == null) {
            showErrorMessage("No se encontró la tarea con el ID proporcionado.");
        } else {
            mostrarMenuDetalle(tarea);
        }
    } catch (Exception e) {
        showErrorMessage("Error en la entrada de datos. Intente nuevamente.");
        scanner.nextLine(); // Limpiar entrada inválida
    }
}

private void listarTareasIncompletas() {
    controller.listarTareasIncompletas();
}

    private void listarHistorialCompleto() {
        controller.listarTareas();
    }

    private void crearTarea() {
        try {
            System.out.print("ID: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
    
            System.out.print("Título: ");
            String titulo = scanner.nextLine();
    
            System.out.print("Contenido: ");
            String contenido = scanner.nextLine();
    
            System.out.print("Prioridad (1-5): ");
            int prioridad = scanner.nextInt();
    
            System.out.print("Duración Estimada (en minutos): ");
            int duracion = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
    
            System.out.print("Fecha (dd/MM/yyyy): ");
            String fecha = scanner.nextLine();
    
            // Llamar al controlador para crear la tarea
            controller.crearTarea(id, titulo, contenido, prioridad, duracion, fecha);
        } catch (Exception e) {
            showErrorMessage("Error "+e.getMessage());
            scanner.nextLine(); // Limpiar entrada inválida
        }
    }
    
    private void mostrarMenuDetalle(Task tarea) {
        while (true) {
            showMessage("\nDetalle de la Tarea:");
            showMessage(tarea.toString()); // Mostrar los datos de la tarea
            if(tarea.isCompletada()){
            showMenuDetalleCompletada();
            }
            else{
            showMenuDetallePendiente();
            }
            
            int opcion = leerOpcion();
    
            if (opcion == 0) return; // Volver al menú CRUD
    
            procesarOpcionDetalle(tarea, opcion);
        }
    }

    private void procesarOpcionDetalle(Task tarea, int opcion) {
        switch (opcion) {
            case 1: // Marcar como completada/pendiente
                boolean estadoActual = tarea.isCompletada();
                tarea.setCompletada(!estadoActual); // Cambiar estado
                controller.modificarTarea(tarea); // Actualizar en el modelo
                showMessage("Tarea marcada como " + (tarea.isCompletada() ? "completada." : "pendiente."));
                break;
    
            case 2: // Modificar tarea
                modificarTarea(tarea);
                break;
    
            case 3: // Eliminar tarea
                controller.eliminarTarea(tarea.getIdentificador());
                showMessage("Tarea eliminada con éxito.");
                return; // Volver al menú CRUD
    
            default:
                showErrorMessage("Opción no válida. Intente nuevamente.");
        }
    }

    private void modificarTarea(Task tarea) {
        try {
            System.out.print("Nuevo título: ");
            String nuevoTitulo = scanner.nextLine();
            System.out.print("Nuevo contenido: ");
            String nuevoContenido = scanner.nextLine();
            System.out.print("Nueva prioridad (1-5): ");
            int nuevaPrioridad = scanner.nextInt();
            scanner.nextLine(); // Limpiar buffer
    
            // Actualizar los valores de la tarea
            tarea.setTitulo(nuevoTitulo);
            tarea.setContenido(nuevoContenido);
            tarea.setPrioridad(nuevaPrioridad);
    
            // Llamar al controlador para guardar los cambios
            controller.modificarTarea(tarea);
            showMessage("Tarea modificada con éxito.");
        } catch (Exception e) {
            showErrorMessage("Error en la entrada de datos. Intente nuevamente.");
            scanner.nextLine(); // Limpiar entrada inválida
        }
    }

    private void ejecutarExportacionImportacion() {
        while (true) {
            showMenuExportImport();
            int opcion = leerOpcion();
            if (opcion == 0) return; // Volver al menú principal
            procesarOpcionExportacionImportacion(opcion);
        }
    }

    private void procesarOpcionExportacionImportacion(int opcion) {
        switch (opcion) {
            case 1:
                exportarJSON();
                break;
            case 2:
                exportarCSV();
                break;
            case 3:
                importarJSON();
                break;
            case 4:
                importarCSV();
                break;
            default:
                showErrorMessage("Opción no válida.");
        }
    }

    private void exportarJSON() {
        try {
            controller.exportarTareas("JSON");
        } catch (Exception e) {
            showErrorMessage("Error al exportar a JSON: " + e.getMessage());
        }
    }

    private void exportarCSV() {
        try {
            controller.exportarTareas("CSV");
        } catch (Exception e) {
            showErrorMessage("Error al exportar a CSV: " + e.getMessage());
        }
    }

    private void importarJSON() {
        try {
            controller.importarTareas("JSON");
        } catch (Exception e) {
            showErrorMessage("Error al importar desde JSON: " + e.getMessage());
        }
    }

    private void importarCSV() {
        try {
            controller.importarTareas("CSV");
        } catch (Exception e) {
            showErrorMessage("Error al importar desde CSV: " + e.getMessage());
        }
    }
}
