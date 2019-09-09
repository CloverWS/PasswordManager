package controller;

import java.io.File;
//import java.nio.file.Path;
//import java.nio.file.Paths;

// import for the XML PARSER

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import aes.AES;
import exceptions.DecryptionException;
import exceptions.EncryptionException;
import exceptions.LoginException;
//import model.Category;
import model.DataManager;
import model.MasterPassword;
import model.UserPassword;

/**
 * contains the methods for loading and saving the data in the apps. 
 * @author Delrich und Kevin
 *
 */

public class IOController {

	public final String defaultfolder = System.getProperty("user.dir") + File.separator + "data";
	public final String dataFile = defaultfolder + File.separator + "dataFile.xml";
	
	public final String defaultPath = defaultfolder;
	
	private DataManagerController dmc;

	public IOController(DataManagerController dmc) {
		this.dmc = dmc;
	}
	
//	public DataManager importData() throws JAXBException {
//		File file = new File(defaultPath.toString());
//		return importData(file);
//	}
	
	/**
	 * creates a DataManager containing the parsed content of "file"
	 * @param file
	 * the file that contains the data
	 */
	public DataManager createDataManager(File file) throws JAXBException {
		DataManager dataManager = new DataManager();
		JAXBContext jaxbContext = JAXBContext.newInstance(DataManager.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		dataManager = (DataManager) jaxbUnmarshaller.unmarshal(file);

		return dataManager;
	}

	/**
	 * the method will be called when the user clicks on import and will be prompted for their password
	 * @param file
	 * the selected file from user, that contains the data
	 * @param masterPassword 
	 * the master -password that the user enters
	 * @return returns an object of type DataManager
	 * @throws throws an Exception if the file parse failed
	 */
	public void onImport(String masterPassword, File file) throws JAXBException, LoginException {
		DataManager dataManager = createDataManager(file);
		
		String originalMasterPasswordHash = dmc.getMasterPasswordController().hash(masterPassword);
		String inFIlelMasterPasswordHash = dataManager.getMasterPassword().getHash();
		if (originalMasterPasswordHash.equals(inFIlelMasterPasswordHash)) {
			for (UserPassword userPassword : dataManager.getPasswordList()) {
				try {
					userPassword.setPassword(decrypt(userPassword.getPassword(),
							masterPassword));
				} catch (DecryptionException e) {
					System.out.println("error in decrypting the password");
					e.printStackTrace();
				}
			}
			dmc.setDataManager(dataManager);
			dataManager.getMasterPassword().setPassword(masterPassword);
			cleanUp();
		} else {
			throw new LoginException();
		}
	}
	
	/**
	 * the method will be called when the user clicks on import and will be prompted for their password. A file from which will import the data is defined by default
	 * @param masterPassword 
	 * the master -password that the user enters
	 * @throws throws an Exception if the file parse failed
	 */
	
	public void onImport(String masterPassword) throws JAXBException, LoginException {
		File file = new File(dataFile);
		onImport(masterPassword, file);
	}
	
	/**
	 * checks if the program has been started before
	 * @return true if the default file already exists, false if it doesn't
	 */
	public boolean alreadyStarted() {
		File file = new File(dataFile);
		return file.exists();
	}
	
	/**
	 * see  exportData(File file)
	 */
	
	public void exportData() throws JAXBException {
		exportData(dataFile);
	}
	
	/**
	 * export the content of DataManger in File
	 * @param file
	 * the file that contains the data
	 * @throws throws an exception if writing to file failed
	 */

	public void exportData(String path) throws JAXBException {	
		DataManager dataManager = dmc.getDataManager();
		//encrypt passwords
		for (UserPassword userPassword : dataManager.getPasswordList()) {
			try {
				userPassword.setPassword(
						encrypt(userPassword.getPassword(), dataManager.getMasterPassword().getPassword()));
			} catch (EncryptionException e) {
				System.out.println("error in encrypting the password");
				e.printStackTrace();
			}
		}
		
		//hide the real masterpassword
		MasterPassword masterPassword = dataManager.getMasterPassword();
		String password = masterPassword.getPassword();
		masterPassword.setPassword("hidden");
		
		//export data
		JAXBContext jaxbContext = JAXBContext.newInstance(DataManager.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
				
		marshaller.marshal(dataManager, new File(path));
		
		//make the real masterpassword visible again
		masterPassword.setPassword(password);
	}

	/**
	 * encrypt a string with a key using the AES Library
	 * @param pwd
	 * string to encrypt
	 * @param key
	 * string use by AES to encrypt
	 * @throws throws an exception if encrypting of the password failed
	 */
	public String encrypt(String pwd, String key) throws EncryptionException {
		return AES.encrypt(pwd, key);
	}
	
	/**
	 * decrypt a string with a key using the AES Library
	 * @param pwd
	 * string to encrypt
	 * @param key string use by AES to encrypt
	 * @return returns the encrypted password
	 * @throws throws an exception if encrypting of the password failed
	 */

	public String decrypt(String pwd, String key) throws DecryptionException {
		return AES.decrypt(pwd, key);
	}

	private void cleanUp() {
		for (UserPassword userPassword : dmc.getDataManager().getPasswordList()) {
			dmc.getDataManager().getRootCategory().replace(userPassword);
		}
	}

}
