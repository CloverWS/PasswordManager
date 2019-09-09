package model;

//import java.util.Collection;
//import java.time.Period;
//import java.util.ArrayList;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlElement;

/**
 * Kategorien haben eine Bezeichnung
 * Subkategorien, 
 * und zugehoerige Passwoerter, welche entweder in ihnen oder in ihren Subkategorien gespeichert sind.
 * 
 * @author Rabea Wagner
 *
 */
public class Category implements Comparable<Category>{

	private String name;

	private final boolean isRoot;

	private TreeSet<Category> subCategories;

	private TreeSet<UserPassword> passwords;

	/**
	 * erzeugt eine neue Kategorie, mit uebergebenem Namen, die nicht die Root-Kategorie ist
	 * 
	 * @param name
	 * Bezeichnung der Kategorie
	 */
	public Category(String name) {
		this.name = name;
		this.isRoot = false;
		this.subCategories = new TreeSet<Category>();
		this.passwords = new TreeSet<UserPassword>();
	}
	
	public Category() {
		this("");
	}

	/**
	 * erzeugt eine neue Kategorie, mit uebergebenem Namen
	 * spezifiziert mit uebergebenem Parameter, ob es sich um die Root-Kategorie handelt
	 * (dieser Konstruktor soll nur fuer die Root-Kategorie verwendet werden)
	 * 
	 * @param name
	 * Bezeichnung der Kategorie
	 * @param isRoot
	 * ob es sich um die Root-Kategorie handelt
	 */
	public Category(String name, boolean isRoot) {
		this.name = name;
		this.isRoot = isRoot;
		this.subCategories = new TreeSet<Category>();
		this.passwords = new TreeSet<UserPassword>();
	}

	/**
	 * ueberprueft, ob es sich um die Root-Kategorie handelt
	 * 
	 * @return ob es sich um die Root-Kategorie handelt
	 */
	public boolean isRoot() {
		return isRoot;
	}

	/**
	 * ueberprueft, ob es sich um ein Blatt (im Baum) handelt
	 * 
	 * @return ob es sich um ein Blatt (im Baum) handelt
	 */
	public boolean isLeaf() {
		return subCategories.isEmpty();
	}

	/**
	 * ueberprueft, ob die Kategorie keine zugehoerigen Passwoerter hat
	 * 
	 * @return ob die Kategorie keine zugehoerigen Passwoerter hat
	 */
	public boolean isEmpty() {
		return passwords.isEmpty();
	}

	/**
	 * fuegt die uebergebene Kategorie als Subkategorie hinzu
	 * 
	 * @param category
	 * welche Kategorie als Subkategorie hinzugefuegt wird
	 */
	public void addSubCategory(Category category) {
		subCategories.add(category);
	}

	/**
	 * entfernt die uebergebene Kategorie als Subkategorie, falls diese eine Subkategorie war, sonst passiert nichts
	 * 
	 * @param category
	 * welche Kategorie als Subkategorie entfernt werden soll
	 */
	public void removeSubCategory(Category category) {
		subCategories.remove(category);
	}
	
	/**
	 * gibt eine Liste der Subkategorien zurueck
	 * 
	 * @return alle Subkategorien
	 */
	public TreeSet<Category> getSubCategoriesClone() {
		return (TreeSet<Category>) subCategories.clone();
	}

	/**
	 * gibt eine Liste der nur in dieser Kategorie gespeicherten Passwoerter zurueck
	 * 
	 * @return nur in dieser Kategorie gespeicherten Passwoerter
	 */
	public TreeSet<UserPassword> getPasswordsClone() {
		return (TreeSet<UserPassword>) passwords.clone();
	} 

	public TreeSet<Category> getSubCategories() {
		return subCategories;
	}

	public TreeSet<UserPassword> getPasswords() {
		return passwords;
	}

	/**
	 * gibt eine Liste alle zu dieser Kategorie zugehoerigen Passwoerter zurueck, 
	 * auch die, welche in Subkategorien gespeichert sind
	 * 
	 * @return alle zu dieser Kategorie zugehoerigen Passwoerter, auch in Subkategorien gespeicherte
	 */
	public TreeSet<UserPassword> getAllPasswords() {
		TreeSet<UserPassword> list = getPasswordsClone();
		if(!this.isLeaf()){
			for(Category subCategory : subCategories){					//iterieren über alle Subkategorien (falls es welche gibt)
				list.addAll(subCategory.getAllPasswords());				//deren zugehörigen Passwörter an Liste der eigenen Passwörter anhängen
			}
		}
		return list;
	}

	/**
	 * ueberprueft, ob das uebergebene Passwort in dieser Kategorie gespeichert ist
	 * (also keine Zugehoerigkeit des Passworts zu den Subkategorien)
	 * 
	 * @param password
	 * Passwort, dessen Existenz, in dieser Kategorie, ueberprueft wird
	 * 
	 * @return ob das Passwort in dieser Kategorie gespeichert ist
	 */
	public boolean containsPassword(UserPassword password) {
		if (password != null) {
			return (passwords.contains(password));
		} return false;
	}

