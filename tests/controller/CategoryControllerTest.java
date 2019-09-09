package controller;

import static org.junit.Assert.*;

//import java.lang.annotation.Repeatable;
import java.security.SecureRandom;
import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Collections;
import java.util.Random;
import java.util.TreeSet;
//import org.junit.FixMethodOrder;

import model.*;

//import org.junit.Assert;
//import org.junit.Before;
import org.junit.Test;

import exceptions.DuplicateCategoryException;
import javafx.util.Pair;

/**
 * Testet, ob CategoryControllerTest alle Funktionen und Aktionen mit 
 * Category,subCategory,superCategory und Passwort erfüllen kann.
 */

public class CategoryControllerTest {

	int repeatTest = 1000;
	
	Random rand;
	
	DataManagerController dmc;
	CategoryController categoryController;
	DataManager dataManager;
	
	Category categoryRoot;
	ArrayList<Category> categoryList;
	ArrayList<UserPassword> passwordList;
	ArrayList<Pair<Category, Category>> hierarchyList;
	
	
	public void init(){
				
		rand = new SecureRandom();
		dmc = new DataManagerController();
		categoryController = dmc.getCategoryController();
		
		dataManager = new DataManager();
		dmc.setDataManager(dataManager);
		//child, parent
		hierarchyList = new ArrayList<Pair<Category, Category>>();
		
		//create Category Tree for DataManager
		categoryRoot = new Category("root", true);
		categoryList = new ArrayList<Category>();
		categoryList.add(categoryRoot);
		hierarchyList.add(new Pair<Category, Category>(categoryRoot, null));
		
		for(int i=0; i<20; i++){
			
			int x = rand.nextInt(Integer.MAX_VALUE);
			
			Category child = new Category(String.valueOf((char)('a'+i)) + x);
			Category parent = categoryList.get(x % categoryList.size());
			parent.addSubCategory(child);
			
			hierarchyList.add(new Pair<Category, Category>(child, parent));
			categoryList.add(child);
		}
		dataManager.setRootCategory(categoryRoot);
		
		//create Password list and randomly add passwords to category list
		passwordList = new ArrayList<UserPassword>();
		
		for(int i=0; i<50; i++){
			int x = rand.nextInt();
			String str = String.valueOf(x);
			UserPassword password = new UserPassword(str, null);
			passwordList.add(password);
			
			x = rand.nextInt(categoryList.size());
			categoryList.get(x).addPassword(password);
		}
	}
	
	/**
	 * Testet, ob eine subCategorie aus Kategory eine bestimmte Passwort contains
	 */
	@Test
	public void testGetCategory() {
		init();
		
		int x = rand.nextInt(passwordList.size());
		UserPassword password = passwordList.get(x);
		
		//get the list of all searched categories in 2 different ways
		TreeSet<Category> categories = categoryController.getCategory(password, categoryRoot);
		TreeSet<Category> correctList = new TreeSet<>();
		
		for(Category current : categoryList) {
			if(current.getPasswordsClone().contains(password)) {
				correctList.add(current);
			}
		}
		
		//make sure we have all categories we were looking for
		assertEquals(categories.size(), correctList.size());
		
		for(Category current : categories) {
			assertTrue(correctList.contains(current));
		}
		for(Category current : correctList) {
			assertTrue(categories.contains(current));
		}
	}

