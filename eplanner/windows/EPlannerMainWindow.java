/*
 *	Innhold:  Metoder som oppretter og tegner elementene i hovedvinduet, samt kaller opp klasser som tegner indre vinduer.
 *
 *				Konstruktør							--> Setter datastruktur og kaller metoder som tegner hovedvindu, og setter dets egenskaper.
 *				createFileMenu()					--> Oppretter og legger til elementer til menylinjen.
 *				drawNorthPanel()					--> Lager og legger til nordre panel med værktøylinje
 *				drawWestPanel()						--> Lager og legger til vestre panel med menylisten
 *				createMainMenu()					--> Oppretter og legger til elementene som skal være i hovedmenyen, basert på tilgangsnivå til bruker.
 *				drawSouthPanel() 					--> Lager og legger til søndre panel med statuslinje
 *				createCards()						--> Oppretter alle indre vinduer og legger de til i et cardspanel i center panelet i hovedvinduet
 *				drawArtistTabbedPane()				--> Oppretter artistpanelet.
 *				drawVenueTabbedPane()				--> Oppretter lokalepanelet
 *				drawConcertTabbedPane()				--> Oppretter konsertpanelet
 *				drawSalesTabbedPane()				--> Oppretter Salgspanel
 *				drawSearchTabbedPane()				--> Oppretter Søkepanel
 *				drawAccountTabbedPane()				--> Oppretter Brukerpanel
 *				drawAdminTabbedPane()				--> Oppretter Adminpanel
 *				repaintArtistView()					--> Retegner Artistoversikt-panelet
 *				repaintVenueView()					--> Retegner Lokaleoversikt-panelet
 *				repaintConcertView()				--> Retegner Konsertoversikt-panelet
 *				updateArtistJList()					--> Oppdaterer artistoversikt-listen, i artistpanelet og ny-konsertpanelet.
 *				updateVenueJList()					--> Oppdaterer lokaleoversikt-listen, i lokalepanelet og ny-konsertpanelet.
 *				updateConcertJList()				--> Oppdaterer konsertoversikt-listen, i konsertpanelet og salgspanelet.
 *				addArtistEditTab()					--> Legger til Endre-Artist-Fanen i Artistpanelet
 *				addVenueEditTab()					--> Legger til Endre-Lokale-Fanen i Lokalepanelet
 *				addConcertEditTab()					--> Legger til Endre-Konsert-Fanen i Konsertpanelet
 *				searchTool()						--> Oppretter et nytt søkepanel i en ekstern JFrame
 *				newArtistView()						--> Oppretter et nytt artistoversikt-panel i en ekstern JFrame
 *				newVenueView()						--> Oppretter et nytt lokaleoversikt-panel i en ekstern JFrame
 *				newConcertView()					--> Oppretter et nytt konsetoversikt-panel i en ekstern JFrame
 *				newSaleView()						--> Oppretter et nytt salgs-panel i en ekstern JFrame
 *				newUserView()						--> Oppretter et nytt bruker-panel i en ekstern JFrame
 *				openArtist()						--> Åpner inkommen artist i artistpanelet
 *				openVenue()							--> Åpner inkommen lokale i lokalepanelet
 *				userRestriction()					--> Setter restriksjoner basert på innlogget bruker.
 *				setLook()							--> Endrer utseende på programmet til det motsatte av det det har.
 *				saveToFile()						--> Lagrer hele databasen til fil.
 *				createNew()							--> Setter valgt fane til ny-fane.
 *				editTool()							--> Åpner valgt artist/lokale/konsert for redigering.
 *				deleteTool()						--> Sletter valgt artist/lokale/konsert.
 *				googleTool()						--> Googler(i standard nettleser) valgt artist/lokale/konsert.
 *
 *				Indre klasser:
 *					leftMenuRenderer 				--> Klassen er til for å kunne tegne en JLabel(med ikon og tekst) i en JList.
 *					ResizeListener					--> Lytter på justering av vindusstørrelse, for å reskalere bilder.
 *					ListListener					--> Lytter på valg i hovedmenyen for å vise korrekt indre vindu.
 *					MainWindowListener				--> Lytter på knapper og menylinje som er langt til i hovedvinduet (men ikke de indre vinduene).
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 13.05.2012.
 */

package eplanner.windows;

import eplanner.*;
import eplanner.users.*;
import eplanner.utilities.*;
import eplanner.windows.components.IconButton;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Date;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/*
  Hensikt: Tegne hovedvinduet som fungerer som et rammeverk for alle indre vinduer
		   samt fungere som et mellomledd som tar vare på alle referansene til
		   datastrukturen og de ulike indre vinduene, slik at de kan kalle hverandre
		   indirekte via denne klassen.
*/
@SuppressWarnings("serial")
public class EPlannerMainWindow extends JFrame
{
	//---------------------------------- Hovedlister - datastruktur
		protected Account user;
		private VenueList venueList;
		private GenreList genreList;
		private ConcertList concertList;
		private ArtistList artistList;
		private AccountList accountList;
		private Database database;

		//Foreldrevindu
		private LoginWindow loginWindow;

	//---------------------------------- Filmeny
	//MenuBar
		private JMenuBar fileMenuBar;										//Selve menylinjen på vinduet.
		private JMenu fileMenu, editMenu, salesMenu, viewMenu, helpMenu;	//Menyene på menylinjen

	// JMenuItems
		private JMenuItem file_NewArtist, file_NewVenue, file_NewEvent, file_Save, file_ImportDatabase, file_ImportArtist;					// Items i fil-menyen
		private JMenuItem file_ImportVenue, file_ExportDatabase, file_ExportArtist, file_ExportVenue, file_Settings, file_Logout, file_Exit;	// Items i fil-menyen
		private JMenuItem edit_New, edit_EditItem, edit_Delete, edit_Search, edit_Google, edit_AddPhoto, edit_AddVideo;						// Items i rediger-menyen
		private JMenuItem edit_AddMusic, edit_AddReview, edit_ManageGenres, edit_AddAccount, edit_EditAccount;			// Items i rediger-menyen
		private JMenuItem sales_NewSale, sales_UndoSale, sales_GetSalesStats;																	// Items i salg-menyen
		private JCheckBoxMenuItem view_SwitchView;																							// Checkbox i visnings-menyen
		private JMenuItem view_showArtists, view_showVenues, view_showConcerts, view_showSales, view_showSearch, view_showUsers;				// Items i visnings-menyen
		private JMenuItem help_Help, help_About;																								// Items i hjelp-menyen


