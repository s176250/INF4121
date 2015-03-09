/*
 *	Innhold:  Metoder som oppretter og tegner elementene i artistPanel, og lytter på slett, ny og endre knapper.
 *
 *				displayNewArtist()						--> Tegner hele senterpanelet med felter for info om ny artist.
 *				createArtist()							--> Oppretter en ny artist med infoen hentet fra feltene i vinduet.
 *				setGenreSelection()						--> Setter valgt sjanger i sjangerlisten.
 *
 *				Indre klasser
 *				NewArtistListener						--> Lytter på knapper lagt til i panelet.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 13.05.2012.
 */

package eplanner.windows;

import eplanner.ArtistList;
import eplanner.GenreList;
import eplanner.Person;
import eplanner.Soloartist;
import eplanner.utilities.*;
import eplanner.windows.components.ExtendedJList;
import eplanner.windows.components.IconButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.Border;

//Hensikt: Tegne ny soloartist panel, og opprette nye soloartister.
@SuppressWarnings("serial")
public class ArtistNewPanel extends JPanel
{
	//Lister
		protected ExtendedJList<String> genreJList;

	//Tekstbokser og felter
		protected JTextArea infoInput, descInput;
		protected JTextField nameField, adrField, countryField, telField, emailField;
		protected JTextField contactNameField, contactAdrField, contactCountryField, contactTelField, contactEmailField;
		protected JTextField webField, instrumentField, coverImageField;

	//Paneler og boxer
		private JPanel eastPanel, leftTopGrid, rightTopGrid, leftLeftTopGrid, leftRightTopGrid, rightRightTopGrid, rightLeftTopGrid;
		private Box centerTopBox, centerBottomBox, leftBottomBox, middleBottomBox, rightBottomBox, topCenterBox, topBottomBox, centerPanel;
		protected JPanel southPanel;

	//Knapper
		private JButton autofillButton, exploreButton, addButton, emptyFieldsButton;

	//Lyttere
		private NewArtistListener actionListener;

	//Annet
		private ArtistList artistList;
		protected GenreList genreList;
		private EPlannerMainWindow parent;
		public static final int DEFAULT_FIELD_SIZE = 20;	//Standard bredde på tekstfelter.

