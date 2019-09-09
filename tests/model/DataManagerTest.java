package model;

import static org.junit.Assert.*;

//import java.util.Iterator;
import java.util.TreeSet;

import org.junit.Test;

//import junit.framework.Assert;

import org.junit.Before;

/**
 * Testet Methoden von Data Manager:
 * getPasswordList()
 * addPassword()
 * removePassword()
 * 
 * @author Rabea Wagner
 *
 */
public class DataManagerTest {
	
	private DataManager dataManager;
	private UserPassword pwd1;
	private UserPassword pwd2;
	
	@Before
	public void setUp() throws Exception {
		dataManager = new DataManager();
		pwd1 = new UserPassword("pwd1", null);
		pwd2 = new UserPassword("pwd2", null);
		TreeSet<UserPassword> list = new TreeSet<UserPassword>();
		list.add(pwd1);
		list.add(pwd2);
		dataManager.setPasswords(list);
	}
	
	/**
	 * Falls keine UserPassword Objekte gespeichert sind, soll ein leeres TreeSet zurück gegeben werden
	 */
	@Test
	public void testGetPasswordListEmpty() {
		TreeSet<UserPassword> liste = new DataManager().getPasswordList();
		assertNotNull(liste);
		assertTrue(liste.isEmpty());
	}
	
	/**
	 * TreeSet mit gespeicherten UserPassword Objekten soll zurück gegeben werden
	 */
	@Test 
	public void testGetPasswordList() {
		TreeSet<UserPassword> liste = dataManager.getPasswordList();
		assertNotNull(liste);
		assertFalse(liste.isEmpty());
		assertTrue(liste.size()==2);
		assertTrue(liste.contains(pwd1));
		assertTrue(liste.contains(pwd2));
		}

	/**
	 * fügt (korrektes) UserPasswort Objekt der Passwortliste hinzu
	 */
	@Test
	public void testAddNewPassword() {
		UserPassword pwd3 = new UserPassword("pwd3", null);
		dataManager.addPassword(pwd3);
		TreeSet<UserPassword> liste = dataManager.getPasswords();
		assertTrue(liste.size()==3);
		assertTrue(liste.contains(pwd3));
	}
	
	/**
	 *  Es verändert sich nichts, kein Objekt wird hinzugefügt
	 */
	@Test
	public void testAddExistingPassword() {
		dataManager.addPassword(pwd2);
		TreeSet<UserPassword> liste = dataManager.getPasswords();
		assertTrue(liste.size()==2);
		assertTrue(liste.contains(pwd1));
		assertTrue(liste.contains(pwd2));
	}
	
	/**
	 * 'null' darf/kann nicht hinzugefügt werden, es wird eine NullPonterException geworfen 
	 */
	@Test(expected = NullPointerException.class)
	public void testAddNullPassword() {
		dataManager.addPassword(null);
		TreeSet<UserPassword> liste = dataManager.getPasswords();
		assertTrue(liste.size()==2);
		assertTrue(liste.contains(pwd1));
		assertTrue(liste.contains(pwd2));
	}

	/**
	 * (korrektes) UserPasswort wird entfernt
	 */
	@Test
	public void testRemovePassword() {
		dataManager.removePassword(pwd1);
		TreeSet<UserPassword> liste = dataManager.getPasswords();
		assertTrue(liste.size()==1);
		assertFalse(liste.contains(pwd1));
		assertTrue(liste.contains(pwd2));
	}
	
	/**
	 * es verändert sich nichts, kein Objekt wird gelöscht
	 */
	@Test
	public void testRemoveNonExistingPassword() {
		UserPassword pwd3 = new UserPassword("pwd3", null);
		dataManager.removePassword(pwd3);
		TreeSet<UserPassword> liste = dataManager.getPasswords();
		assertTrue(liste.size()==2);
		assertTrue(liste.contains(pwd1));
		assertTrue(liste.contains(pwd2));
	}
	
	/**
	 * 'null' darf/kann nicht entfernt werden, es wird eine NullPonterException geworfen 
	 */
	@Test(expected = NullPointerException.class)
	public void testRemoveNullPassword() {
		dataManager.removePassword(null);
		TreeSet<UserPassword> liste = dataManager.getPasswords();
		assertTrue(liste.size()==2);
		assertTrue(liste.contains(pwd1));
		assertTrue(liste.contains(pwd2));
	}
	
	/**
	 * es verändert sich nichts, die Liste bleibt leer
	 */
	@Test
	public void testRemovePasswordFromEmpty() {
		DataManager newDataManager = new DataManager();
		TreeSet<UserPassword> liste = newDataManager.getPasswordList();
		assertTrue(liste.isEmpty());
		newDataManager.removePassword(pwd1);
		assertTrue(liste.isEmpty());
	}

}