	//---------------------------------- Hovedvindu
	//Værktøylinje
		private JToolBar toolbar;																			//Værktøylinjen
		private IconButton toolbarNew,toolbarEdit,toolbarDelete,toolbarSearch,toolbarGoogle,toolbarSave;	//Værktøylinjeknapper
		private IconButton toolbarClose,toolbarSettings, toolbarLogout;									//Værktøylinjeknapper

	// Hovedmeny
		private JList<JLabel> mainMenuList;																					//Menylisten
		private JLabel[] leftTabs;																							//Elementene i menylisten
		private final static String[] mainMenuItems = {"Artister","Lokaler", "Events", "Salg", "Søk", "Brukere", "Admin"};	//Navnene på elementene i menylisten

	//Layoutpaneler
		private JPanel mainNorthPanel, mainBorderPanel, mainSouthPanel, mainCenterPanel;
		private JPanel cardPanel;

	//Indre vinduer
		protected JTabbedPane artistTabs, venueTabs, concertTabs, salesTabs, searchTabs, accountTabs, adminTabs;
		protected ArtistPanel artistPanel;
		protected ArtistNewPanel artistNewPanel;
		private ArtistEditPanel artistEditPanel;
		private BandNewPanel bandNewPanel;
		private BandEditPanel bandEditPanel;
		protected VenuePanel venuePanel;
		protected VenueNewPanel venueNewPanel;
		private VenueEditPanel venueEditPanel;
		protected ConcertNewPanel concertNewPanel;
		private ConcertEditPanel concertEditPanel;
		protected ConcertPanel concertPanel;
		private SalesPanel salesPanel;
		private SearchPanel searchPanel;
		private AccountPanel accountPanel;
		private AdminPanel adminPanel;

	//Lyttere
		private ListListener listListener;
		private ResizeListener compListener;
		private MainWindowListener mainActionListener;
		private MainWindowAdapter mainWindowAdapter;

	//---------------------------------- Statiske variabler
		public static final Color defaultColor = new Color(230, 230, 230);			//Standard gråfarge i hovedvinduet
		public static final Color BASE_COLOR = Color.WHITE;							//Standard hvitfarge i alle undermenyer.
		public static final String EXPLORE_ICON = "/Images/Icons/exploreIcon.png";	//Standard utforsk ikon (brukes i flere vinduer)
		public static final Cursor WAITCURSOR = new Cursor(Cursor.WAIT_CURSOR);		//Standard opptatt musepeker
		public static final Cursor NORMALCURSOR = new Cursor(Cursor.DEFAULT_CURSOR);//Standard normal musepeker
		public static final Cursor HANDCURSOR= new Cursor(Cursor.HAND_CURSOR);		//Standard hånd musepeker
		public static final Font MENUFONT = new Font("Arial",Font.BOLD, 20 );


	// Konstruktør for nytt hovedvuindu
	public EPlannerMainWindow(Account user, Database database, LoginWindow loginWindow)
	{
		super("ePlanner - " + user.getName());

		//Setter utseende til vinduet når det startet opp, basert på hvilket utseende som ble brukt sist.
		if(database.getSystemLook())
		{
			try{
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); //Setter windows utseende.
			}
			catch (Exception e){IOActions.writeErrorLog(e);}
		}
		else
		{
			try{
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); //Setter java utseende.
			}
			catch (Exception e){IOActions.writeErrorLog(e);}
		}


		//Setter variabler for datastrukturen.
			this.user = user;
			this.database = database;
			this.loginWindow = loginWindow;

			venueList = database.getVenueList();
			genreList = database.getGenreList();
			concertList = database.getConcertList();
			artistList = database.getArtistList();
			accountList = database.getAccountList();

		//Oppretter lyttere til hovedvinduet.
			mainActionListener = new MainWindowListener();
			listListener = new ListListener();
			compListener = new ResizeListener();
			mainWindowAdapter = new MainWindowAdapter();
			addWindowListener(mainWindowAdapter);
			addComponentListener(compListener);				//Lytter på justering av vindusstørrelse, for å reskalere bilder.


		//Hovedlayout - (contentpane)
			mainBorderPanel = new JPanel(new BorderLayout());


		// Tooltipmanager
			ToolTipManager.sharedInstance().setDismissDelay(5000);


		//------------------------------------------------------------------------------------ FILMENY

			fileMenuBar = new JMenuBar();
			createFileMenu();	//Oppretter og legger til elementer til menylinjen.

			setJMenuBar(fileMenuBar);

		//------------------------------------------------------------------------------------- PAGE START (North Panel - Værktøylinje)

			drawNorthPanel();	//Lager og legger til nordre panel med værktøylinje


		//------------------------------------------------------------------------------------- LINE START (West Panel - Menyliste)

			drawWestPanel();	//Lager og legger til vestre panel med menylisten


		//------------------------------------------------------------------------------------- PAGE END (South Panel - Statuslinje)

			drawSouthPanel();	//Lager og legger til søndre panel med statuslinje


		//------------------------------------------------------------------------------------- CENTER (Center Pane)

			createCards();		//Oppretter alle indre vinduer

			mainBorderPanel.add(cardPanel, BorderLayout.CENTER);


		//-------------------------------------------------------------------------------------. Standard attributter / Vinduplassering
					//bredde, høyde
			setSize(1000, 700);							//Setter størrelse
			setMinimumSize(new Dimension(1000, 700));

			Utilities.setIcon(this);						//Setter ikon på vinduet
			setLocationRelativeTo(loginWindow);
			setContentPane(mainBorderPanel);
			setVisible(true);
			setResizable(true);
			setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		artistPanel.setDefaultSelectedArtist();		//Setter valgt artist i artistvinduet
		venuePanel.setDefaultSelectedVenue();		//Setter valgt lokale i lokalevinduet
		concertPanel.setDefaultSelectedConcert();	//Setter valgt konsert i konsertvinduet

		userRestriction();							//Setter restriksjoner basert på innlogget bruker.
	}
	//Slutt Konstruktør


