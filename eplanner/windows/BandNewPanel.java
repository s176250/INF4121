/*
 *	Innhold:  Metoder som oppretter og tegner elementene i artistPanel, og lytter på slett, ny og endre knapper.
 *
 *
 *				createUppermostPanel()					--> Legger til felt for bandnavn
 *				createTopTopLeftPanel() 				--> Oppretter Panel øverst til venstre med personinfo om artisten og JListe over bandmedlemmer.
 *				createTopTopMiddlePanel()				--> Top top Middle (Øverste midtre panel med autofyll knapp)
 *				createTopTopRightPanel() 				--> Top top Right (Øvre høyre panel med felter til kontaktperson info)
 *				createMiddlePanel() 					--> Middle (Midre panel med innfyllingsfelter for misc info)
 *				createBottomLeftPanel() 				--> Bottom Left (Nedre venstre panel med JList med tilgjengelige sjangere)
 *				createBottomMiddlePanel() 				--> Bottom Middle (Nedre midtre panel med tekstfelt for å skrive inn bandbeskrivelse)
 *				createBottomRightPanel() 				--> Bottom Right (Nedre høyre panel med tekstfelt for å skrive inn interninfo
 *				createBand()							--> Oppretter en nytt band med infoen hentet fra feltene i vinduet.
 *				addBandmembers()						--> Oppretter et personobjekt og legger det til i Jlisten som viser bandmedlemmer.
 *				removeBandmembers()						--> Fjerner valgt person fra Jlisten som viser bandmedlemmer.
 *				setGenreSelection()						--> Setter valgt sjanger i sjangerlisten.
 *
 *				Indre klasse
 *				NewArtistListener						--> Lytter på knapper lagt til i panelet.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 13.05.2012.
 */

package eplanner.windows;

import eplanner.*;
import eplanner.utilities.*;
import eplanner.windows.components.ExtendedJList;
import eplanner.windows.components.IconButton;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.border.Border;

//Hensikt: Tegne nytt band panel, og opprette nye band.
@SuppressWarnings("serial")
public class BandNewPanel extends JPanel
{
	//Lister
		protected ExtendedJList<String> genreJList;
		protected ExtendedJList<Person> bandmembersList;

	//Tekstbokser og felter
		protected JTextArea infoInput, descInput;
		protected JTextField nameField, adrField, countryField, telField, emailField;
		protected JTextField contactNameField, contactAdrField, contactCountryField, contactTelField, contactEmailField;
		protected JTextField webField, nationalityField, coverImageField, bandnameField;

	//Paneler og boxer
		private JPanel eastPanel, leftTopGrid, rightTopGrid, leftLeftTopGrid, leftRightTopGrid, rightRightTopGrid, rightLeftTopGrid;
		protected JPanel southPanel;
		private Box upperTopBox, bottomBox, leftBottomBox, middleBottomBox, rightBottomBox, topBox, lowerTopBox, centerPanel, uppermostBox;

	//Knapper
		private JButton autofillButton, exploreButton, addButton, emptyFieldsButton, addBandmember, removeBandmember;

	//Lyttere
		private NewArtistListener actionListener;

	//Annet
		private EPlannerMainWindow parent;
		private ArtistList artistList;
		protected GenreList genreList;
		public static final int DEFAULT_FIELD_SIZE = 20;	//Standard feltbredde.

	//Konstruktør
	public BandNewPanel(EPlannerMainWindow parent, ArtistList artistList, GenreList genreList)
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

		// Oppretter Lyttere
			actionListener = new NewArtistListener();

		// Oppretter JTextAreas
			infoInput = new JTextArea(2,2);
			infoInput.setLineWrap(true);
			infoInput.setWrapStyleWord(true);
			descInput = new JTextArea(2,2);
			descInput.setLineWrap(true);
			descInput.setWrapStyleWord(true);

		// Oppretter JButtons
			autofillButton = new IconButton(Utilities.getIcon("/Images/Icons/autofillIcon2.png"));
			autofillButton.addActionListener(actionListener);
			autofillButton.setBackground(EPlannerMainWindow.BASE_COLOR);
			autofillButton.setPreferredSize(new Dimension(44,32));
			autofillButton.setToolTipText("Autofyll");
			exploreButton = new IconButton(Utilities.getIcon(EPlannerMainWindow.EXPLORE_ICON), EPlannerMainWindow.BASE_COLOR, new Dimension(26,20));
			exploreButton.addActionListener(actionListener);
			exploreButton.setToolTipText("Finn bilde...");
			addButton = new JButton("Lagre Band");
			addButton.addActionListener(actionListener);
			emptyFieldsButton = new JButton("Tøm Felter");
			emptyFieldsButton.addActionListener(actionListener);
			addBandmember = new JButton("Legg til bandmedlem");
			addBandmember.addActionListener(actionListener);
			removeBandmember = new JButton("Fjern bandmedlem");
			removeBandmember.addActionListener(actionListener);

