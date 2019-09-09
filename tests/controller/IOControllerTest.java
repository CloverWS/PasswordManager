package controller;

import static org.junit.Assert.*;
import java.io.File;
import javax.xml.bind.JAXBException;
import org.junit.Before;
import org.junit.Test;
import exceptions.DecryptionException;
import exceptions.EncryptionException;
import exceptions.LoginException;
import model.Category;
import model.MasterPassword;


public class IOControllerTest {
	
	private DataManagerController dmController;
	private IOController iOController;
	

	/**Erzeugt vor jeder Testmethode eine Testumgebung
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
        dmController = new DataManagerController();
        iOController = dmController.getIOController();

	}
	
	/**
	 * Test ob der Konstruktor funktioniert
	 */
	@Test
	public void testIOController() {
		 IOController io = new IOController(dmController);
		 assertNotNull(io);
	}
	
    /**Test ob der Export und Inport funktionieren
	 * @throws JAXBException
     * @throws LoginException 
	 */
	
	@Test
	public void testExportImportFile() throws JAXBException, LoginException {
		String infile = "/sopra/sopgr03/sopr038/Dokumente/data/importFile.xml";
		File exfile = new File(infile);
		String masterPassword = "test";
		MasterPassword mast = new MasterPassword(masterPassword,null);
		dmController.getDataManager().setMasterPassword(mast);
		dmController.getDataManager().getMasterPassword().setHash(dmController.getMasterPasswordController().hash(masterPassword));
		Category root = dmController.getDataManager().getRootCategory();
		Category sub = new Category("sub");
		root.addSubCategory(sub);
		Category sub1 = new Category("sub1");
		sub.addSubCategory(sub1);
		
		iOController.exportData(infile);
		iOController.onImport(masterPassword, exfile);
		
		//test
		root = dmController.getDataManager().getCategoryRoot();
		Category nSub = root.getSubCategoriesClone().first();
		assertEquals(nSub.getName(),"sub");
		Category nSub1 = nSub.getSubCategoriesClone().first();
		assertEquals(nSub1.getName(),"sub1");
		
		
	}


	/**Test ob der Encrypt und Decrypt funktioniert
	 * @throws EncryptionException
	 * @throws DecryptionException 
	 */
	@Test
	public void testEncryptDecrypt() throws EncryptionException, DecryptionException{
		String psw = "password";
		String encrypt = iOController.encrypt(psw, "128");
		String decrypt = iOController.decrypt(encrypt, "128");
		assertEquals(psw,decrypt);
	}
	
}