//-------------------------------------------------------------------------------------------------------- TEGN HOVEDVINDU Metoder

	private void createFileMenu()	//--> Oppretter og legger til elementer til menylinjen.
	{
		//Create menu(s) for the menu bar
			fileMenu = new JMenu("Fil");
			editMenu = new JMenu("Rediger");
			salesMenu = new JMenu("Salg");
			viewMenu = new JMenu("Visning");
			helpMenu = new JMenu("Hjelp");

		//Add menu(s) to menubar
			fileMenuBar.add(fileMenu);
			fileMenuBar.add(editMenu);
			fileMenuBar.add(salesMenu);
			fileMenuBar.add(viewMenu);
			fileMenuBar.add(helpMenu);

		//Create menu items for "menu".
			// File Meny:
				file_NewArtist = new JMenuItem("Ny Artist...");
				file_NewArtist.addActionListener(mainActionListener);
				file_NewArtist.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_MASK));
				file_NewVenue = new JMenuItem("Nytt Lokale...");
				file_NewVenue.addActionListener(mainActionListener);
				file_NewVenue.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.ALT_MASK));
				file_NewEvent = new JMenuItem("Ny Event...");
				file_NewEvent.addActionListener(mainActionListener);
				file_NewEvent.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.ALT_MASK));
				file_ImportDatabase = new JMenuItem("Importer Database...");
				file_ImportDatabase.addActionListener(mainActionListener);
				file_ImportArtist = new JMenuItem("Importer Artister...");
				file_ImportArtist.addActionListener(mainActionListener);
				file_ImportVenue = new JMenuItem("Importer Lokaler...");
				file_ImportVenue.addActionListener(mainActionListener);
				file_ExportDatabase = new JMenuItem("Eksporter Database...");
				file_ExportDatabase.addActionListener(mainActionListener);
				file_ExportArtist = new JMenuItem("Eksporter Artister...");
				file_ExportArtist.addActionListener(mainActionListener);
				file_ExportVenue = new JMenuItem("Eksporter Lokaler...");
				file_ExportVenue.addActionListener(mainActionListener);
				file_Settings = new JMenuItem("Instillinger...");
				file_Settings.addActionListener(mainActionListener);
				file_Settings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.SHIFT_MASK ));
				file_Save = new JMenuItem("Lagre");
				file_Save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
				file_Save.addActionListener(mainActionListener);
				file_Logout = new JMenuItem("Logg ut");
				file_Logout.addActionListener(mainActionListener);
				file_Logout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
				file_Exit = new JMenuItem("Lukk");
				file_Exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
				file_Exit.addActionListener(mainActionListener);

			//Rediger Meny:
				edit_New = new JMenuItem("Ny...");
				edit_New.addActionListener(mainActionListener);
				edit_New.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
				edit_EditItem = new JMenuItem("Endre...");
				edit_EditItem.addActionListener(mainActionListener);
				edit_EditItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
				edit_Delete = new JMenuItem("Slett");
				edit_Delete.addActionListener(mainActionListener);
				edit_Delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
				edit_Search = new JMenuItem("Søk");
				edit_Search.addActionListener(mainActionListener);
				edit_Search.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
				edit_Google = new JMenuItem("Google");
				edit_Google.addActionListener(mainActionListener);
				edit_Google.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
				edit_AddPhoto = new JMenuItem("Legg til Bilde...");
				edit_AddPhoto.addActionListener(mainActionListener);
				edit_AddVideo = new JMenuItem("Legg til Video...");
				edit_AddVideo.addActionListener(mainActionListener);
				edit_AddMusic = new JMenuItem("Legg til Musikk...");
				edit_AddMusic.addActionListener(mainActionListener);
				edit_AddReview = new JMenuItem("Legg til Anmeldelse...");
				edit_AddReview.addActionListener(mainActionListener);
				edit_ManageGenres = new JMenuItem("Administrer Sjangere...");
				edit_ManageGenres.addActionListener(mainActionListener);
				edit_AddAccount = new JMenuItem("Legg til Ny Bruker...");
				edit_AddAccount.addActionListener(mainActionListener);
				edit_EditAccount = new JMenuItem("Endre Bruker...");
				edit_EditAccount.addActionListener(mainActionListener);


			//Salg Meny:
				sales_NewSale = new JMenuItem("Nytt salg...");
				sales_NewSale.addActionListener(mainActionListener);
				sales_UndoSale = new JMenuItem("Angre salg");
				sales_UndoSale.addActionListener(mainActionListener);
				sales_GetSalesStats = new JMenuItem("Hent Salgsoversikt...");
				sales_GetSalesStats.addActionListener(mainActionListener);

			//Visning Meny:
				view_SwitchView = new JCheckBoxMenuItem("System utseende");
				view_SwitchView.setSelected(database.getSystemLook());
				view_SwitchView.addActionListener(mainActionListener);
				view_showArtists = new JMenuItem("Vis Artister...");
				view_showArtists.addActionListener(mainActionListener);
				view_showArtists.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, 0));
				view_showVenues = new JMenuItem("Vis Lokaler...");
				view_showVenues.addActionListener(mainActionListener);
				view_showVenues.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
				view_showConcerts = new JMenuItem("Vis Eventer...");
				view_showConcerts.addActionListener(mainActionListener);
				view_showConcerts.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
				view_showSales = new JMenuItem("Vis Salg...");
				view_showSales.addActionListener(mainActionListener);
				view_showSales.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
				view_showSearch = new JMenuItem("Vis Søk...");
				view_showSearch.addActionListener(mainActionListener);
				view_showSearch.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F8, 0));
				view_showUsers = new JMenuItem("Vis Brukere...");
				view_showUsers.addActionListener(mainActionListener);
				view_showUsers.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F9, 0));

			//Hjelp Meny:
				help_Help = new JMenuItem("Hjelp");
				help_Help.addActionListener(mainActionListener);
				help_Help.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
				help_Help.setEnabled(false); //Ikke laget.
				help_About = new JMenuItem("Om ePlanner");
				help_About.addActionListener(mainActionListener);
				help_About.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));

		//Legger til items til Fil-meny:
			fileMenu.add(file_NewArtist);
			fileMenu.add(file_NewVenue);
			fileMenu.add(file_NewEvent);
			fileMenu.addSeparator();
			fileMenu.add(file_ImportDatabase);
			fileMenu.add(file_ImportArtist);
			fileMenu.add(file_ImportVenue);
			fileMenu.add(file_ExportDatabase);
			fileMenu.add(file_ExportArtist);
			fileMenu.add(file_ExportVenue);
			fileMenu.addSeparator();
			fileMenu.add(file_Settings);
			fileMenu.add(file_Save);
			fileMenu.add(file_Logout);
			fileMenu.add(file_Exit);

		//Legger til items til Endre-meny:
			editMenu.add(edit_New);
			editMenu.add(edit_EditItem);
			editMenu.add(edit_Delete);
			editMenu.add(edit_Search);
			editMenu.addSeparator();
			editMenu.add(edit_Google);
			editMenu.add(edit_ManageGenres);
			editMenu.addSeparator();
			editMenu.add(edit_AddPhoto);
			editMenu.add(edit_AddVideo);
			editMenu.add(edit_AddMusic);
			editMenu.add(edit_AddReview);
			editMenu.addSeparator();
			editMenu.add(edit_AddAccount);
			editMenu.add(edit_EditAccount);

		//Legger til items til Salgs-meny:
			salesMenu.add(sales_NewSale);
			salesMenu.add(sales_UndoSale);
			salesMenu.addSeparator();
			salesMenu.add(sales_GetSalesStats);

		//Legger til items til Visning-meny:
			viewMenu.add(view_SwitchView);
			viewMenu.add(view_showArtists);
			viewMenu.add(view_showVenues);
			viewMenu.add(view_showConcerts);
			viewMenu.add(view_showSales);
			viewMenu.add(view_showSearch);
			viewMenu.add(view_showUsers);

		//Legger til items til Hjelp-meny:
			helpMenu.add(help_Help);
			helpMenu.add(help_About);

		//Disse fjernes pga tidsmangel.
			file_ImportDatabase.setEnabled(false);
			file_ImportArtist.setEnabled(false);
			file_ImportVenue.setEnabled(false);
			file_ExportDatabase.setEnabled(false);
			file_ExportArtist.setEnabled(false);
			file_ExportVenue.setEnabled(false);
			edit_AddPhoto.setEnabled(false);
			edit_AddVideo.setEnabled(false);
			edit_AddMusic.setEnabled(false);
			edit_ManageGenres.setEnabled(false);
			sales_UndoSale.setEnabled(false);
			sales_GetSalesStats.setEnabled(false);
	}

	private void drawNorthPanel()	//--> Lager og legger til nordre panel med værktøylinje
	{
		//Oppretter Værktøylinjeknapper
		//Venstreside
			toolbarNew = new IconButton(Utilities.getIcon("/Images/Icons/newIcon.png"));
			toolbarNew.addActionListener(mainActionListener);
			toolbarNew.setToolTipText("Opprett ny (Ctrl+N)");
			toolbarEdit = new IconButton(Utilities.getIcon("/Images/Icons/editIcon.png"));
			toolbarEdit.addActionListener(mainActionListener);
			toolbarEdit.setToolTipText("Endre (Ctrl+E)");
			toolbarDelete = new IconButton(Utilities.getIcon("/Images/Icons/deleteIcon.png"));
			toolbarDelete.addActionListener(mainActionListener);
			toolbarDelete.setToolTipText("Slett (Del)");
			toolbarSearch = new IconButton(Utilities.getIcon("/Images/Icons/searchIcon.png"));
			toolbarSearch.addActionListener(mainActionListener);
			toolbarSearch.setToolTipText("Søk (Ctrl+F)");
			toolbarGoogle = new IconButton(Utilities.getIcon("/Images/Icons/googleIcon.png"));
			toolbarGoogle.addActionListener(mainActionListener);
			toolbarGoogle.setToolTipText("Søk på Google (F3)");

		//Høyreside
			toolbarSave = new IconButton(Utilities.getIcon("/Images/Icons/saveIcon.png"));
			toolbarSave.addActionListener(mainActionListener);
			toolbarSave.setToolTipText("Lagre (Ctrl+S)");
			toolbarClose = new IconButton(Utilities.getIcon("/Images/Icons/closeIcon.png"));
			toolbarClose.addActionListener(mainActionListener);
			toolbarClose.setToolTipText("Lukk (Esc)");
			toolbarLogout = new IconButton(Utilities.getIcon("/Images/Icons/loginIcon.png"));
			toolbarLogout.setToolTipText("Logg ut (Ctrl+X)");
			toolbarLogout.addActionListener(mainActionListener);
			toolbarSettings = new IconButton(Utilities.getIcon("/Images/Icons/settingsIcon.png"));
			toolbarSettings.addActionListener(mainActionListener);
			toolbarSettings.setToolTipText("Instillinger (Shift+P)");


		//Oppretter og legger til elementer i Værktøylinjen
			JPanel leftToolbar = new JPanel();
			leftToolbar.setBackground(defaultColor);
			leftToolbar.add(toolbarNew);
			leftToolbar.add(toolbarEdit);
			leftToolbar.add(toolbarDelete);
			leftToolbar.add(toolbarSearch);
			leftToolbar.add(toolbarGoogle);

			JPanel rightToolbar = new JPanel();
			rightToolbar.setBackground(defaultColor);
			rightToolbar.add(toolbarSave);
			rightToolbar.add(toolbarSettings);
			rightToolbar.add(toolbarLogout);
			rightToolbar.add(toolbarClose);

			toolbar = new JToolBar("Verktøy");
			toolbar.setBackground(defaultColor);
			toolbar.setBorder(BorderFactory.createEtchedBorder());
			toolbar.setLayout(new BorderLayout());
			toolbar.add(leftToolbar, BorderLayout.LINE_START);
			toolbar.add(rightToolbar, BorderLayout.LINE_END);

		mainBorderPanel.add(toolbar, BorderLayout.PAGE_START);
	}


	private void drawWestPanel()		//--> Lager og legger til vestre panel med menylisten
	{
		JPanel mainWestPanel = new JPanel();

		createMainMenu();

		mainWestPanel.setPreferredSize(new Dimension(195, 700));
		mainWestPanel.setMinimumSize(new Dimension(195, 500));
		mainWestPanel.setBackground(defaultColor);

		mainMenuList = new JList<>(leftTabs);
		mainMenuList.setCellRenderer(new leftMenuRenderer());
		mainMenuList.setFixedCellHeight(80);
		mainMenuList.setFixedCellWidth(190);
		mainMenuList.setBackground(defaultColor);
		mainMenuList.setFont(MENUFONT);
		mainMenuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mainMenuList.setSelectedIndex(0);
		mainMenuList.addListSelectionListener(listListener);

		mainWestPanel.add(mainMenuList);
		mainWestPanel.setBorder(BorderFactory.createEtchedBorder());

		mainBorderPanel.add(mainWestPanel, BorderLayout.LINE_START);
	}

	private void createMainMenu()		//--> Oppretter og legger til elementene som skal være i hovedmenyen, basert på tilgangsnivå til bruker.
	{
		int userAccess = user.getAccess();

		if(userAccess < Account.MANAGER)
		{
			leftTabs = new JLabel[5];
		}
		else if(userAccess == Account.MANAGER)
		{
			leftTabs = new JLabel[6];
			leftTabs[5] = new JLabel(mainMenuItems[5], Utilities.getIcon("/Images/Icons/accountIcon.png"), JLabel.LEFT);
			leftTabs[5].setFont(MENUFONT);
		}
		else if(userAccess == Account.SUPERADMIN)
		{
			leftTabs = new JLabel[7];
			leftTabs[5] = new JLabel(mainMenuItems[5], Utilities.getIcon("/Images/Icons/accountIcon.png"), JLabel.LEFT);
			leftTabs[5].setFont(MENUFONT);
			leftTabs[6] = new JLabel(mainMenuItems[6], Utilities.getIcon("/Images/Icons/adminIcon.png"), JLabel.LEFT);
			leftTabs[6].setFont(MENUFONT);
		}
		leftTabs[0] = new JLabel(mainMenuItems[0], Utilities.getIcon("/Images/Icons/ArtistIcon.png"), JLabel.LEFT);
		leftTabs[0].setFont(MENUFONT);
		leftTabs[1] = new JLabel(mainMenuItems[1], Utilities.getIcon("/Images/Icons/venueIcon.png"), JLabel.LEFT);
		leftTabs[1].setFont(MENUFONT);
		leftTabs[2] = new JLabel(mainMenuItems[2], Utilities.getIcon("/Images/Icons/eventIcon.png"), JLabel.LEFT);
		leftTabs[2].setFont(MENUFONT);
		leftTabs[3] = new JLabel(mainMenuItems[3], Utilities.getIcon("/Images/Icons/salesIcon.png"), JLabel.LEFT);
		leftTabs[3].setFont(MENUFONT);
		leftTabs[4] = new JLabel(mainMenuItems[4], Utilities.getIcon("/Images/Icons/searchIcon2.png"), JLabel.LEFT);
		leftTabs[4].setFont(MENUFONT);
	}

	private void drawSouthPanel() //--> Lager og legger til søndre panel med statuslinje
	{
		mainSouthPanel = new JPanel();
		mainSouthPanel.setPreferredSize(new Dimension(1000, 50));
		mainSouthPanel.setMinimumSize(new Dimension(1000, 50));
		mainSouthPanel.setBorder(BorderFactory.createEtchedBorder());

		mainBorderPanel.add(mainSouthPanel, BorderLayout.PAGE_END);
	}
	//Slutt Tegn Hovedvindu