	//Konstruktør
	public ArtistNewPanel(EPlannerMainWindow parent, ArtistList artistList, GenreList genreList)
	{
		super(new BorderLayout());

		//Lagre innkomne parametere
		this.artistList = artistList;
		this.genreList = genreList;
		this.parent = parent;

		//Sette preferanser
		setPreferredSize(new Dimension(810, 700));
		setBackground(EPlannerMainWindow.BASE_COLOR);
		setVisible(true);

		//Opprette lyttere
		actionListener = new NewArtistListener();

		//Opprette tekstbokser
		infoInput = new JTextArea(2,2);
		infoInput.setLineWrap(true);
		infoInput.setWrapStyleWord(true);
		descInput = new JTextArea(2,2);
		descInput.setLineWrap(true);
		descInput.setWrapStyleWord(true);

		//Opprette Buttons
		autofillButton = new IconButton(Utilities.getIcon("/Images/Icons/autofillIcon2.png"));
		autofillButton.addActionListener(actionListener);
		autofillButton.setBackground(EPlannerMainWindow.BASE_COLOR);
		autofillButton.setPreferredSize(new Dimension(44,32));
		autofillButton.setToolTipText("Autofyll");
		exploreButton = new IconButton(Utilities.getIcon(EPlannerMainWindow.EXPLORE_ICON), EPlannerMainWindow.BASE_COLOR, new Dimension(26,20));
		exploreButton.addActionListener(actionListener);
		exploreButton.setToolTipText("Finn bilde...");
		addButton = new JButton("Lagre Artist");
		addButton.addActionListener(actionListener);
		emptyFieldsButton = new JButton("Tøm Felter");
		emptyFieldsButton.addActionListener(actionListener);

		//Opprette tekstfelter
		nameField = new JTextField(DEFAULT_FIELD_SIZE);
		nameField.addActionListener(actionListener);
		adrField = new JTextField(DEFAULT_FIELD_SIZE);
		adrField.addActionListener(actionListener);
		countryField = new JTextField(DEFAULT_FIELD_SIZE);
		countryField.addActionListener(actionListener);
		telField = new JTextField(DEFAULT_FIELD_SIZE);
		telField.addActionListener(actionListener);
		emailField = new JTextField(DEFAULT_FIELD_SIZE);
		emailField.addActionListener(actionListener);

		contactNameField = new JTextField(DEFAULT_FIELD_SIZE);
		contactNameField.addActionListener(actionListener);
		contactAdrField = new JTextField(DEFAULT_FIELD_SIZE);
		contactAdrField.addActionListener(actionListener);
		contactCountryField = new JTextField(DEFAULT_FIELD_SIZE);
		contactCountryField.addActionListener(actionListener);
		contactTelField = new JTextField(DEFAULT_FIELD_SIZE);
		contactTelField.addActionListener(actionListener);
		contactEmailField = new JTextField(DEFAULT_FIELD_SIZE);
		contactEmailField.addActionListener(actionListener);

		webField = new JTextField(DEFAULT_FIELD_SIZE);
		webField.addActionListener(actionListener);
		instrumentField = new JTextField(DEFAULT_FIELD_SIZE);
		instrumentField.addActionListener(actionListener);
		coverImageField = new JTextField(DEFAULT_FIELD_SIZE+20);
		coverImageField.addActionListener(actionListener);


	//------------------------------------------------------------------------------------- CENTER Panel
		centerPanel = new Box(BoxLayout.PAGE_AXIS);
		centerPanel.setMinimumSize(new Dimension(500, 500));
		centerPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10,0,0,15));

		topCenterBox = new Box(BoxLayout.PAGE_AXIS);
			centerTopBox = new Box(BoxLayout.LINE_AXIS);
		centerBottomBox = new Box(BoxLayout.LINE_AXIS);

		topCenterBox.add(centerTopBox);
		centerPanel.add(topCenterBox);
		centerPanel.add(centerBottomBox);

		displayNewArtist();	//Tegner senterpanelet.

		add(centerPanel, BorderLayout.CENTER);


	//--------------------------------------------------------------------------------PAGE END (South Panel) --> buttons
		southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		southPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
		southPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,60));

		southPanel.add(addButton);
		southPanel.add(emptyFieldsButton);

		add(southPanel, BorderLayout.PAGE_END);

	}
	//Slutt konstruktør.


