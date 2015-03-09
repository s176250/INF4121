/*
 *	Innhold:  Metoder for å tegne søkepanel og for å søke gjennom databasen:
 *
 *
 *					drawNorth()					--> Tegner det nordre panelet med Søkeikon, søkefelt og søkeknapp.
 *					drawWest()					--> Tegner det vestre panelet med søkealternativer.
 *					drawCenter()				--> Tegner senterpanel med JTable for søkeresultater.
 *					updateTableColumns()		--> Oppdaterer hvilke kolonner som skal vises i søkeresultatpanelet.
 *					setDefaultColumns()			--> Setter kolonnene som passer for alle synlig.
 *					setArtistColumns()			--> Setter kolonnene som relaterer til artister synlig.
 *					setVenueColumns()			--> Setter kolonnene som relaterer til lokaler synlig.
 *					setConcertColumns()			--> Setter kolonnene som relaterer til konserter synlig.
 *					search(int index)			--> Søker gjennom artister, lokaler og/eller konserter.
 *					searchAll()					--> Søker gjennom alt med de alternativer brukeren har angitt.
 *					searchArtistName()			--> Søker gjennom Artister med de alternativer brukeren har angitt
 *					searchVenueName()			--> Søker gjennom Lokaler med de alternativer brukeren har angitt
 *					searchConcertName()			--> Søker gjennom Konserter med de alternativer brukeren har angitt
 *					openItem()					--> Åpner alle valgte i nye vinduer.
 *
 *					Indre klasser
 *					ListListener				--> Lytter på valg i typelisten.
 *					SearchListener				--> Lytter på knapper lagt til i søkerpanelet
 *					PopupMouseListener			--> Lytter på høyreklikk på JTabelen
 *					PopUpMenu					--> Lage en popupmeny
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 12.05.2012.
 */

package eplanner.windows;

import eplanner.*;
import eplanner.utilities.Utilities;
import eplanner.windows.components.ExtendedJList;
import eplanner.windows.components.ExtendedJTable;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;
import java.util.TreeSet;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

//Hensikt: Opprette søkepanelet og vise søkeresultater.
@SuppressWarnings("serial")
public class SearchPanel extends JPanel
{
	//Tekstfelter og Checkboxer
	private JTextField searchField, nameFromField, nameToField, dateFromField, dateToField;
	private JCheckBox activeCheckBox, nameCheckBox, dateCheckBox, notActiveCheckBox;

	//Labels og Knapper
	private JLabel nameFromLabel, nameToLabel, dateFromLabel, dateToLabel, searchIcon;
	private JButton searchButton, showButton;

	//Paneler
	private JPanel searchBarPanel;
	private Box optionPanel, optionBox;
	private JScrollPane resultPanel;

	//Lister og comboboxer
	private ExtendedJList<String> searchTypes;
	private JComboBox<String> genreComboBox;

	//JTable
	private ExtendedJTable searchResults;
	private TableColumnModel tableModel;
	private TableColumn[] tableColumns;

	//Lyttere
	private ListListener listListener;
	private SearchListener searchListener;
	private PopupMouseListener popupListener;

	//Annet
	private PopUpMenu popMenu;
	private EPlannerMainWindow parent;
	private ArtistList artistList;
	private VenueList venueList;
	private ConcertList concertList;
	private GenreList genreList;
	private JMenuItem showItem;

	public static final String ARTIST = "Artist";
	public static final String VENUE = "Lokale";
	public static final String CONCERT = "Event";
	public static final String ALL_GENRES = "Alle Sjangere";
	public static final String[] TYPES = {ARTIST, VENUE, CONCERT};
	public static final String[] COLUMNS = {"Type", "Navn", "Dato", "Webside", "Aktiv", "Aktve konserter", "Kapasitet", "Ledige", "Lokale", "Hovedartist"};