//----------------------------------------------------------------------------------------------------------------- INDRE VINDUER (Metoder for å tegne indrevinduer og legge de i cardslayout)
	private void createCards()	//--> Oppretter alle indre vinduer og legger de til i et cardspanel i center panelet i hovedvinduet
	{
		cardPanel = new JPanel(new CardLayout());

		cardPanel.add(drawArtistTabbedPane(), mainMenuItems[0] );
		cardPanel.add(drawVenueTabbedPane(), mainMenuItems[1] );
		cardPanel.add(drawConcertTabbedPane(), mainMenuItems[2] );
		cardPanel.add(drawSalesTabbedPane(), mainMenuItems[3] );
		cardPanel.add(drawSearchTabbedPane(), mainMenuItems[4] );
		cardPanel.add(drawAccountTabbedPane(), mainMenuItems[5] );
		cardPanel.add(drawAdminTabbedPane(), mainMenuItems[6] );
	}

	private JTabbedPane drawArtistTabbedPane()	//--> Oppretter artistpanelet.
	{
		artistTabs = new JTabbedPane();
		artistPanel = new ArtistPanel(this, artistList);
		artistPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		artistNewPanel = new ArtistNewPanel(this, artistList, genreList);
		bandNewPanel = new BandNewPanel(this, artistList, genreList);
		artistTabs.addTab("Artister", artistPanel);
		artistTabs.addTab("Ny Soloartist", artistNewPanel);
		artistTabs.addTab("Nytt Band", bandNewPanel);

		return artistTabs;
	}

	private JTabbedPane drawVenueTabbedPane()	//--> Oppretter lokalepanelet
	{
		venueTabs = new JTabbedPane();
		venuePanel = new VenuePanel(this, venueList);
		venuePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		venueNewPanel = new VenueNewPanel(this, venueList, genreList);
		venueTabs.addTab("Lokaler", venuePanel);
		venueTabs.addTab("Nytt Lokale", venueNewPanel);
		return venueTabs;
	}

	private JTabbedPane drawConcertTabbedPane()		//--> Oppretter konsertpanelet
	{
		concertTabs = new JTabbedPane();
		concertPanel = new ConcertPanel(this, venueList, artistList, concertList);
		concertPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		concertNewPanel = new ConcertNewPanel(this, venueList, artistList, concertList);
		concertTabs.addTab("Events", concertPanel);
		concertTabs.addTab("Ny Event", concertNewPanel);

		return concertTabs;
	}

	private JTabbedPane drawSalesTabbedPane()	//--> Oppretter Salgspanel
	{
		salesTabs = new JTabbedPane();
		salesPanel = new SalesPanel(this, concertList);
		salesPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		salesTabs.addTab("Salg", salesPanel);
		salesTabs.addTab("Statistikk", new JPanel());

		return salesTabs;
	}

	private JTabbedPane drawSearchTabbedPane()	//--> Oppretter Søkepanel
	{
		searchTabs = new JTabbedPane();
		searchPanel = new SearchPanel(this, artistList, venueList, concertList, genreList);
		searchPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		searchTabs.addTab("Søk", searchPanel);

		return searchTabs;
	}

	private JTabbedPane drawAccountTabbedPane()	//--> Oppretter Brukerpanel
	{
		accountTabs = new JTabbedPane();
		accountPanel = new AccountPanel(this, accountList);
		accountPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		accountTabs.addTab("Brukere", accountPanel);

		return accountTabs;
	}

	private JTabbedPane drawAdminTabbedPane()	//--> Oppretter Adminpanel
	{
		adminTabs = new JTabbedPane();
		adminPanel = new AdminPanel();
		adminPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		adminTabs.addTab("Admin", adminPanel);

		return adminTabs;
	}
	//Slutt INDRE VINDUER


