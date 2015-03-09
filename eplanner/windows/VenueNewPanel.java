/*
 *	Innehold: Kode for å opprette og drive panelet som lar deg opprette nye Lokaler<Venue> i programmet.
 *
 *				Konstruktør						--> Inneholder den grunnleggende borderlayouten og er delt inn i de forskjellige seksjonene med kommentarer.
 *				createTopTopLeftPanel()			--> Oppretter panel for overskrift.
 *				createTopTopRightPanel()		--> Top top Right (Øvre høyre panel med felter til kontaktperson info).
 *				createMiddlePanel()				--> Middle (Midre panel med innfyllingsfelter for misc info).
 				createBottomMiddlePanel()		--> Bottom Middle (Nedre midtre panel med tekstfelt for å skrive inn beskrivelse).
 				createBottomRightPanel()		-->  Inneholder interninfo tekstområdet
 				setGenreSelection()				--> Setter første sjanger i listen til valgt.
 *				createVenue()					--> Oppretter selve lokalet slik at den kan puttes i lokale-listen
 *		|class| NewVenueListener   			--> ActionListener.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 13.05.2012.
 */

package eplanner.windows;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.math.*;

//--------------------------------- Eplanner import-setninger
import eplanner.users.*;
import eplanner.utilities.*;
import eplanner.windows.components.*;
import eplanner.*;

//	Hensikt: Å vise panelet som lar deg opprette et nytt Lokale.
@SuppressWarnings("serial")
public class VenueNewPanel extends JPanel
{
	//JList
		protected ExtendedJList<String> genreJList;

	//Button, fields, textAreas
		private JButton autofillButton, exploreButton, addButton, emptyFieldsButton, addBandmember, removeBandmember;
		protected JTextArea infoInput, descInput;
		protected JTextField maxCapField, defaultPrice, adrField, countryField, telField, emailField;
		protected JTextField contactNameField, contactAdrField, contactCountryField, contactTelField, contactEmailField;
		protected JTextField webField, coverImageField, venueNameField, rentPrice;

	//Lister
		private VenueList venueList;
		protected GenreList genreList;

	//JPanel/ Box
		private JPanel leftTopGrid, rightTopGrid, leftLeftTopGrid, leftRightTopGrid, rightRightTopGrid, rightLeftTopGrid, topLeftCornerPane, uppermostWestFlowLeft, uppermostWestFlowRight;
		private Box upperTopBox, bottomBox, leftBottomBox, middleBottomBox, rightBottomBox, topBox, lowerTopBox, centerPanel, uppermostBox;
		protected JPanel southPanel;

	//JCheckBox
		protected JCheckBox isNumbered;

	//Lyttere
		private NewVenueListener actionListener;

	//Parent
		private EPlannerMainWindow parent;

	//Konstant for størrelsen til JTextField
		public static final int DEFAULT_FIELD_SIZE = 17;

