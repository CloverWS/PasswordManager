package controller;

import java.util.ArrayList;
import java.util.TreeSet;

import exceptions.DuplicateCategoryException;
import model.Category;
import model.UserPassword;
import view.category.CategoryViewController;
import view.passwordedit.PasswordCategoryEditController;
import view.passwordedit.PasswordEditController;

public class CategoryController {

	private DataManagerController dataManagerController;
	private CategoryViewController categoryView;
	private PasswordCategoryEditController passwordCategoryView;
	private PasswordEditController passwordView;
 
	/**konstruktor,erzeugt eine DataManagerController mit der Uebergebenen Bezeichung.
	 * @param dmc : eine Uebergebene DataManagerController.        
	 */
	public CategoryController(DataManagerController dmc) {
		this.dataManagerController = dmc;
	}

	/**
	 * get all categories, that contain password (as directly saved).
	 * @param password :gesuchte Passwort.
	 * @param category
	 * root category to search into
	 * @return TreeSet<Category>
	 */
	public TreeSet<Category> getCategory(UserPassword password, Category root) {
		TreeSet<Category> categories = new TreeSet<Category>();
		
		if(root.containsPassword(password)){
			categories.add(root);
		}else{
			TreeSet<Category> subCategories = root.getSubCategoriesClone();
			for(Category subCategory : subCategories){
				categories.addAll(getCategory(password,subCategory));
			}
		}
		
		return categories;
	}

	

	
	/** liefert SuperKategorien zurück von category , falls es existiert , ansonsten wird das root category zurückgegeben.
	 * 
	 * @param category  : eine Uebergebene Kategorie
	 * @return Category
	 */
	//TODO: write test method for this
	public Category getSuperCategory(Category category) {

		Category root = dataManagerController.getDataManager().getCategoryRoot();
				
		Category result = getSuperCategory(category, root);
		if(result != null) return result;
		return root;
	}
	
	
	public Category getSuperCategory(Category category, Category root){		
		if(root.isLeaf()) return null;
		
		for(Category current : root.getSubCategoriesClone()){
			if(current.equals(category)) return root;
			
			Category result = getSuperCategory(category, current);
			if(result != null) return result;
		}

		return null;
	}

	/**suche alle kategorien, bei denen `search` ein Substring ist und gebe dessen Passwoerter aus.
	 * @param search :ist der String im Such Textfeld. 
	 * @param category :eine Uebergebene Kategorie.
	 * @return TreeSet<UserPassword>
	 */
	public TreeSet<UserPassword> searchPasswords(String search, Category category) {
		search = search.toLowerCase();
		String name;
		
		TreeSet<UserPassword> passwords = new TreeSet<UserPassword>(); 
		
		name = category.getName().toLowerCase();
		if(name.contains(search)) passwords.addAll(category.getAllPasswords());
		
		for(Category current : category.getSubCategoriesClone()){
			name = current.getName().toLowerCase();
			if(name.contains(search)) passwords.addAll(current.getAllPasswords());
			else passwords.addAll(searchPasswords(search, current));
		}
		
		return passwords;
	}

	/**erzeugt eine Kategorie.
	 * if supercatagory is null, it is always the root
	 * @param name :die Name von eine Kategorie
	 * @param superCategory :seine SupKategorie
	 * @return void
	 */
	public void createCategory(String name, Category superCategory) throws IllegalArgumentException{
		//TODO: if supercatagory is null, is it always the root?
		if(superCategory == null){
			Category root = dataManagerController.getDataManager().getCategoryRoot();
			TreeSet<Category> categories = root.getSubCategoriesClone();
			for(Category current: categories){
				if(current.getName().equals(name)){
					throw new IllegalArgumentException("the category name already exists");
				}
			}
			Category category = new Category(name);
			root.addSubCategory(category);
			categoryView.refresh();
			passwordCategoryView.refresh();
			passwordView.refreshCategory();
		}else{
			TreeSet<Category> categories = superCategory.getSubCategoriesClone();
			for(Category current: categories){
				if(current.getName().equals(name)){
					throw new IllegalArgumentException("the category name already exists");
				}
			}
			Category category = new Category(name);
			superCategory.addSubCategory(category);
			categoryView.refresh();
			passwordCategoryView.refresh();
			passwordView.refreshCategory();
			
			//TODO: erstelle eine Klasse die vom Interface erbt, sodass refresh() aufgerufen werden kann
			//this.categoryViewAUI.refresh();
		}
	}
	
	/**
	 * renames category to the given name
	 * @param category
	 * to be renamed
	 * @param name
	 * new name of the category
	 */
	public void renameCategory(Category category, String name) {
		
		if (category != null && name != null) {
			
			category.setName(name);
			categoryView.refresh();
			passwordCategoryView.refresh();
			passwordView.refreshCategory();
			
			//TODO: erstelle eine Klasse die vom Interface erbt, sodass refresh() aufgerufen werden kann
			//this.categoryViewAUI.refresh();
		}
	}