// ------------------------------------------------------------------------------------------------------------------------------GUI Metoder:
	// Alle metodene som slutter på panel/box er paneler som opprettes i tilsvarende posisjon til navnet.

	protected void displayNewArtist()	//--> Tegner hele senterpanelet med felter for info om ny artist.
	{
		//--------------------------------------------------------------------------------Top top Left (Felter til artist personinfo)
			Box leftTopBox = new Box(BoxLayout.LINE_AXIS);

			leftTopBox.setMaximumSize(new Dimension(1000,150 ));
			leftTopBox.setPreferredSize(new Dimension(350, 150));
			leftTopBox.setBackground(EPlannerMainWindow.BASE_COLOR);
			leftTopBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Artist"));


			leftLeftTopGrid = new JPanel(new GridLayout(5, 1));
			leftLeftTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

			leftRightTopGrid = new JPanel(new GridLayout(5, 1));
			leftRightTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

			//leftLeftTopGrid - Labels
			JPanel artistLabelFlow1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			artistLabelFlow1.setBackground(EPlannerMainWindow.BASE_COLOR);
			artistLabelFlow1.add(new JLabel("Navn:"));
			JPanel artistLabelFlow2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			artistLabelFlow2.setBackground(EPlannerMainWindow.BASE_COLOR);
			artistLabelFlow2.add(new JLabel("Adr:"));
			JPanel artistLabelFlow3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			artistLabelFlow3.setBackground(EPlannerMainWindow.BASE_COLOR);
			artistLabelFlow3.add(new JLabel("Land:"));
			JPanel artistLabelFlow4 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			artistLabelFlow4.setBackground(EPlannerMainWindow.BASE_COLOR);
			artistLabelFlow4.add(new JLabel("Tlf:"));
			JPanel artistLabelFlow5 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			artistLabelFlow5.setBackground(EPlannerMainWindow.BASE_COLOR);
			artistLabelFlow5.add(new JLabel("Epost:"));

			//leftRightTopGrid - Tekstfelter
			JPanel artistFlow1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			artistFlow1.setBackground(EPlannerMainWindow.BASE_COLOR);
			artistFlow1.add(nameField);
			JPanel artistFlow2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			artistFlow2.setBackground(EPlannerMainWindow.BASE_COLOR);
			artistFlow2.add(adrField);
			JPanel artistFlow3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			artistFlow3.setBackground(EPlannerMainWindow.BASE_COLOR);
			artistFlow3.add(countryField);
			JPanel artistFlow4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			artistFlow4.setBackground(EPlannerMainWindow.BASE_COLOR);
			artistFlow4.add(telField);
			JPanel artistFlow5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			artistFlow5.setBackground(EPlannerMainWindow.BASE_COLOR);
			artistFlow5.add(emailField);

			//Adding Left
			leftLeftTopGrid.add(artistLabelFlow1);
			leftLeftTopGrid.add(artistLabelFlow2);
			leftLeftTopGrid.add(artistLabelFlow3);
			leftLeftTopGrid.add(artistLabelFlow4);
			leftLeftTopGrid.add(artistLabelFlow5);

			//Adding Right
			leftRightTopGrid.add(artistFlow1);
			leftRightTopGrid.add(artistFlow2);
			leftRightTopGrid.add(artistFlow3);
			leftRightTopGrid.add(artistFlow4);
			leftRightTopGrid.add(artistFlow5);


			leftTopBox.add(leftLeftTopGrid);
			leftTopBox.add(leftRightTopGrid);

			centerTopBox.add(leftTopBox);

		//--------------------------------------------------------------------------------Top top Middle ( autofyllknapp )
			JPanel middleTopGrid = new JPanel();

			middleTopGrid.setMinimumSize(new Dimension(100,50));
			middleTopGrid.setMaximumSize(new Dimension(150,50));
			middleTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);
			middleTopGrid.add(autofillButton);

			centerTopBox.add(middleTopGrid);

		//--------------------------------------------------------------------------------Top top Right	(Felter for kontaktpersoninfo)

			Box rightTopBox = new Box(BoxLayout.LINE_AXIS);
			rightTopBox.setMaximumSize(new Dimension(1000,150 ));
			rightTopBox.setPreferredSize(new Dimension(350, 150));
			rightTopBox.setBackground(EPlannerMainWindow.BASE_COLOR);
			rightTopBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Kontaktperson"));

			rightLeftTopGrid = new JPanel(new GridLayout(5, 1));
			rightLeftTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

			rightRightTopGrid = new JPanel(new GridLayout(5, 1));
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

			centerTopBox.add(rightTopBox);
			centerTopBox.add(Box.createHorizontalGlue());

		//--------------------------------------------------------------------------------Top bottom/middle (Felter for misc info)
			topBottomBox = new Box(BoxLayout.PAGE_AXIS);
			topBottomBox.setBackground(EPlannerMainWindow.BASE_COLOR);
			topBottomBox.setPreferredSize(new Dimension(10000,130 ));
			topBottomBox.setMaximumSize(new Dimension(10000,130 ));

			topBottomBox.setBorder( BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Misc"));

			JPanel coverFlow = new JPanel(new FlowLayout(FlowLayout.LEFT));
			coverFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
			coverFlow.add(new JLabel("Legg til cover-bilde:"));
			coverFlow.add(coverImageField);
			coverFlow.add(exploreButton);

			JPanel URLFlow = new JPanel(new FlowLayout(FlowLayout.LEFT));
			URLFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
			URLFlow.add(new JLabel("URL:"));
			URLFlow.add(webField);

			JPanel instrumentFlow = new JPanel(new FlowLayout(FlowLayout.LEFT));
			instrumentFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
			instrumentFlow.add(new JLabel("Instrumenter:"));
			instrumentFlow.add(instrumentField);

			topBottomBox.add(coverFlow);
			topBottomBox.add(URLFlow);
			topBottomBox.add(instrumentFlow);
			topCenterBox.add(topBottomBox);


		//--------------------------------------------------------------------------------Bottom Left (Liste over sjangere)
			Box leftBottomBox = new Box(BoxLayout.LINE_AXIS);
			leftBottomBox.setPreferredSize(new Dimension(200,150 ));
			leftBottomBox.setMaximumSize(new Dimension(250,1000 ));
			leftBottomBox.setBackground(EPlannerMainWindow.BASE_COLOR);
			leftBottomBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Sjangere"));

			genreJList = new ExtendedJList<>(genreList.getGenreNames());
			genreJList.setFixedCellHeight(20);
			genreJList.setFixedCellWidth(160);
			setGenreSelection();

			JScrollPane scrollGenreList = new JScrollPane(genreJList);
			scrollGenreList.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
			scrollGenreList.setPreferredSize(new Dimension(180, 150));
			scrollGenreList.setFont(new Font("Arial",Font.PLAIN, 16 ));

			leftBottomBox.add(scrollGenreList);

			centerBottomBox.add(leftBottomBox);


		//--------------------------------------------------------------------------------Bottom Middle (Bekrivelse tekstboks)
			middleBottomBox = new Box(BoxLayout.LINE_AXIS);

			Border lineBM = BorderFactory.createLineBorder(Color.BLACK, 1);
			Border emptyBM = BorderFactory.createEmptyBorder(10,10,10,10);
			descInput.setBorder(BorderFactory.createCompoundBorder(emptyBM, lineBM));

			JScrollPane scrollDescInput = new JScrollPane(descInput);
			scrollDescInput.setPreferredSize(new Dimension(190,250 ));

			scrollDescInput.setBackground(EPlannerMainWindow.BASE_COLOR);
			scrollDescInput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Beskrivelse"));

			middleBottomBox.add(scrollDescInput);
			centerBottomBox.add(middleBottomBox);


		//--------------------------------------------------------------------------------Bottom Right (Interninfo tekstboks)
			rightBottomBox = new Box(BoxLayout.LINE_AXIS);

			Border lineBR = BorderFactory.createLineBorder(Color.BLACK, 1);
			Border emptyBR = BorderFactory.createEmptyBorder(10,10,10,10);
			infoInput.setBorder(BorderFactory.createCompoundBorder(emptyBR, lineBR));

			JScrollPane scrollInfoInput = new JScrollPane(infoInput);
			scrollInfoInput.setPreferredSize(new Dimension(190,250 ));
			scrollInfoInput.setBackground(EPlannerMainWindow.BASE_COLOR);
			scrollInfoInput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Intern-info"));

			rightBottomBox.add(scrollInfoInput);
			centerBottomBox.add(rightBottomBox);
	}