	public VenueNewPanel(EPlannerMainWindow parent, VenueList venueList, GenreList genreList)// --> Konstruktør
	{
		super(new BorderLayout());

		//Mottak av variabler fra parameterene.
			this.venueList = venueList;
			this.genreList = genreList;
			this.parent = parent;

		//Setter størrelse og farge på panelet.
			setPreferredSize(new Dimension(810, 700));
			setBackground(EPlannerMainWindow.BASE_COLOR);
			setVisible(true);


		// Oppretter Lyttere
			actionListener = new NewVenueListener();


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
			exploreButton = new IconButton(Utilities.getIcon(EPlannerMainWindow.EXPLORE_ICON), EPlannerMainWindow.BASE_COLOR, new Dimension(26,20));
			exploreButton.addActionListener(actionListener);
			exploreButton.setToolTipText("Finn bilde...");
			addButton = new JButton("Lagre Lokale");
			addButton.addActionListener(actionListener);
			emptyFieldsButton = new JButton("Tøm Felter");
			emptyFieldsButton.addActionListener(actionListener);

		// Oppretter JCheckBox
			isNumbered = new JCheckBox("", false);
			isNumbered.setBackground(EPlannerMainWindow.BASE_COLOR);


		// Oppretter JTextFields
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

			maxCapField = new JTextField(5);
			maxCapField.addActionListener(actionListener);
			defaultPrice = new JTextField(5);
			defaultPrice.addActionListener(actionListener);
			rentPrice = new JTextField(5);
			rentPrice.addActionListener(actionListener);

			webField = new JTextField(DEFAULT_FIELD_SIZE);
			webField.addActionListener(actionListener);
			coverImageField = new JTextField(DEFAULT_FIELD_SIZE+20);
			coverImageField.addActionListener(actionListener);
			venueNameField = new JTextField(DEFAULT_FIELD_SIZE+10);
			venueNameField.addActionListener(actionListener);


		// Ingen vestre(LINE START) eller nordre(PAGE START) panel blir opprettet.
		// Hovedinnhold blir lagt i senterpanelet(CENTER), og et par knapper i søndre(PAGE END) panel.


		//------------------------------------------------------------------------------------------------------------------ Oppretter Senterpanel (CENTER)
			// Oppretter hovedpanel for nytt lokale vindu.

			centerPanel = new Box(BoxLayout.PAGE_AXIS);
			centerPanel.setMinimumSize(new Dimension(500, 500));
			centerPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
			centerPanel.setBorder(BorderFactory.createEmptyBorder(10,0,0,15));

			topBox = new Box(BoxLayout.PAGE_AXIS);
				uppermostBox = new Box(BoxLayout.LINE_AXIS);
					createUppermostPanel();//--> -------------------------------------------------- Oppretter panel for overskrift.
				upperTopBox = new Box(BoxLayout.LINE_AXIS);
					createTopTopLeftPanel();	//--> --------------------------------------------------Øverst til venstre med personinfo om artisten og JListe over bandmedlemmer.
					createTopTopRightPanel(); 	//--> ------------------------------------------------- Øvre høyre panel med felter til kontaktperson info)
				lowerTopBox = new Box(BoxLayout.PAGE_AXIS);
					createMiddlePanel(); 		//--> ------------------------------------------------- Midre panel med innfyllingsfelter for misc info)

			bottomBox = new Box(BoxLayout.LINE_AXIS);
				createBottomLeftPanel(); 	//--> ----------------------------------------------------- Nedre venstre panel med JList med tilgjengelige sjangere)
				createBottomMiddlePanel();	//--> ----------------------------------------------------- Nedre midtre panel med tekstfelt for å skrive inn bandbeskrivelse)
				createBottomRightPanel(); 	//--> ----------------------------------------------------- BottomRight


			topBox.add(uppermostBox);
			topBox.add(upperTopBox);
			topBox.add(lowerTopBox);

			centerPanel.add(topBox);
			centerPanel.add(bottomBox);

			add(centerPanel, BorderLayout.CENTER);

		//------------------------------------------------------------------------------------------------------------------ Oppretter Sørpanel (PAGE END)
			southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			southPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
			southPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,60));

			southPanel.add(addButton);
			southPanel.add(emptyFieldsButton);

			add(southPanel, BorderLayout.PAGE_END);
	}