	//Konstruktør
	public SearchPanel(EPlannerMainWindow parent, ArtistList artistList, VenueList venueList, ConcertList concertList, GenreList genreList)
	{
		super(new BorderLayout());


		//Lagre innkomne parametere
			this.artistList = artistList;
			this.venueList = venueList;
			this.concertList = concertList;
			this.parent = parent;
			this.genreList = genreList;


		//Sette preferanser
			setBackground(EPlannerMainWindow.BASE_COLOR);


		//Opprette lyttere og Popupmeny
			listListener = new ListListener();
			searchListener = new SearchListener();
			popupListener = new PopupMouseListener();
			popMenu = new PopUpMenu();
			popMenu.addPopupMenuListener(popupListener);
			showItem.addActionListener(searchListener);


		// JLabels
			searchIcon = new JLabel(Utilities.getIcon("/Images/Icons/searchIcon.png"));
			nameFromLabel = new JLabel("Fra: ");
			nameToLabel = new JLabel("Til: ");
			dateFromLabel = new JLabel("Fra: ");
			dateToLabel = new JLabel("Til:  ");

		// JButtons
			searchButton = new JButton("Søk");
			searchButton.addActionListener(searchListener);

		// JTextFields
			searchField = new JTextField();
			searchField.setFont(new Font("Arial",Font.PLAIN, 14));
			searchField.addActionListener(searchListener);
			nameFromField = new JTextField(4);
			nameToField = new JTextField(4);
			dateFromField = new JTextField(10);
			dateToField = new JTextField(10);

		// JLists
			searchTypes = new ExtendedJList<>(TYPES);
			searchTypes.setFixedCellHeight(30);
			searchTypes.setFixedCellWidth(190);
			searchTypes.setFont(new Font("Arial",Font.BOLD, 15));
			searchTypes.addListSelectionListener(listListener);

		// JTable
			/*  Kolonner:
			0 	 Type
			1 	 Navn
			2 	 Dato
			3    Website
			4	 Aktiv
			5	 Aktive konserter
			6	 Kapasitet
			7 	 Solgte
			8 	 Lokale
			9 	 Hovedartist
			*/
			searchResults = new ExtendedJTable(COLUMNS);
			searchResults.setComponentPopupMenu(popMenu);
			searchResults.setFont(new Font("Arial",Font.PLAIN, 13));
			searchResults.setAutoCreateRowSorter(true);
			tableColumns = searchResults.getTableColumns();
			tableModel = searchResults.getColumnModel();
			tableColumns[0].setMaxWidth(60);  // Type
			tableColumns[2].setMaxWidth(190); // Dato
			tableColumns[4].setMaxWidth(60);  // Aktiv
			tableColumns[5].setMaxWidth(120); // Aktive konserter
			tableColumns[6].setMaxWidth(60);  // Kapasitet
			tableColumns[7].setMaxWidth(60);  // Solgte
			searchResults.addSet(artistList.getArtists());
			searchResults.addSet(venueList.getVenues());
			searchResults.addSet(concertList.getConcerts());


		// JCheckBox
			activeCheckBox = new JCheckBox("Aktive");
			activeCheckBox.setSelected(false);
			activeCheckBox.setBackground(EPlannerMainWindow.BASE_COLOR);
			notActiveCheckBox = new JCheckBox("Ikke-aktive");
			notActiveCheckBox.setSelected(false);
			notActiveCheckBox.setBackground(EPlannerMainWindow.BASE_COLOR);
			nameCheckBox = new JCheckBox("Restrikter navn");
			nameCheckBox.setSelected(false);
			nameCheckBox.setBackground(EPlannerMainWindow.BASE_COLOR);
			dateCheckBox = new JCheckBox("Restrikter dato");
			dateCheckBox.setSelected(false);
			dateCheckBox.setBackground(EPlannerMainWindow.BASE_COLOR);

		// JComboBox
			genreComboBox = new JComboBox<>(genreList.getGenreNames());
			genreComboBox.insertItemAt(ALL_GENRES, 0);
			genreComboBox.setSelectedIndex(0);


		// -------------------------------------------------------- North pane

			searchBarPanel = new JPanel(new BorderLayout());
			searchBarPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
			drawNorth();

			add(searchBarPanel, BorderLayout.PAGE_START);


		// -------------------------------------------------------- West pane

			optionPanel = new Box(BoxLayout.PAGE_AXIS);
			optionPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
			drawWest();

			add(optionPanel, BorderLayout.LINE_START);


		// -------------------------------------------------------- center pane

			resultPanel = new JScrollPane(searchResults);
			resultPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
			drawCenter();

			add(resultPanel, BorderLayout.CENTER);
	}

// ------------------------------------------------------------------------------------------------------------------------------GUI Metoder:


