package view.setting;

import java.io.IOException;
import java.net.URL;
import java.time.Period;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBException;

import controller.DataManagerController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import model.MasterPassword;
import view.main.MainViewController;

public class SettingViewController extends AnchorPane implements Initializable {

	@FXML
	private Label masterPwdChangeLabel;

	@FXML
	private Tab general;

	@FXML
	private Tab aboutus;

	@FXML
	private Label oldPass;

	@FXML
	private Label newPass;

	@FXML
	private Label reminder;

	@FXML
	private ChoiceBox<String> reminderChoiceBox;

	@FXML
	private ComboBox<String> languageCombobox;

	@FXML
	private ColorPicker colorPicker;

	@FXML
	private Button save;

	@FXML
	private Button cancel;

	@FXML
	private SplitPane pane_1;

	@FXML
	private SplitPane pane_2;

	@FXML
	private TextArea aboutusTextArea;

	@FXML
	private PasswordField oldPassField;

	@FXML
	private PasswordField newPassField;

	@FXML
	private Button buttonPassword;
	
	@FXML
	private TextField newPassFieldVisible;
	
	@FXML
	private TextField reminderField;

	private Period period;

	private ResourceBundle bundle;

	private Locale locale;

	private FXMLLoader loader;

	private MainViewController mainViewController;
	
//	private ReminderLanguageSetting rlSetting;

	@FXML
	void onEnableButton(KeyEvent event) {
		if (!oldPassField.getText().isEmpty() && !newPassField.getText().isEmpty()) {
//			save.setDisable(false);
//			cancel.setDisable(false);
		}

	}

	ObservableList<String> languageComboboxList = FXCollections.observableArrayList("English", "German", "French",
			"Bulgarian", "Chinese", "Arabic");

	@FXML
	void colorSelect(ActionEvent event) {
//		String color = "-fx-background-color: #" + colorPicker.getValue().toString().substring(2, 8) + ";";
//		pane_1.setStyle(color);
//		pane_2.setStyle(color);
		mainViewController.getDMController().getDataManager().setColor(colorPicker.getValue());
		mainViewController.setColor(colorPicker.getValue());
	}
	// Variabless

	@FXML
	void onCancel(ActionEvent event) {
		oldPassField.setText("");
		newPassField.setText("");
		newPassFieldVisible.setText("");

	}

	@FXML
	void onSave(ActionEvent event) {
		DataManagerController dmc = mainViewController.getDMController();
		MasterPassword masterPwd = dmc.getDataManager().getMasterPassword();
		int checked = reminderChoiceBox.getSelectionModel().getSelectedIndex();
		switch (checked) {
		case 0:
			period = period.plusDays(7);
			break;
		case 1:
			period = period.plusDays(14);
			break;
		case 2:
			period = period.plusDays(28);
			break;
		case 3:
			period = period.plusMonths(3);
			break;
		case 4:
			period = period.plusMonths(6);
			break;
		case 5:
			period = period.plusYears(1);
			break;
		}
		if(newPassFieldVisible.isVisible()){
			newPassField.setText(newPassFieldVisible.getText());
		}
		
		MasterPassword newMasterPwd = new MasterPassword(newPassField.getText(), period);
		newMasterPwd.setHash(dmc.getMasterPasswordController().hash(newPassField.getText()));
		if (oldPassField.getText().equals(masterPwd.getPassword())) {
			if (!dmc.getMasterPasswordController().isSecure(newPassField.getText())) {
				JOptionPane.showMessageDialog(null, "Password isn't safe", "Warning", JOptionPane.WARNING_MESSAGE);
			} else {
				dmc.getMasterPasswordController().modifyPassword(newMasterPwd);
				try {
					mainViewController.getDMController().getIOController().exportData();
					JOptionPane.showMessageDialog(null, "Change Succeed", "OK", JOptionPane.INFORMATION_MESSAGE);
				} catch (JAXBException e) {
					JOptionPane.showMessageDialog(null, "Failed to save data", "Failure", JOptionPane.ERROR_MESSAGE);
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "OldPassword False", "Failure", JOptionPane.ERROR_MESSAGE);
		}

	}
	public SettingViewController(MainViewController mainViewController) {

		this.mainViewController = mainViewController;
		this.period = Period.ZERO; 
		locale = new Locale("en", "EN");
		loader = new FXMLLoader(getClass().getResource("SettingView.fxml"));
		loader.setResources(ResourceBundle.getBundle("view.setting.bundles.settings", locale));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
	
    @FXML
    void passwordVisiblity(ActionEvent event) {
    	if(newPassFieldVisible.isVisible()) {
    		newPassFieldVisible.setVisible(false);
    		newPassField.setText(newPassFieldVisible.getText());
    		newPassField.setVisible(true);
    	} else {
    		newPassFieldVisible.setVisible(true);
    		newPassFieldVisible.setText(newPassField.getText());
    		newPassField.setVisible(false);
    	}
    }
   
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		languageCombobox.getItems().add("Arabic");
		reminderChoiceBox.getItems().add("Eine Woche");
		reminderChoiceBox.getItems().add("Zwei Wochen");
		reminderChoiceBox.getItems().add("Vier Wochen");
		reminderChoiceBox.getItems().add("Drei Monate");
		reminderChoiceBox.getItems().add("Sechs Monate");
		reminderChoiceBox.getItems().add("Ein Jahr");
		reminderChoiceBox.getSelectionModel().select(0);
		newPassFieldVisible.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.equals("")) {
				save.setDisable(true);
			}
			else if (!newValue.equals("")) {
				save.setDisable(false);
			}
		});
		
		newPassField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.equals("")) {
				save.setDisable(true);
			}
			else if (!newValue.equals("")) {
				save.setDisable(false);
			}