// ------------------------------------------------------------------------------------------------------------------------------GUI Metoder:

	private void createUppermostPanel() //--> Oppretter panel for overskrift.
	{

		JPanel uppermostPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		uppermostPanel.setMinimumSize(new Dimension(1000,29));
		uppermostPanel.setMaximumSize(new Dimension(10000,29));
		uppermostPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
		uppermostPanel.setBorder(BorderFactory.createEmptyBorder(0,3,0,0));
		uppermostPanel.add(new JLabel("Navn på lokale: "));
		uppermostPanel.add(venueNameField);

		uppermostBox.add(uppermostPanel);
	}


	private void createTopTopLeftPanel() //--> Oppretter Panel øverst til venstre med valg angående plasser i lokalet.
	{
		topLeftCornerPane = new JPanel(new BorderLayout());

		// --- West
			Box leftTopWestPanel = new Box(BoxLayout.LINE_AXIS);


			//--Plasser labels
				uppermostWestFlowLeft = new JPanel(new GridLayout(4, 1));
				uppermostWestFlowLeft.setBackground(EPlannerMainWindow.BASE_COLOR);
					JPanel maxCapLabelFlow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
					maxCapLabelFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
					maxCapLabelFlow.add(new JLabel("Maks cap.:"));
					JPanel defaultPriceLabelFlow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
					defaultPriceLabelFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
					defaultPriceLabelFlow.add(new JLabel("Std. pris:"));
					JPanel rentPriceLabelFlow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
					rentPriceLabelFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
					rentPriceLabelFlow.add(new JLabel("Leiepris:"));
					JPanel isNumberedLabelFlow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
					isNumberedLabelFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
					isNumberedLabelFlow.add(new JLabel("Nr-plasser"));

				//Adding Left
					uppermostWestFlowLeft.add(maxCapLabelFlow);
					uppermostWestFlowLeft.add(defaultPriceLabelFlow);
					uppermostWestFlowLeft.add(rentPriceLabelFlow);
					uppermostWestFlowLeft.add(isNumberedLabelFlow);

			//--Plasser tekstbokser
				uppermostWestFlowRight = new JPanel(new GridLayout(4, 1));
				uppermostWestFlowRight.setBackground(EPlannerMainWindow.BASE_COLOR);
					JPanel maxCapFlow = new JPanel(new FlowLayout(FlowLayout.LEFT));
					maxCapFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
					maxCapFlow.add(maxCapField);
					JPanel defaultPriceFlow = new JPanel(new FlowLayout(FlowLayout.LEFT));
					defaultPriceFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
					defaultPriceFlow.add(defaultPrice);
					JPanel rentPriceFlow = new JPanel(new FlowLayout(FlowLayout.LEFT));
					rentPriceFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
					rentPriceFlow.add(rentPrice);
					JPanel isNumberedFlow = new JPanel(new FlowLayout(FlowLayout.LEFT));
					isNumberedFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
					isNumberedFlow.add(isNumbered);

				//Adding Right
					uppermostWestFlowRight.add(maxCapFlow);
					uppermostWestFlowRight.add(defaultPriceFlow);
					uppermostWestFlowRight.add(rentPriceFlow);
					uppermostWestFlowRight.add(isNumberedFlow);


				leftTopWestPanel.add(uppermostWestFlowLeft);
				leftTopWestPanel.add(uppermostWestFlowRight);

			leftTopWestPanel.setMinimumSize(new Dimension(155, 100));
			leftTopWestPanel.setPreferredSize(new Dimension(155, 100));
			leftTopWestPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
			leftTopWestPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Plasser"));


		// --- Center
				Box leftTopBox = new Box(BoxLayout.LINE_AXIS);
					leftTopBox.setMaximumSize(new Dimension(310,150 ));
					leftTopBox.setPreferredSize(new Dimension(310, 100));
					leftTopBox.setBackground(EPlannerMainWindow.BASE_COLOR);
					leftTopBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Lokale"));


			//--leftLeftTopGrid
				leftLeftTopGrid = new JPanel(new GridLayout(4, 1));
				leftLeftTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);
					JPanel artistLabelFlow1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
					artistLabelFlow1.setBackground(EPlannerMainWindow.BASE_COLOR);
					artistLabelFlow1.add(new JLabel("Adr:"));
					JPanel artistLabelFlow2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
					artistLabelFlow2.setBackground(EPlannerMainWindow.BASE_COLOR);
					artistLabelFlow2.add(new JLabel("Land:"));
					JPanel artistLabelFlow3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
					artistLabelFlow3.setBackground(EPlannerMainWindow.BASE_COLOR);
					artistLabelFlow3.add(new JLabel("Tlf:"));
					JPanel artistLabelFlow4 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
					artistLabelFlow4.setBackground(EPlannerMainWindow.BASE_COLOR);
					artistLabelFlow4.add(new JLabel("Epost:"));
				//Adding Left
					leftLeftTopGrid.add(artistLabelFlow1);
					leftLeftTopGrid.add(artistLabelFlow2);
					leftLeftTopGrid.add(artistLabelFlow3);
					leftLeftTopGrid.add(artistLabelFlow4);

			//--leftRightTopGrid
				leftRightTopGrid = new JPanel(new GridLayout(4, 1));
				leftRightTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);
					JPanel artistFlow1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
					artistFlow1.setBackground(EPlannerMainWindow.BASE_COLOR);
					artistFlow1.add(adrField);
					JPanel artistFlow2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
					artistFlow2.setBackground(EPlannerMainWindow.BASE_COLOR);
					artistFlow2.add(countryField);
					JPanel artistFlow3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
					artistFlow3.setBackground(EPlannerMainWindow.BASE_COLOR);
					artistFlow3.add(telField);
					JPanel artistFlow4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
					artistFlow4.setBackground(EPlannerMainWindow.BASE_COLOR);
					artistFlow4.add(emailField);

				//Adding Right
					leftRightTopGrid.add(artistFlow1);
					leftRightTopGrid.add(artistFlow2);
					leftRightTopGrid.add(artistFlow3);
					leftRightTopGrid.add(artistFlow4);

				leftTopBox.add(leftLeftTopGrid);
				leftTopBox.add(leftRightTopGrid);


		// --- Adding panels
		topLeftCornerPane.add(leftTopWestPanel, BorderLayout.LINE_START);
		topLeftCornerPane.add(leftTopBox, BorderLayout.CENTER);
		topLeftCornerPane.setMaximumSize(new Dimension(750,150 ));
		topLeftCornerPane.setPreferredSize(new Dimension(750, 150));
		topLeftCornerPane.setBackground(EPlannerMainWindow.BASE_COLOR);

		upperTopBox.add(topLeftCornerPane);
	}

	private void createTopTopRightPanel() //--> Top top Right (Øvre høyre panel med felter til kontaktperson info)
	{
		Box rightTopBox = new Box(BoxLayout.LINE_AXIS);
			rightTopBox.setMaximumSize(new Dimension(1000,180 ));
			rightTopBox.setPreferredSize(new Dimension(310, 180));
			rightTopBox.setBackground(EPlannerMainWindow.BASE_COLOR);
			rightTopBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Kontaktperson"));

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

		topLeftCornerPane.add(rightTopBox, BorderLayout.LINE_END);
		upperTopBox.add(Box.createHorizontalGlue());
	}

	private void createMiddlePanel() //--> Middle (Midre panel med innfyllingsfelter for misc info)
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

		lowerTopBox.add(coverFlow);
		lowerTopBox.add(URLFlow);
	}

	private void createBottomLeftPanel() //--> Bottom Left (Nedre venstre panel med JList med tilgjengelige sjangere)
	{
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

		bottomBox.add(leftBottomBox);
	}

	private void createBottomMiddlePanel() //-->  Bottom Middle (Nedre midtre panel med tekstfelt for å skrive inn beskrivelse)
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


	private void createBottomRightPanel() //-->  Inneholder interninfo tekstområdet
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

	protected void setGenreSelection()// --> Setter første sjanger i listen til valgt.
	{
		genreJList.setSelectedIndex(0);
	}

	// ----------------------------------------------------------------------------- Funksjonsmetoder:

	protected void createVenue()// --> Oppretter selve lokalet slik at den kan puttes i lokale-listen
	{
		String vName = venueNameField.getText();

		if(vName.trim().equals(""))
		{
			Utilities.showError(parent, "Vennligst fyll inn navn");
			return;
		}

		String adrTmp = adrField.getText();
		String telTmp = telField.getText();
		String emailTmp = emailField.getText();
		String webTmp = webField.getText();
		BigDecimal stdTmp = null;
		BigDecimal rentTmp = null;
		int maxTmp = 0;

		try
		{
			maxTmp = Integer.parseInt(maxCapField.getText());
			stdTmp = new BigDecimal(defaultPrice.getText());
			rentTmp = new BigDecimal(rentPrice.getText());
		}
		catch(NumberFormatException nfe)
		{
			Utilities.showError(parent, "Skriv kun tall i maks kapasitet- og std. pris feltene!");
			return;
		}

		String infoTmp = infoInput.getText();
		String descTmp = descInput.getText();
		Person contact = new Person(contactNameField.getText(), contactAdrField.getText(), contactCountryField.getText(), contactTelField.getText(), contactEmailField.getText());

		int[] selectedGenres = genreJList.getSelectedIndices();
		ArrayList<String> newGenres = new ArrayList<>(selectedGenres.length);

		for(int i = 0; i < selectedGenres.length; i++)
			newGenres.add(i, genreList.getGenreOnIndex(selectedGenres[i]));

		Venue newVenue = new Venue(vName, adrTmp, telTmp, emailTmp, descTmp, webTmp, maxTmp, isNumbered.isSelected(), newGenres, stdTmp,contact, infoTmp, rentTmp);


		if(!venueList.addVenue(newVenue))
		{
			Utilities.showError(parent, "Lokale med samme navn finnes allerede.");
			return;
		}

		IOActions.createMediaPath(vName, Database.VENUE);
		String coverTmp = coverImageField.getText();
		if(!coverTmp.equals(""))
		{
			File coverArt = new File(coverTmp);
			if(!coverArt.exists())
				Utilities.showError(parent, "Fant ikke bildefil, setter standardbilde.");
			else
				IOActions.copyImage(coverArt, vName, Database.VENUE);
		}
		parent.updateVenueJList();
		Utilities.showInfo(parent, "Lokale opprettet!");
		emptyFieldsButton.doClick();
	}

// ----------------------------------------------------------------------------- Lytteklasser

	private class NewVenueListener implements ActionListener// --> ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == venueNameField)			// Convenience Start -- Gjør at man kan trykke enter for å komme til neste felt.
				maxCapField.requestFocusInWindow();

			else if(e.getSource() == maxCapField)
				defaultPrice.requestFocusInWindow();

			else if(e.getSource() == defaultPrice)
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
				descInput.requestFocusInWindow();		// Convenience End

			else if(e.getSource() == addButton)
			{
				createVenue();
			}
			else if(e.getSource() == exploreButton)
			{
				Utilities.setPath(parent, coverImageField, JFileChooser.FILES_ONLY);
			}
			else if(e.getSource() == emptyFieldsButton)
			{
				infoInput.setText("");
				descInput.setText("");
				venueNameField.setText("");
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
				coverImageField.setText("");
				maxCapField.setText("0");
				defaultPrice.setText("0");
				rentPrice.setText("0");
			}
		}
	}//End of Listener class
} // End of class Myclass

