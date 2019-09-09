package view.category;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TreeSet;


import controller.CategoryController;
import controller.DataManagerController;
import view.AUI.CategoryViewAUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import model.Category;
import model.UserPassword;

public class CategoryViewController extends GridPane implements CategoryViewAUI{

	@FXML
    private GridPane categoryViewPane;

    @FXML
    private TreeView<Category> categoryTreeView;

    @FXML
    private TextField categoryNameTextField;

    @FXML
    private Button categoryAddButton;

    @FXML
    private Button categoryEditButton;

    @FXML
    private ImageView categoryEditImage;

    @FXML
    private Button categorySaveButton;

    @FXML
    private Button categoryDeleteButton;

    @FXML
    private ImageView categoryDeleteIcon;
    
    @FXML
    private Button categoryCancButton;
    
    @FXML
    private Label categoryNameLabel;

    @FXML
    private Label categoryPasswordLabel;

    @FXML
    private Label categoryPasswordsLabel;

    
    //language
    private ResourceBundle bundle;
	private Locale locale;
	
	//popups
	private Alert noSuperCat;
		//noSuperCat Buttons
		private ButtonType buttonType1;
		private ButtonType buttonType2;
		private ButtonType buttonTypeCancel;
	private Alert alreadyExists;
	private Alert createConfirm;
	private Alert deleteConfirm;
	private Alert noDeleteCat;
	
    private DataManagerController dataManagerController;
    
    private boolean editMode;
    
