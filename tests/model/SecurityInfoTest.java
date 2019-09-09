package model;

import static org.junit.Assert.*;

import org.junit.Test;

public class SecurityInfoTest {

	@Test
	public void testSecurityInfo() {
		SecurityInfo info = new SecurityInfo("question", "answer");
		assertEquals("question", info.getSQuestion());
		assertEquals("answer", info.getSAnswer());
	}
	
	/**
	 * es soll keine Exception geworfen werden, da Null-SecurityInfo legitim ist
	 */
	@Test
	public void testNullSecurityInfo() {
		SecurityInfo info = new SecurityInfo(null, null);
	}

}