//----------------------------------------------------------------------------------------------------------------- OPPDATER INDRE VINDUER (Metoder for å tegne indrevinduer og legge de i cardslayout)
	public void repaintArtistView()		//--> Retegner Artistoversikt-panelet
	{
		if(artistTabs.getSelectedComponent() == artistEditPanel)
			artistTabs.remove(artistEditPanel);
		else if(artistTabs.getSelectedComponent() == bandEditPanel)
			artistTabs.remove(bandEditPanel);

		artistTabs.setSelectedComponent(artistPanel);
		artistPanel.repaintArtistView();
	}

	public void repaintVenueView()		//--> Retegner Lokaleoversikt-panelet
	{
		if(venueEditPanel != null)
			venueTabs.remove(venueEditPanel);

		venueTabs.setSelectedComponent(venuePanel);
		venuePanel.repaintVenueView();
	}

	public void repaintConcertView()	//-->  Retegner Konsertoversikt-panelet
	{
		if(concertEditPanel != null)
			concertTabs.remove(concertEditPanel);

		concertTabs.setSelectedComponent(concertPanel);
		concertPanel.repaintConcertView();
	}

	public void updateArtistJList()		//--> Oppdaterer artistoversikt-listen, i artistpanelet og ny-konsertpanelet.
	{
		artistPanel.updateArtistJList();
		concertNewPanel.updateArtistJList();
	}

	public void updateVenueJList()		//--> Oppdaterer lokaleoversikt-listen, i lokalepanelet og ny-konsertpanelet.
	{
		venuePanel.updateVenueJList();
		concertNewPanel.updateVenueJList();
	}

	public void updateConcertJList()	//--> Oppdaterer konsertoversikt-listen, i konsertpanelet og salgspanelet.
	{
		concertPanel.updateConcertJList();
		salesPanel.updateConcertJList();
	}
	//Slutt OPPDATER INDRE VINDUER