    public CategoryViewController(DataManagerController dataManagerController){
		FXMLLoader loader = new FXMLLoader(getClass().getResource("CategoryView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		this.dataManagerController = dataManagerController;
	}
    
    public void refresh(){
    	loadTreeView();
    	showCategoryMainView(null);
    	selectionListener(null);
    }

    @FXML
    void saveChanges(ActionEvent event) {
    	if(editMode){ //speichern vom Edit
    		TreeItem<Category> selectedItem = categoryTreeView.getSelectionModel().getSelectedItem();
        	if(selectedItem == null){
        		return;
        	} else {
        		Category selectedCategory = selectedItem.getValue();
        		if(selectedCategory.isRoot()){
        			return;
        		}
        		CategoryController categoryController = dataManagerController.getCategoryController();
        		boolean nameChanged = !selectedCategory.getName().equals(categoryNameTextField.getText());
        		if(nameChanged){
        			categoryController.renameCategory(selectedCategory, categoryNameTextField.getText());
        		}
        	}
    		return;
    	} else { //speichern neuer kategorie
    		TreeItem<Category> selectedItem = categoryTreeView.getSelectionModel().getSelectedItem();
        	if(selectedItem == null){
        		noSuperCat = new Alert(AlertType.CONFIRMATION);
        		noSuperCat.setTitle(bundle.getString("createCategory"));
        		noSuperCat.setHeaderText(null);
        		noSuperCat.setContentText(bundle.getString("noSuperCatTxt"));
    	    	buttonType1 = new ButtonType(bundle.getString("chooseCat"));
    	    	buttonType2 = new ButtonType(bundle.getString("createCategory"));
    	    	buttonTypeCancel = new ButtonType(bundle.getString("cancel.text"), ButtonData.CANCEL_CLOSE);
    	    	noSuperCat.getButtonTypes().setAll(buttonType1, buttonType2, buttonTypeCancel);
    	    	Optional<ButtonType> result = noSuperCat.showAndWait();
    	    	if (result.get() == buttonType1){
    	    		noSuperCat.close();
    	    	} else if(result.get() == buttonType2){
    	    		try{
    	    			CategoryController categoryController = dataManagerController.getCategoryController();
        	    		categoryController.createCategory(categoryNameTextField.getText(), null);
    	    		} catch(IllegalArgumentException e){
    	    			alreadyExists = new Alert(AlertType.ERROR);
    	    			alreadyExists.setTitle(bundle.getString("createCategory")+" "+bundle.getString("error.text"));
    	    			alreadyExists.setHeaderText(null);
    	    			alreadyExists.setContentText(bundle.getString("alreadyExistsTxt")+categoryNameTextField.getText());
    	    			alreadyExists.showAndWait();
    	    			showCategoryMainView(null);
    	    			return;
    	    		}
    	    	} else {
    	    		noSuperCat.close();
    	    		showCategoryMainView(null);
    	    	    return;
    	    	}
        	} else {
        		Category selectedCategory = (Category) selectedItem.getValue();
        		CategoryController categoryController = dataManagerController.getCategoryController();
    	    	createConfirm = new Alert(AlertType.CONFIRMATION);
    	    	createConfirm.setTitle(bundle.getString("createCategory"));
    	    	createConfirm.setHeaderText(null);
    	    	createConfirm.setContentText(bundle.getString("createConfirm1")+categoryNameTextField.getText()
    	    		+"\nin: "+selectedCategory.toString()+" "+bundle.getString("createConfirm2"));
    	    	buttonTypeCancel = new ButtonType(bundle.getString("cancel.text"), ButtonData.CANCEL_CLOSE);
    	    	createConfirm.getButtonTypes().set(1, buttonTypeCancel);
    	    	Optional<ButtonType> result = createConfirm.showAndWait();
    	    	if (result.get() == ButtonType.OK){
    	    		try{
	    	    		categoryController.createCategory(categoryNameTextField.getText(), selectedCategory);
    	    		} catch(IllegalArgumentException e){
    	    			alreadyExists = new Alert(AlertType.ERROR);
    	    			alreadyExists.setTitle(bundle.getString("createCategory")+bundle.getString("error.text"));
    	    			alreadyExists.setHeaderText(null);
    	    			alreadyExists.setContentText(bundle.getString("alreadyExistsTxt")+categoryNameTextField.getText());
    	    			alreadyExists.showAndWait();
    	    			showCategoryMainView(null);
    	    			return;
    	    		}
    	    	} else {
    	    		createConfirm.close();
    	    		showCategoryMainView(null);
    	    	}
        	}
    	}

    }
    
    @FXML
    void deleteCategory(ActionEvent event) {
    	TreeItem<Category> selectedItem = categoryTreeView.getSelectionModel().getSelectedItem();
    	if(selectedItem != null){
	    	Category selectedCategory = (Category) selectedItem.getValue();
	    	if(selectedCategory.isRoot()){
    			return;
    		}
	    	CategoryController categoryController = dataManagerController.getCategoryController();
	    	Category selectedSuperCategory = categoryController.getSuperCategory(selectedCategory);
	    	//Hinweisfenster 
	    	deleteConfirm = new Alert(AlertType.CONFIRMATION);
	    	deleteConfirm.setTitle(bundle.getString("deleteCategory"));
	    	deleteConfirm.setHeaderText(bundle.getString("deleteTxt1")+selectedCategory.toString()
	    			+bundle.getString("deleteTxt2")+selectedSuperCategory.toString()+" "+bundle.getString("deleteTxt3"));
	    	deleteConfirm.setContentText(bundle.getString("deleteConfirm1")+ selectedCategory.toString()+" "+bundle.getString("deleteConfirm2"));
	    	buttonTypeCancel = new ButtonType(bundle.getString("cancel.text"), ButtonData.CANCEL_CLOSE);
	    	Optional<ButtonType> result = deleteConfirm.showAndWait();
	    	//Kategorie löschen
	    	if (result.get() == ButtonType.OK){
	    	    categoryController.deleteCategory(selectedCategory);
	    	} else {
	    		deleteConfirm.close();
	    	}
    	} else {
    		noDeleteCat = new Alert(AlertType.ERROR);
    		noDeleteCat.setTitle(bundle.getString("deleteCategory")+bundle.getString("error.text"));
    		noDeleteCat.setHeaderText(null);
    		noDeleteCat.setContentText(bundle.getString("deletePrompt"));
    		noDeleteCat.showAndWait();
    	}
    }
    
    @FXML
    void showCategoryMainView(ActionEvent event) {
    	editMode = false;
    	categoryAddButton.visibleProperty().setValue(true);
    	categoryEditButton.visibleProperty().setValue(true);
    	categoryDeleteButton.visibleProperty().setValue(true);
    	categoryNameTextField.visibleProperty().setValue(false);
    	categorySaveButton.visibleProperty().setValue(false);
    	categorySaveButton.setDisable(true);
    	categoryCancButton.setVisible(false);
    	categoryNameTextField.setText("");
    	TreeItem<Category> selectedItem = categoryTreeView.getSelectionModel().getSelectedItem();
		if(selectedItem == null){
			categoryNameLabel.setText("");
			categoryPasswordLabel.setText("");
			categoryPasswordsLabel.setText("");
		}
    }

    @FXML
    void showAddCategoryView(ActionEvent event) {
    	editMode = false;
    	categoryAddButton.visibleProperty().setValue(false);
    	categoryEditButton.visibleProperty().setValue(false);
    	categoryDeleteButton.visibleProperty().setValue(false);
    	categoryNameTextField.visibleProperty().setValue(true);
    	categorySaveButton.visibleProperty().setValue(true);
    	categorySaveButton.setDisable(true);
    	categoryCancButton.setVisible(true);
    	categoryNameTextField.setText("");
    }

    @FXML
    void showEditCategoryView(ActionEvent event) {
    	TreeItem<Category> selectedItem = categoryTreeView.getSelectionModel().getSelectedItem();
    	if(selectedItem != null){
	    	Category selectedCategory = (Category) selectedItem.getValue();
	    	if(selectedCategory.isRoot()){
	    		editMode = false;
    			return;
    		}
	    	editMode = true;
    		categoryNameTextField.visibleProperty().setValue(true);
    		categoryNameTextField.setText(selectedCategory.getName());
    		categoryAddButton.visibleProperty().setValue(false);
        	categoryEditButton.visibleProperty().setValue(false);
        	categoryDeleteButton.visibleProperty().setValue(false);
        	categorySaveButton.visibleProperty().setValue(true);
        	categorySaveButton.setDisable(true);
        	categoryCancButton.setVisible(true);
    	} else {
    		return;
    	}
    }
    
    @FXML
    void selectionListener(MouseEvent event) {
    	
		TreeItem<Category> selectedItem = categoryTreeView.getSelectionModel().getSelectedItem();
		if(selectedItem != null){
    		Category selectedCategory = selectedItem.getValue();
			if(editMode){
		    	if(selectedCategory.isRoot()){
		    		categoryNameTextField.visibleProperty().setValue(false);
	    		} else {
					categoryNameTextField.visibleProperty().setValue(true);
	        		categoryNameTextField.setText(selectedCategory.getName());
	    		}
			}
    		categoryNameLabel.setText(selectedCategory.getName());
			categoryPasswordLabel.setText(bundle.getString("passwords")+":");
			loadPasswords(selectedCategory);
    	} else {
    		categoryNameTextField.setText("");
    	}
    }
    
    @FXML
    void enableSave(KeyEvent event) {
    	categorySaveButton.setDisable(false);
    }

    
    private void generateCategoryTree(TreeItem<Category> currentNode)
    {
    	TreeSet<Category> subCategories = currentNode.getValue().getSubCategoriesClone();
    	for(Category subCategory : subCategories){
    		TreeItem<Category> nodeChild = new TreeItem<Category>(subCategory);
    		currentNode.getChildren().add(nodeChild);
    		if(!subCategory.isLeaf()){
    			generateCategoryTree(nodeChild);
    		}
    	}
    }
    
    private void loadTreeView()
    {
    	Category root = dataManagerController.getDataManager().getCategoryRoot();
    	TreeItem<Category> treeRoot = new TreeItem<Category>(root);
    	categoryTreeView.setRoot(treeRoot);
    	if(!root.isLeaf()){
    		generateCategoryTree(treeRoot);
    	}
    	treeRoot.setExpanded(true);
    }
    
    private void loadPasswords(Category category){
    	TreeSet<UserPassword> passwords = category.getAllPasswords();
    	String passwordsLabel = "";
    	for(UserPassword current : passwords){
    		passwordsLabel = passwordsLabel+current.getApplication()+"\n";
    	}
    	categoryPasswordsLabel.setText(passwordsLabel);
    }
    
	public void loadLanguage(String lang) {
		locale = new Locale(lang);
		bundle = ResourceBundle.getBundle("view.setting.bundles.settings", locale);
		Category root = dataManagerController.getDataManager().getCategoryRoot();
		root.setName(bundle.getString("rootCategoryName"));
		categoryAddButton.setText(bundle.getString("createCategory"));
		categorySaveButton.setText(bundle.getString("save.text"));
		refresh();
//		categoryNameTextField.setPromptText(bundle.getString("categoryName")+"...");
		//oldPass.setText(bundle.getString("oldPass"));
	}
	public void setColor(Color color) {
//		 vbox.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		categoryAddButton.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		categoryEditButton.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		categoryDeleteButton.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		categorySaveButton.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");
		categoryCancButton.setStyle("-fx-background-color: " + color.toString().replace("0x", "#") + ";");

	}
}
