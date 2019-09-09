package controller;

import java.time.LocalDateTime;
import java.util.TreeSet;

import model.DataManager;
import model.UserPassword;

public class ReminderController {

	DataManagerController dmc;
	DataManager dataManager;
		
	public ReminderController(DataManagerController dataManagerController) {
		dmc = dataManagerController;
		dataManager = dmc.getDataManager();
	}

	public TreeSet<UserPassword> getExpiredPasswords(){
		TreeSet<UserPassword> expired = new TreeSet<UserPassword>();
				
		for(UserPassword current : dataManager.getPasswords()){
			LocalDateTime remindDate = current.getTimeStamp().plus(current.getReminder());
			if(remindDate.isBefore(LocalDateTime.now())){
				expired.add(current);
			}
		}
		
		return expired;
	}
}
