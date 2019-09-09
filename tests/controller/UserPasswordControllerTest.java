package controller;

import static org.junit.Assert.*;

import java.time.Period;
import java.util.TreeSet;

import org.junit.Before;
import org.junit.Test;

import model.UserPassword;

/**
 * Die Klasse testet die Methoden des UserPasswordControllers
 * @author David Henke
 *
 */
public class UserPasswordControllerTest {
	
	private DataManagerController dmc;
	private UserPasswordController upc;
	private TreeSet<UserPassword> testSet;
	private UserPassword pw1, pw2;

	/**
	 * Methode zur Initialisierung vor jedem Test
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		//Initialisierung des Tests
		dmc = new DataManagerController();
		upc = new UserPasswordController(dmc);
		testSet = new TreeSet<UserPassword>();
		dmc.getDataManager().setPasswords(testSet);
		pw1 = new UserPassword("Password 1", Period.of(0, 0, 1));
		pw1.setApplication("TestApplication");
		pw2 = new UserPassword("Password 2", Period.of(0, 0, 2));
		pw2.setApplication("Application2");
	}

	/**
	 * Testet die Methode getPasswordList
	 * @throws Exception
	 */
	@Test
	public void testGetPasswordList() throws Exception {
		//Wird in testSearchPasswords getestet.
	}
	
	/**
	 * Testet die Methode createPassword
	 */
	@Test
	public void testCreatePassword() {
		assertEquals(0,testSet.size());
		upc.createPassword(pw1);
		assertEquals(1,testSet.size());
		assertEquals("Password 1",testSet.first().getPassword());
		assertEquals(Period.of(0, 0, 1),testSet.first().getReminder());
	}
	
	/**
	 * Testet die Methode searchPassword
	 */
	@Test
	public void testSearchPasswords() {
		assertEquals(0,testSet.size());
		upc.createPassword(pw1);
		upc.createPassword(pw2);
		TreeSet<UserPassword> answer1 = upc.searchPasswords("TestApplication");
		assertEquals(1,answer1.size());
		TreeSet<UserPassword> answer2 = upc.searchPasswords("Application2");
		assertEquals(1,answer2.size());
		TreeSet<UserPassword> answer3 = upc.searchPasswords("Application");
		assertEquals(2,answer3.size());
		TreeSet<UserPassword> answer4 = upc.searchPasswords("Hallo");
		assertEquals(0,answer4.size());
		TreeSet<UserPassword> answer5 = upc.searchPasswords("Hallo");
		assertNotEquals(3,answer5.size());
	}

	/**
	 * Testet die Methode modifyPassword
	 */
	@Test
	public void testModifyPassword() {
		assertEquals(0,testSet.size());
		upc.createPassword(pw1);
		assertEquals(1,testSet.size());
		upc.modifyPassword(pw1, pw2);
		assertNotEquals("Password 1",testSet.first().getPassword());
		upc.modifyPassword(pw1, pw1);
		assertEquals(Period.of(0, 0, 1),testSet.first().getReminder());
	}

	/**
	 * Testet die Methode deletePassword
	 */
	@Test
	public void testDeletePassword() {
		assertEquals(0,testSet.size());
		upc.createPassword(pw1);
		assertEquals(1,testSet.size());
		upc.deletePassword(pw1);
		assertEquals(0,testSet.size());
	}

}
