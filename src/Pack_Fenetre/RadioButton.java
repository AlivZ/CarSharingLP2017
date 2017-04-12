package Pack_Fenetre;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * Button de type Radio.
 * @author Romain Duret
 * @version Build III -  v0.0
 * @since Build III -  v0.0
 */
public class RadioButton extends JRadioButton{
	private static final long serialVersionUID = 6729994551712467626L;

	/**
	 * Constructeur
	 * @param buttonName titre de la fonction de cout 
	 * @param buttonGroup groupe qui gère l'exclusivité de la sélection
	 * @param layout Layout où sera visible le boutton
	 * @param isEnable Activable ou non.
	 */
	RadioButton(String buttonName, ButtonGroup buttonGroup, JPanel layout, boolean isEnable)
	{
		setText(buttonName);
		setEnabled(isEnable);
		buttonGroup.add(this);
		layout.add(this);
	}
}
