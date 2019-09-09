package controller;

import java.time.LocalDateTime;

//import javax.xml.bind.JAXBException;

import model.DataManager;
import model.MasterPassword;

/**
 * In MasterPasswordController wird das Master Passwort erzeugen, editieren, pruefen und geschluesselt.
 * 
 * @author Dimitar und Kevin
 */
public class MasterPasswordController extends PasswordController {

	private DataManagerController dmc;

	/**
	 * 	DataManagerController kennt die andere Komponente die Anwendung und fuehrt die Verbindung dazwischen.
	 * @param dmc
	 * 			Verbindung mit dem dataManagerController
	 */
	public MasterPasswordController(DataManagerController dmc) {
		this.dmc = dmc;
	}

	/**
	 * Erzeugt neues Master Passwort beim erste Programmstart.
	 * @param password
	 * 			Das Passwort, die man eingeben soll um das Passwort Liste zu sehen.
	 */
	public void createPassword(MasterPassword password) {
		if(password == null){
			throw new NullPointerException("the given MasterPassword Object is null");
		}
		
		password.setHash(this.hash(password.getPassword()));

		DataManager manager = dmc.getDataManager();
		manager.setMasterPassword(password);
	}

	/**
	 * Mit dem modifyPassword kann das aktuelle Passwort Aendern.
	 * @param password
	 * 			Das Passwort, die man eingeben soll, um das Passwort Liste zu sehen.
	 */
	public void modifyPassword(MasterPassword password) {
		
		DataManager manager = dmc.getDataManager();
		
		manager.setMasterPassword(password);

	}

	/**
	 * Es wird gecheckt, ob der Nutzer der richtige Master Passwort eingegeben hat.
	 * @param password
	 * 			Das Passwort, die man eingeben soll, um das Passwort Liste zu sehen.
	 * @return
	 * 			Passwort ist falsch. Nochmal Eingeben
	 */
	public boolean checkRemindDateOutdated() {
		DataManager dataManager = dmc.getDataManager();
		MasterPassword masterPassword = dataManager.getMasterPassword();
		LocalDateTime remindDate = masterPassword.getTimeStamp().plus(masterPassword.getReminder());
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LocalDateTime currentDateTime = LocalDateTime.now();
		return remindDate.isBefore(currentDateTime);
	}

	/**
	 * Das Master Passwort wird gehasht.
	 * @param password
	 * 			Das Passwort, die man eingeben soll, um das Passwort Liste zu sehen.
	 * @return
	 * 			MasterPassword ist gehasht und der gibt null als Parameter. MasterPassword soll entschluesseln werden
	 * 			um die Daten einzusehen.
	 */
	public String hash(String password) {
		//TODO: do this with MD5 
		  try {
		        java.security.MessageDigest msgDigest = java.security.MessageDigest.getInstance("MD5");
		        byte[] array = msgDigest.digest(password.getBytes());
		        StringBuffer strB = new StringBuffer();
		        for (int i = 0; i < array.length; ++i) {
		          strB.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
		       }
		        return strB.toString();
		    } catch (java.security.NoSuchAlgorithmException e) {
		    }
		return null;
	}

}