	private void drawNorth()	//--> Tegner det nordre panelet med Søkeikon, søkefelt og søkeknapp.
	{
		// -------------------------------------------------------------------------- Top Left
			JPanel searchIconFlow = new JPanel();
			searchIconFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
			searchIconFlow.add(searchIcon);

			searchBarPanel.add(searchIconFlow, BorderLayout.LINE_START);

		// -------------------------------------------------------------------------- Top Middle
			searchBarPanel.add(searchField, BorderLayout.CENTER);

		// -------------------------------------------------------------------------- Top Right
			JPanel searchButtonFlow = new JPanel();
			searchButtonFlow.setBackground(EPlannerMainWindow.BASE_COLOR);
			searchButtonFlow.add(searchButton);

			searchBarPanel.add(searchButtonFlow, BorderLayout.LINE_END);
			searchBarPanel.setBorder(BorderFactory.createEmptyBorder(15,5,10,5));
	}

	private void drawWest()	//--> Tegner det vestre panelet med søkealternativer.
	{
		// -------------------------------------------------------------------------------------------- Left top (JList med typer)
			searchTypes.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Type"));
			optionPanel.add(searchTypes);
			optionPanel.add(Box.createRigidArea(new Dimension(190, 20)));


		// --------------------------------------------------------------------------------------------- Left bottom
			optionBox = new Box(BoxLayout.PAGE_AXIS);
				optionBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Alternativer"));
				optionBox.setPreferredSize(new Dimension(190, 400));
				optionBox.setPreferredSize(new Dimension(190, 400));


			// Kun Aktive
			JPanel option1 = new JPanel();
					option1.setBackground(EPlannerMainWindow.BASE_COLOR);
					option1.add(activeCheckBox);
					option1.add(notActiveCheckBox);

			// Restrikter Navn
			Box option2 = new Box(BoxLayout.PAGE_AXIS);
				option2.setBackground(EPlannerMainWindow.BASE_COLOR);

				JPanel option2Flow0 = new JPanel(new FlowLayout(FlowLayout.LEFT));
					option2Flow0.setBackground(EPlannerMainWindow.BASE_COLOR);
					option2Flow0.add(nameCheckBox);

				JPanel option2Flow = new JPanel(new FlowLayout(FlowLayout.LEFT));
					option2Flow.setBackground(EPlannerMainWindow.BASE_COLOR);
					option2Flow.add(nameFromLabel);
					option2Flow.add(nameFromField);
					option2Flow.add(nameToLabel);
					option2Flow.add(nameToField);

			option2.add(option2Flow0);
			option2.add(option2Flow);
			option2.add(Box.createVerticalGlue());


			// Restrikter Dato
			Box option3 = new Box(BoxLayout.PAGE_AXIS);
				option3.setBackground(EPlannerMainWindow.BASE_COLOR);

				JPanel option3Flow0 = new JPanel(new FlowLayout(FlowLayout.LEFT));
					option3Flow0.setBackground(EPlannerMainWindow.BASE_COLOR);
					option3Flow0.add(dateCheckBox);

				JPanel option3Flow1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
					option3Flow1.setBackground(EPlannerMainWindow.BASE_COLOR);
					option3Flow1.add(dateFromLabel);
					option3Flow1.add(dateFromField);

				JPanel option3Flow2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
					option3Flow2.setBackground(EPlannerMainWindow.BASE_COLOR);
					option3Flow2.add(dateToLabel);
					option3Flow2.add(dateToField);

			option3.add(option3Flow0);
			option3.add(option3Flow1);
			option3.add(option3Flow2);


			// Restrikter Sjanger
			JPanel option4 = new JPanel();
					option4.setBackground(EPlannerMainWindow.BASE_COLOR);
					option4.add(genreComboBox);

			JSeparator seperator1 = new JSeparator(SwingConstants.HORIZONTAL);
			seperator1.setPreferredSize(new Dimension(190,20));
			JSeparator seperator2 = new JSeparator(SwingConstants.HORIZONTAL);
			seperator2.setPreferredSize(new Dimension(190,20));
			JSeparator seperator3 = new JSeparator(SwingConstants.HORIZONTAL);
			seperator3.setPreferredSize(new Dimension(190,20));


			// Sleng det sammen
			optionBox.add(option1);
			optionBox.add(seperator1);
			optionBox.add(option2);
			optionBox.add(seperator2);
			optionBox.add(option3);
			optionBox.add(seperator3);
			optionBox.add(option4);
			optionBox.add(Box.createVerticalGlue());


		optionPanel.add(optionBox);
		optionPanel.setPreferredSize(new Dimension(195, 400));
		optionPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
	}

	private void drawCenter()	//--> Tegner senterpanel med JTable for søkeresultater.
	{
		Border etchedB = BorderFactory.createEtchedBorder();
		Border emptyB = BorderFactory.createEmptyBorder(10,0,15,15);
		setDefaultColumns();

		resultPanel.setBorder(BorderFactory.createCompoundBorder(emptyB, etchedB));
	}

// ------------------------------------------------------------------------------------------------------------------------------Funksjons Metoder:


	private void updateTableColumns(int index)	//--> Oppdaterer hvilke kolonner som skal vises i søkeresultatpanelet.
	{
		switch(index)
		{
			case 0:
					setArtistColumns();
					break;
			case 1:
					setVenueColumns();
					break;
			case 2:
					setConcertColumns();
					break;
			default:
					setDefaultColumns();
					break;
		}
	}

	private void setDefaultColumns()	//--> Setter kolonnene som passer for alle synlig.
	{
		searchResults.clearColumns(tableColumns);

		for(int i = 0; i < tableColumns.length - 3; i++) // -3 fordi vi fjerner de tre siste kolonnene
		{
			searchResults.addColumn(tableColumns[i]);
		}

		searchResults.setSet(concertList.getConcerts());
		searchResults.addSet(artistList.getArtists());
		searchResults.addSet(venueList.getVenues());

	}

	private void setArtistColumns()	//--> Setter kolonnene som relaterer til artister synlig.
	{
		searchResults.clearColumns(tableColumns);

		searchResults.addColumn(tableColumns[0]);
		searchResults.addColumn(tableColumns[1]);
		searchResults.addColumn(tableColumns[3]);
		searchResults.addColumn(tableColumns[4]);
		searchResults.addColumn(tableColumns[5]);

		searchResults.setSet(artistList.getArtists());
	}

	private void setVenueColumns()	//--> Setter kolonnene som relaterer til lokaler synlig.
	{
		searchResults.clearColumns(tableColumns);

		searchResults.addColumn(tableColumns[0]);
		searchResults.addColumn(tableColumns[1]);
		searchResults.addColumn(tableColumns[3]);
		searchResults.addColumn(tableColumns[4]);
		searchResults.addColumn(tableColumns[5]);
		searchResults.addColumn(tableColumns[6]);

		searchResults.setSet(venueList.getVenues());
	}

	private void setConcertColumns()	//--> Setter kolonnene som relaterer til konserter synlig.
	{
		searchResults.clearColumns(tableColumns);

		searchResults.addColumn(tableColumns[0]);
		searchResults.addColumn(tableColumns[1]);
		searchResults.addColumn(tableColumns[2]);
		searchResults.addColumn(tableColumns[4]);
		searchResults.addColumn(tableColumns[6]);
		searchResults.addColumn(tableColumns[7]);
		searchResults.addColumn(tableColumns[8]);
		searchResults.addColumn(tableColumns[9]);

		searchResults.setSet(concertList.getConcerts());
	}

	private void search(int index)	//--> Søker gjennom artister, lokaler og/eller konserter.
	{
		switch(index)
		{
			case 0:
					searchArtistName(false);
					break;
			case 1:
					searchVenueName(false);
					break;
			case 2:
					searchConcertName(false);
					break;
			default:
					searchAll();
					break;
		}
	}

	private void searchAll()	//--> Søker gjennom alt med de alternativer brukeren har angitt.
	{
		searchArtistName(false);
		searchVenueName(true);
		searchConcertName(true);
	}

	private void searchArtistName(boolean inclusive)	//--> Søker gjennom Artister med de alternativer brukeren har angitt
	{
		TreeSet<Artist> result = artistList.getArtists();

		if(activeCheckBox.isSelected() && !notActiveCheckBox.isSelected())
			result = artistList.getArtistsActive(true);
		else if(!activeCheckBox.isSelected() && notActiveCheckBox.isSelected())
			result = artistList.getArtistsActive(false);

		if(nameCheckBox.isSelected())
			result = artistList.getSubsetToFrom(nameFromField.getText(), nameToField.getText(), parent, result);

		String selectedGenre = (String)genreComboBox.getSelectedItem();
		if(selectedGenre != ALL_GENRES)
			result = artistList.getArtistOnGenre(selectedGenre, result);

		if(!searchField.getText().equals(""))
			result = artistList.findArtistsOnPartialNameSet(searchField.getText(), result);

		if(inclusive)
			searchResults.addSet(result);
		else
			searchResults.setSet(result);
	}