		// Oppretter JTextFields
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
			nationalityField = new JTextField(DEFAULT_FIELD_SIZE);
			nationalityField.addActionListener(actionListener);
			coverImageField = new JTextField(DEFAULT_FIELD_SIZE+20);
			coverImageField.addActionListener(actionListener);
			bandnameField = new JTextField(DEFAULT_FIELD_SIZE+10);
			bandnameField.addActionListener(actionListener);


		// Ingen vestre(LINE START) eller nordre(PAGE START) panel blir opprettet.
		// Hovedinnhold blir lagt i senterpanelet(CENTER), og et par knapper i søndre(PAGE END) panel.


		//------------------------------------------------------------------------------------------------------------------ Oppretter Senterpanel (CENTER)
			centerPanel = new Box(BoxLayout.PAGE_AXIS);							// Oppretter hovedpanel for ny artist vindu.
			centerPanel.setMinimumSize(new Dimension(500, 500));
			centerPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
			centerPanel.setBorder(BorderFactory.createEmptyBorder(10,0,0,15));

			topBox = new Box(BoxLayout.PAGE_AXIS);
				uppermostBox = new Box(BoxLayout.LINE_AXIS);
					createUppermostPanel();
				upperTopBox = new Box(BoxLayout.LINE_AXIS);
					createTopTopLeftPanel();	//--> --------------------------------------------------Øverst til venstre med personinfo om artisten og JListe over bandmedlemmer.
					createTopTopMiddlePanel(); 	//--> ------------------------------------------------- Øverste midtre panel med autofyll knapp)
					createTopTopRightPanel(); 	//--> ------------------------------------------------- Øvre høyre panel med felter til kontaktperson info)
				lowerTopBox = new Box(BoxLayout.PAGE_AXIS);
					createMiddlePanel(); 		//--> ------------------------------------------------- Midre panel med innfyllingsfelter for misc info)

			bottomBox = new Box(BoxLayout.LINE_AXIS);
				createBottomLeftPanel(); 	//--> ----------------------------------------------------- Nedre venstre panel med JList med tilgjengelige sjangere.
				createBottomMiddlePanel();	//--> ----------------------------------------------------- Nedre midtre panel med tekstfelt for å skrive inn bandbeskrivelse.
				createBottomRightPanel(); 	//--> ----------------------------------------------------- Nedre høyre panel med tekstfelt for å skrive inn interninfo.


			topBox.add(uppermostBox);
			topBox.add(upperTopBox);
			topBox.add(lowerTopBox);

			centerPanel.add(topBox);
			centerPanel.add(bottomBox);

			add(centerPanel, BorderLayout.CENTER);

		//------------------------------------------------------------------------------------------------------------------ Oppretter Sørpanel (PAGE END) - Knapper
			southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			southPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
			southPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,60));

			southPanel.add(addButton);
			southPanel.add(emptyFieldsButton);

			add(southPanel, BorderLayout.PAGE_END);


	}
	//Slutt konstruktør