// ------------------------------------------------------------------------------------------------------------------------------Funksjonsmetoder:

	protected void createArtist()	//--> Oppretter en ny artist med infoen hentet fra feltene i vinduet.
	{
		String aName = nameField.getText();

		if(aName.trim().equals(""))
		{
			Utilities.showError(parent, "Vennligst fyll inn bandnavn");
			return;
		}

		String webTmp = webField.getText();
		String infoTmp = infoInput.getText();
		String descTmp = descInput.getText();
		String instTmp = instrumentField.getText();

		Person contact = new Person(contactNameField.getText(), contactAdrField.getText(), contactCountryField.getText(), contactTelField.getText(), contactEmailField.getText());
		Person soloartist = new Person(aName, adrField.getText(), countryField.getText(), telField.getText(), emailField.getText());

		Soloartist newSolo = new Soloartist(webTmp, infoTmp, descTmp, contact, instTmp, soloartist);

		if(!artistList.addArtist(newSolo))
		{
			Utilities.showError(parent, "Artist/Band med samme navn finnes allerede.");
			return;
		}

		int[] selectedGenres = genreJList.getSelectedIndices();	//Henter valgte sjangere

		ArrayList<String> newGenres = new ArrayList<>(selectedGenres.length);

		for(int i = 0; i < selectedGenres.length; i++)
		{
			newGenres.add(i, genreList.getGenreOnIndex(selectedGenres[i]));
		}
		newSolo.modifyGenres(newGenres);	//Setter artistens sjangere


		IOActions.createMediaPath(aName, Database.ARTIST);	//Oppretter en mappe med artistens navn.

		String coverTmp = coverImageField.getText();
		if(!coverTmp.equals(""))
		{
			File coverArt = new File(coverTmp);
			if(!coverArt.exists())
				Utilities.showError(parent, "Fant ikke bildefil, setter standardbilde.");
			else
				IOActions.copyImage(coverArt, aName, Database.ARTIST); //kopierer valgt bilde til artistens mappe.
		}
		parent.updateArtistJList();
		Utilities.showInfo(parent, "Artist opprettet!");
		emptyFieldsButton.doClick();
	}//End of createArtist()


	protected void setGenreSelection()	//Setter valgt sjanger i sjangerlisten.
	{
		genreJList.setSelectedIndex(0);
	}