//----------------------------------------------------------------------------------------------------------------- ENDRE MENYER (Metoder for å opprette rediger/endre menyer)
	public void addArtistEditTab(Artist artist)		//--> Legger til Endre-Artist-Fanen i Artistpanelet
	{
		if(artist instanceof Soloartist)
		{
			if(artistEditPanel != null)
				artistTabs.remove(artistEditPanel);

			artistEditPanel = new ArtistEditPanel(this, artistList, genreList, (Soloartist)artist);
			artistTabs.addTab("Endre Soloartist", artistEditPanel);
			artistTabs.setSelectedComponent(artistEditPanel);
		}
		else
		{
			if(bandEditPanel != null)
				artistTabs.remove(bandEditPanel);

			bandEditPanel = new BandEditPanel(this, artistList, genreList, (Band)artist);
			artistTabs.addTab("Endre Band", bandEditPanel);
			artistTabs.setSelectedComponent(bandEditPanel);
		}
	}

	public void addVenueEditTab(Venue venue)	//--> Legger til Endre-Lokale-Fanen i Lokalepanelet
	{
		if(venueEditPanel != null)
			venueTabs.remove(venueEditPanel);

		venueEditPanel = new VenueEditPanel(this, venueList, genreList, venue);
		venueTabs.addTab("Endre Lokale", venueEditPanel);
		venueTabs.setSelectedComponent(venueEditPanel);
	}

	public void addConcertEditTab(Concert concert)	//--> Legger til Endre-Konsert-Fanen i Konsertpanelet
	{
		if(concertEditPanel != null)
			concertTabs.remove(concertEditPanel);

		concertEditPanel = new ConcertEditPanel(this, venueList, artistList, concertList, concert);
		concertTabs.addTab("Endre Event", concertEditPanel);
		concertTabs.setSelectedComponent(concertEditPanel);
	}
	//Slutt ENDRE MENYER



