package controller;

import java.util.TreeSet;
import model.DataManager;
import model.UserPassword;
//import view.AUI.PasswordViewAUI;

/**
 * Im UserPasswordController werden die verschiedenen Aktionen fuer die Passwoerter bereitgestellt.
 * 
 * @author Marcel Schu
 *
 */

public class UserPasswordController extends PasswordController {
	
	/**
	 * Der UserPasswordController kennt den DataManagerController.
	 */
	private DataManagerController dmc;
	
	/**
	 * Der UserPasswordController kennt die passwordViewAUI um diese zu refreshen.
	 */

	//private PasswordViewAUI passwordViewAUI;

	/**
	 * Konstruktor, erzeugt einen UserPasswordController mit dem Uebergebenem DataManagerController
	 * @param dmc
	 */
	public UserPasswordController(DataManagerController dmc) {
		this.dmc = dmc;
	}
	
	/**
	 * getPasswordList gibt die Passwort-Liste zurueck.
	 * @return dmc.getDataManager().getPasswordList()
	 */
	public TreeSet<UserPassword> getPasswordList() {
		return dmc.getDataManager().getPasswordList();
	}

	/**
	 * searchPasswords guckt, ob ein Passwort mit dem Namen "name" existiert und fuegt dieses zu einem neuen
	 * TreeSet hinzu.
	 * @param name
	 * @return passwords
	 */
	public TreeSet<UserPassword> searchPasswords(String name) {
		name = name.toLowerCase();
		TreeSet<UserPassword> passwords = dmc.getDataManager().getPasswordList();
		TreeSet<UserPassword> matches = new TreeSet<UserPassword>();
		for(UserPassword password : passwords) {
			if(password.getApplication().toLowerCase().contains(name)){
				matches.add(password);
			}
		}
		return matches;
	}

	/**
	 * createPassword erstellt ein neues Passwort.
	 * @param password
	 */
	public void createPassword(UserPassword password) {
		dmc.getDataManager().addPassword(password);
	}

	/**
	 * modifyPassword setzt die Attribute von "oldPassword" auf die Attribute von "newPassword". Wenn es
	 * schon ein solches Objekt gibt wird nichts verändert.
	 * @param oldPassword
	 * @param newPassword
	 */
	public void modifyPassword(UserPassword oldPassword, UserPassword newPassword) {
		boolean alreadyExists = false;
		DataManager dataManager = dmc.getDataManager();
		TreeSet<UserPassword> passwords = dataManager.getPasswordList();
		for(UserPassword p : passwords) {
			if(p.compareTo(newPassword) == 0){
				alreadyExists = true;
			}
		}
		if (!alreadyExists){
			oldPassword.setApplication(newPassword.getApplication());
			oldPassword.setNote(newPassword.getNote());
			oldPassword.setPassword(newPassword.getPassword());
			oldPassword.setUsername(newPassword.getUsername());
			oldPassword.setUrl(newPassword.getUrl());
			oldPassword.setSecurityInfo(newPassword.getSecurityInfo());
			oldPassword.setReminder(newPassword.getReminder());
		}
		
	}

	/**
	 * deletePassword loescht erst die Verbindung vom eingegebenen Passwort zu seinen Kategorien und anschliessend das Passwort selbst.
	 * @param password
	 */
	public void deletePassword(UserPassword password) {
		dmc.getCategoryController().removePasswordInAll(password);
		dmc.getDataManager().removePassword(password);
	}
	
}
