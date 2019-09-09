package controller;

import java.security.SecureRandom;
//import java.util.Arrays;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.UserPassword;

/**
 * Im PasswordController werden Methodem zum generieren und überprüfen von passwörtern bereit gestellt.
 * 
 * @author Kevin Bähre
 *
 */
public abstract class PasswordController {

	final private char[] SYMBOLS = "^$*.[]{}()?-\"!@#%&/\\,><':;|_~`".toCharArray();
    final private char[] LOWERCASE = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    final private char[] UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    final private char[] NUMBERS = "0123456789".toCharArray();
    final private char[] ALL_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789^$*.[]{}()?-\"!@#%&/\\,><':;|_~`".toCharArray();
    static private Random rand = new SecureRandom();
    
	/**
	 * generiert ein sicheres Passwort 
	 * 
	 * @return ein String der ein sicheres Passwort beinhaltet
	 */
    public String generatePassword() {
        int length = 10;
        char[] password = new char[length];

        //get the requirements out of the way
        password[0] = LOWERCASE[rand.nextInt(LOWERCASE.length)];
        password[1] = UPPERCASE[rand.nextInt(UPPERCASE.length)];
        password[2] = NUMBERS[rand.nextInt(NUMBERS.length)];
        password[3] = SYMBOLS[rand.nextInt(SYMBOLS.length)];

        //populate rest of the password with random chars
        for (int i = 4; i < length; i++) {
            password[i] = ALL_CHARS[rand.nextInt(ALL_CHARS.length)];
        }

        //shuffle it up
        for (int i = 0; i < password.length; i++) {
            int randomPosition = rand.nextInt(password.length);
            char temp = password[i];
            password[i] = password[randomPosition];
            password[randomPosition] = temp;
        }

        return new String(password);
    }
	
	/**
	 * ueberprueft, ob das uebergebene Passwort sicher ist
	 * 
	 * @param UserPassword password
	 * @return boolean, ob das Passwort sicher ist
	 */
	public boolean isSecure(UserPassword password) {
		return isSecure(password.getPassword());
	}

	/**
	 * ueberprueft, ob das uebergebene Passwort sicher ist
	 * 
	 * @param String password
	 * @return boolean, ob das Passwort sicher ist
	 */
	public boolean isSecure(String password) {
		//check if basic requirements are met
		String pattern = "((?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,})";
		Pattern regex = Pattern.compile(pattern);
		Matcher matcher = regex.matcher(password);
		Boolean basic = matcher.matches();
		
		//check if there are symbols inside the password
		for(Character character : SYMBOLS){
			String current = String.valueOf(character);
			if(password.contains(current))return basic;
		}

		
        return false;		
	}
	
	//helper method
	public boolean contains(char[] arr, char cha){
		for(int i=0; i<arr.length; i++){
			if(arr[i] == cha) return true;
		}
		return false;
	}
}
