package view.passwordsListView;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.UserPassword;

public class PasswordModelView {

	private final StringProperty applicationProperty;
	
	private final StringProperty urlProperty;
	
	private UserPassword password;

	
	public PasswordModelView(UserPassword password){
		this.password = password;
	
		this.applicationProperty = new SimpleStringProperty(password.getApplication());
		this.urlProperty = new SimpleStringProperty(password.getUrl());
	}
	
	// Getter für UserPassword
	public UserPassword getPassword(){
		return password;
	}

	public String getApplication() {
		return applicationProperty.get();
	}

	public String getUrl() {
		return urlProperty.get();
	}
	
	public void setApplication(String applicationProperty) {
		this.applicationProperty.set(applicationProperty);
		
	}

	public void setUrl(String urlProperty) {
		this.urlProperty.set(urlProperty);
	}
	
	public StringProperty getApplicationProperty(){
		return applicationProperty;
	}
	

	public StringProperty getUrlProperty(){
		return urlProperty;
	}
	
}
