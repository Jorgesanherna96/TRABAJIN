package view;
import controller.*;

public abstract class BaseView {

    protected Controller controller;

    public BaseView(Controller controller) {
        this.controller = controller;
    }

    
    public abstract void init();

    public abstract void showMessage(String message);

    public abstract void showErrorMessage(String errormessage);

    public abstract void end();

    public void setController(Controller controller){

        this.controller=controller;
    }



}
