import controller.Controller;
import model.BinaryRepository;
import model.Model;
import view.InteractiveView;

public class App {
    public static void main(String[] args) {
    
    
        BinaryRepository repositorio = new BinaryRepository();
        Model modelo = new Model(repositorio);

        InteractiveView vista = new InteractiveView(null); 
        Controller controller = new Controller(modelo, vista); 
        
        controller.start();
    }
}