// ------------------------------------------------------------------------------------------------------------------------------GUI Metoder:

	private void createUppermostPanel()	//--> Felt for bandnavn
	{

		JPanel uppermostPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		uppermostPanel.setMinimumSize(new Dimension(1000,29));
		uppermostPanel.setMaximumSize(new Dimension(10000,29));
		uppermostPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
		uppermostPanel.setBorder(BorderFactory.createEmptyBorder(0,3,0,0));
		uppermostPanel.add(new JLabel("Bandnavn: "));
		uppermostPanel.add(bandnameField);

		uppermostBox.add(uppermostPanel);
	}


	private void createTopTopLeftPanel() //--> ---------------------------------------------------------Oppretter Panel øverst til venstre med personinfo om artisten og JListe over bandmedlemmer.
	{
		JPanel topLeftCornerPane = new JPanel(new BorderLayout());

		// --- West
			JPanel leftTopWestPanel = new JPanel();

			bandmembersList = new ExtendedJList<>();
			bandmembersList.setFixedCellHeight(15);
			bandmembersList.setFixedCellWidth(100);
			bandmembersList.setPreferredSize(new Dimension(120, 115));
			bandmembersList.setFont(new Font("Arial",Font.PLAIN, 12 ));

			leftTopWestPanel.add(bandmembersList);
			leftTopWestPanel.setMinimumSize(new Dimension(125, 150));
			leftTopWestPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
			leftTopWestPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Bandmedlemmer"));


		// --- Center
				Box leftTopBox = new Box(BoxLayout.LINE_AXIS);
					leftTopBox.setMaximumSize(new Dimension(1000,150 ));
					leftTopBox.setPreferredSize(new Dimension(350, 150));
					leftTopBox.setBackground(EPlannerMainWindow.BASE_COLOR);
					leftTopBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Artist"));


			//--leftLeftTopGrid
				leftLeftTopGrid = new JPanel(new GridLayout(5, 1));
				leftLeftTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);
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
				//Adding Left
					leftLeftTopGrid.add(artistLabelFlow1);
					leftLeftTopGrid.add(artistLabelFlow2);
					leftLeftTopGrid.add(artistLabelFlow3);
					leftLeftTopGrid.add(artistLabelFlow4);
					leftLeftTopGrid.add(artistLabelFlow5);

			//--leftRightTopGrid
				leftRightTopGrid = new JPanel(new GridLayout(5, 1));
				leftRightTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);
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
				//Adding Right
					leftRightTopGrid.add(artistFlow1);
					leftRightTopGrid.add(artistFlow2);
					leftRightTopGrid.add(artistFlow3);
					leftRightTopGrid.add(artistFlow4);
					leftRightTopGrid.add(artistFlow5);

				leftTopBox.add(leftLeftTopGrid);
				leftTopBox.add(leftRightTopGrid);


		// --- South
			JPanel leftTopSouthPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			leftTopSouthPanel.setMinimumSize(new Dimension(100,50));
			leftTopSouthPanel.setMaximumSize(new Dimension(100,50));
			leftTopSouthPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
			leftTopSouthPanel.add(removeBandmember);
			leftTopSouthPanel.add(addBandmember);


		topLeftCornerPane.add(leftTopWestPanel, BorderLayout.LINE_START);
		topLeftCornerPane.add(leftTopBox, BorderLayout.CENTER);
		topLeftCornerPane.add(leftTopSouthPanel, BorderLayout.PAGE_END);
		topLeftCornerPane.setMaximumSize(new Dimension(1000,180 ));
		topLeftCornerPane.setPreferredSize(new Dimension(550, 180));
		topLeftCornerPane.setBackground(EPlannerMainWindow.BASE_COLOR);

		upperTopBox.add(topLeftCornerPane);
	}

	private void createTopTopMiddlePanel() //--> -------------------------------------- Top top Middle (Øverste midtre panel med autofyll knapp)
	{
		JPanel middleTopGrid = new JPanel();
			middleTopGrid.setMinimumSize(new Dimension(100,50));
			middleTopGrid.setMaximumSize(new Dimension(150,50));
			middleTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);
			middleTopGrid.add(autofillButton);

		upperTopBox.add(middleTopGrid);
	}

	private void createTopTopRightPanel() //--> --------------------------------------- Top top Right (Øvre høyre panel med felter til kontaktperson info)
	{
		Box rightTopBox = new Box(BoxLayout.LINE_AXIS);
			rightTopBox.setMaximumSize(new Dimension(1000,180 ));
			rightTopBox.setPreferredSize(new Dimension(350, 180));
			rightTopBox.setBackground(EPlannerMainWindow.BASE_COLOR);
				Border etchedB = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Kontaktperson");
				Border emptyB = BorderFactory.createEmptyBorder(0,0,30,0);
			rightTopBox.setBorder(BorderFactory.createCompoundBorder(emptyB, etchedB));


	//--rightLeftTopGrid
		rightLeftTopGrid = new JPanel(new GridLayout(5, 1));
		rightLeftTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

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
		//Adding Left
			rightLeftTopGrid.add(contactLabelFlow1);
			rightLeftTopGrid.add(contactLabelFlow2);
			rightLeftTopGrid.add(contactLabelFlow3);
			rightLeftTopGrid.add(contactLabelFlow4);
			rightLeftTopGrid.add(contactLabelFlow5);


	//--rightRightTopGrid
		rightRightTopGrid = new JPanel(new GridLayout(5, 1));
		rightRightTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

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
		//Adding Right
			rightRightTopGrid.add(contactFlow1);
			rightRightTopGrid.add(contactFlow2);
			rightRightTopGrid.add(contactFlow3);
			rightRightTopGrid.add(contactFlow4);
			rightRightTopGrid.add(contactFlow5);


		rightTopBox.add(rightLeftTopGrid);
		rightTopBox.add(rightRightTopGrid);

		upperTopBox.add(rightTopBox);
		upperTopBox.add(Box.createHorizontalGlue());
	}

	private void createMiddlePanel() //--> --------------------------------------------------- Middle (Midre panel med innfyllingsfelter for misc info)
	{
		lowerTopBox.setBackground(EPlannerMainWindow.BASE_COLOR);
		lowerTopBox.setPreferredSize(new Dimension(10000,130 ));
		lowerTopBox.setMaximumSize(new Dimension(10000,130 ));
		lowerTopBox.setBorder( BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Misc"));

		JPanel coverFlow = new JPanel(new FlowLayout(FlowLayout.LEFT));
		coverFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
		coverFlow.add(new JLabel("Legg til cover-bilde:"));
		coverFlow.add(coverImageField);
		coverFlow.add(exploreButton);

		JPanel URLFlow = new JPanel(new FlowLayout(FlowLayout.LEFT));
		URLFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
		URLFlow.add(new JLabel("URL:"));
		URLFlow.add(webField);


		JPanel nationalityFlow = new JPanel(new FlowLayout(FlowLayout.LEFT));
		nationalityFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
		nationalityFlow.add(new JLabel("Bandets Nasjonalitet:"));
		nationalityFlow.add(nationalityField);


		lowerTopBox.add(coverFlow);
		lowerTopBox.add(URLFlow);
		lowerTopBox.add(nationalityFlow);
	}

	private void createBottomLeftPanel() //--> ----------------------------------------------------- Bottom Left (Nedre venstre panel med JList med tilgjengelige sjangere)
	{
		Box leftBottomBox = new Box(BoxLayout.LINE_AXIS);
		leftBottomBox.setPreferredSize(new Dimension(200,150 ));
		leftBottomBox.setMaximumSize(new Dimension(250,1000 ));
		leftBottomBox.setBackground(EPlannerMainWindow.BASE_COLOR);
		leftBottomBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Sjangere"));

		genreJList = new ExtendedJList<>(genreList.getGenreNames());
		genreJList.setFixedCellHeight(20);
		genreJList.setFixedCellWidth(160);
		genreJList.setSelectedIndex(0);


		JScrollPane scrollGenreList = new JScrollPane(genreJList);
		scrollGenreList.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		scrollGenreList.setPreferredSize(new Dimension(180, 150));
		scrollGenreList.setFont(new Font("Arial",Font.PLAIN, 16 ));


		leftBottomBox.add(scrollGenreList);

		bottomBox.add(leftBottomBox);
	}

	private void createBottomMiddlePanel() //--> ----------------------------------------------------- Bottom Middle (Nedre midtre panel med tekstfelt for å skrive inn bandbeskrivelse)
	{
		middleBottomBox = new Box(BoxLayout.LINE_AXIS);

		Border lineBM = BorderFactory.createLineBorder(Color.BLACK, 1);
		Border emptyBM = BorderFactory.createEmptyBorder(10,10,10,10);
		descInput.setBorder(BorderFactory.createCompoundBorder(emptyBM, lineBM));

		JScrollPane scrollDescInput = new JScrollPane(descInput);
		scrollDescInput.setPreferredSize(new Dimension(190,250 ));

		scrollDescInput.setBackground(EPlannerMainWindow.BASE_COLOR);
		scrollDescInput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Beskrivelse"));

		middleBottomBox.add(scrollDescInput);
		bottomBox.add(middleBottomBox);
	}


	private void createBottomRightPanel() //--> ----------------------------------------------------- Bottom Right (Nedre høyre panel med tekstfelt for å skrive inn interninfo
	{
		rightBottomBox = new Box(BoxLayout.LINE_AXIS);

		Border lineBR = BorderFactory.createLineBorder(Color.BLACK, 1);
		Border emptyBR = BorderFactory.createEmptyBorder(10,10,10,10);
		infoInput.setBorder(BorderFactory.createCompoundBorder(emptyBR, lineBR));

		JScrollPane scrollInfoInput = new JScrollPane(infoInput);
		scrollInfoInput.setPreferredSize(new Dimension(190,250 ));
		scrollInfoInput.setBackground(EPlannerMainWindow.BASE_COLOR);
		scrollInfoInput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Intern-info"));

		rightBottomBox.add(scrollInfoInput);
		bottomBox.add(rightBottomBox);
	}


//------------------------------------------------------------------------------------------------------------------------------ Funksjonsmetoder:

	protected void createBand()	//--> Oppretter en nytt band med infoen hentet fra feltene i vinduet.
	{
		String bName = bandnameField.getText();
		String webTmp = webField.getText();
		String infoTmp = infoInput.getText();
		String descTmp = descInput.getText();
		String natTmp = nationalityField.getText();
		Person contact = new Person(contactNameField.getText(), contactAdrField.getText(), contactCountryField.getText(), contactTelField.getText(), contactEmailField.getText());

		Object[] bandmembersArray = bandmembersList.toArray();
		LinkedList<Person> bmembers = new LinkedList<>();
		for(Object o : bandmembersArray)
			bmembers.add((Person) o);

		Band newBand = new Band(webTmp, infoTmp, descTmp, contact, bName, natTmp, bmembers);

		if(bName.trim().equals(""))
		{
			Utilities.showError(parent, "Vennligst fyll inn bandnavn");
			return;
		}

		if(!artistList.addArtist(newBand))
		{
			Utilities.showError(parent, "Artist/Band med samme navn finnes allerede.");
			return;
		}

		int[] selectedGenres = genreJList.getSelectedIndices();
		ArrayList<String> newGenres = new ArrayList<>(selectedGenres.length);

		for(int i = 0; i < selectedGenres.length; i++)
			newGenres.add(i, genreList.getGenreOnIndex(selectedGenres[i]));

		newBand.modifyGenres(newGenres);

		IOActions.createMediaPath(bName, Database.ARTIST);	//Oppretter en mappe med artistens navn
		String coverTmp = coverImageField.getText();
		if(!coverTmp.equals(""))
		{
			File coverArt = new File(coverTmp);
			if(!coverArt.exists())
				Utilities.showError(parent, "Fant ikke bildefil, setter standardbilde.");
			else
				IOActions.copyImage(coverArt, bName, Database.ARTIST);	//kopierer valgt bilde til artistens mappe.
		}
		parent.updateArtistJList();

		int svar = Utilities.showOption(parent, "Band opprettet! Tømme felter?");

		if(svar == JOptionPane.YES_OPTION)
			emptyFieldsButton.doClick();
	}

	protected void addBandmembers()	//Oppretter et personobjekt og legger det til i Jlisten som viser bandmedlemmer.
	{
		if(nameField.getText().equals(""))
			return;

		Person member = new Person(nameField.getText(), adrField.getText(), countryField.getText(), telField.getText(), emailField.getText());
		bandmembersList.add(member);
	}

	protected void removeBandmembers()	//Fjerner valgt person fra Jlisten som viser bandmedlemmer.
	{
		bandmembersList.remove(bandmembersList.getSelectedIndex());
	}

	protected void setGenreSelection()	//Setter valgt sjanger i sjangerlisten.
	{
		genreJList.setSelectedIndex(0);
	}

	protected void removeBandmember()	//Tømmer bandmedlemmer.
	{
		bandmembersList.clear();
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
				nationalityField.requestFocusInWindow();
			else if(e.getSource() == nationalityField)
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
				createBand();
			}
			else if(e.getSource() == exploreButton)
			{
				Utilities.setPath(parent, coverImageField, JFileChooser.FILES_ONLY);
			}
			else if(e.getSource() == addBandmember)
			{
				addBandmembers();
			}
			else if(e.getSource() == removeBandmember)
			{
				removeBandmembers();
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
				contactNameField.setText("");
				contactAdrField.setText("");
				contactCountryField.setText("");
				contactTelField.setText("");
				contactEmailField.setText("");
				webField.setText("");
				nationalityField.setText("");
				coverImageField.setText("");
				bandnameField.setText("");
				removeBandmember();
			}
		}
	}//End of class NewArtistListener
} // End of class BandNewPanel

