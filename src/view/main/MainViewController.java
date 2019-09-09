package view.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.xml.bind.JAXBException;

import controller.DataManagerController;
import exceptions.LoginException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import view.category.CategoryViewController;
import view.login.LoginViewController;
import view.passwordedit.PasswordEditController;
import view.passwordsListView.PasswordsListViewController;
import view.setting.SettingViewController;

public class MainViewController extends BorderPane {

	private CategoryViewController categoryView;
	private SettingViewController settingsView;
	private PasswordsListViewController homeView;
	private PasswordEditController editView;
	private Stage primaryStage;
	private DataManagerController dataManagerController;

	@FXML
	private AnchorPane menuHome;

	@FXML
	private VBox vbox;

	@FXML
	private Label homeLabel;

	@FXML
	private Label passwordLabel;

	@FXML
	private AnchorPane menuCategory;

	@FXML
	private Label catLabel;

	@FXML
	private AnchorPane menuSave;

	@FXML
	private Label saveLabel;

	@FXML
	private AnchorPane menuImport;

	@FXML
	private AnchorPane menuPwd;

	@FXML
	private Label importLabel;

	@FXML
	private AnchorPane menuExport;

	@FXML
	private Label exportLabel;

	@FXML
	private AnchorPane menuLock;

	@FXML
	private Label blockLabel;

	@FXML
	private AnchorPane menuSettings;

	@FXML
	private Label parameterLabel;

	private ArrayList<AnchorPane> attrArr = new ArrayList<AnchorPane>();
	private ResourceBundle bundle;
	private Locale locale;

	public MainViewController(Stage primaryStage, DataManagerController dataManagerController) {
		this.dataManagerController = dataManagerController;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {

			e.printStackTrace();
		}
		settingsView = new SettingViewController(this);
		homeView = new PasswordsListViewController(dataManagerController, this);
		categoryView = new CategoryViewController(dataManagerController);
		editView = new PasswordEditController(dataManagerController, this);

		// Übergabe der View an den Controller -> um refresh() aufrufen zu
		// können bei Model-Veränderungen
		dataManagerController.getCategoryController().setcategoryView(categoryView);
		dataManagerController.getCategoryController().setPasswordEditView(editView);

		attrArr.add(menuHome);
		attrArr.add(menuCategory);
		attrArr.add(menuSave);
		attrArr.add(menuImport);
		attrArr.add(menuExport);
		attrArr.add(menuLock);
		attrArr.add(menuSettings);

		locale = dataManagerController.getDataManager().getLanguage();
		this.loadLanguage(locale.toString());

		this.primaryStage = primaryStage;
		this.setCenter(homeView);
		this.setRight(editView);
		setAlignment(homeView, Pos.TOP_LEFT);
		homeView.setMinHeight(800);
		selectPane(menuHome);
		centerScreen();
		this.showHome(null);
	}

	public void loadLanguage(String lang) {
		settingsView.loadLanguage(lang);
		categoryView.loadLanguage(lang);
		editView.loadLanguage(lang);
		setColor(dataManagerController.getDataManager().getColor());
		homeView.loadLanguage(lang);

		locale = new Locale(lang);
		bundle = ResourceBundle.getBundle("view.setting.bundles.settings", locale);
		catLabel.setText(bundle.getString("catTxt"));
		homeLabel.setText(bundle.getString("homeTxt"));
		importLabel.setText(bundle.getString("importTxt"));
		exportLabel.setText(bundle.getString("exportTxt"));
		blockLabel.setText(bundle.getString("lockTxt"));
		saveLabel.setText(bundle.getString("saveTxt"));
		parameterLabel.setText(bundle.getString("settingsTxt"));
		passwordLabel.setText(bundle.getString("passwordLabel"));
	}
	

	@FXML
	void showHome(MouseEvent event) {
		this.setCenter(homeView);
		this.setRight(editView);
		setAlignment(homeView, Pos.TOP_LEFT);
		homeView.setMinHeight(800);
		selectPane(menuHome);
		this.requestFocus();
	}

