package view.setting;



import javafx.collections.FXCollections;
import javafx.scene.control.ChoiceBox;


/**diese Klass ist für Reminder zu übersetzen,aber funktioniert nicht.....
 * @author sopr038
 *
 */
public class ReminderLanguageSetting {
	public void reminderLanguageSetting(String language,ChoiceBox<String> reminderChoiceBox) {
		switch (language) {
		case "English":
			reminderChoiceBox.setItems((FXCollections.observableArrayList("One Week","Two Week","Four Week","Three Months","Six Months","One Year")));
			reminderChoiceBox.getSelectionModel().select(0);
			break;
		case "German":
			reminderChoiceBox.setItems((FXCollections.observableArrayList("Eine Woche","Zwei Wochen","Vier Wochen","Drei Monate","Sechs Monate","Ein Jahr")));
			reminderChoiceBox.getSelectionModel().select(0);
			break;
		case "Chinese":
			reminderChoiceBox.setItems((FXCollections.observableArrayList("一周","两周","四周","三个月","六个月","一年")));
			reminderChoiceBox.getSelectionModel().select(0);
			break;
		case "Bulgarian":
			reminderChoiceBox.setItems((FXCollections.observableArrayList("Една седмица","Две седмици","Четири седмици","Три месеца","Шест месеца","Една година")));
			reminderChoiceBox.getSelectionModel().select(0);
			break;
		case "French":
			reminderChoiceBox.setItems((FXCollections.observableArrayList("Une semaine","Deux semaines","Quatre semaines","Trois mois","Six mois","Un ans")));
			reminderChoiceBox.getSelectionModel().select(0);
			break;
		}
	}

}
