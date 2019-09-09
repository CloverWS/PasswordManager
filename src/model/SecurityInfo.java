package model;

/**
 * Die Klasse SecurityInfo verwaltet die Sicherheitsfragen zu den Passwoertern des Users
 * @author David Henke
 *
 */
public class SecurityInfo {

	// Attribute
	
	private String sQuestion;

	private String sAnswer;

	/**
	 * Konstruktor
	 * @param sQuestion: Sicherheitsfrage 
	 * @param sAnswer: Zugehoerige Sicherheitsantwort
	 */
	public SecurityInfo() {
		this.sQuestion = "";
		this.sAnswer = "";
	}
	public SecurityInfo(String sQuestion, String sAnswer) {
		this.sQuestion = sQuestion;
		this.sAnswer = sAnswer;
	}

	// Getter & Setter
	
	/**
	 * Getter fuer die Sicherheitsfrage
	 * @return die Sicherheitsfrage
	 */
	public String getSQuestion() {
		return sQuestion;
	}

	/**
	 * Setter fuer die Sicherheitsfrage
	 * @param sQuestion: die neue Sicherheitsfrage
	 */
	public void setSQuestion(String sQuestion) {
		this.sQuestion = sQuestion;
	}

	/**
	 * Getter fuer die Antwort auf die Sicherheitsfrage
	 * @return die Antwort auf die Sicherheitsfrage
	 */
	public String getSAnswer() {
		return sAnswer;
	}

	/**
	 * Setter fuer die Antwort auf die Sicherheitsfrage
	 * @param sAnswer: die neue Antwort auf die Sicherheitsfrage
	 */
	public void setSAnswer(String sAnswer) {
		this.sAnswer = sAnswer;
	}

}