//----------------------------------------------------------------------------------------------------------------- NYE VINDUER (Oppretter indre vinduer i egne JFrames metoder)
	private void searchTool()	//--> Oppretter et nytt søkepanel i en ekstern JFrame
	{
		JFrame searchWindow = new JFrame("Søk");
		searchWindow.setLocationRelativeTo(this);
		searchWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		JPanel searchPane = new SearchPanel(this, artistList, venueList, concertList, genreList);
		searchWindow.add(searchPane);
		searchWindow.setVisible(true);
		Utilities.setIcon(searchWindow);
		searchWindow.pack();
	}

	protected void newArtistView(Artist selected)	//--> Oppretter et nytt artistoversikt-panel i en ekstern JFrame
	{
		JFrame newWindow = new JFrame("Artister");
		newWindow.setLocationRelativeTo(this);
		newWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		ArtistPanel newPane = new ArtistPanel(this, artistList);
		newPane.disableButtons();	//Det eksterne vinduet er kun for visning, så kobler ut knappene.
		newWindow.add(newPane);
		newWindow.setVisible(true);
		Utilities.setIcon(newWindow);
		newWindow.setSize(690, 500);

		if(selected != null)
			newPane.setSelected(selected);
	}

	protected void newVenueView(Venue selected)		//--> Oppretter et nytt lokaleoversikt-panel i en ekstern JFrame
	{
		JFrame newWindow = new JFrame("Lokaler");
		newWindow.setLocationRelativeTo(this);
		newWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		VenuePanel newPane = new VenuePanel(this, venueList);
		newPane.disableButtons();	//Det eksterne vinduet er kun for visning, så kobler ut knappene.
		newWindow.add(newPane);
		newWindow.setVisible(true);
		Utilities.setIcon(newWindow);
		newWindow.setSize(690, 500);
		if(selected != null)
			newPane.setSelected(selected);
	}

	protected void newConcertView(Concert selected)		//--> Oppretter et nytt konsetoversikt-panel i en ekstern JFrame
	{
		JFrame newWindow = new JFrame("Eventer");
		newWindow.setLocationRelativeTo(this);
		newWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		ConcertPanel newPane = new ConcertPanel(this, venueList, artistList, concertList);
		newPane.disableButtons();	//Det eksterne vinduet er kun for visning, så kobler ut knappene.
		newWindow.add(newPane);
		newWindow.setVisible(true);
		Utilities.setIcon(newWindow);
		newWindow.setSize(690, 500);
		if(selected != null)
			newPane.setSelected(selected);
	}

	private void newSaleView()	//--> Oppretter et nytt salgs-panel i en ekstern JFrame
	{
		JFrame newWindow = new JFrame("Salg");
		newWindow.setLocationRelativeTo(this);
		newWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		JPanel newPane = new SalesPanel(this, concertList);
		newWindow.add(newPane);
		newWindow.setVisible(true);
		Utilities.setIcon(newWindow);
		newWindow.pack();
	}

	private void newUserView()	//--> Oppretter et nytt bruker-panel i en ekstern JFrame
	{
		JFrame newWindow = new JFrame("Brukere");
		newWindow.setLocationRelativeTo(this);
		newWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		JPanel newPane = new AccountPanel(this, accountList);
		newWindow.add(newPane);
		newWindow.setVisible(true);
		Utilities.setIcon(newWindow);
		newWindow.pack();
	}
	// Slutt NYE VINDUER


//----------------------------------------------------------------------------------------------------------------- Generelle Funksjonsmetoder
	public void openArtist(Artist artist)	//--> Åpner inkommen artist i artistpanelet
	{
		mainMenuList.setSelectedIndex(0);
		artistTabs.setSelectedIndex(0);
		artistPanel.setSelected(artist);
		setCursor(NORMALCURSOR);
	}

	public void openVenue(Venue venue)	//--> Åpner inkommen lokale i lokalepanelet
	{
		mainMenuList.setSelectedIndex(1);
		venueTabs.setSelectedIndex(0);
		venuePanel.setSelected(venue);
		setCursor(NORMALCURSOR);
	}

	public void openSettings()
	{
		new SettingsWindow(this, user);
	}

	private void userRestriction()	//--> Setter restriksjoner basert på innlogget bruker.
	{

		if(user.getAccess() == Account.SALESMAN)
		{
			concertPanel.reviewButton.setEnabled(false);
			concertPanel.extraButton.setEnabled(false);
		}

		if(user.getAccess() < Account.MANAGER)	//De under managere skal ikke kunne se eller endre brukere
		{
			edit_AddAccount.setEnabled(false);
			edit_EditAccount.setEnabled(false);
			view_showUsers.setEnabled(false);
		}

		if(user.getAccess() < Account.EVENTPLANNER)	//De under eventplanlegger skal ikke kunne endre eller opprette noe nytt, annet enn salg.
		{
			file_NewArtist.setEnabled(false);
			file_NewVenue.setEnabled(false);
			file_NewEvent.setEnabled(false);
			edit_New.setEnabled(false);
			edit_EditItem.setEnabled(false);
			edit_Delete.setEnabled(false);
			edit_AddPhoto.setEnabled(false);
			edit_AddVideo.setEnabled(false);
			edit_AddMusic.setEnabled(false);
			edit_AddReview.setEnabled(false);
			edit_ManageGenres.setEnabled(false);
			sales_GetSalesStats.setEnabled(false);

			artistTabs.remove(artistNewPanel);
			artistTabs.remove(bandNewPanel);
			venueTabs.remove(venueNewPanel);
			concertTabs.remove(concertNewPanel);
			venuePanel.disableButtons();
			artistPanel.disableButtons();
			concertPanel.disableButtons();
		}
	}

	private void setLook()	//--> Endrer utseende på programmet til det motsatte av det det har.
	{
		int response = Utilities.showOption(this, "Er du sikker på at du vil endre utseende? \nAll ulagret data vil gå tapt.");
		if(response == JOptionPane.YES_OPTION)
		{
			database.setSystemLook(view_SwitchView.getState());
			toolbarSave.doClick();
			loginWindow.changeLook();
		}
		else
			view_SwitchView.setSelected(database.getSystemLook());
	}

	protected void saveToFile()	//--> Lagrer hele databasen til fil.
	{
		if(database == null)
			Utilities.showError(null,"Databasen er null");

		try
		{
			setCursor(WAITCURSOR);
			IOActions.writeToFile(new File("Log/Save.dta"), database);
			Thread.sleep(400);		//Pauser tråden før musepekeren blir satt tilbake, slik at brukeren er garantert en visuell indikasjon på at programmet lagrer.
		}
		catch(Exception k){IOActions.writeErrorLog(k);}
		finally
		{
			setCursor(NORMALCURSOR);
		}
	}

	private void createNew(int index)	//--> Setter valgt fane til ny-fane.
	{
		mainMenuList.setSelectedIndex(index);

		switch(index)
		{
			case 0:
				   artistTabs.setSelectedComponent(bandNewPanel);
				   break;
			case 1:
				   venueTabs.setSelectedComponent(venueNewPanel);
				   break;
			case 2:
				   concertTabs.setSelectedComponent(concertNewPanel);
				   break;
			default:
				   mainMenuList.setSelectedIndex(2);
				   concertTabs.setSelectedComponent(concertNewPanel);
				   break;
		}
	}

	private void editTool()	//--> Åpner valgt artist/lokale/konsert for redigering.
	{
		int index = mainMenuList.getSelectedIndex();

		switch(index)
		{
			case 0:
				   artistPanel.editSelected();
				   break;
			case 1:
				   venuePanel.editSelected();
				   break;
			case 2:
				   concertPanel.editSelected();
				   break;
		}
	}

	private void deleteTool()	//--> Sletter valgt artist/lokale/konsert.
	{
		int index = mainMenuList.getSelectedIndex();

		switch(index)
		{
			case 0:
				   artistPanel.deleteArtist();
				   break;
			case 1:
				   venuePanel.deleteVenue();
				   break;
			case 2:
				   concertPanel.deleteConcert();
				   break;
		}
	}

	private void googleTool()	//--> Googler(i standard nettleser) valgt artist/lokale/konsert.
	{
		int index = mainMenuList.getSelectedIndex();

		switch(index)
		{
			case 0:
				   Utilities.google(artistPanel.getSelected());
				   break;
			case 1:
				   Utilities.google(venuePanel.getSelected());
				   break;
			case 2:
				   Utilities.google(concertPanel.getSelected());
				   break;
		}
	}

	private EPlannerMainWindow getThis()
	{
		return this;
	}
	//Slutt Generelle Funksjonsmetoder


