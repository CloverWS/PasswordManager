package model;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Locale;
//import java.util.ResourceBundle;
import java.util.TreeSet;

/**
 * Die Klasse DataManager verwaltet zur Kommunikation mit den Controllern alle Daten aus den Model-Klassen.
 * @author David Henke
 *
 */
@XmlRootElement
public class DataManager {

	//Attribute
	
	private MasterPassword masterPassword;
	
	private TreeSet<UserPassword> passwords;
	
	private Category rootCategory;
	
	private Locale language;
	
	private Color color;

	public DataManager(){
		passwords = new TreeSet<UserPassword>();
		rootCategory = new Category("root", true);
		language = Locale.ENGLISH;
		color = Color.WHITE;
	}

	// Getter & Setter
	
	/**
	 * Getter fuer die Passwortliste
	 * @return Passwortliste als TreeSet
	 */
	@XmlElement
	public TreeSet<UserPassword> getPasswords() {
		return passwords;
	}

	/**
	 * Setter fuer die Passwortliste
	 * @param passwords: die neue Passwortliste
	 */
	public void setPasswords(TreeSet<UserPassword> passwords) {
		this.passwords = passwords;
	}

	/**
	 * Getter fuer die Default-Kategorie
	 * @return die Default-Kategorie
	 */
	public Category getRootCategory() {
		return rootCategory;
	}

	/**
	 * Setter fuer die Default-Kategorie
	 * @param rootCategory: die neue Default-Kategorie
	 */
	public void setRootCategory(Category rootCategory) {
		Category newRoot = new Category(rootCategory.getName(), true);
		newRoot.setPasswords(rootCategory.getPasswordsClone());
		newRoot.setSubCategories(rootCategory.getSubCategoriesClone());
		this.rootCategory = newRoot;
	}	
	
	public Locale getLanguage() {
		return language;
	}

	@XmlJavaTypeAdapter(value=LocaleAdapter.class)
	public void setLanguage(Locale language) {
		this.language = language;
	}

	public Color getColor() {
		return color;
	}

	@XmlJavaTypeAdapter(value=ColorAdapter.class)
	public void setColor(Color color) {
		this.color = color;
	}
	
	// Methoden
	
	/**
	 * Rueckgabe der Liste aller Passwoerter
	 * @return Passwortliste als Treeset
	 */
	public TreeSet<UserPassword> getPasswordList() {
		TreeSet<UserPassword> passwordList = new TreeSet<UserPassword>();
		passwordList.addAll(passwords);
		return passwordList;
	}

	/**
	 * Hinzufuegen eines Passworts zur Passwortliste
	 * @param password: Passwort, welches hinzugefuegt werden soll
	 */
	public void addPassword(UserPassword password) {
		passwords.add(password);
	}

	/**
	 * Rueckgabe der obersten Kategorie (Default-Kategorie)
	 * @return Default-Kategorie
	 */
	public Category getCategoryRoot() {
		return rootCategory;
	}

	/**
	 * Rueckgabe des Masterpassworts
	 * @return Masterpasswort-Objekt
	 */
	@XmlElement
	public MasterPassword getMasterPassword() {
		return masterPassword;
	}

	/**
	 * Rueckgabe des Masterpassworts fuer den IOController
	 * @return Masterpasswort-Objekt
	 */

	/**
	 * Setzen eines neuen Masterpassworts
	 * @param password: neues Masterpasswort
	 */
	public void setMasterPassword(MasterPassword password) {
		masterPassword = password;
	}

	/**
	 * Loeschen eines Passworts
	 * @param password: Passwort das geloescht werden soll
	 */
	public void removePassword(UserPassword password) {
		// Das Passwort wurde bereits vom Controller aus den Kategorien gelöscht.
		passwords.remove(password);
	}
	
	public void printInfo() {
		System.out.println(masterPassword);
		if(masterPassword != null && masterPassword.getPassword() != null) {
			System.out.println("Data Manager: master password found");
		}else {
			System.out.println("Data Manager: master password missing");
		}
		
		System.out.println("Data Manager: " + passwords.size() + " passwords found");

		ArrayList<Category> categories = new ArrayList<>();
		categories.add(rootCategory);
		int size = categories.size();
		for(int i=0; i<size; i++) {
			categories.addAll(categories.get(i).getSubCategoriesClone());
			size = categories.size();
		}
		
		System.out.println("Data Manager: " + categories.size() + " categories found");
	}

}