//			System.out.println("textfield changed from " + oldValue + " to " + newValue);
		});

		oldPassField.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue.equals("")) {
				cancel.setDisable(true);
			}
			else if (!newValue.equals("")) {
				cancel.setDisable(false);
			}
//			System.out.println("textfield changed from " + oldValue + " to " + newValue);
		});

		//Laden der vorher ausgewählten Sprache und Farbe
		bundle = resources;
		refresh();
		languageCombobox.setItems(languageComboboxList);
		languageCombobox.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
//				System.out.println(languageCombobox.getValue());
//				rlSetting.reminderLanguageSetting(languageCombobox.getValue().toString(), reminderChoiceBox);
				switch (languageCombobox.getValue().toString()) {
				case "English":
					mainViewController.loadLanguage("en");
					mainViewController.getDMController().getDataManager().setLanguage(Locale.ENGLISH);
					reminderChoiceBox.setItems((FXCollections.observableArrayList("One Week","Two Week","Four Week","Three Months","Six Months","One Year")));
					reminderChoiceBox.getSelectionModel().select(0);
					break;
				case "German":
					mainViewController.loadLanguage("de");
					mainViewController.getDMController().getDataManager().setLanguage(Locale.GERMAN);
					reminderChoiceBox.setItems((FXCollections.observableArrayList("Eine Woche","Zwei Wochen","Vier Wochen","Drei Monate","Sechs Monate","Ein Jahr")));
					reminderChoiceBox.getSelectionModel().select(0);
					break;
				case "Chinese":
					mainViewController.loadLanguage("zh");
					mainViewController.getDMController().getDataManager().setLanguage(Locale.CHINESE);
					reminderChoiceBox.setItems((FXCollections.observableArrayList("一周","两周","四周","三个月","六个月","一年")));
					reminderChoiceBox.getSelectionModel().select(0);
					break;
				case "Bulgarian":
					mainViewController.loadLanguage("bg");
					mainViewController.getDMController().getDataManager().setLanguage(Locale.forLanguageTag("bg"));
					reminderChoiceBox.setItems((FXCollections.observableArrayList("Една седмица","Две седмици","Четири седмици","Три месеца","Шест месеца","Една година")));
					reminderChoiceBox.getSelectionModel().select(0);
					break;
				case "French":
					mainViewController.loadLanguage("fr");
					mainViewController.getDMController().getDataManager().setLanguage(Locale.FRENCH);
					reminderChoiceBox.setItems((FXCollections.observableArrayList("Une semaine","Deux semaines","Quatre semaines","Trois mois","Six mois","Un ans")));
					reminderChoiceBox.getSelectionModel().select(0);
					break;
				case "Arabic":
					mainViewController.loadLanguage("ar");
					mainViewController.getDMController().getDataManager().setLanguage(Locale.forLanguageTag("ar"));
					reminderChoiceBox.setItems((FXCollections.observableArrayList("اسبوع واحد","أسبوعين","أربعة أسابيع","ثلاثة اشهر","ستة أشهر","سنة واحدة")));
					reminderChoiceBox.getSelectionModel().select(0);
					break;
				}
			}
		});

	}
	
	public void refresh() {
		colorPicker.setValue(mainViewController.getDMController().getDataManager().getColor());
		languageCombobox
				.setValue(mainViewController.getDMController().getDataManager().getLanguage().getDisplayLanguage());
	}

	public void loadLanguage(String lang) {
		locale = new Locale(lang);
		bundle = ResourceBundle.getBundle("view.setting.bundles.settings", locale);
		oldPass.setText(bundle.getString("oldPass"));
		newPass.setText(bundle.getString("newPass"));
		save.setText(bundle.getString("save.text"));
		cancel.setText(bundle.getString("cancel.text"));
		reminder.setText(bundle.getString("reminder"));
		masterPwdChangeLabel.setText(bundle.getString("masterPwdChangeLabel"));
		general.setText(bundle.getString("general"));
		aboutus.setText(bundle.getString("aboutus"));
		aboutusTextArea.setText(bundle.getString("aboutusTextArea"));
	}
	public void setColor(Color color) {
		save.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		cancel.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		reminderChoiceBox.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		languageCombobox.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		colorPicker.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		buttonPassword.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");

	}

}
