package view.login;

import java.io.IOException;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBException;

import controller.DataManagerController;
import controller.IOController;
import controller.MasterPasswordController;
import exceptions.LoginException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.DataManager;
import view.main.MainViewController;

public class LoginViewController extends Pane{
	
    @FXML
    private Button loginButton;

    @FXML
    private Label welcomeLogin;

    @FXML
    private PasswordField masterPWLoginTextField;

    @FXML
    private Label introTextLogin;

    @FXML
    private Label masterpasswordTextLogin;
	    
	    //Controller
	    private DataManagerController dataManagerController;
	    private IOController ioController;
	    private MasterPasswordController masterPasswordController;
	    
	    //Variables
	    private Stage primaryStage;
    
	    //Constructor
	    public LoginViewController(Stage primaryStage, DataManagerController dataManagerController) {
	    	this.dataManagerController = dataManagerController;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
			loader.setRoot(this);
			loader.setController(this);
			try {
				loader.load();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			dataManagerController.setDataManager(new DataManager());
			this.primaryStage = primaryStage;
			ioController = dataManagerController.getIOController();
			masterPasswordController = dataManagerController.getMasterPasswordController();
			
		}
	    
	    @FXML
	    void login(ActionEvent event) {
	    	loginAct();
	    }
	    
	    void loginAct(){
	    	String masterPWString = masterPWLoginTextField.getText();
	    	try {
				ioController.onImport(masterPWString);
				if (masterPasswordController.checkRemindDateOutdated()) {
	        		JOptionPane.showMessageDialog(null, "Your reminder period has expired. Please remember to update your masterpassword for safety reasons.", "Information", JOptionPane.INFORMATION_MESSAGE);
				}
				Scene scene = new Scene(new MainViewController(primaryStage, dataManagerController));
	       		primaryStage.setScene(scene);
	    	} catch (LoginException e) {
				JOptionPane.showMessageDialog(null, "The masterpassword didn't match", "Failure", JOptionPane.ERROR_MESSAGE);
	    	} catch (JAXBException e) {
				JOptionPane.showMessageDialog(null, "Failed to load data", "Failure", JOptionPane.ERROR_MESSAGE);
	    	}
	    }
	    
	    @FXML
	    void enterAction(KeyEvent event) {
	    	if(event.getCode() == KeyCode.ENTER){
	    		loginAct();
	    	}
	    }
	}
