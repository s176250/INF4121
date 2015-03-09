/*
 *	Innehold:  Metoder for endring av passord.
 *
 *				drawUserInfoPanel()				--> Tegner brukerinstillinger panelet.
 *				save()							--> Oppdaterer passordet til innlogget bruker
 *
 *				Indre klasse
 *		 		Listener						--> Lytteklasse for knappene i SettingsWindow
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 13.05.2012.
 */

package eplanner.windows;

import eplanner.*;
import eplanner.utilities.*;
import eplanner.users.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;

//Hensikt: Panel for at brukeren kan endre instillinger.
@SuppressWarnings("serial")
public class SettingsWindow extends JFrame
{
	private JButton saveSettings, close;
	private JTabbedPane settingsTabs;
	private JPanel userInfoPanel, settingsPanel, southPanel;
	private JTextField oldPwdField, newPwdField, repeatPwdField;
	private EPlannerMainWindow parent;
	private Account user;

	public SettingsWindow(EPlannerMainWindow parent, Account user)
	{
		super("Instillinger");

		this.parent = parent;
		this.user = user;

		//Oppretter lytter
			Listener listen = new Listener();

		//Opprett Tekstfelt
			oldPwdField = new JTextField(10);
			newPwdField = new JTextField(10);
			repeatPwdField = new JTextField(10);

		//Oppretter Knapper
			saveSettings = new JButton("Lagre");
			saveSettings.addActionListener(listen);
			close = new JButton("Lukk");
			close.addActionListener(listen);

		//Setter preferanser
			setSize(400, 400);
			setVisible(true);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			Utilities.setIcon(this);
			setLocationRelativeTo(parent);

		settingsPanel = new JPanel(new BorderLayout());

		//---------------------------------------------------------CENTER
		settingsTabs = new JTabbedPane();
		userInfoPanel = new JPanel();
		settingsTabs.addTab("Brukerinfo", userInfoPanel);
		drawUserInfoPanel();
		settingsPanel.add(settingsTabs, BorderLayout.CENTER);


		//--------------------------------------------------------SOUTH
		southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		southPanel.add(saveSettings);
		southPanel.add(close);
		settingsPanel.add(southPanel, BorderLayout.PAGE_END);


		setContentPane(settingsPanel);
	}

	private void drawUserInfoPanel()	//--> Tegner brukerinstillinger panelet.
	{
			Box leftTopBox = new Box(BoxLayout.LINE_AXIS);

			leftTopBox.setMaximumSize(new Dimension(1000,150 ));
			leftTopBox.setPreferredSize(new Dimension(350, 150));
			leftTopBox.setBackground(EPlannerMainWindow.BASE_COLOR);
			leftTopBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Endre Passord"));

			JPanel leftLeftTopGrid = new JPanel(new GridLayout(3, 1));
			leftLeftTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

			JPanel leftRightTopGrid = new JPanel(new GridLayout(3, 1));
			leftRightTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

			//leftLeftTopGrid - Labels
			JPanel artistLabelFlow1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			artistLabelFlow1.setBackground(EPlannerMainWindow.BASE_COLOR);
			artistLabelFlow1.add(new JLabel("Gammelt Passord:"));
			JPanel artistLabelFlow2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			artistLabelFlow2.setBackground(EPlannerMainWindow.BASE_COLOR);
			artistLabelFlow2.add(new JLabel("Nytt Passord:"));
			JPanel artistLabelFlow3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			artistLabelFlow3.setBackground(EPlannerMainWindow.BASE_COLOR);
			artistLabelFlow3.add(new JLabel("Repeter:"));


			//leftRightTopGrid - Tekstfelter
			JPanel artistFlow1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			artistFlow1.setBackground(EPlannerMainWindow.BASE_COLOR);
			artistFlow1.add(oldPwdField);
			JPanel artistFlow2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			artistFlow2.setBackground(EPlannerMainWindow.BASE_COLOR);
			artistFlow2.add(newPwdField);
			JPanel artistFlow3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			artistFlow3.setBackground(EPlannerMainWindow.BASE_COLOR);
			artistFlow3.add(repeatPwdField);

			//Adding Left
			leftLeftTopGrid.add(artistLabelFlow1);
			leftLeftTopGrid.add(artistLabelFlow2);
			leftLeftTopGrid.add(artistLabelFlow3);

			//Adding Right
			leftRightTopGrid.add(artistFlow1);
			leftRightTopGrid.add(artistFlow2);
			leftRightTopGrid.add(artistFlow3);

			leftTopBox.add(leftLeftTopGrid);
			leftTopBox.add(leftRightTopGrid);

			userInfoPanel.add(leftTopBox);
		}

	private void save()	//--> Oppdaterer passordet til innlogget bruker
	{
		String newPwd = newPwdField.getText();
		String repeatPwd = repeatPwdField.getText();

		if(newPwd.length() < 3)
		{
			Utilities.showError(parent, "Passord må være minimum 4 tegn!");
			return;
		}

		if(!newPwd.equals(repeatPwd))
		{
			Utilities.showError(parent, "Nytt passord matcher ikke!");
			return;
		}

		if(!user.setPwd(oldPwdField.getText(), newPwd))
		{
			Utilities.showError(parent, "Gammelt passord er feil!");
			return;
		}

		Utilities.showInfo(parent, "Passord er endret.");
		parent.saveToFile();
	}

	private void close()
	{
		this.dispose();
	}

//--------------------------------------------------------------------INDRE KLASSE

	//Hensikt: Lytteklasse for knappene i SettingsWindow
	private class Listener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if( e.getSource() == saveSettings)
			{
				save();
			}
			else
				close();
		}
	}//End of Listener class

}// End of class SettingsWindow


