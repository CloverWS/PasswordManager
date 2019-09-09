package model;

import static org.junit.Assert.*;

import java.time.Period;

import org.junit.Test;

public class MasterPasswordTest {

	/**
	 * es darf kein "leeres" (Master-)Password erstellt werden
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testMasterPasswordNull() {
		MasterPassword password = new MasterPassword(null, null);
		
	}
	
	/**
	 * 
	 */
	@Test
	public void testMasterPassword() {
		MasterPassword password = new MasterPassword("test", Period.ofDays(21));
		assertNotNull(password);
		assertEquals("test", password.getPassword());
		assertEquals(Period.ofDays(21), password.getReminder());
	}
}
