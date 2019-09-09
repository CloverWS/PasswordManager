package controller; 
import model.DataManager;
import view.AUI.MainViewAUI;



/**
 * 
 * DataManagerController creates and manages instances of all other controllers in controller layer.
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * @author : Abdulhamed Chribati 
 * 
 * */

public class DataManagerController {
	
	private DataManager dataManager;

	private UserPasswordController userPasswordController;

	private CategoryController categoryController;

	private IOController iOController;

	private MasterPasswordController masterPasswordController;
	
	private ReminderController reminderController;

	private MainViewAUI mainViewAUI;
	
	public DataManagerController() {
		
		dataManager = new DataManager();
		userPasswordController = new UserPasswordController(this);
		categoryController = new CategoryController(this);
		iOController = new IOController(this);
		masterPasswordController = new MasterPasswordController(this);
		reminderController = new ReminderController(this);
	}	
	
	
	
	/**
	 * Getter for attribute dataManager 
	 * 
	 * @return dataManager object
	 * 
	 */
	public DataManager getDataManager() {
		return dataManager;
	}
	
	/**
	 * Getter for attribute ioController 
	 * 
	 * @return ioController object
	 * 
	 */
	
	public IOController getIOController() {
		return iOController;
	}

	/**
	 * Getter for attribute userPasswordController 
	 * 
	 * @return userPasswordController object
	 * 
	 */
	public UserPasswordController getUserPasswordController() {
		return userPasswordController;
	}
	/**
	 * Getter for attribute masterPasswordController 
	 * 
	 * @return masterPasswordController object
	 * 
	 */
	public MasterPasswordController getMasterPasswordController() {
		return masterPasswordController;
	}
	
	
	/**
	 * Getter for attribute reminderController 
	 * 
	 * @return reminderController object
	 * 
	 */
	public ReminderController getReminderController() {
		return reminderController;
	}
	
	/**
	 * Getter for attribute categoryController 
	 * 
	 * @return categoryController object
	 * 
	 */
	public CategoryController getCategoryController() {
		return categoryController;
	}
	
	/**
	 * Setter for attribute dataManager 
	 * 
	 * @return dataManager object
	 * 
	 */
	
	public void setDataManager(DataManager dataManager) {
		this.dataManager = dataManager;
	}
	
	/**
	 * Setter for attribute mainViewAUI
	 * 
	 * @return mainViewAUI object
	 * 
	 */
	public MainViewAUI getMainViewAUI(){
		return mainViewAUI;
	}
	/**
	 * Setter for attribute ioController
	 * 
	 * @return ioController object
	 * 
	 */
	public void setIOController(IOController iOController) {
		this.iOController = iOController;
	}
	/**
	 * Setter for attribute userPasswordController 
	 * 
	 * @return userPasswordController object
	 * 
	 */
	public void setUserPasswordController(UserPasswordController userPasswordController) {
		this.userPasswordController = userPasswordController;
	}	
	/**
	 * Setter for attribute categoryController 
	 * 
	 * @return categoryController object
	 * 
	 */
	public void setCategoryController(CategoryController categoryController) {
		this.categoryController = categoryController;
	}
	/**
	 * Setter for attribute masterPasswordController
	 * 
	 * @return masterPassworController object
	 * 
	 */
	public void setMasterPasswordController(MasterPasswordController masterPasswordController) {
		this.masterPasswordController = masterPasswordController;
	}
	
	/**
	 * Setter for attribute reminderController
	 * 
	 * @return reminderController object
	 * 
	 */
	public void setReminderController(ReminderController reminderController) {
		this.reminderController = reminderController;
	}
	
	
	/**
	 * Setter for attribute mainViewAUI
	 * 
	 * @return mainViewAUI object
	 * 
	 */
	public void setMainViewAUI(MainViewAUI mainViewAUI) {
		this.mainViewAUI = mainViewAUI;
	}

}