//----------------------------------------------------------------------------------------------------------------- INDRE KLASSER


	//Hensikt: Klassen er til for å kunne tegne en JLabel(med ikon og tekst) i en JList.
	public class leftMenuRenderer extends DefaultListCellRenderer
	{
		@SuppressWarnings("rawtypes")
		@Override
		public Component getListCellRendererComponent(JList list, Object value, int index,boolean isSelected, boolean cellHasFocus)
		{
			JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if(value instanceof JLabel)
			{
				this.setText(((JLabel)value).getText());
				this.setIcon(((JLabel)value).getIcon());
			}
			return this;
		}
	}


	//---------------------------------------------------------------- LYTTERE


	//Hensikt: Lytter på justering av vindusstørrelse, for å reskalere bilder.
	private class ResizeListener extends ComponentAdapter
	{
		Date last = new Date();
		Date now = new Date();

		@Override
        public void componentResized(ComponentEvent e)
        {
			now = new Date();

			if((now.getTime()-last.getTime() > 200))
			{
				if(!artistList.isEmpty())
				{
					artistPanel.resizeImage(artistPanel.artistThumb, 0, 0);
					artistPanel.repaintArtistView();
				}

				if(!venueList.isEmpty())
				{
					venuePanel.resizeImage(venuePanel.venueThumb, 0, 0);
					venuePanel.repaintVenueView();
				}

				if(!concertList.isEmpty())
				{
					concertPanel.resizeImage(concertPanel.concertThumb, 0, 0);
					concertPanel.repaintConcertView();
				}
				last = new Date();
			}
		}
	}//End of ResizeListener class


	//Hensikt: Lytter på valg i hovedmenyen for å vise korrekt indre vindu.
	private class ListListener implements ListSelectionListener
	{
		@Override
		public void valueChanged(ListSelectionEvent e)
		{
			if(e.getSource() == mainMenuList)
			{
				if (e.getValueIsAdjusting() == false)
				{
					int index = mainMenuList.getSelectedIndex();
					if(index > -1)
					{
						CardLayout cl = (CardLayout)(cardPanel.getLayout());
						cl.show(cardPanel, mainMenuItems[index]);
					}

				}
			}
		}
	}//End of ListListener class


	//Hensikt: Lytter på knapper og menylinje som er langt til i hovedvinduet (men ikke de indre vinduene).
	private class MainWindowListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == toolbarSave || e.getSource() == file_Save)
			{
				saveToFile();
			}
			else if(e.getSource() == toolbarClose || e.getSource() == file_Exit)
			{
				mainWindowAdapter.windowClosing(new WindowEvent(getThis(), WindowEvent.WINDOW_CLOSING));
			}
			else if(e.getSource() == file_NewArtist)
				createNew(0);
			else if(e.getSource() == file_NewVenue)
				createNew(1);
			else if(e.getSource() == file_NewEvent)
				createNew(2);
			else if(e.getSource() == toolbarNew || e.getSource() == edit_New)
				createNew(mainMenuList.getSelectedIndex());
			else if(e.getSource() == toolbarEdit || e.getSource() == edit_EditItem)
				editTool();
			else if(e.getSource() == toolbarDelete || e.getSource() == edit_Delete)
				deleteTool();
			else if(e.getSource() == toolbarGoogle || e.getSource() == edit_Google)
				googleTool();
			else if(e.getSource() == edit_Search)
				mainMenuList.setSelectedIndex(4);
			else if(e.getSource() == edit_AddAccount)
			{
				mainMenuList.setSelectedIndex(5);
				accountPanel.newButton.doClick();
			}
			else if(e.getSource() == edit_EditAccount)
			{
				mainMenuList.setSelectedIndex(5);
				accountPanel.editButton.doClick();
			}
			else if(e.getSource() == sales_NewSale)
			{
				mainMenuList.setSelectedIndex(3);
			}
			else if(e.getSource() == toolbarSearch || e.getSource() == view_showSearch)
				searchTool();
			else if(e.getSource() == toolbarLogout || e.getSource() == file_Logout)
				loginWindow.logout();
			else if(e.getSource() == toolbarSettings || e.getSource() == file_Settings)
				openSettings();
			else if(e.getSource() == view_SwitchView)
				setLook();
			else if(e.getSource() == view_showArtists)
				newArtistView(null);
			else if(e.getSource() == view_showVenues)
				newVenueView(null);
			else if(e.getSource() == view_showConcerts)
				newConcertView(null);
			else if(e.getSource() == view_showSales)
				newSaleView();
			else if(e.getSource() == view_showUsers)
				newUserView();
			else if(e.getSource() == help_About)
				Utilities.showInfo(getThis(),"ePlanner 1.0\nLaget av Martin W Løkkeberg og Jonas Moltzau.");
		}
	}//End of MainWindowListener class

	private class MainWindowAdapter extends WindowAdapter
	{
		@Override
		public void windowClosing(WindowEvent e)
		{
			int response = Utilities.showOption(getThis(), "Vil du lagre endringene gjort i programmet?");
			if(response == JOptionPane.YES_OPTION)
			{
				saveToFile();
				System.exit(0);
			}
			else if(response == JOptionPane.NO_OPTION)
				System.exit(0);

		}
	}

	//Slutt Lyttere
	//Slutt Indre Klasser

}//End of class EPlannerMainWindow