// ------------------------------------------------------------------------------------------------------------------------------INDRE KLASSER
	//Hensikt: Lytter på knapper lagt til i panelet.
	private class NewArtistListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == nameField)			// Flytter markør til neste felt hver gang bruker trykker enter.
				adrField.requestFocusInWindow();
			else if(e.getSource() == adrField)
				countryField.requestFocusInWindow();
			else if(e.getSource() == countryField)
				telField.requestFocusInWindow();
			else if(e.getSource() == telField)
				emailField.requestFocusInWindow();
			else if(e.getSource() == emailField)
				contactNameField.requestFocusInWindow();
			else if(e.getSource() == contactNameField)
				contactAdrField.requestFocusInWindow();
			else if(e.getSource() == contactAdrField)
				contactCountryField.requestFocusInWindow();
			else if(e.getSource() == contactCountryField)
				contactTelField.requestFocusInWindow();
			else if(e.getSource() == contactTelField)
				contactEmailField.requestFocusInWindow();
			else if(e.getSource() == contactEmailField)
				coverImageField.requestFocusInWindow();
			else if(e.getSource() == coverImageField)
				webField.requestFocusInWindow();
			else if(e.getSource() == webField)
				instrumentField.requestFocusInWindow();
			else if(e.getSource() == instrumentField)
				descInput.requestFocusInWindow();		// Slutt markørflytting.

			else if(e.getSource() == autofillButton)
			{
				contactNameField.setText(nameField.getText());
				contactAdrField.setText(adrField.getText());
				contactCountryField.setText(countryField.getText());
				contactTelField.setText(telField.getText());
				contactEmailField.setText(emailField.getText());
				contactNameField.requestFocusInWindow();
			}
			else if(e.getSource() == addButton)
			{
				createArtist();
			}
			else if(e.getSource() == exploreButton)
			{
				Utilities.setPath(parent, coverImageField, JFileChooser.FILES_ONLY);
			}
			else if(e.getSource() == emptyFieldsButton)
			{
				infoInput.setText("");
				descInput.setText("");
				nameField.setText("");
				adrField.setText("");
				countryField.setText("");
				telField.setText("");
				emailField.setText("");
				instrumentField.setText("");
				contactNameField.setText("");
				contactAdrField.setText("");
				contactCountryField.setText("");
				contactTelField.setText("");
				contactEmailField.setText("");
				webField.setText("");
				coverImageField.setText("");
			}
		}//End of actionPerformed()
	}//End of class NewArtistListener

} // End of class ArtistNewPanel

