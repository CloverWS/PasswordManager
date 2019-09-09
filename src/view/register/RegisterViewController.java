package view.register;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.time.Period;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBException;

import controller.DataManagerController;
import controller.IOController;
import controller.MasterPasswordController;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.DataManager;
import model.MasterPassword;
import view.main.MainViewController;

public class RegisterViewController extends Pane {

    @FXML
    private PasswordField masterPWRegisterTextField;
    
    @FXML
    private PasswordField masterPWRegisterRepeatTextField;

    @FXML
    private ChoiceBox<String> reminderChoiceBox;

    @FXML
    private Button registerButton;
    
    //Controller
    private DataManagerController dataManagerController;
    private MasterPasswordController masterPasswordController;
    private IOController ioController;
    
    //Variables
    private Stage primaryStage;
    private Period reminder;
    
    //Constructor
    public RegisterViewController(Stage primaryStage, DataManagerController dataManagerController) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("RegisterView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		this.primaryStage = primaryStage;
		this.dataManagerController = dataManagerController;
		masterPasswordController = dataManagerController.getMasterPasswordController();
		ioController = dataManagerController.getIOController();
		dataManagerController.setDataManager(new DataManager());
		reminder = Period.of(0,0,0);
	}

    @FXML
    public void initialize() {
        reminderChoiceBox.getItems().add("One week");
        reminderChoiceBox.getItems().add("Two weeks");
        reminderChoiceBox.getItems().add("Four weeks");
        reminderChoiceBox.getItems().add("Three months");
        reminderChoiceBox.getItems().add("Six months");
        reminderChoiceBox.getItems().add("One year");
        reminderChoiceBox.getSelectionModel().select(0);
    }
    
    @FXML
    void register(ActionEvent event) {
    	registerAct();

    }
    void registerAct(){
    	String masterPWString = masterPWRegisterTextField.getText();
    	String masterPWRepeatedString = masterPWRegisterRepeatTextField.getText();
    	int checked = reminderChoiceBox.getSelectionModel().getSelectedIndex();
    	switch (checked) {
    	case 0: reminder = reminder.plusDays(7); break;
    	case 1: reminder = reminder.plusDays(14); break;
    	case 2: reminder = reminder.plusDays(28); break;
    	case 3: reminder = reminder.plusMonths(3); break;
    	case 4: reminder = reminder.plusMonths(6); break;
    	case 5: reminder = reminder.plusYears(1); break;
    	}
    	MasterPassword masterPW = new MasterPassword (masterPWString, reminder);
    	boolean secure = masterPasswordController.isSecure(masterPWString);
    	if(masterPWString.equals(masterPWRepeatedString)) {
        	if(secure) {	
        		masterPasswordController.createPassword(masterPW);
        		try {
        			ioController.exportData();
        		} catch (JAXBException e) {
					JOptionPane.showMessageDialog(null, "Failed to save data", "Failure", JOptionPane.ERROR_MESSAGE);
				}
        		JOptionPane.showMessageDialog(null, "Your registration was succesfull!", "Information", JOptionPane.INFORMATION_MESSAGE);
        		Scene scene = new Scene(new MainViewController(primaryStage, dataManagerController));
        		primaryStage.setScene(scene);
        	} else {
        		JOptionPane.showMessageDialog(null, "Your password doesn't fulfil the safety measures", "Failure", JOptionPane.ERROR_MESSAGE);
        	}
    	} else {
    		JOptionPane.showMessageDialog(null, "The passwords do not match", "Failure", JOptionPane.ERROR_MESSAGE);
    	}
    }
    @FXML
    void enterAction(KeyEvent event) {
    	if(event.getCode() == KeyCode.ENTER){
    		registerAct();
    	}
    }

}
