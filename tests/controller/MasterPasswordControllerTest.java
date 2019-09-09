package controller;
import static org.junit.Assert.*;
//import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.time.Period;

import org.junit.Before;
import org.junit.Test;

import model.MasterPassword;

/**
 * JUnit Test für die Klasse MasterPasswordController
 * @author Marcel Schu
 *
 */
public class MasterPasswordControllerTest {

	private DataManagerController dmc;
	private MasterPassword omp,nmp;
	/**
	 * Erzeugt vor jeder Testmethode eine Testumgebung
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		dmc = new DataManagerController();
	}

	/**
	 * Test ob der Konstruktor funktioniert
	 */
	@Test
	public void testMasterPasswordController() {
		MasterPasswordController mpc = new MasterPasswordController(dmc);
		assertNotNull(mpc);
	}

	/**
	 * Wenn der Parameter 'password' null ist, wird eine NullPointerException
	 * geworfen.
	 * @throws Exception
	 */
	@Test (expected = NullPointerException.class)
	public void testCreatePassword() throws Exception {
		omp = new MasterPassword("oldPassword",null);
		dmc.getMasterPasswordController().createPassword(omp);
		dmc.getMasterPasswordController().createPassword(null);
		
		
	}

	/**
	 * Prüft ob das Masterpasswort fehlerfrei geändert werden kann.
	 */
	@Test
	public void testModifyPassword() {
		omp = new MasterPassword("oldPasssword",null);
		nmp = new MasterPassword("newPassword",null);
		
		assertNotNull(omp);
		assertNotNull(nmp);
		
		dmc.getMasterPasswordController().createPassword(omp);
		
		assertNotNull(dmc.getDataManager().getMasterPassword());
		assertEquals(dmc.getDataManager().getMasterPassword(), omp);
		
		dmc.getMasterPasswordController().modifyPassword(nmp);
		
		assertNotNull(dmc.getDataManager().getMasterPassword());
		assertEquals(dmc.getDataManager().getMasterPassword(), nmp);
	}

	/**
	 * Prüft ob ein der Erinnerungszeitraum abgelaufen ist.
	 * 
	 */
	@Test 
	public void testCheckRemindDatedOutdated() throws Exception {
		omp = new MasterPassword("oldPassword",Period.of(0, 0, 0));
		omp.setHash(dmc.getMasterPasswordController().hash("oldPassword"));
		omp.setTimeStamp(LocalDateTime.now());
		dmc.getMasterPasswordController().createPassword(omp);
		boolean test = dmc.getMasterPasswordController().checkRemindDateOutdated();
		assertTrue(test);
		omp = new MasterPassword("oldPassword2",Period.of(0, 0, 1));
		omp.setHash(dmc.getMasterPasswordController().hash("oldPassword"));
		omp.setTimeStamp(LocalDateTime.now());
		dmc.getMasterPasswordController().createPassword(omp);
		assertFalse(dmc.getMasterPasswordController().checkRemindDateOutdated());

	}

}