	private void searchVenueName(boolean inclusive)//--> Søker gjennom Lokaler med de alternativer brukeren har angitt
	{
		TreeSet<Venue> result = venueList.getVenues();

		if(activeCheckBox.isSelected() && !notActiveCheckBox.isSelected())
			result = venueList.getVenuesActive(true);
		else if(!activeCheckBox.isSelected() && notActiveCheckBox.isSelected())
			result = venueList.getVenuesActive(false);

		if(nameCheckBox.isSelected())
			result = venueList.getSubsetToFrom(nameFromField.getText(), nameToField.getText(), parent, result);

		String selectedGenre = (String)genreComboBox.getSelectedItem();
		if(selectedGenre != ALL_GENRES)
			result = venueList.getVenuesOnGenre(selectedGenre, result);

		if(!searchField.getText().equals(""))
			result = venueList.findVenuesOnPartialNameSet(searchField.getText(), result);

		if(inclusive)
			searchResults.addSet(result);
		else
			searchResults.setSet(result);
	}

	private void searchConcertName(boolean inclusive)//--> Søker gjennom Konserter med de alternativer brukeren har angitt
	{
		TreeSet<Concert> result = concertList.getConcerts();

		if(activeCheckBox.isSelected() && !notActiveCheckBox.isSelected())
			result = concertList.getActiveConcerts(true);
		else if(!activeCheckBox.isSelected() && notActiveCheckBox.isSelected())
			result = concertList.getActiveConcerts(false);

		if(dateCheckBox.isSelected())
		{
			GregorianCalendar from = Utilities.stringToDate(dateFromField.getText());
			GregorianCalendar to = Utilities.stringToDate(dateToField.getText());

			if(from == null && to == null)
			{
				Utilities.showError(parent, "Ugyldig datorestriksjon.");
				return;
			}
			result = concertList.getSubsetToFrom(from, to, parent, result);
		}

		if(nameCheckBox.isSelected())
			result = concertList.getSubsetToFrom(nameFromField.getText(), nameToField.getText(), parent, result);

		String selectedGenre = (String)genreComboBox.getSelectedItem();
		if(selectedGenre != ALL_GENRES)
			result = concertList.getConcertsOnGenre(selectedGenre, result);

		if(!searchField.getText().equals(""))
			result = concertList.findConcertsOnPartialNameSet(searchField.getText(), result);

		if(inclusive)
			searchResults.addSet(result);
		else
			searchResults.setSet(result);
	}

	private void openItem()		//--> Åpner alle valgte i nye vinduer.
	{
		int[] openSelected = searchResults.getSelectedRows();

		for(int i : openSelected)
		{
			String type = (String) searchResults.getValueAt(i,0);
			String name = (String) searchResults.getValueAt(i,1);

			if(type.equals(ARTIST))
			{
				Artist select = artistList.findArtist(name);
				parent.newArtistView(select);

			}
			else if(type.equals(VENUE))
			{
				Venue select = venueList.findVenue(name);
				parent.newVenueView(select);
			}
			else
			{
				Concert select = concertList.findConcertOnName(name);
				parent.newConcertView(select);
			}
		}
	}


// ------------------------------------------------------------------------------------------------------------------------------INDRE KLASSER

	//Hensikt: Lytter på valg i typelisten.
	private class ListListener implements ListSelectionListener
	{
		@Override
		public void valueChanged(ListSelectionEvent e)
		{
			if (e.getValueIsAdjusting() == false)
			{
				updateTableColumns(searchTypes.getSelectedIndex());
			}
		}
	}//End of ListListener class


	//Hensikt: Lytter på knapper lagt til i søkerpanelet
	private class SearchListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == searchButton || e.getSource() == searchField)
			{
				search(searchTypes.getSelectedIndex());
			}
			else if(e.getSource() == showItem)
			{
				openItem();
			}
		}
	}//End of SearchListener class


	//Hensikt: Lytter på høyreklikk på JTabelen
	private class PopupMouseListener implements PopupMenuListener
	{
		@Override
		public void	popupMenuCanceled(PopupMenuEvent e)
		{
		}

		@Override
		public void	popupMenuWillBecomeInvisible(PopupMenuEvent e)
		{
		}

		@Override
		public void	popupMenuWillBecomeVisible(PopupMenuEvent e)
		{
			if(searchResults.getSelectedRow() < 0)
				showItem.setEnabled(false);
			else
				showItem.setEnabled(true);
		}
	}//End of PopupMouseListener class


	//Hensikt: Lage en popupmeny
	private class PopUpMenu extends JPopupMenu
	{
		public PopUpMenu()
		{
			showItem = new JMenuItem("Vis");
			add(showItem);
		}
	}

}//End of SearchPanel class
