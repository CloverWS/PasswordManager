package model;

import java.time.Period;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Die Klasse UserPassword speichert und verwaltet die Passwoerter des Users.
 * @author David Henke
 *
 */

public class UserPassword extends Password implements Comparable<UserPassword>{

	//Attribute
	
	private String username;

	private String url;

	private String application;

	private String note;

	private Collection<SecurityInfo> securityInfo;

	/**
	 * Konstruktor
	 * @param password: Passwort
	 * @param reminder: Erinnerungszeitraum
	 */
	public UserPassword(String password, Period reminder) {
		super(password, reminder);
		
	}
	public UserPassword() {
		super();
		
	}
	
	// Getter & Setter
	
	/**
	 * Getter fuer den Username
	 * @return dem Username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Setter fuer den Username
	 * @param username: Der neue Username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Getter fuer die URL
	 * @return die URL
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Setter fuer die URL
	 * @param url: Die URL
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Getter fuer die Application
	 * @return die Application
	 */
	public String getApplication() {
		return application;
	}

	/**
	 * Setter fuer die Application
	 * @param application: Die neue Application
	 */
	public void setApplication(String application) {
		this.application = application;
	}

	/**
	 * Getter fuer die Notiz
	 * @return die Notiz
	 */
	public String getNote() {
		return note;
	}

	/**
	 * Setter fuer die Notiz
	 * @param note: Die neue Notiz
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * Getter fuer die Sicherheitsfragen
	 * @return Collection der Sicherheitsfragen
	 */
	public Collection<SecurityInfo> getSecurityInfo() {
		return securityInfo;
	}

	/**
	 * Setter fuer die Sicherheitsfragen
	 * @param securityInfo: Collection der neuen Sicherheitsfragen
	 */
	public void setSecurityInfo(Collection<SecurityInfo> securityInfo) {
		this.securityInfo = securityInfo;
	}

	//Methoden
	
	/**
	 * Ueberprueft, ob alle definierenden Attribute der verglichenen Passwoerter Uebereinstimmen
	 * (password, application, url, username)
	 * 
	 * @param password: Passwort, mit welchem verglichen wird
	 * @return true, falls alle definierenden Attribute des Passworts Uebereinstimmen
	 */
	@Override
	public boolean equals(Object obj) {
		if(this == obj) return true;
		if(obj == null) return false;
		if(getClass() != obj.getClass()) return false;
		
		UserPassword other = (UserPassword) obj;
		
		if(this.compareTo(other) != 0) return false;
		
		return true;
	}
	
	/**
	 * Vergleich von zwei Passwoertern nach Passwort, Username und Apllication.
	 * 
	 * @param password: Passwort, mit dem verglichen wird
	 * @return 0 falls es eine Übereinstimmung gibt, und != 0 falls es keine gibt.
	 */
	@Override
	public int compareTo(UserPassword password) {
		
		if (password.getPassword() == null && getPassword() != null) return -1;
		if(password.getPassword() != null && !password.getPassword().equals(getPassword())) {
			return password.getPassword().compareTo(getPassword());
		}
		if (password.getUsername() == null && getUsername() != null) return -1;
		if(password.getUsername() != null && !password.getUsername().equals(getUsername())) {
			return password.getUsername().compareTo(getUsername());
		}
		if (password.getApplication() == null && getApplication() != null) return -1;
		if(password.getApplication() != null && !password.getApplication().equals(getApplication())) {
			return password.getApplication().compareTo(getApplication());
		}
		if (password.getUrl() == null && getUrl() != null) return -1;
		if(password.getUrl() != null && !password.getUrl().equals(getUrl())) {
			return password.getUrl().compareTo(getUrl());
		}
		if (password.getNote() == null && getNote() != null) return -1;
		if(password.getNote() != null && !password.getNote().equals(getNote())) {
			return password.getNote().compareTo(getNote());
		}
		
		if(securityInfo == null && password.getSecurityInfo() != null) return -1;
		if(securityInfo != null && password.getSecurityInfo() == null) return 1;
		if(securityInfo == null && password.getSecurityInfo() == null) return 0;
		
		ArrayList<SecurityInfo> listOld = new ArrayList<>(securityInfo);
		ArrayList<SecurityInfo> listNew = new ArrayList<>(password.getSecurityInfo());
		if(listOld.size() > listNew.size()) return 1;
		if(listOld.size() < listNew.size()) return -1;
		
		for(int i=0; i<listOld.size(); i++){
			SecurityInfo currentOld = listOld.get(i);
			SecurityInfo currentNew = listNew.get(i);
			if(!currentNew.getSAnswer().equals(currentOld.getSAnswer())) {
				return currentNew.getSAnswer().compareTo(currentOld.getSAnswer());
			}
			if(!currentNew.getSQuestion().equals(currentOld.getSQuestion())) {
				return currentNew.getSQuestion().compareTo(currentOld.getSQuestion());
			}
		}
		
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((application == null) ? 0 : application.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}
	

	
	@Override
	public String toString() {
		return super.toString();
	}
	
}
