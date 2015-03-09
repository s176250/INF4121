/*
 *	Innehold:  JFrame som viser kontakt og interninfo til lokale og artist.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 15.05.2012.
 */

package eplanner.windows;

import eplanner.utilities.*;
import eplanner.comparators.*;
import eplanner.Person;

import java.awt.*;
import javax.swing.*;

//Hensikt: Panel for at brukeren kan se interninfo.
@SuppressWarnings("serial")
public class InternalInfoWindow<E extends HasContact> extends JFrame
{
	private JTabbedPane infoTabs;
	private JPanel contactInfoPanel;
	private JScrollPane internalInfoPanel;
	private JTextArea internalInfo;
	private JTextField contactNameField, contactAdrField, contactCountryField, contactTelField, contactEmailField;
	private EPlannerMainWindow parent;
	private E element;

	public InternalInfoWindow(EPlannerMainWindow parent, E element)
	{
		super("Interninfo");

		this.parent = parent;
		this.element = element;


		//Opprett Tekstfelt
		contactNameField = new JTextField(ArtistNewPanel.DEFAULT_FIELD_SIZE);
		contactAdrField = new JTextField(ArtistNewPanel.DEFAULT_FIELD_SIZE);
		contactCountryField = new JTextField(ArtistNewPanel.DEFAULT_FIELD_SIZE);
		contactTelField = new JTextField(ArtistNewPanel.DEFAULT_FIELD_SIZE);
		contactEmailField = new JTextField(ArtistNewPanel.DEFAULT_FIELD_SIZE);
		internalInfo = new JTextArea();
		internalInfo.setEditable(false);
		internalInfo.setLineWrap(true);
		internalInfo.setWrapStyleWord(true);

		//Setter preferanser
			setSize(400, 400);
			setVisible(true);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setLocationRelativeTo(parent);
			Utilities.setIcon(this);

		infoTabs = new JTabbedPane();

		contactInfoPanel = new JPanel();
		contactInfoPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
		infoTabs.addTab("Kontakt", contactInfoPanel);
		internalInfoPanel = new JScrollPane(internalInfo);
		infoTabs.addTab("Info", internalInfoPanel);

		drawContactInfoPanel();
		drawInternalInfoPanel();


		setContentPane(infoTabs);
	}

	private void drawContactInfoPanel()	//--> Tegner brukerinstillinger panelet.
	{
			Box rightTopBox = new Box(BoxLayout.LINE_AXIS);
			rightTopBox.setMaximumSize(new Dimension(1000,160 ));
			rightTopBox.setPreferredSize(new Dimension(350, 160));
			rightTopBox.setBackground(EPlannerMainWindow.BASE_COLOR);
			rightTopBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Kontaktperson"));

			JPanel rightLeftTopGrid = new JPanel(new GridLayout(5, 1));
			rightLeftTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

			JPanel rightRightTopGrid = new JPanel(new GridLayout(5, 1));
			rightRightTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

			//rightLeftTopGrid - Labels
			JPanel contactLabelFlow1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			contactLabelFlow1.setBackground(EPlannerMainWindow.BASE_COLOR);
			contactLabelFlow1.add(new JLabel("Navn:"));
			JPanel contactLabelFlow2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			contactLabelFlow2.setBackground(EPlannerMainWindow.BASE_COLOR);
			contactLabelFlow2.add(new JLabel("Adr:"));
			JPanel contactLabelFlow3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			contactLabelFlow3.setBackground(EPlannerMainWindow.BASE_COLOR);
			contactLabelFlow3.add(new JLabel("Land:"));
			JPanel contactLabelFlow4 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			contactLabelFlow4.setBackground(EPlannerMainWindow.BASE_COLOR);
			contactLabelFlow4.add(new JLabel("Tlf:"));
			JPanel contactLabelFlow5 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			contactLabelFlow5.setBackground(EPlannerMainWindow.BASE_COLOR);
			contactLabelFlow5.add(new JLabel("Epost:"));

			//rightRightTopGrid - Tekstfelter
			JPanel contactFlow1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			contactFlow1.setBackground(EPlannerMainWindow.BASE_COLOR);
			contactFlow1.add(contactNameField);
			JPanel contactFlow2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			contactFlow2.setBackground(EPlannerMainWindow.BASE_COLOR);
			contactFlow2.add(contactAdrField);
			JPanel contactFlow3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			contactFlow3.setBackground(EPlannerMainWindow.BASE_COLOR);
			contactFlow3.add(contactCountryField);
			JPanel contactFlow4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			contactFlow4.setBackground(EPlannerMainWindow.BASE_COLOR);
			contactFlow4.add(contactTelField);
			JPanel contactFlow5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			contactFlow5.setBackground(EPlannerMainWindow.BASE_COLOR);
			contactFlow5.add(contactEmailField);

			//Adding Left
			rightLeftTopGrid.add(contactLabelFlow1);
			rightLeftTopGrid.add(contactLabelFlow2);
			rightLeftTopGrid.add(contactLabelFlow3);
			rightLeftTopGrid.add(contactLabelFlow4);
			rightLeftTopGrid.add(contactLabelFlow5);

			//Adding Right
			rightRightTopGrid.add(contactFlow1);
			rightRightTopGrid.add(contactFlow2);
			rightRightTopGrid.add(contactFlow3);
			rightRightTopGrid.add(contactFlow4);
			rightRightTopGrid.add(contactFlow5);

			rightTopBox.add(rightLeftTopGrid);
			rightTopBox.add(rightRightTopGrid);

			contactInfoPanel.add(rightTopBox);

			Person con = element.getContact();
			contactNameField.setText(con.getName());
			contactNameField.setEditable(false);
			contactAdrField.setText(con.getAdr());
			contactAdrField.setEditable(false);
			contactCountryField.setText(con.getLand());
			contactCountryField.setEditable(false);
			contactTelField.setText(con.getTel());
			contactTelField.setEditable(false);
			contactEmailField.setText(con.getEmail());
			contactEmailField.setEditable(false);
		}

		private void drawInternalInfoPanel()
		{
			internalInfo.setText(element.getInfo());
		}

}// End of class InternalInfoWindow


