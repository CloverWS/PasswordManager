package model;

import java.time.LocalDateTime;
import java.time.Period;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * Die abstrakte Klasse Password dient als Oberklasse fuer das Masterpasswort und die Passwoerter des Users.
 * @author David Henke
 *
 */
public abstract class Password {

	// Attribute
	
	private String password;

	private LocalDateTime timeStamp;

	private Period reminder;

	/**
	 * Konstruktor
	 * @param password: Passwort
	 * @param reminder: Erinnerungszeitraum
	 */
	public Password(String password, Period reminder) {
		if(password == null) {
			throw new IllegalArgumentException("der Password String sollte niemals 'null' sein, h�chstens leer.");
		}
		
		this.password = password;
		this.reminder = reminder;
		this.timeStamp = LocalDateTime.now();
	}
	
	public Password() {}
	// Getter & Setter

	/**
	 * Getter fuer das Passowrt
	 * @return das Passwort
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter fuer das Passwort
	 * @param password: Das neue Passwort
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Getter fuer den Zeitstempel
	 * @return den Zeitstempel
	 */
	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	/**
	 * Setter fuer den Zeitstempel
	 * @param timeStamp: Der neue Zeitstempel
	 */
	@XmlJavaTypeAdapter(value=LocalDataTimeAdapter.class)
	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * Getter fuer den Erinnerungszeitpunkt
	 * @return den Erinnerungszeitpunkt
	 */
	public Period getReminder() {
		return reminder;
	}

	/**
	 * Setter fuer den Erinnerungszeitpunkt
	 * @param reminder: Der neue Erinnerungszeitpunkt
	 */
	@XmlJavaTypeAdapter(value=PeriodAdapter.class)
	public void setReminder(Period reminder) {
		this.reminder = reminder;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		
		Password other = (Password) obj;
		if (password == null && other.password != null) return false;
		if (password != null && !password.equals(other.password)) return false;
		return true;
	}	
	
	@Override 
	public String toString(){
		return "Password : "+ password + ", Reminder : " + reminder;
	}
	
}
