package application;


import javax.xml.bind.JAXBException;

import controller.DataManagerController;
import controller.IOController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.login.LoginViewController;
import view.register.RegisterViewController;


public class Main extends Application {
	static final int HEIGHT = 800;
	static final int WIDTH = 1200;
	
	//Start des Programms, Testen ob zum ersten Mal gestartet wird
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("ParadiseKey");
		primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("paradise_key_logo.png")));
		DataManagerController dataManagerController = new DataManagerController();
		IOController ioController = dataManagerController.getIOController();
		try {
			if (ioController.alreadyStarted()) {
				LoginViewController loginView = new LoginViewController(primaryStage, dataManagerController);
				Scene scene = new Scene(loginView);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setScene(scene);
			} else {
				RegisterViewController registerView = new RegisterViewController(primaryStage, dataManagerController);
				Scene scene = new Scene(registerView);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				primaryStage.setScene(scene);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		primaryStage.show();

		primaryStage.setResizable(false);
		
		
		//Sicherheitsabfrage zum Speichern  
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {

		    @Override
		    public void handle(WindowEvent event) {
		    	//Test ob das MainFenster geöffnet ist
		    	if(primaryStage.getHeight() >= 800) {
		    	String res = FxDialog.showConfirm("Do you want to save possible changes before you quit?", "", FxDialog.YES, FxDialog.NO);
		    		if (res.equals(FxDialog.YES)) {
		    			try {
	    					dataManagerController.getIOController().exportData();
	    					System.exit(0);
	    				} catch (JAXBException e) {
	    					FxDialog.showError("Failed to save data", "Failure");
	    				}
		    		} else if (res.equals(FxDialog.NO)) {
		    			System.exit(0);
		    		}
		    	}
		    }
		});
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
