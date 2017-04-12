package Pack_Fenetre;

import java.awt.Dimension;

import javax.swing.JButton;

import Pack_Appli.Application;

/**
 * Classe Bouton
 * @author Romain Duret
 * @version Build III -  v0.0
 * @since Build III -  v0.0
 */
class Button extends JButton{
	

	private static final long serialVersionUID = 637262569694527029L;

	/**
	 * Cr�ation d'un Button
	 * @param buttonName text du Bouton
	 * @param app Classe-application o� sera envoyer le signal du bouton
	 * @param isEnable Activ� ou non
	 */
	protected Button(String buttonName,Application app,boolean isEnable){
		setText(buttonName);
		setEnabled(isEnable);
		//On permet aux boutons de s'�tirer autant que possible
		setMaximumSize(new Dimension(Short.MAX_VALUE,Short.MAX_VALUE));
		addActionListener(app);
	}
}
