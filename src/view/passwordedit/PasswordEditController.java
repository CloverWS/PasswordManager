package view.passwordedit;
import java.io.IOException;
import java.net.URL;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;
//import javax.xml.bind.JAXBException;
//import application.FxDialog;
import controller.DataManagerController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.SecurityInfo;
import model.UserPassword;
import view.main.MainViewController;
import view.passwordsListView.PasswordModelView;
public class PasswordEditController extends Pane implements Initializable {
	private UserPassword oldPassword;
	private UserPassword newPassword;
	private DataManagerController dmc;
	@FXML
	private Pane pane;
	@FXML
	private VBox vBox;
	
    @FXML
    private Text passwordInfoLabel;
	@FXML
	private Text textPassword;
	@FXML
	private PasswordField passwordFieldPassword;
	@FXML
	private TextField textFieldPassword;
	@FXML
	private Button buttonPassword;
	@FXML
	private TextField textFieldApplication;
	@FXML
	private Text textApplication;
	@FXML
	private TextField textFieldUsername;
	@FXML
	private Text textUsername;
	@FXML
	private TextField textFieldLink;
	@FXML
	private Text textLink;
	@FXML
	private TextField textFieldCategory;
	@FXML
	private Text textCategory;
	@FXML
	private TextField textFieldNote;
	@FXML
	private Text textNote;
	@FXML
	private TextField textFieldSecurityQuestion1;
	@FXML
	private Text textSecurityInfo;
	@FXML
	private TextField textFieldSecurityAnswer1;
	@FXML
	private TextField textFieldSecurityQuestion2;
	@FXML
	private Text textSecurityInfo1;
	@FXML
	private TextField textFieldSecurityAnswer2;
	@FXML
	private Text textSecurityInfo2;
	@FXML
	private TextField textFieldSecurityAnswer3;
	@FXML
	private TextField textFieldSecurityQuestion3;
	@FXML
	private Button buttonCancel;
	@FXML
	private Button buttonSave;
	@FXML
	private Button buttonEdit;
	@FXML
    private Button editCategoryButton;
	@FXML
	private ImageView imageViewPassword;
	private boolean editMode;
//	private FXMLLoader loader;
	private PasswordModelView passwordMV;
	private MainViewController mvc;
	@FXML
	private Button buttonDelete;
	private boolean isTextField;
	private ResourceBundle bundle;
	private Locale locale;
	@FXML
	private ChoiceBox<String> choiceBoxReminder;
	@FXML
	private Text textReminder;
    @FXML
    private Button buttonGeneratePassword;
    private PasswordCategoryEditController passwordCategory;
    private Stage categoryStage;
	public PasswordEditController(DataManagerController dmc, MainViewController mvc) {
		this.oldPassword = null;
		this.dmc = dmc;
		editMode = false;
		isTextField = false;
		this.mvc = mvc;
		initCategoryEditWindow();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("PasswordEditView4.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	public void initCategoryEditWindow(){
		passwordCategory = new PasswordCategoryEditController(dmc);
		dmc.getCategoryController().setPasswordCategoryView(passwordCategory);
		categoryStage = new Stage();
		Scene scene = new Scene(passwordCategory);
   		categoryStage.setScene(scene);
   		centerScreen();
   		categoryStage.hide();
		categoryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent event) {
		    	editCategoryButton.setVisible(true);
		    }
		});
	}
	public void centerScreen() {
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		categoryStage.setX((primScreenBounds.getWidth() - this.getPrefWidth()) / 2);
		categoryStage.setY((primScreenBounds.getHeight() - this.getPrefHeight()) / 2);
	}
	public void load(PasswordModelView password) {
		editMode = false;
		notEditable();
		passwordMV = password;
		oldPassword = password.getPassword();
		setOldPasswordAttributes();
	}
	void setOldPasswordAttributes() {
		textFieldPassword.setText(oldPassword.getPassword());
		passwordFieldPassword.setText(oldPassword.getPassword());
		textFieldApplication.setText(oldPassword.getApplication());
		textFieldLink.setText(oldPassword.getUrl());
		textFieldUsername.setText(oldPassword.getUsername());
		textFieldNote.setText(oldPassword.getNote());
		switch (oldPassword.getReminder().toString()) {
		case "P0Y0M7D":
			choiceBoxReminder.getSelectionModel().select(0);
			break;
		case "P0Y0M14D":
			choiceBoxReminder.getSelectionModel().select(1);
			break;
		case "P0Y0M28D":
			choiceBoxReminder.getSelectionModel().select(2);
			break;
		case "P0Y3M0D":
			choiceBoxReminder.getSelectionModel().select(3);
			break;
		case "P0Y6M0D":
			choiceBoxReminder.getSelectionModel().select(4);
			break;
		case "P1Y0M0D":
			choiceBoxReminder.getSelectionModel().select(5);
			break;
		}
		//get and show security info
		ArrayList<SecurityInfo> info;
		if (oldPassword.getSecurityInfo() != null) {
			info = new ArrayList<>(oldPassword.getSecurityInfo());
		} else {
			info = new ArrayList<>();
		}
		for (int i = 0; i < info.size(); i++) {
			SecurityInfo current = info.get(i);
			if(i == 0) {
				textFieldSecurityQuestion1.setText(current.getSQuestion());
				textFieldSecurityAnswer1.setText(current.getSAnswer());
			}else if(i == 1) {
				textFieldSecurityQuestion2.setText(current.getSQuestion());
				textFieldSecurityAnswer2.setText(current.getSAnswer());
			}else {
				textFieldSecurityQuestion3.setText(current.getSQuestion());
				textFieldSecurityAnswer3.setText(current.getSAnswer());
			}		
		}
		textFieldCategory.setText(dmc.getCategoryController().getCategoryPath(oldPassword));
	}
	@FXML
	public void buttonEditClicked(ActionEvent event) {
		buttonSave.setVisible(true);
		buttonCancel.setVisible(true);
		buttonDelete.setVisible(true);
		buttonGeneratePassword.setVisible(true);
		editCategoryButton.setVisible(true);
		passwordFieldPassword.setEditable(true);
		textFieldPassword.setEditable(true);
		textFieldApplication.setEditable(true);
		textFieldLink.setEditable(true);
		textFieldUsername.setEditable(true);
		textFieldNote.setEditable(true);
		choiceBoxReminder.setDisable(false);
		textFieldSecurityQuestion1.setEditable(true);
		textFieldSecurityQuestion2.setEditable(true);
		textFieldSecurityQuestion3.setEditable(true);
		textFieldSecurityAnswer1.setEditable(true);
		textFieldSecurityAnswer2.setEditable(true);
		textFieldSecurityAnswer3.setEditable(true);
		editMode = true;
		newPassword = new UserPassword("", null);
	}
	@FXML
	void cancel(ActionEvent event) {
		if(oldPassword==null){
			notEditable();
			editMode = false;
		}else{
			setOldPasswordAttributes();
			notEditable();
			editMode = false;
		}
		if(categoryStage.isShowing()){
			categoryStage.hide();
		}
		editCategoryButton.setVisible(false);
		
	}
	@FXML
	void generatePasswordClicked(ActionEvent event) {
		String generated = dmc.getUserPasswordController().generatePassword();
		textFieldPassword.setText(generated);
		passwordFieldPassword.setText(generated);
	}
	@FXML
	void save(ActionEvent event) {
		String password;
		if (isTextField == true) {
			password = textFieldPassword.getText();
			passwordFieldPassword.setText(password);
		} else {
			password = passwordFieldPassword.getText();
			textFieldPassword.setText(password);
		}
		newPassword.setPassword(password);
		newPassword.setApplication(textFieldApplication.getText());
		newPassword.setUrl(textFieldLink.getText());
		newPassword.setUsername(textFieldUsername.getText());
		newPassword.setNote(textFieldNote.getText());
		Collection<SecurityInfo> securityInfo = new ArrayList<SecurityInfo>();
		SecurityInfo secure1 = new SecurityInfo(textFieldSecurityQuestion1.getText(),
				textFieldSecurityAnswer1.getText());
		SecurityInfo secure2 = new SecurityInfo(textFieldSecurityQuestion2.getText(),
				textFieldSecurityAnswer2.getText());
		SecurityInfo secure3 = new SecurityInfo(textFieldSecurityQuestion3.getText(),
				textFieldSecurityAnswer3.getText());
		securityInfo.add(secure1);
		securityInfo.add(secure2);
		securityInfo.add(secure3);
		newPassword.setSecurityInfo(securityInfo);
		int checked = choiceBoxReminder.getSelectionModel().getSelectedIndex();
		switch (checked) {
		case 0:
			newPassword.setReminder(Period.of(0, 0, 7));
			break;
		case 1:
			newPassword.setReminder(Period.of(0, 0, 14));
			break;
		case 2:
			newPassword.setReminder(Period.of(0, 0, 28));
			break;
		case 3:
			newPassword.setReminder(Period.of(0, 3, 0));
			break;
		case 4:
			newPassword.setReminder(Period.of(0, 6, 0));
			break;
		case 5:
			newPassword.setReminder(Period.of(1, 0, 0));
			break;
		default:
			newPassword.setReminder(Period.ZERO);
			break;
		}
		editMode = false;
		if (oldPassword == null) {
			if (!newPassword.getPassword().isEmpty()) {
				if (!dmc.getUserPasswordController().isSecure(newPassword.getPassword())) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warnung");
					alert.setHeaderText("Das Passwort ist unsicher");
					alert.setContentText("Sie sollten ihr Passwort nochmal überarbeiten.");
					alert.showAndWait();
				}
				dmc.getUserPasswordController().createPassword(newPassword);
				PasswordModelView newPasswordMV = new PasswordModelView(newPassword);
				mvc.getHomeView().addPasswordToList(newPasswordMV);

			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warnung");
				alert.setHeaderText("Es wurde kein Passwort eingegeben");
				alert.setContentText("Geben Sie ein gültiges Passwort ein.");
				alert.showAndWait();
				editMode = true;
			}
		} else {
			if (!newPassword.getPassword().isEmpty()) {
				if (!dmc.getUserPasswordController().isSecure(newPassword.getPassword())) {
					Alert alert = new Alert(AlertType.WARNING);
					alert.setTitle("Warnung");
					alert.setHeaderText("Das Passwort ist unsicher");
					alert.setContentText("Sie sollten Ihr Passwort nochmal überarbeiten.");
					alert.showAndWait();
				}
				dmc.getUserPasswordController().modifyPassword(oldPassword, newPassword);
				passwordMV.setApplication(oldPassword.getApplication());
				passwordMV.setUrl(oldPassword.getUrl());
				this.mvc.getHomeView().refresh();
			} else {
				Alert alert = new Alert(AlertType.WARNING);
				alert.setTitle("Warnung");
				alert.setHeaderText("Es wurde kein Passwort eingegeben");
				alert.setContentText("Geben Sie ein gültiges Passwort ein.");
				alert.showAndWait();
				editMode = true;
			}
		}
		if (editMode == false) {
			notEditable();
		} else {
			editCategoryButton.setVisible(true);
		}
		if(categoryStage.isShowing()){
			categoryStage.hide();
		}
	}
	@FXML
	void passwordVisiblity(ActionEvent event) {
		if (textFieldPassword.isVisible()) {
			textFieldPassword.setVisible(false);
			passwordFieldPassword.setText(textFieldPassword.getText());
			passwordFieldPassword.setVisible(true);
			isTextField = false;
		} else {
			textFieldPassword.setVisible(true);
			textFieldPassword.setText(passwordFieldPassword.getText());
			passwordFieldPassword.setVisible(false);
			isTextField = true;
		}
	}
	@FXML
	void remove(ActionEvent event) {
		if(oldPassword!=null){
			dmc.getUserPasswordController().deletePassword(oldPassword);
			mvc.getHomeView().removePassword(passwordMV);
			clearAll();
		}
	}
	@FXML
    void showEditCategory(ActionEvent event) {
		editCategoryButton.setVisible(false);
		if(oldPassword !=null){
			passwordCategory.load(oldPassword);
		} else {
			passwordCategory.load(newPassword);
		}
		categoryStage.show();
		}
	public void loadLanguage(String lang) {
		locale = new Locale(lang);
		bundle = ResourceBundle.getBundle("view.setting.bundles.settings", locale);
		textPassword.setText(bundle.getString("textPassword"));
		textApplication.setText(bundle.getString("textApplication"));
		textLink.setText(bundle.getString("textLink"));
		textCategory.setText(bundle.getString("textCategory"));
		textNote.setText(bundle.getString("textNote"));
		textSecurityInfo.setText(bundle.getString("textSecurityInfo"));
		textSecurityInfo1.setText(bundle.getString("textSecurityInfo1"));
		textSecurityInfo2.setText(bundle.getString("textSecurityInfo2"));
		textUsername.setText(bundle.getString("textUsername"));
		buttonSave.setText(bundle.getString("saveBtn"));
		buttonCancel.setText(bundle.getString("cancelBtn"));
		buttonDelete.setText(bundle.getString("deleteBtn"));
		textReminder.setText(bundle.getString("textReminder"));
		passwordInfoLabel.setText(bundle.getString("textFieldPasswordInfo"));
		textFieldSecurityQuestion1.setPromptText(bundle.getString("firstQuestionPromptText"));
		textFieldSecurityQuestion2.setPromptText(bundle.getString("secondQuestionPromptText"));
		textFieldSecurityQuestion3.setPromptText(bundle.getString("thirdQuestionPromptText"));
		textFieldSecurityAnswer1.setPromptText(bundle.getString("firstAnswerPromptText"));
		textFieldSecurityAnswer2.setPromptText(bundle.getString("secondAnswerPromptText"));
		textFieldSecurityAnswer3.setPromptText(bundle.getString("thirdAnswerPromptText"));
	}
	public void setColor(Color color) {
		buttonSave.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		buttonCancel.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		buttonEdit.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		buttonEdit.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		buttonPassword.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		buttonGeneratePassword.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		editCategoryButton.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		buttonDelete.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		choiceBoxReminder.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");

	}
	public void notEditable() {
		buttonSave.setVisible(false);
		buttonCancel.setVisible(false);
		buttonDelete.setVisible(false);
		buttonGeneratePassword.setVisible(false);
		editCategoryButton.setVisible(false);
		passwordFieldPassword.setEditable(false);
		textFieldPassword.setEditable(false);
		textFieldApplication.setEditable(false);
		textFieldLink.setEditable(false);
		textFieldUsername.setEditable(false);
		textFieldNote.setEditable(false);
		choiceBoxReminder.setDisable(true);
		textFieldSecurityQuestion1.setEditable(false);
		textFieldSecurityQuestion2.setEditable(false);
		textFieldSecurityQuestion3.setEditable(false);
		textFieldSecurityAnswer1.setEditable(false);
		textFieldSecurityAnswer2.setEditable(false);
		textFieldSecurityAnswer3.setEditable(false);
		textFieldPassword.setVisible(false);
		passwordFieldPassword.setVisible(true);
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		choiceBoxReminder.getItems().add("One week");
		choiceBoxReminder.getItems().add("Two weeks");
		choiceBoxReminder.getItems().add("Four weeks");
		choiceBoxReminder.getItems().add("Three months");
		choiceBoxReminder.getItems().add("Six months");
		choiceBoxReminder.getItems().add("One year");
		choiceBoxReminder.setValue("One Week");
		choiceBoxReminder.setDisable(true);
	}
	public void clearAll(){
		   passwordFieldPassword.clear();
		   textFieldApplication.clear();
		   textFieldUsername.clear();
		   textFieldNote.clear();
		   textFieldCategory.clear();
		   textFieldLink.clear();
		   textFieldSecurityQuestion1.clear();
		   textFieldSecurityQuestion2.clear();
		   textFieldSecurityQuestion3.clear();
		   textFieldSecurityAnswer1.clear();
		   textFieldSecurityAnswer2.clear();
		   textFieldSecurityAnswer3.clear();
		   oldPassword = null;
	   }
	public void refreshCategory() {
		textFieldCategory.setText(dmc.getCategoryController().getCategoryPath(oldPassword));
	}
}