	@Test
	public void testGetSuperCategoryCategory() {
		init();
		
		int x = rand.nextInt(categoryList.size());
		
		Category category = categoryList.get(x);
		Category superCategory = categoryController.getSuperCategory(category);
		
		for(Pair<Category, Category> current : hierarchyList){
			if(current.getKey().equals(category)){
				assertEquals(current.getValue(), superCategory);
			}
		}
	}
	/**
	 * testet, ob alle Kategorien, bei denen `search` ein Substring
	 *  ist,dessen Passwörter ausgeben können.
	 */
	@Test
	public void testSearchPasswords() {
		init();
		
		String search = "1";
		
		//get the list of all searched passwords in 2 different ways
		TreeSet<UserPassword> passwords = categoryController.searchPasswords(search, categoryRoot);
		TreeSet<UserPassword> correctList = new TreeSet<>();
		
		for(Category category : categoryList) {
			if(!category.getName().contains(search)) continue;
			correctList.addAll(category.getPasswordsClone());
		}
		
		//make sure we have all passwords we were looking for
		assertEquals(passwords.size(), correctList.size());
		
		for(UserPassword password : passwords) {
			assertTrue(correctList.contains(password));
		}
		for(UserPassword password : correctList) {
			assertTrue(passwords.contains(password));
		}
	}
	/**
	 * Testet, ob eine Kategorie erzeugt werden kann.
	 */
	@Test
	public void testCreateCategory() {
		init();
		
		String categoryName = "test7897897897";
		int x = rand.nextInt(categoryList.size());
		
		Category category = categoryList.get(x);
		int subCategoriesSize = category.getSubCategoriesClone().size();
		
		
		categoryController.createCategory(categoryName, category);
		
		assertEquals(subCategoriesSize+1, category.getSubCategoriesClone().size());	
	}
	/**
	 * Testet, ob der Name eine Kategorie ändern kann.
	 */
	@Test
	public void testRenameCategory() {
		init();
		
		int x = rand.nextInt(categoryList.size());
		
		Category category = categoryList.get(x);
		String str = "tester"; 
		
		categoryController.renameCategory(category, str);
		
		assertEquals(str, category.getName());
	}
	/**
	 * Testet, ob eine Kategorie in subCategorie transferiert werden kann.
	 * @throws DuplicateCategoryException 
	 * @throws IllegalArgumentException 
	 */
	@Test
	public void testMoveCategory() throws IllegalArgumentException {
		init();
		
		int x = rand.nextInt(categoryList.size()-5)+5;
		
		Category superCategory = categoryList.get(x/2);
		Category category = categoryList.get(x);
		
		try {
			categoryController.moveCategory(category, superCategory);
		}catch(DuplicateCategoryException e) {
			assertTrue(superCategory.getSubCategoriesClone().contains(category));
		}
		
		assertTrue(superCategory.getSubCategoriesClone().contains(category));		
	}
	/**
	 * Testet, ob eine Kategorie enfernt wird.
	 */
	//assert size of tree is reduced by exactly 1 after deleteCategory()
	@Test
	public void testDeleteCategory() {
		init();
		
		//init
		ArrayList<Category> subsBeforeDelete = new ArrayList<>();
		subsBeforeDelete.add(categoryRoot);
		ArrayList<Category> subsAfterDelete = new ArrayList<>();
		subsAfterDelete.add(categoryRoot);
		
		//get the list of categories before delete
		int size = subsBeforeDelete.size();
		for(int i=0; i<size; i++) {
			subsBeforeDelete.addAll(subsBeforeDelete.get(i).getSubCategoriesClone());
			size = subsBeforeDelete.size();
		}
		
		//delete a random category (except the root)
		int x = rand.nextInt(categoryList.size()-1)+1;
		Category category = categoryList.get(x);
//		Category superCat = categoryController.getSuperCategory(category); //TODO: delete after testing
		categoryController.deleteCategory(category);
				
		//get the list of categories after delete
		size = subsAfterDelete.size();
		for(int i=0; i<size; i++) {
			subsAfterDelete.addAll(subsAfterDelete.get(i).getSubCategoriesClone());
			size = subsAfterDelete.size();
		}
		
		//make sure we deleted EXACTLY 1 category and this will be shown in the tree
		assertEquals(subsBeforeDelete.size(), subsAfterDelete.size()+1);
		
		//make sure that ONLY the category we wanted to delete got deleted
		subsAfterDelete.add(category);
		for(Category current : subsBeforeDelete) {
			assertTrue(subsAfterDelete.contains(current));
		}		
	}
	/**
	 * Testet, ob ein Passwort zu einer bestimmte Kategorie hinzugefügt wird.
	 */
	@Test
	public void testAddPassword() {
		init();

		UserPassword password = new UserPassword("test", null);
		
		int x = rand.nextInt(categoryList.size());
		
		categoryList.get(x).addPassword(password);

		boolean success = categoryList.get(x).getPasswordsClone().contains(password);
				
		assertTrue(success);
		
	}
	/**
	 * Testet, ob ein Passwort aus ein Kategorie entfernt wird.
	 */
	@Test
	public void testRemovePassword() {
		init();	
		
		int max = 0;
		Category category = null;
		
		for(Category current : categoryList){
			if(current.getPasswordsClone().size() > max){
				max = current.getPasswordsClone().size();
				category = current;
			}
		}
				
		int x = rand.nextInt(max);
		
		ArrayList<UserPassword> categoryPasswords = new ArrayList<>(category.getPasswordsClone());
		UserPassword password = categoryPasswords.get(x);
		categoryController.removePassword(password, category);
		
		boolean success = true;
		for(UserPassword current : category.getPasswordsClone()){
			if(current.equals(password)){
				success = false;
			}
		}
				
		assertTrue(success);
			
	}
	/**
	 *Testet, ob ein Passwort aus jeder Kategorie entfernt wurde. 
	 */
	@Test
	public void testRemovePasswordInAll() {
		init();	
		
		boolean success = true;
		
		int x = rand.nextInt(passwordList.size());
		UserPassword password = passwordList.get(x);
		
		categoryController.removePasswordInAll(password);

		for(Category current: categoryList){
			TreeSet<UserPassword> passwords = current.getPasswordsClone();
			if(passwords.contains(password)){
				success = false;
			}
		}
				
		assertTrue(success);
	}
	@Test
	public void testEverythingMultipleTimes() {
		for (int i = 0; i < repeatTest; i++) {
			testGetCategory();
			testGetSuperCategoryCategory();
			testSearchPasswords();
			testCreateCategory();
			testRenameCategory();
			testMoveCategory();
			testDeleteCategory();
			testAddPassword();
			testRemovePassword();
			testRemovePasswordInAll();	
		}
	}
}
