package model;

import java.time.Period;

/**
 * Die Klasse MasterPassword speichert und verwaltet das Masterpasswort.
 * @author David Henke
 *
 */
public class MasterPassword extends Password implements Cloneable {

	// Attribute
	
	private String hash;

	/**
	 * Konstruktor
	 * @param password: Passwort
	 * @param reminder: Erinnerungszeitraum
	 */
	public MasterPassword(String password, Period reminder) {
		super(password, reminder);
	}
	
	public MasterPassword() {
		super();
	}

	// Getter & Setter
	
	/**
	 * Getter fuer das gehashte Masterpasswort
	 * @return das gehashte Masterpasswort
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * Setter fuer den Hash des Masterpassworts
	 * @param hash: der neue Hash
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}

}
