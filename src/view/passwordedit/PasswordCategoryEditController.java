package view.passwordedit;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TreeSet;

import controller.CategoryController;
import controller.DataManagerController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
//import javafx.stage.Stage;
import model.Category;
import model.UserPassword;
import view.AUI.CategoryViewAUI;
//import view.main.MainViewController;

public class PasswordCategoryEditController extends GridPane implements CategoryViewAUI{

    @FXML
    private TreeView<Category> currentCategoryTreeView;

    @FXML
    private Button categoryclearButton;

    @FXML
    private Button categoryRemoveButton;

    @FXML
    private Button categoryAddButton;

    @FXML
    private TreeView<Category> allCategoriesTreeView;

    @FXML
    private Label currentCategoriesLabel;

    @FXML
    private Label allCategoriesLabel;
    
    private ResourceBundle bundle;
	private Locale locale;
    
    private DataManagerController dataManagerController;
    private CategoryController categoryController;
//    private MainViewController mvc;
    private UserPassword currentPassword;
    
    public PasswordCategoryEditController(DataManagerController dmc){
    	dataManagerController = dmc;
    	categoryController = dmc.getCategoryController();
    	FXMLLoader loader = new FXMLLoader(getClass().getResource("PasswordCategoryView.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {

			e.printStackTrace();
		}
		loadLanguage();
    }
//test
    @FXML
    void clearCategories(ActionEvent event) {
		categoryController.removePasswordInAll(currentPassword);
    }

    @FXML
    void removeCategory(ActionEvent event) {
    	TreeItem<Category> selectedItem = currentCategoryTreeView.getSelectionModel().getSelectedItem();
		if(selectedItem != null){
			Category selectedCategory = selectedItem.getValue();
			categoryController.removePassword(currentPassword, selectedCategory);
			categoryRemoveButton.setDisable(true);
		}
    }
    
    @FXML
    void addCategory(ActionEvent event) {
    	TreeItem<Category> selectedItem = allCategoriesTreeView.getSelectionModel().getSelectedItem();
		if(selectedItem != null){
			Category selectedCategory = selectedItem.getValue();
			categoryController.addPassword(currentPassword, selectedCategory);
		}
    }
    
    @FXML
    void actionListenerCurrent(MouseEvent event) {
    	TreeItem<Category> selectedItem = currentCategoryTreeView.getSelectionModel().getSelectedItem();
		if(selectedItem != null){
			categoryRemoveButton.setDisable(false);
		} else {
			categoryRemoveButton.setDisable(true);
		}
    }
    
    @FXML
    void actionListenerAll(MouseEvent event) {
    	TreeItem<Category> selectedItem = allCategoriesTreeView.getSelectionModel().getSelectedItem();
		if(selectedItem != null){
			categoryAddButton.setDisable(false);
		} else {
			categoryAddButton.setDisable(true);
		}
    }
    
    void loadLanguage() {
    	locale = new Locale(dataManagerController.getDataManager().getLanguage().toString());
		bundle = ResourceBundle.getBundle("view.setting.bundles.settings", locale);
    	categoryclearButton.setText(bundle.getString("categoryclearButton"));
		categoryRemoveButton.setText(bundle.getString("categoryRemoveButton"));
		categoryAddButton.setText(bundle.getString("categoryAddButton"));
		currentCategoriesLabel.setText(bundle.getString("currentCategoriesLabel"));
		allCategoriesLabel.setText(bundle.getString("allCategoriesLabel"));
    }

	@Override
	public void refresh() {
		loadTreeView();
	}
	
	public void load(UserPassword password){
		currentPassword = password;
		loadTreeView();
		loadLanguage();
	}
	
	private void generateCategoryTree(TreeItem<Category> currentNode)
    {
    	TreeSet<Category> subCategories = currentNode.getValue().getSubCategoriesClone();
    	for(Category subCategory : subCategories){
    		TreeItem<Category> nodeChild = new TreeItem<Category>(subCategory);
    		currentNode.getChildren().add(nodeChild);
    		nodeChild.setExpanded(true);
    		if(!subCategory.isLeaf()){
    			generateCategoryTree(nodeChild);
    		}
    	}
    }
	
	private void generateCurrentCategoryTree(TreeItem<Category> currentNode)
    {
		Category currentCategory = currentNode.getValue();
    	TreeSet<Category> subCategories = currentCategory.getSubCategoriesClone();
    	for(Category subCategory : subCategories){
    		if(subCategory.containsPasswordInAll(currentPassword)){
	    		TreeItem<Category> nodeChild = new TreeItem<Category>(subCategory);
	    		currentNode.getChildren().add(nodeChild);
	    		nodeChild.setExpanded(true);
	    		if(!subCategory.containsPassword(currentPassword)){
	    			System.out.println(subCategory.toString());
	    			generateCurrentCategoryTree(nodeChild);
	    		}
    		}
    	} 
    }
	
	private void loadTreeView()
    {
    	Category root = dataManagerController.getDataManager().getCategoryRoot();
    	TreeItem<Category> treeRoot = new TreeItem<Category>(root);
    	allCategoriesTreeView.setRoot(treeRoot);
    	allCategoriesTreeView.setShowRoot(false);
    	if(!root.isLeaf()){
    		generateCategoryTree(treeRoot);
    	}
    	treeRoot.setExpanded(true);
    	
    	TreeItem<Category> currentTreeRoot = new TreeItem<Category>(root);
    	currentCategoryTreeView.setRoot(currentTreeRoot);
    	currentCategoryTreeView.setShowRoot(false);
    	if(!root.containsPassword(currentPassword)){
    		generateCurrentCategoryTree(currentTreeRoot);
    	}
    	treeRoot.setExpanded(true);
    }

}