	public void centerScreen() {
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		primaryStage.setX((primScreenBounds.getWidth() - this.getPrefWidth()) / 2);
		primaryStage.setY((primScreenBounds.getHeight() - this.getPrefHeight()) / 2);
	}

	@FXML
	void exportData(MouseEvent event) {
		selectPane(menuExport);
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML file (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(primaryStage);
		if (file != null) {
			try {
				dataManagerController.getIOController().exportData(file.getPath());
				JOptionPane.showMessageDialog(null, "Dateien erfolgreich exportiert!", "Information",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (JAXBException e) {
				JOptionPane.showMessageDialog(null, "Fehler beim Exportieren der Daten", "Fehlermeldung",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	@FXML
	void importData(MouseEvent event) {
		selectPane(menuImport);
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML file (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setTitle("Öffnen");
		File file = fileChooser.showOpenDialog(primaryStage);
		if (file != null) {
			try {
				dataManagerController.getIOController()
						.onImport(dataManagerController.getDataManager().getMasterPassword().getPassword(), file);
				JOptionPane.showMessageDialog(null, "Dateien erfolgreich importiert", "Information",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (JAXBException e) {
				JOptionPane.showMessageDialog(null, "Fehler beim Importieren der Daten", "Fehlermeldung",
						JOptionPane.ERROR_MESSAGE);
			} catch (LoginException e) {
				JOptionPane.showMessageDialog(null, "Fehler beim Entschlüsseln der Daten", "Fehlermeldung",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		//Refreshen aller Komponenten
		homeView.refresh();
		categoryView.refresh();
		setColor(dataManagerController.getDataManager().getColor());
		loadLanguage(dataManagerController.getDataManager().getLanguage().getLanguage());
		settingsView.refresh();
	}

	@FXML
	void lock(MouseEvent event) {
		Scene scene = new Scene(new LoginViewController(primaryStage, dataManagerController));
		primaryStage.setScene(scene);
		centerScreen();
	}

	@FXML
	void save(MouseEvent event) {
		selectPane(menuSave);
		JOptionPane.showMessageDialog(null, "Dateien erfolgreich gespeichert", "Information",
				JOptionPane.INFORMATION_MESSAGE);
		try {
			dataManagerController.getIOController().exportData();
		} catch (JAXBException e) {
			JOptionPane.showMessageDialog(null, "Fehler beim Speichern der Daten", "Fehlermeldung",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@FXML
	void showCategories(MouseEvent event) {
		selectPane(menuCategory);
		this.setRight(categoryView);
		setAlignment(homeView, Pos.TOP_LEFT);
		categoryView.refresh();
	}

	@FXML
	void showSettings(MouseEvent event) {

		setAlignment(settingsView, Pos.TOP_LEFT);
		settingsView.setMinHeight(this.getPrefHeight());
		settingsView.setMinWidth(this.getPrefWidth());
		this.setCenter(settingsView);
		selectPane(menuSettings);
		this.setRight(null);
	}

	void selectPane(AnchorPane sel) {
		for (AnchorPane pane : attrArr) {
			if (pane.getId().equals(sel.getId())) {
				pane.getStyleClass().add("selected");
			} else {
				pane.getStyleClass().remove("selected");
			}

		}
	}

	public DataManagerController getDMController() {
		return dataManagerController;
	}

	public PasswordEditController getEditView() {
		return editView;
	}

	public PasswordsListViewController getHomeView() {
		return homeView;
	}

	public void setColor(Color color) {
		settingsView.setColor(color);
		categoryView.setColor(color);
		editView.setColor(color);
		vbox.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		menuPwd.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		menuHome.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		menuCategory.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		menuSave.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		menuImport.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		menuExport.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		menuLock.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		menuSettings.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
	}

	@FXML 
	public void showNewPasswordView(MouseEvent event){
		this.setRight(null);
		this.setRight(editView);
		this.showHome(null);
		editView.clearAll();
		editView.buttonEditClicked(null);
	}

}
