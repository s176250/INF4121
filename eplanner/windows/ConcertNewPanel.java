/*
 *	Innehold: Kode for å opprette og drive panelet som lar deg opprette nye Events<Concert> i programmet.
 *
 *				Konstruktør						--> Inneholder den grunnleggende borderlayouten og er delt inn i de forskjellige seksjonene med kommentarer.
 *				createUppermostPanel()			--> Oppretter panel for overskrift.
 *				createEastPanel()				--> Oppretter østre panel med oppsummering.
 *				createTopTopLeftPanel()			--> Oppretter Panel øverst til venstre med personinfo om artisten og JListe over bandmedlemmer.
 *				createBottomLeftPanel()			--> Bottom Left (Nedre venstre panel med JList med tilgjengelige sjangere)
 *				createBottomMiddlePanel()		--> Bottom Middle (Nedre midtre panel med tekstfelt for å skrive inn bandbeskrivelse)
 *				createBottomRightPanel()		--> BottomRight(JList med oppvarmingsartist)
 *				updateVenueJList()				--> Oppdaterer JListen over Lokaler
 *				updateArtistJList()				--> Oppdaterer JListen over Artister.
 *				updateDateFields()				--> Oppdaterer datofeltene i oppsummeringen slik at de viser det spinnerne viser.
 *				checkVenueOccupied()			--> Sjekker om lokalet er opptatt på den datoen brukeren har valgt. Setter teksten i JTextFieldet rød i så fall.
 *				createConcert()					--> Oppretter selve eventen slik at den kan puttes i eventListen
 *		|class| FocusLostListener 				--> Oppdaterer oppsummeringsfeltene når fokus er mistet på originalfeltene.
 *		|class| ListListener  					--> Sørger for å oppdatere oppsummeringfeltene når noe er valgt i JListen til lokaler eller artister
 *		|class| NewConcertListener  			--> ActionListener
 *				setListenerActive()				--> Se kommentar over SpinnerListener.
 *		|class| SpinnerListener  				--> Oppdaterer spinnerne slik at de hele tiden står i forhold til et GregorianCalendar objekt.
 *													Dette gjør at spinnerne ruller riktig slik at måneden oppdateres når du går over den siste dagen i forrige måned osv.
 *													Fordi spinnerne skyter en ChangeEvent når de blir satt til en ny verdi bruker vi en boolean variabel for å hoppe over å sette spinneren
 *													når det er vi som setter dem. Hvis ikke vil de aldri bli oppdatert.
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

//	Hensikt: Å vise panelet som lar deg opprette en ny Event.
@SuppressWarnings("serial")
public class ConcertNewPanel extends JPanel
{
	//JList
		protected ExtendedJList<Venue> venueJList;
		protected ExtendedJList<Artist> mainArtistJList, secondaryArtistJList;

	//JSpinner
		protected ExtendedJSpinner yearSpinner, monthSpinner, daySpinner, hourSpinner, minSpinner;

	//Button, fields, textAreas
		private JButton exploreButton, emptyFieldsButton;
		protected JButton addButton;
		private JScrollPane scrollDescInput;
		protected JTextArea descInput;
		protected JTextField ticketField, defaultPriceField, reservedField;
		protected JTextField coverImageField, concertNameField;
		protected JTextField summaryTicketField, summaryPriceField, summaryResField, summaryNameField, summaryDateField;
		protected JTextField summaryTimeField, summaryVenueField, summaryMArtistField,summarySArtistField;

	//Lister
		protected VenueList venueList;
		protected ArtistList artistList;
		private ConcertList concertList;

	//Paneler/ Boxer
		protected JPanel southPanel, summaryLeftGrid, summaryRightGrid;
		private JPanel eastPanel, leftTopGrid, rightTopGrid, leftLeftTopGrid, leftRightTopGrid, topLeftCornerPane, uppermostWestFlowLeft, uppermostWestFlowRight;
		private Box upperTopBox, bottomBox, leftBottomBox, rightBottomBox, topBox, lowerTopBox, centerPanel, uppermostBox, centerBox, eastBox;
		protected Box summaryBox;

	//Lyttere
		private ListListener listListener;
		private NewConcertListener actionListener;
		private FocusLostListener focusListener;
		protected SpinnerListener spinnerListener;

	//Parent
		private EPlannerMainWindow parent;

	//Tid
		protected GregorianCalendar eventTime;

	//Layout
		protected GridLayout summaryLeftGridLayout, summaryRightGridLayout;

	//Konstant for størrelsen til JTextField
		public static final int DEFAULT_FIELD_SIZE = 10;

	public ConcertNewPanel(EPlannerMainWindow parent, VenueList venueList, ArtistList artistList, ConcertList concertList)// --> Konstruktør
	{
		super(new BorderLayout());

		//Mottak av variabler fra parameterene.
			this.venueList = venueList;
			this.artistList = artistList;
			this.concertList = concertList;
			this.parent = parent;

		//Setter størrelse og farge på panelet.
			setPreferredSize(new Dimension(810, 700));
			setBackground(EPlannerMainWindow.BASE_COLOR);
			setVisible(true);

		// Oppretter Lyttere
			actionListener = new NewConcertListener();
			listListener = new ListListener();
			focusListener = new FocusLostListener();
			spinnerListener =  new SpinnerListener();

		// Oppretter JTextAreas
			descInput = new JTextArea(2,2);
			descInput.setLineWrap(true);
			descInput.setWrapStyleWord(true);

		// Oppretter JButtons
			exploreButton = new IconButton(Utilities.getIcon(EPlannerMainWindow.EXPLORE_ICON), EPlannerMainWindow.BASE_COLOR, new Dimension(26,20));
			exploreButton.addActionListener(actionListener);
			exploreButton.setToolTipText("Finn bilde...");
			addButton = new JButton("Lagre Event");
			addButton.addActionListener(actionListener);
			emptyFieldsButton = new JButton("Tøm Felter");
			emptyFieldsButton.addActionListener(actionListener);

		// Oppretter JTextFields
			ticketField = new JTextField(DEFAULT_FIELD_SIZE);
			ticketField.addActionListener(actionListener);
			ticketField.addFocusListener(focusListener);
			defaultPriceField = new JTextField(DEFAULT_FIELD_SIZE);
			defaultPriceField.addActionListener(actionListener);
			defaultPriceField.addFocusListener(focusListener);
			reservedField = new JTextField(DEFAULT_FIELD_SIZE);
			reservedField.addActionListener(actionListener);
			reservedField.addFocusListener(focusListener);
			concertNameField = new JTextField(DEFAULT_FIELD_SIZE*3);
			concertNameField.addActionListener(actionListener);
			concertNameField.addFocusListener(focusListener);
			coverImageField	= new JTextField(DEFAULT_FIELD_SIZE);
			coverImageField.addActionListener(actionListener);
			coverImageField.addFocusListener(focusListener);

			summaryTicketField = new JTextField(DEFAULT_FIELD_SIZE+3);
			summaryTicketField.setEditable(false);
			summaryPriceField = new JTextField(DEFAULT_FIELD_SIZE+3);
			summaryPriceField.setEditable(false);
			summaryResField = new JTextField(DEFAULT_FIELD_SIZE+3);
			summaryResField.setEditable(false);
			summaryNameField = new JTextField(DEFAULT_FIELD_SIZE+3);
			summaryNameField.setEditable(false);
			summaryDateField = new JTextField(DEFAULT_FIELD_SIZE+3);
			summaryDateField.setEditable(false);
			summaryTimeField = new JTextField(DEFAULT_FIELD_SIZE+3);
			summaryTimeField.setEditable(false);
			summaryVenueField = new JTextField(DEFAULT_FIELD_SIZE+3);
			summaryVenueField.setEditable(false);
			summaryMArtistField = new JTextField(DEFAULT_FIELD_SIZE+3);
			summaryMArtistField.setEditable(false);
			summarySArtistField = new JTextField(DEFAULT_FIELD_SIZE+3);
			summarySArtistField.setEditable(false);

		// Oppretter ExtendedJSpinner
			eventTime = (GregorianCalendar) Calendar.getInstance();
			yearSpinner = new ExtendedJSpinner(eventTime.get(Calendar.YEAR),eventTime.getMinimum(Calendar.YEAR), eventTime.get(Calendar.YEAR)+100);
			yearSpinner.addChangeListener(spinnerListener);
			monthSpinner = new ExtendedJSpinner(eventTime.get(Calendar.MONTH)+1,eventTime.getMinimum(Calendar.MONTH), eventTime.getMaximum(Calendar.MONTH)+2);
			monthSpinner.addChangeListener(spinnerListener);
			daySpinner = new ExtendedJSpinner(eventTime.get(Calendar.DAY_OF_MONTH),eventTime.getMinimum(Calendar.DAY_OF_MONTH)-1, eventTime.getMaximum(Calendar.DAY_OF_MONTH)+1);
			daySpinner.addChangeListener(spinnerListener);
			hourSpinner = new ExtendedJSpinner(eventTime.get(Calendar.HOUR_OF_DAY),eventTime.getMinimum(Calendar.HOUR_OF_DAY)-1, eventTime.getMaximum(Calendar.HOUR_OF_DAY)+1);
			hourSpinner.addChangeListener(spinnerListener);
			minSpinner = new ExtendedJSpinner(eventTime.get(Calendar.MINUTE),eventTime.getMinimum(Calendar.MINUTE)-1, eventTime.getMaximum(Calendar.MINUTE)+1);
			minSpinner.addChangeListener(spinnerListener);

		// Oppretter ExtendedJList
			venueJList = new ExtendedJList<>(venueList.getArray());
			venueJList.addFocusListener(focusListener);
			venueJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			mainArtistJList = new ExtendedJList<>(artistList.getArray());
			mainArtistJList.addFocusListener(focusListener);
			mainArtistJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			secondaryArtistJList = new ExtendedJList<>(artistList.getArray());
			secondaryArtistJList.addFocusListener(focusListener);
			secondaryArtistJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		// Ingen vestre(LINE START) eller nordre(PAGE START) panel blir opprettet.
		// Hovedinnhold blir lagt i senterpanelet(CENTER), og et par knapper i søndre(PAGE END) panel.


		//------------------------------------------------------------------------------------------------------------------ Oppretter Senterpanel (CENTER)
			centerBox = new Box(BoxLayout.LINE_AXIS);
			centerPanel = new Box(BoxLayout.PAGE_AXIS);
			centerPanel.setMinimumSize(new Dimension(500, 500));
			centerPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
			centerPanel.setBorder(BorderFactory.createEmptyBorder(10,0,0,15));

			uppermostBox = new Box(BoxLayout.LINE_AXIS);
				createUppermostPanel();//--> -------------------------------------------------- Oppretter panel for overskrift.
			upperTopBox = new Box(BoxLayout.LINE_AXIS);
			lowerTopBox = new Box(BoxLayout.LINE_AXIS);
				createTopTopLeftPanel();	//--> --------------------------------------------------Øverst til venstre med personinfo om artisten og JListe over bandmedlemmer.

			bottomBox = new Box(BoxLayout.LINE_AXIS);
				createBottomLeftPanel(); 	//--> ----------------------------------------------------- Nedre venstre panel med JList med tilgjengelige sjangere)
				createBottomMiddlePanel();	//--> ----------------------------------------------------- Nedre midtre panel med tekstfelt for å skrive inn bandbeskrivelse)
				createBottomRightPanel(); 	//--> ----------------------------------------------------- BottomRight


			centerPanel.add(uppermostBox);
			centerPanel.add(upperTopBox);
			centerPanel.add(lowerTopBox);
			centerPanel.add(bottomBox);
			centerBox.add(centerPanel);

			createEastPanel();
			centerBox.add(eastPanel);
			centerBox.add(Box.createHorizontalGlue());


			add(centerBox, BorderLayout.CENTER);

		//------------------------------------------------------------------------------------------------------------------ Oppretter Sørpanel (PAGE END)
			southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			southPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
			southPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,60));

			southPanel.add(addButton);
			southPanel.add(emptyFieldsButton);

			add(southPanel, BorderLayout.PAGE_END);

			updateDateFields();

	}


// ------------------------------------------------------------------------------------------------------------------------------GUI Metoder:
		// Alle metodene som slutter på panel er paneler som opprettes i tilsvarende posisjon til navnet.

	private void createUppermostPanel() //-->  Oppretter panel for overskrift.
	{

		JPanel uppermostPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		uppermostPanel.setMinimumSize(new Dimension(1000,29));
		uppermostPanel.setMaximumSize(new Dimension(10000,29));
		uppermostPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
		uppermostPanel.setBorder(BorderFactory.createEmptyBorder(0,3,0,0));
		uppermostPanel.add(new JLabel("Navn på event: "));
		uppermostPanel.add(concertNameField);

		uppermostBox.add(uppermostPanel);
	}

	private void createEastPanel() //-->  Oppretter østre panel med oppsummering.
	{
		eastBox = new Box(BoxLayout.LINE_AXIS);
		eastPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		eastPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
		Border titledEast = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Oppsummering");
		Border emptyEast = BorderFactory.createEmptyBorder(39,0,0,0);
		eastPanel.setBorder(BorderFactory.createCompoundBorder(emptyEast, titledEast));
		eastPanel.setPreferredSize(new Dimension(250, 500));;

			    summaryBox = new Box(BoxLayout.LINE_AXIS);
				summaryBox.setMinimumSize(new Dimension(250, 250));
				summaryBox.setMaximumSize(new Dimension(500, 250));
				summaryBox.setPreferredSize(new Dimension(250, 250));
				summaryBox.setBackground(EPlannerMainWindow.BASE_COLOR);


		//--leftLeftTopGrid
			summaryLeftGridLayout = new GridLayout(9, 1);
			summaryLeftGrid = new JPanel(summaryLeftGridLayout);
			summaryLeftGrid.setBackground(EPlannerMainWindow.BASE_COLOR);
				JPanel summaryLeftFlow0 = new JPanel(new FlowLayout(FlowLayout.LEFT));
				summaryLeftFlow0.setBackground(EPlannerMainWindow.BASE_COLOR);
				summaryLeftFlow0.add(new JLabel("Eventnavn: "));
				JPanel summaryLeftFlow1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
				summaryLeftFlow1.setBackground(EPlannerMainWindow.BASE_COLOR);
				summaryLeftFlow1.add(new JLabel("Standardpris: "));
				JPanel summaryLeftFlow2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
				summaryLeftFlow2.setBackground(EPlannerMainWindow.BASE_COLOR);
				summaryLeftFlow2.add(new JLabel("Billetter:"));
				JPanel summaryLeftFlow3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
				summaryLeftFlow3.setBackground(EPlannerMainWindow.BASE_COLOR);
				summaryLeftFlow3.add(new JLabel("Res. bill.:"));
				JPanel summaryLeftFlow4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
				summaryLeftFlow4.setBackground(EPlannerMainWindow.BASE_COLOR);
				summaryLeftFlow4.add(new JLabel("Dato: "));
				JPanel summaryLeftFlow5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
				summaryLeftFlow5.setBackground(EPlannerMainWindow.BASE_COLOR);
				summaryLeftFlow5.add(new JLabel("Kl.: "));
				JPanel summaryLeftFlow6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
				summaryLeftFlow6.setBackground(EPlannerMainWindow.BASE_COLOR);
				summaryLeftFlow6.add(new JLabel("Lokale:"));
				JPanel summaryLeftFlow7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
				summaryLeftFlow7.setBackground(EPlannerMainWindow.BASE_COLOR);
				summaryLeftFlow7.add(new JLabel("Hovedartist"));
				JPanel summaryLeftFlow8 = new JPanel(new FlowLayout(FlowLayout.LEFT));
				summaryLeftFlow8.setBackground(EPlannerMainWindow.BASE_COLOR);
				summaryLeftFlow8.add(new JLabel("Andre artister:"));

			//Adding Left
				summaryLeftGrid.add(summaryLeftFlow0);
				summaryLeftGrid.add(summaryLeftFlow1);
				summaryLeftGrid.add(summaryLeftFlow2);
				summaryLeftGrid.add(summaryLeftFlow3);
				summaryLeftGrid.add(summaryLeftFlow4);
				summaryLeftGrid.add(summaryLeftFlow5);
				summaryLeftGrid.add(summaryLeftFlow6);
				summaryLeftGrid.add(summaryLeftFlow7);
				summaryLeftGrid.add(summaryLeftFlow8);


		//--leftRightTopGrid
			summaryRightGridLayout = new GridLayout(9, 1);
			summaryRightGrid = new JPanel(summaryRightGridLayout);
			summaryRightGrid.setBackground(EPlannerMainWindow.BASE_COLOR);
				JPanel summaryRightFlow0 = new JPanel(new FlowLayout(FlowLayout.LEFT));
				summaryRightFlow0.setBackground(EPlannerMainWindow.BASE_COLOR);
				summaryRightFlow0.add(summaryNameField);
				JPanel summaryRightFlow1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
				summaryRightFlow1.setBackground(EPlannerMainWindow.BASE_COLOR);
				summaryRightFlow1.add(summaryPriceField);
				JPanel summaryRightFlow2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
				summaryRightFlow2.setBackground(EPlannerMainWindow.BASE_COLOR);
				summaryRightFlow2.add(summaryTicketField);
				JPanel summaryRightFlow3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
				summaryRightFlow3.setBackground(EPlannerMainWindow.BASE_COLOR);
				summaryRightFlow3.add(summaryResField);
				JPanel summaryRightFlow4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
				summaryRightFlow4.setBackground(EPlannerMainWindow.BASE_COLOR);
				summaryRightFlow4.add(summaryDateField);
				JPanel summaryRightFlow5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
				summaryRightFlow5.setBackground(EPlannerMainWindow.BASE_COLOR);
				summaryRightFlow5.add(summaryTimeField);
				JPanel summaryRightFlow6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
				summaryRightFlow6.setBackground(EPlannerMainWindow.BASE_COLOR);
				summaryRightFlow6.add(summaryVenueField);
				JPanel summaryRightFlow7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
				summaryRightFlow7.setBackground(EPlannerMainWindow.BASE_COLOR);
				summaryRightFlow7.add(summaryMArtistField);
				JPanel summaryRightFlow8 = new JPanel(new FlowLayout(FlowLayout.LEFT));
				summaryRightFlow8.setBackground(EPlannerMainWindow.BASE_COLOR);
				summaryRightFlow8.add(summarySArtistField);

			//Adding Right
				summaryRightGrid.add(summaryRightFlow0);
				summaryRightGrid.add(summaryRightFlow1);
				summaryRightGrid.add(summaryRightFlow2);
				summaryRightGrid.add(summaryRightFlow3);
				summaryRightGrid.add(summaryRightFlow4);
				summaryRightGrid.add(summaryRightFlow5);
				summaryRightGrid.add(summaryRightFlow6);
				summaryRightGrid.add(summaryRightFlow7);
				summaryRightGrid.add(summaryRightFlow8);

			summaryBox.add(summaryLeftGrid);
			summaryBox.add(summaryRightGrid);
			eastPanel.add(summaryBox);
	}



	private void createTopTopLeftPanel() //--> Oppretter Panel øverst til venstre med personinfo om artisten og JListe over bandmedlemmer.
	{
		topLeftCornerPane = new JPanel(new BorderLayout());

		// --- West
			Box leftTopWestPanel = new Box(BoxLayout.LINE_AXIS);


			//--Plasser labels
				uppermostWestFlowLeft = new JPanel(new GridLayout(4, 1));
				uppermostWestFlowLeft.setBackground(EPlannerMainWindow.BASE_COLOR);
					JPanel dayLabelFlow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
					dayLabelFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
					dayLabelFlow.add(new JLabel("Dag:"));
					JPanel monthLabelFlow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
					monthLabelFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
					monthLabelFlow.add(new JLabel("Mnd:"));
					JPanel yearLabelFlow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
					yearLabelFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
					yearLabelFlow.add(new JLabel("År:"));
					JPanel timeLabelFlow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
					timeLabelFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
					timeLabelFlow.add(new JLabel("Kl.:"));

				//Adding Left
					uppermostWestFlowLeft.add(timeLabelFlow);
					uppermostWestFlowLeft.add(dayLabelFlow);
					uppermostWestFlowLeft.add(monthLabelFlow);
					uppermostWestFlowLeft.add(yearLabelFlow);

			//--Plasser tekstbokser
				uppermostWestFlowRight = new JPanel(new GridLayout(4, 1));
				uppermostWestFlowRight.setBackground(EPlannerMainWindow.BASE_COLOR);
					JPanel dayFlow = new JPanel(new FlowLayout(FlowLayout.LEFT));
					dayFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
					dayFlow.add(daySpinner);
					JPanel monthFlow = new JPanel(new FlowLayout(FlowLayout.LEFT));
					monthFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
					monthFlow.add(monthSpinner);
					JPanel yearFlow = new JPanel(new FlowLayout(FlowLayout.LEFT));
					yearFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
					yearFlow.add(yearSpinner);
					JPanel timeFlow = new JPanel(new FlowLayout(FlowLayout.LEFT));
					timeFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
					timeFlow.add(hourSpinner);
					timeFlow.add(minSpinner);

				//Adding Right
					uppermostWestFlowRight.add(timeFlow);
					uppermostWestFlowRight.add(dayFlow);
					uppermostWestFlowRight.add(monthFlow);
					uppermostWestFlowRight.add(yearFlow);


				leftTopWestPanel.add(uppermostWestFlowLeft);
				leftTopWestPanel.add(uppermostWestFlowRight);

			leftTopWestPanel.setMinimumSize(new Dimension(190, 150));
			leftTopWestPanel.setMaximumSize(new Dimension(190, 150));
			leftTopWestPanel.setPreferredSize(new Dimension(190, 150));
			leftTopWestPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
			leftTopWestPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Dato"));


		// --- Center
				Box leftTopBox = new Box(BoxLayout.LINE_AXIS);
					leftTopBox.setMinimumSize(new Dimension(310,150 ));
					leftTopBox.setMaximumSize(new Dimension(500,150 ));
					leftTopBox.setPreferredSize(new Dimension(410, 150));
					leftTopBox.setBackground(EPlannerMainWindow.BASE_COLOR);
					leftTopBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Event"));


			//--leftLeftTopGrid
				leftLeftTopGrid = new JPanel(new GridLayout(4, 1));
				leftLeftTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);
					JPanel concertLabelFlow1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
					concertLabelFlow1.setBackground(EPlannerMainWindow.BASE_COLOR);
					concertLabelFlow1.add(new JLabel("Standardpris: "));
					JPanel concertLabelFlow2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
					concertLabelFlow2.setBackground(EPlannerMainWindow.BASE_COLOR);
					concertLabelFlow2.add(new JLabel("Antall Billetter til salgs:"));
					JPanel concertLabelFlow3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
					concertLabelFlow3.setBackground(EPlannerMainWindow.BASE_COLOR);
					concertLabelFlow3.add(new JLabel("Reserverte billetter"));
					JPanel concertLabelFlow4 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
					concertLabelFlow4.setBackground(EPlannerMainWindow.BASE_COLOR);
					concertLabelFlow4.add(new JLabel("Cover-bilde: "));
				//Adding Left
					leftLeftTopGrid.add(concertLabelFlow1);
					leftLeftTopGrid.add(concertLabelFlow2);
					leftLeftTopGrid.add(concertLabelFlow3);
					leftLeftTopGrid.add(concertLabelFlow4);

			//--leftRightTopGrid
				leftRightTopGrid = new JPanel(new GridLayout(4, 1));
				leftRightTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);
					JPanel concertFlow1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
					concertFlow1.setBackground(EPlannerMainWindow.BASE_COLOR);
					concertFlow1.add(defaultPriceField);
					JPanel concertFlow2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
					concertFlow2.setBackground(EPlannerMainWindow.BASE_COLOR);
					concertFlow2.add(ticketField);
					JPanel concertFlow3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
					concertFlow3.setBackground(EPlannerMainWindow.BASE_COLOR);
					concertFlow3.add(reservedField);
					JPanel concertFlow4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
					concertFlow4.setBackground(EPlannerMainWindow.BASE_COLOR);
					concertFlow4.add(coverImageField);
					concertFlow4.add(exploreButton);
				//Adding Right
					leftRightTopGrid.add(concertFlow1);
					leftRightTopGrid.add(concertFlow2);
					leftRightTopGrid.add(concertFlow3);
					leftRightTopGrid.add(concertFlow4);

				leftTopBox.add(leftLeftTopGrid);
				leftTopBox.add(leftRightTopGrid);

		// --- South
			Box middleBox = new Box(BoxLayout.LINE_AXIS);
				middleBox.setPreferredSize(new Dimension(200*3,200 ));
				middleBox.setMaximumSize(new Dimension(250*3,200 ));

			Border lineBM = BorderFactory.createLineBorder(Color.BLACK, 1);
			Border emptyBM = BorderFactory.createEmptyBorder(10,10,10,10);

			descInput.setBorder(BorderFactory.createCompoundBorder(emptyBM, lineBM));
			scrollDescInput = new JScrollPane(descInput);
				scrollDescInput.setBackground(EPlannerMainWindow.BASE_COLOR);
				scrollDescInput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Beskrivelse"));

			middleBox.add(scrollDescInput);

		upperTopBox.add(leftTopWestPanel);
		upperTopBox.add(leftTopBox);
		lowerTopBox.add(middleBox);

	}

	private void createBottomLeftPanel() //-->  Bottom Left (Nedre venstre panel med JList med tilgjengelige sjangere)
	{
		Box leftBottomBox = new Box(BoxLayout.LINE_AXIS);
			leftBottomBox.setPreferredSize(new Dimension(200,350 ));
			leftBottomBox.setMaximumSize(new Dimension(250,1000 ));
			leftBottomBox.setBackground(EPlannerMainWindow.BASE_COLOR);
			leftBottomBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Lokaler"));

		venueJList.setFixedCellHeight(20);
		venueJList.setFixedCellWidth(160);
		venueJList.addListSelectionListener(listListener);


		JScrollPane scrollVenueList = new JScrollPane(venueJList);
			scrollVenueList.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
			scrollVenueList.setFont(new Font("Arial",Font.PLAIN, 16 ));

		leftBottomBox.add(scrollVenueList);

		bottomBox.add(leftBottomBox);
	}

	private void createBottomMiddlePanel() //-->  Bottom Middle (Nedre midtre panel med tekstfelt for å skrive inn bandbeskrivelse)
	{
		Box middleBottomBox = new Box(BoxLayout.LINE_AXIS);
			middleBottomBox.setPreferredSize(new Dimension(200,350 ));
			middleBottomBox.setMaximumSize(new Dimension(250,1000 ));
			middleBottomBox.setBackground(EPlannerMainWindow.BASE_COLOR);
			middleBottomBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Hovedartist"));

		mainArtistJList.setFixedCellHeight(20);
		mainArtistJList.setFixedCellWidth(160);
		mainArtistJList.addListSelectionListener(listListener);


		JScrollPane scrollMainArtistList = new JScrollPane(mainArtistJList);
			scrollMainArtistList.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
			scrollMainArtistList.setFont(new Font("Arial",Font.PLAIN, 16 ));

		middleBottomBox.add(scrollMainArtistList);

		bottomBox.add(middleBottomBox);
	}


	private void createBottomRightPanel() //-->  BottomRight(JList med oppvarmingsartist)
	{
		Box rightBottomBox = new Box(BoxLayout.LINE_AXIS);
			rightBottomBox.setPreferredSize(new Dimension(200,350 ));
			rightBottomBox.setMaximumSize(new Dimension(250,1000 ));
			rightBottomBox.setBackground(EPlannerMainWindow.BASE_COLOR);
			rightBottomBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Sekundærartister"));

		secondaryArtistJList.setFixedCellHeight(20);
		secondaryArtistJList.setFixedCellWidth(160);
		secondaryArtistJList.addListSelectionListener(listListener);


		JScrollPane scrollSArtistList = new JScrollPane(secondaryArtistJList);
			scrollSArtistList.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
			scrollSArtistList.setFont(new Font("Arial",Font.PLAIN, 16 ));

		rightBottomBox.add(scrollSArtistList);

		bottomBox.add(rightBottomBox);
	}


	// ----------------------------------------------------------------------------- Funksjonsmetoder:

	protected void updateVenueJList()// --> Oppdaterer JListen over Lokaler
	{
		venueJList.setContent(venueList.getArray());
	}

	protected void updateArtistJList()// --> Oppdaterer JListen over Artister.
	{
		Venue current = venueJList.getSelectedValue();
		if(current == null)
		{
			mainArtistJList.clear();
			secondaryArtistJList.clear();
			return;
		}

		Set<Artist> genreArtists = artistList.getArtistOnGenres(current.getGenres());

		mainArtistJList.setContent(genreArtists);
		secondaryArtistJList.setContent(genreArtists);
	}

	protected void updateDateFields()// --> Oppdaterer datofeltene i oppsummeringen slik at de viser det spinnerne viser.
	{
		summaryDateField.setText(Utilities.getStringDate(eventTime));
		summaryTimeField.setText(Utilities.getStringTime(eventTime));
		checkVenueOccupied();
	}

	protected void checkVenueOccupied()// --> Sjekker om lokalet er opptatt på den datoen brukeren har valgt. Setter teksten i JTextFieldet rød i så fall.
	{
		Venue current = venueJList.getSelectedValue();
		if(current != null)
		{
			if(current.getOccupied(eventTime))
			{
				summaryVenueField.setText("Ikke ledig");
				summaryVenueField.setForeground(Color.RED);
				addButton.setEnabled(false);
			}
			else
			{
				summaryVenueField.setText(current.getName());
				ticketField.setText(current.getMaxCapacity() + "");
				summaryVenueField.setForeground(Color.BLACK);
				addButton.setEnabled(true);
			}
		}
		else
			summaryVenueField.setText("");

		updateArtistJList();
	}

	protected void createConcert()// --> Oppretter selve eventen slik at den kan puttes i eventListen
	{
		String cName = concertNameField.getText();

		if(cName.trim().equals(""))
		{
			Utilities.showError(parent, "Vennligst fyll inn navn");
			return;
		}

		BigDecimal stdTmp = null;
		int maxTmp = 0;
		int resTmp = 0;
		String descTmp = descInput.getText();

		try
		{
			resTmp = Integer.parseInt(reservedField.getText());
			maxTmp = Integer.parseInt(ticketField.getText());
			stdTmp = new BigDecimal(defaultPriceField.getText());
		}
		catch(NumberFormatException nfe)
		{
			Utilities.showError(parent, "Skriv kun tall i tall-feltene!");
			return;
		}

		Artist mainArtist = mainArtistJList.getSelectedValue();
		Venue venue = venueJList.getSelectedValue();

		if(mainArtist == null || venue == null)
		{
			Utilities.showError(parent, "Vennligst velg lokale og hovedartist." );
			return;
		}

		if(maxTmp > venueJList.getSelectedValue().getMaxCapacity() || resTmp > maxTmp)
		{
			Utilities.showError(parent, "Totalt antall billetter kan ikke overstige lokalets kapasitet.\nReserverte billetter kan ikke overstige maks antall billetter til salgs." );
			return;
		}

		java.util.List<Artist> secondaryArtists = secondaryArtistJList.getSelectedValuesList();



		Concert newConcert = new Concert(eventTime,cName, parent.user, mainArtist, secondaryArtists, venue,
							stdTmp, maxTmp, resTmp, descTmp);

		if(!concertList.addConcert(newConcert))
		{
			Utilities.showError(parent, "Event med samme navn finnes allerede.");
			return;
		}

		IOActions.createMediaPath(cName, Database.CONCERT);
		String coverTmp = coverImageField.getText();
		if(!coverTmp.equals(""))
		{
			File coverArt = new File(coverTmp);
			if(!coverArt.exists())
				Utilities.showError(parent, "Fant ikke bildefil, setter standardbilde.");
			else
				IOActions.copyImage(coverArt, cName, Database.CONCERT);
		}
		parent.updateConcertJList();
		Utilities.showInfo(parent, "Event opprettet!");
		emptyFieldsButton.doClick();
	}

// ----------------------------------------------------------------------------- Lytteklasser

	private class FocusLostListener implements FocusListener// --> Oppdaterer oppsummeringsfeltene når fokus er mistet på originalfeltene.
	{
		public void focusGained(FocusEvent e)
		{
			return;
		}

		public void focusLost(FocusEvent e)
		{
			if(e.getSource() == ticketField)
				summaryTicketField.setText(ticketField.getText());

			else if(e.getSource() == defaultPriceField)
				summaryPriceField.setText(defaultPriceField.getText());

			else if(e.getSource() == reservedField)
				summaryResField.setText(reservedField.getText());

			else if(e.getSource() == concertNameField)
				summaryNameField.setText(concertNameField.getText());
		}
	}



	private class ListListener implements ListSelectionListener// --> Sørger for å oppdatere oppsummeringfeltene når noe er valgt i JListen til lokaler eller artister
	{
		public void valueChanged(ListSelectionEvent e)
		{
			if(e.getSource() == secondaryArtistJList)
			{
				Artist art = secondaryArtistJList.getSelectedValue();
				if(art != null)
				{
					String more = "";
					if(secondaryArtistJList.getSelectedIndices().length > 1)
						more = "++";
					summarySArtistField.setText(art.getName() + more);
				}
				else
					summarySArtistField.setText("");
			}
			else if(e.getSource() == venueJList)
				checkVenueOccupied();
			else if(e.getSource() == mainArtistJList)
			{
				Artist art = mainArtistJList.getSelectedValue();
				if(art != null)
					summaryMArtistField.setText(art.getName());
				else
					summaryMArtistField.setText("");
			}
		}
	}//End of Listener class

	private class NewConcertListener implements ActionListener// --> ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == addButton)
			{
				createConcert();
			}
			else if(e.getSource() == exploreButton)
			{
				Utilities.setPath(parent, coverImageField, JFileChooser.FILES_ONLY);
			}
			else if(e.getSource() == emptyFieldsButton)
			{
				concertNameField.setText("");
				coverImageField.setText("");
				ticketField.setText("0");
				reservedField.setText("0");
				descInput.setText("");
				defaultPriceField.setText("0");
				summaryTicketField.setText("");
				summaryPriceField.setText("");
				summaryResField.setText("");
				summaryNameField.setText("");
			}

		}
	}//End of Listener class

	protected void setListenerActive(boolean active)// --> Se kommentar over SpinnerListener.
	{
		spinnerListener.setActive(active);
	}

	/*
	 * Oppdaterer spinnerne slik at de hele tiden står i forhold til et GregorianCalendar objekt.
	 * Dette gjør at spinnerne ruller riktig slik at måneden oppdateres når du går over den siste dagen i forrige måned osv.
	 * Fordi spinnerne skyter en ChangeEvent når de blir satt til en ny verdi bruker vi en boolean variabel for å hoppe over å sette spinneren
	 * når det er vi som setter dem. Hvis ikke vil de aldri bli oppdatert.
	 */

	private class SpinnerListener implements ChangeListener
	{
		private boolean active = true;

		public void stateChanged(ChangeEvent e)
		{
			if(active)
			{
				active = false;

				eventTime.set(yearSpinner.getInt(), (monthSpinner.getInt()-1), daySpinner.getInt(), hourSpinner.getInt(), minSpinner.getInt());

				yearSpinner.setValue(eventTime.get(Calendar.YEAR));
				monthSpinner.setValue(eventTime.get(Calendar.MONTH)+1);
				daySpinner.setValue(eventTime.get(Calendar.DAY_OF_MONTH));
				hourSpinner.setValue(eventTime.get(Calendar.HOUR_OF_DAY));
				minSpinner.setValue(eventTime.get(Calendar.MINUTE));

				active = true;

				updateDateFields();
			}
		}

		public void setActive(boolean active)// --> Overrides i ConcertEditPanel
		{
			this.active = active;
		}
	}
} // End of class Myclass

