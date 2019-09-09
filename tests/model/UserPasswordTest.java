package model;

import static org.junit.Assert.*;

import java.time.Period;

import org.junit.Before;
import org.junit.Test;

public class UserPasswordTest {
	
	private UserPassword password;
	
	@Before
	public void setUp() throws Exception {
		password = new UserPassword("test", Period.ofDays(21));
	}

	@Test
	public void testUserPassword() {
		assertNotNull(password);
		assertEquals("test", password.getPassword());
		assertEquals(Period.ofDays(21), password.getReminder());
	}
	
	/**
	 * die GUI/ der IOController sollten niemals Passwörter mit dem Passwort String = null anlegen
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testUserPasswordNull() {
		UserPassword nPassword = new UserPassword(null, null);
	}

	@Test
	public void testCompareTo() {
		assertTrue(password.compareTo(password)==0);
		UserPassword password2 = new UserPassword("test", Period.ofDays(21));
		assertTrue(password.compareTo(password2)==0);
	}
	
	@Test
	public void testCompareToDifferent() {
		UserPassword password2 = new UserPassword("test2", Period.ofDays(21));
		assertFalse(password.compareTo(password2)==0);
	}
	
	@Test(expected = NullPointerException.class)
	public void testCompareToNull() {
		password.compareTo(null);
	}

	@Test
	public void testEqualsUserPassword() {
		assertTrue(password.equals(password));
		UserPassword password2 = new UserPassword("test", Period.ofDays(21));
		assertTrue(password.equals(password2));
	}
	
	@Test
	public void testEqualsDifferentUserPassword() {
		UserPassword password2 = new UserPassword("test2", Period.ofDays(21));
		assertFalse(password.equals(password2));
	}
	
	@Test
	public void testEqualsNullUserPassword() {
		password.equals(null);
	}

}
