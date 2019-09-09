package controller;

import static org.junit.Assert.*;

import java.time.Period;

import org.junit.Before;
import org.junit.Test;

import model.UserPassword;

public class PasswordControllerTest {
	
	private UserPassword userPwd; 
	Period reminder;
	PasswordController pwdController;

	@Before
	public void setUp() throws Exception {
		DataManagerController dmc = new DataManagerController();
		pwdController = dmc.getUserPasswordController();
		userPwd = new UserPassword("1234", Period.of(0, 1, 10));
	}
	/**
	 * tests if the password passes the security test
	 */

	@Test
	public void testIsSecureUserPassword() {
		userPwd.setPassword("1234");
		assertFalse(userPwd.getPassword(), pwdController.isSecure(userPwd));
		userPwd.setPassword("98\\jh");
		assertFalse(userPwd.getPassword(), pwdController.isSecure(userPwd));
		userPwd.setPassword("jackjoe");
		assertFalse(userPwd.getPassword(), pwdController.isSecure(userPwd));
		userPwd.setPassword("frankenstein78_");
		assertFalse(userPwd.getPassword(), pwdController.isSecure(userPwd));
		userPwd.setPassword("frankenstein78_");
		assertFalse(userPwd.getPassword(), pwdController.isSecure(userPwd));
		
		
		
		userPwd.setPassword("cornieBrie15?");
		assertTrue(userPwd.getPassword(), pwdController.isSecure(userPwd));
		userPwd.setPassword("Frankenstein78_");
		assertTrue(userPwd.getPassword(), pwdController.isSecure(userPwd));
		userPwd.setPassword("Brotherhood653!");
		assertTrue(userPwd.getPassword(), pwdController.isSecure(userPwd));
		userPwd.setPassword("Hackerangriff98\\");
		assertTrue(userPwd.getPassword(), pwdController.isSecure(userPwd));
		
		
	}
	
	/**
	 * tests if a string passes the security test
	 */

	@Test
	public void testIsSecureString() {
		assertFalse("98\\jh", pwdController.isSecure("98\\jh"));
		assertFalse("jackjoe", pwdController.isSecure("jackjoe"));
		assertFalse("bouml", pwdController.isSecure("bouml"));
		
		assertTrue("cornieBrie15?", pwdController.isSecure("cornieBrie15?"));
		assertTrue("Frankenstein78_", pwdController.isSecure("Frankenstein78_"));
		assertTrue("Hrotherhood653!", pwdController.isSecure("Brotherhood653!"));
		assertTrue("Hackerangriff98\\", pwdController.isSecure("Hackerangriff98\\"));
		
	}

	/**
	 * tests if the password can be generated correctly
	 */
	@Test
	public void testGeneratePassword() {
		for(int i = 0; i< 5; i++) {
			assertNotNull(pwdController.generatePassword());
		}
		
	}

}