	/**
	 * makes category a sub category of superCategory
	 * @param category
	 * to be moved
	 * @param superCategory
	 * new super category of category
	 * @throws DuplicateCategoryException if category already exists in superCategory
	 */
	public void moveCategory(Category category, Category superCategory) throws DuplicateCategoryException, IllegalArgumentException {	
		//make sure we have valid input we can work with
		if(category == null) return;
		if(superCategory == null) superCategory = dataManagerController.getDataManager().getCategoryRoot();
		
		//make sure we dont have 2 categories with the same name under the same parent
		for(Category current : superCategory.getSubCategoriesClone()) {
			if(current.getName().equals(category.getName())) {
				throw new DuplicateCategoryException("Diese Kategorie existiert dort bereits!");
			}
		}
		
		//make sure our new parent is not a child of our category
		ArrayList<Category> categories = new ArrayList<Category>();
		categories.add(category);
		int size = categories.size();
		for(int i=0; i<size; i++) {
			if(categories.get(i).equals(superCategory)) {
				throw new IllegalArgumentException("Die super Kategorie darf nicht ein kind der Kategorie sein!");
			}
			categories.addAll(categories.get(i).getSubCategoriesClone());
			size = categories.size();
		}
		
		//remove our category from its parent
		getSuperCategory(category).removeSubCategory(category);
		
		//give our category a new parent
		superCategory.addSubCategory(category);
		
		categoryView.refresh();
		passwordCategoryView.refresh();
		passwordView.refreshCategory();
			
		//TODO: erstelle eine Klasse die vom Interface erbt, sodass refresh() aufgerufen werden kann
		//this.categoryViewAUI.refresh();
	}

	/**
	 * deletes category
	 * @param category
	 * to be deleted
	 */
	public void deleteCategory(Category category) {
		
		Category superCategory = getSuperCategory(category);
		if(superCategory == null) return; 
		
		//cut the connection to the superCategory
		superCategory.removeSubCategory(category);
		
		//move all children of our category to its superCatgeory
		for(Category current : category.getSubCategoriesClone()) {
			superCategory.addSubCategory(current);
		}
		
		//move all passwords to the super category
		for(UserPassword password : category.getPasswordsClone()) {
			superCategory.addPassword(password);
		}
		
		categoryView.refresh();
		passwordCategoryView.refresh();
		passwordView.refreshCategory();
		
		//TODO: erstelle eine Klasse die vom Interface erbt, sodass refresh() aufgerufen werden kann
		//this.categoryViewAUI.refresh();
	}

	/**
	 * adds password to the category 
	 * @param password
	 * to be added
	 * @param category
	 * category that password will be added to
	 */
	public void addPassword(UserPassword password, Category category) {
		if (password != null && category != null && !category.containsPasswordInAll(password)) {
			category.addPassword(password);
			
			categoryView.refresh();
			passwordCategoryView.refresh();
			passwordView.refreshCategory();
			
			//TODO: erstelle eine Klasse die vom Interface erbt, sodass refresh() aufgerufen werden kann
			//this.categoryViewAUI.refresh();
		}
	}

	/**
	 * removes password from category
	 * @param password
	 * to be removed
	 * @param category
	 * category that contains password
	 */
	public void removePassword(UserPassword password, Category category) {
		if (password != null && category != null) {
			Category superCategory = getSuperCategory(category);
			category.removePassword(password);
			superCategory.addPassword(password);
			
			categoryView.refresh();
			passwordCategoryView.refresh();
			passwordView.refreshCategory();
			
			//TODO: erstelle eine Klasse die vom Interface erbt, sodass refresh() aufgerufen werden kann
			//this.categoryViewAUI.refresh();
		}
	}

	/**
	 * removes password from all categories
	 * @param password
	 * to be removed
	 */
	public void removePasswordInAll(UserPassword password) {
		
		Category root = this.dataManagerController.getDataManager().getCategoryRoot();
		ArrayList<Category> categories = new ArrayList<>();
		categories.add(root);
		
		int size = categories.size();
		for(int i=0; i<size; i++) {
			Category current = categories.get(i);
			
			current.removePassword(password);
			
			categories.addAll(current.getSubCategoriesClone());
			size = categories.size();
		}
		
		categoryView.refresh();
		passwordCategoryView.refresh();
		passwordView.refreshCategory();
		
		
		//TODO: erstelle eine Klasse die vom Interface erbt, sodass refresh() aufgerufen werden kann
		//this.categoryViewAUI.refresh();
	}
	
	public String getCategoryPath(UserPassword password){
		Category root = dataManagerController.getDataManager().getCategoryRoot();
		TreeSet<Category> categories = getCategory(password, root);
		String categoryPath = "";
		for(Category current : categories){
			categoryPath = current.toString()+"/"+categoryPath; 
			Category superCategory = getSuperCategory(current);
			while(superCategory!= root){
				
				try {
					categoryPath = superCategory.toString()+"/"+categoryPath;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					
					System.out.println(categoryPath);
					e.printStackTrace();
				}
				
				superCategory = getSuperCategory(superCategory);
				
			}
		}
		
		return categoryPath;
	}
	
	public void setcategoryView(CategoryViewController categoryView){
		this.categoryView = categoryView;
	}
	
	public void setPasswordCategoryView(PasswordCategoryEditController categoryView){
		this.passwordCategoryView = categoryView;
	}
	
	public void setPasswordEditView(PasswordEditController passwordView){
		this.passwordView = passwordView;
	}
}