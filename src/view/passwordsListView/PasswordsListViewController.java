package view.passwordsListView;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.TreeSet;

import controller.DataManagerController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import model.Category;
import model.UserPassword;
import view.main.MainViewController;
import view.passwordedit.PasswordEditController;



public class PasswordsListViewController extends VBox {

	
    @FXML
    private TableView<PasswordModelView> mainTable;

    @FXML
    private TableColumn<PasswordModelView, String> applicationColumn;

    @FXML
    private TableColumn<PasswordModelView, String> urlColumn;

    @FXML
    private TextField searchField;

    private DataManagerController dataMC;
    
    private PasswordEditController ped;
    
    private ObservableList<PasswordModelView> entries = FXCollections.observableArrayList();
    
    private MainViewController mvc;
    
    private ResourceBundle bundle;
    
	private Locale locale;
	
	
    public PasswordsListViewController(DataManagerController dmc, MainViewController mvc){
    	this.dataMC = dmc;
    	this.mvc = mvc; 	
    	
    	// Load fxml settings for gui 
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("passwordsListView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
				
		try {
			
			loader.load();
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    }
    
    
    @FXML
    public void initialize() {
    	applicationColumn.setCellValueFactory(new PropertyValueFactory<>("application"));
    	urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
    	mainTable.setItems(entries);
    	addAllPasswords();
    }
    
    
    @FXML
    void itemClicked(MouseEvent event) {
    	String selected = "";
    	this.mvc.showNewPasswordView(null);

    	if (event.getTarget().toString().contains("null")){
    		mainTable.getSelectionModel().clearSelection();
    		this.mvc.getEditView().clearAll();
    	}else {
    		try{
        		selected = mainTable.getSelectionModel().getSelectedItem().getPassword().getPassword();
        		this.mvc.getEditView().load(mainTable.getSelectionModel().getSelectedItem());
        	
        	}catch(NullPointerException e ){
        		;
        	}
        	
        	if(selected == null) mainTable.getSelectionModel().clearSelection();
        	 // passwordModelView object given as arguments 

        	adddToClipboard(selected);
        	
        	//ped = new PasswordEditController(mainTable.getSelectionModel().getSelectedItem(),dataMC,null );
    	}
    	
    }
    
    
    void adddToClipboard(String str){
    	StringSelection selection = new StringSelection(str);
    	Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    	clipboard.setContents(selection, selection);
    }
    
    
    @FXML
    void searchPassword(KeyEvent event) {
    	KeyCombination ctrl_backspace = new KeyCodeCombination(KeyCode.BACK_SPACE, KeyCombination.CONTROL_ANY);
    	KeyCombination ctrl_x = new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_ANY);
    	String searchPhrase =  searchField.getText();
    	if (	event.getCode() != KeyCode.SHIFT && 
    			event.getCode() != KeyCode.TAB && 
    			event.getCode() != KeyCode.BACK_SPACE && 
    			event.getCode() != KeyCode.CAPS ){
    		searchPhrase += event.getText();
    	}else if (event.getCode() == KeyCode.BACK_SPACE && !searchPhrase.equals("")){
    		searchPhrase = searchPhrase.substring(0, searchPhrase.length() - 1);
    	}
    	if(searchPhrase.equals("") || ctrl_backspace.match(event) ||ctrl_x.match(event)) {
    		entries.clear();
    		addAllPasswords();
    	}
    	else {
    		search(searchPhrase);
    	}

    	
    }
    
    
    public void search(String searchPhrase){   	
    	TreeSet<UserPassword> matches = dataMC.getUserPasswordController().searchPasswords(searchPhrase);
    	ArrayList<PasswordModelView> searchBuffer = new ArrayList<>(); // for saving all search results before setting the eobservablelist(entries)
    	PasswordModelView PMV;
    	if (matches.size() != 0) {
	    	for(UserPassword match : matches) {
	    		
	    		TreeSet<Category> categories = dataMC.getCategoryController().getCategory(match,dataMC.getDataManager().getRootCategory());
	    			    		
	    		try {
	    			PMV = new PasswordModelView(match);
	    			searchBuffer.add(PMV);
	    			
	    		}catch(NoSuchElementException e) {
	    			PMV = new PasswordModelView(match);
	    			
	    		}
	    	}
	    	entries.setAll(searchBuffer);
    	}else{
    		entries.clear();
    	}
    	
    }
    
    public void addAllPasswords(){
    	TreeSet<UserPassword> passwordList = dataMC.getDataManager().getPasswordList();
    	PasswordModelView PMV;

    	for(UserPassword password : passwordList){
    		TreeSet<Category> categories = dataMC.getCategoryController().getCategory(password,dataMC.getDataManager().getRootCategory());    		
    		// found in at least one category
    		try{
        		PMV = new PasswordModelView(password);
    		}
    		catch(NoSuchElementException e) {
    			PMV = new PasswordModelView(password);
    		}
    		entries.add(PMV);
    	}
   }
    
    public void refresh(){
    	entries.clear();
    	addAllPasswords();
    	this.mainTable.refresh();
    }
    
    public void addPasswordToList(PasswordModelView pmc) {
    	entries.add(pmc);
    	mainTable.getSelectionModel().select(entries.get(entries.size()-1));
    }
    
    public void removePassword(PasswordModelView pmc) {
    	entries.remove(pmc);
    }
    
    public void loadLanguage(String lang) {
    	locale = new Locale(lang);
		bundle = ResourceBundle.getBundle("view.setting.bundles.settings", locale);
		applicationColumn.setText(bundle.getString("applicationColumn"));
    	urlColumn.setText(bundle.getString("urlColumn"));

    	 mainTable.setPlaceholder(new Label(bundle.getString("emptyTable")));
    	 searchField.setPromptText(bundle.getString("search"));
    	
	}
}