	/**
	 * ueberprueft, ob das uebergebene Passwort zu dieser Kategorie gehoert
	 * (also in dieser Kategorie oder in einer ihrer Subkategorien gespeichert)
	 * 
	 * @param password
	 * Passwort, dessen Zugehoerigkeit, zu dieser Kategorie/Teilbaum, ueberprueft wird
	 * 
	 * @return ob das Passwort zu dieser Kategorie/Teilbaum gehoert
	 */
	public boolean containsPasswordInAll(UserPassword password) {
		if (password != null) {
			return (this.getAllPasswords().contains(password));
		}
		return false;
	}

	/**
	 * speichert das uebergebene Passwort in dieser Kategorie/ fuegt es zur Passwort-Liste hinzu
	 * 
	 * @param password
	 * Passwort, das in dieser Kategorie gespeichert wird
	 */
	
	public void addPassword(UserPassword password) {
		passwords.add(password);
	}

	/**
	 * entfernt das uebergebene Passwort aus der Passwort-Liste, falls es dort gespeichert war, sonst passiert nichts
	 * 
	 * @param password
	 * Passwort, das aus dieser Kategorie geloescht wird
	 */
	public void removePassword(UserPassword password) {
		if (password != null) {
			passwords.remove(password);
		}
	}

	/**
	 * ersetzt ein bereits gespeichertes Passwort (in der Passwort-Liste) mit dem uebergebenen Passwort(-Objekt), falls diese
	 * identisch sind -> fuer Clean-Up nach Initialisierung des Programms
	 * 
	 * @param password
	 * Passwort, dass ausgetauscht wird (falls in dieser Kategorie gespeichert)
	 * 
	 */
	public void replace(UserPassword password) {
		for(UserPassword currentPassword : passwords){
			if(currentPassword.equals(password)){
				passwords.remove(currentPassword);
				passwords.add(password);
			}
		}
		
		for(Category subCategory : subCategories){
			subCategory.replace(password);
		}
		
	}
	
	/**
	 * gibt den Namen der Kategorie zurueck
	 * 
	 * @return den Namen der Kategorie
	 */
	
	public String getName(){
		return name;
	}
	
	/**
	 * veraendert den Namen der Kategorie, setzt diese auf die uebergebene Bezeichnung
	 * 
	 * @param name
	 * neue Bezeichnung der Kategorie
	 */
	public void setName(String name){
		this.name = name;
	}

	/**
	 * Ueberprueft ob alle Attribute der verglichenen Kategorien Uebereinstimmen
	 * 
	 * @param category
	 * Kategorie mit welcher verglichen werden soll
	 * 
	 * @return 0 falls alle Attribute Uebereinstimmen;
	 * ein Wert kleiner als 0 wenn diese Kategorie lexikografisch kleiner ist, als die uebergebene
	 * ein Wert groesser als 0 wenn diese Kategorie lexikografisch groesser ist, als die uebergebene
	 */
	@Override
	public int compareTo(Category category) {
		if(similarOrNull(category) >= 0) return similarOrNull(category);
    
        if (!name.equals(category.name)) return name.compareTo(category.name);
        
        if (checkPasswords(category) != 0) return checkPasswords(category);
        
        if (checkCats(category) != 0) return checkCats(category);
    
        return 0; 
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isRoot ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((passwords == null) ? 0 : passwords.hashCode());
		result = prime * result + ((subCategories == null) ? 0 : subCategories.hashCode());
		return result;
	}
	
	/**
	 * Ueberprueft ob alle Attribute der verglichenen Kategorien Uebereinstimmen
	 * 
	 * @param category
	 * Kategorie mit welcher verglichen werden soll
	 * 
	 * @return ob alle Attribute der verglichenen Kategorien Uebereinstimmen
	 */
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
				
		Category other = (Category) obj;
		if(isRoot != other.isRoot) return false;
		
		if (name == null && other.name != null) return false;
		if (name != null && !name.equals(other.name)) return false;
		
		if (passwords == null && other.passwords != null) return false;
		if (passwords != null && !passwords.equals(other.passwords)) return false;
		
		if (subCategories == null && other.subCategories != null) return false;
		if (subCategories != null && !subCategories.equals(other.subCategories)) return false;
		
		return true;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	private int similarOrNull(Category category) {
        if(this == category) return 0;
        if(category == null) return 1;
        return -1;
    }

    private int checkPasswords(Category category) {
        if (passwords == null && category.passwords != null) return 1;
        if (passwords != null && !passwords.equals(category.passwords)) {
            String psw1 = "";
            for(UserPassword password : passwords) {
                psw1 += password.toString();
            }
            
            String psw2 = "";
            for(UserPassword password : category.passwords) {
                psw2 += password.toString();
            }
            
            if(!psw1.equals(psw2)) return psw1.compareTo(psw2);
        }
        return 0;
    }

    private int checkCats(Category category) {
        if (subCategories == null && category.subCategories != null) return 1;
        if (subCategories != null && !subCategories.equals(category.subCategories)) {
            String cat1 = "";
            for(Category subCategory : subCategories) {
                cat1 += subCategory.toString();
            }
            
            String cat2 = "";
            for(Category subCategory : category.subCategories) {
                cat2 += subCategory.toString();
            }
            
            if(!cat1.equals(cat2)) return cat1.compareTo(cat2);
        }
        return 0;
    }

	public void setSubCategories(TreeSet<Category> subCategories) {
		this.subCategories = subCategories;
	}

	public void setPasswords(TreeSet<UserPassword> passwords) {
		this.passwords = passwords;
	}
}
