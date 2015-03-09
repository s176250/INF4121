/*
 *	Innhold:  Metoder som oppretter og tegner elementene i artistPanel, og lytter på slett, ny og endre knapper.
 *
 *				drawWestPanel()				--> Lager og legger til vestre panel med brukerlisten
 *				drawSouthPanel()			--> Lager og legger til søndre panel med knapper i.
 *				displayAccount()			--> Kalles hver gang en bruker i oversiktslisten blir trykket på, tegner og viser senterpanelet med valgt bruker.
 *				repaintAccountView() 		--> Retegner Brukeroversikt-panelet(dette)
 *				updateAccountJList()		--> Oppdaterer brukeroversikt-listen
 *				setActiveEditFields()		--> Setter brukerinfo-feltene aktive/ikke-aktive
 *				setActiveFields()			--> Setter ny-bruker-feltene aktive/ikke-aktive, tømmer de hvis ikke aktiv.
 *				resetPwd()					--> Resetter og genererer et nytt tilfeldig passord til valgt bruker.
 *				editAccount()				--> Oppdaterer valgt bruker med innskrevet data.
 *				addCoverImage()				--> Legger til et coverbilde på brukeren.
 *				createNewAccount()			--> Oppretter en ny bruker av programmet.
 *				deactivate()				--> Deaktiverer en konto, slik at den ikke synes i listen over brukere og ikke kan logges inn med.
 *
 *				Indre klasser
 *				ListListener				--> Lytter på valg i listene.
 *				Listener					--> Lytter på knapper lagt til i brukerpanelet
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 13.05.2012.
 */

package eplanner.windows;

import eplanner.users.*;
import eplanner.utilities.*;
import eplanner.windows.components.ExtendedJList;
import eplanner.windows.components.IconButton;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

//Hensikt: Tegne bruker oversikt panelet
@SuppressWarnings("serial")
public class AccountPanel extends JPanel
{
	//Lister og Comboboxer
	private ExtendedJList<Account> accountJList;
	private JComboBox<String> accessBox, accessEditBox;

	//Paneler
	private JPanel centerPanel, westPanel, southPanel;

	//Knapper
	protected JButton changeCoverimage, editButton, newButton, deleteButton;
	private IconButton resetPassButton;

	//Tekstfelt
	private JTextField nameField, adrField, usernameField, telField, emailField, emailConfirmField, passField;
	private JTextField nameEditField, adrEditField, usernameEditField, telEditField, emailEditField, passEditField;

	//Lyttere
	private ListListener listListener;
	private Listener actionListener;

	//Annet
	private AccountList accountList;
	private EPlannerMainWindow parent;

	//Konstruktør
	public AccountPanel(EPlannerMainWindow parent, AccountList accountList)
	{
		super(new BorderLayout());

		//Lagre parametere
		this.accountList = accountList;
		this.parent = parent;

		//Sette preferanser
		setPreferredSize(new Dimension(810, 700));
		setBackground(EPlannerMainWindow.BASE_COLOR);
		setVisible(true);

		//Opprette Lyttere
		actionListener = new Listener();
		listListener = new ListListener();

		//Opprette Knapper
		changeCoverimage = new JButton("Endre bilde");
		changeCoverimage.addActionListener(actionListener);
		editButton = new JButton("Endre");
		editButton.addActionListener(actionListener);
		newButton = new JButton("Ny");
		newButton.addActionListener(actionListener);
		deleteButton = new JButton("Deaktiver");
		deleteButton.addActionListener(actionListener);
		resetPassButton = new IconButton(Utilities.getIcon("/Images/Icons/resetIcon.png"), EPlannerMainWindow.BASE_COLOR, new Dimension(20,16));
		resetPassButton.addActionListener(actionListener);
		resetPassButton.setToolTipText("Reset Passord");

		//Opprette CombocBox
		accessBox = new JComboBox<>(Account.ACCESS_NAMES);
		accessEditBox = new JComboBox<>(Account.ACCESS_NAMES);

		//Opprette tekstfelter
		nameField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
		adrField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
		usernameField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
		telField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
		emailField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
		emailConfirmField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
		passField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);

		nameEditField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
		adrEditField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
		usernameEditField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
		telEditField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
		emailEditField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
		passEditField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);


	//------------------------------------------------------------------------------------- LINE START (West Panel)

		drawWestPanel();	// Lager og legger til vestre panel med brukerlisten

	//------------------------------------------------------------------------------------- Center Panel
		centerPanel = new JPanel(new GridLayout(2,2,15,20));

		centerPanel.setMinimumSize(new Dimension(500, 500));
		centerPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10,0,5,15));

		add(centerPanel, BorderLayout.CENTER);

	//--------------------------------------------------------------------------------PAGE END (South Panel) --> buttons
		drawSouthPanel();	// Lager og legger til søndre panel med knapper i.


		//Set redigerbare felter
		usernameEditField.setEditable(false);
		passEditField.setEditable(false);
		setActiveEditFields(false);
		setActiveFields(false);

	}

// ------------------------------------------------------------------------------------------------------------------------------GUI Metoder:
		// Alle metodene som slutter på panel/box er paneler som opprettes i tilsvarende posisjon til navnet.


	private void drawWestPanel()	//--> Lager og legger til vestre panel med brukerlisten
	{

		westPanel = new JPanel(new GridLayout(1,1));

		JScrollPane listScroll = new JScrollPane(westPanel);
		listScroll.setBackground(EPlannerMainWindow.BASE_COLOR);
		listScroll.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

		accountJList = new ExtendedJList<>();
		updateAccountJList();

		accountJList.addListSelectionListener(listListener);
		accountJList.setFixedCellHeight(20);
		accountJList.setFixedCellWidth(190);
		accountJList.setFont(new Font("Arial",Font.PLAIN, 14 ));
		accountJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		accountJList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1), "Brukere"));

		westPanel.add(accountJList);

		add(listScroll, BorderLayout.LINE_START);
	}

	private void drawSouthPanel()	//--> Lager og legger til søndre panel med knapper i.
	{
		southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		southPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
		southPanel.setBorder(BorderFactory.createEmptyBorder(0,0,5,60));

		southPanel.add(newButton);
		southPanel.add(editButton);
		southPanel.add(deleteButton);

		add(southPanel, BorderLayout.PAGE_END);
	}

	protected void displayAccount(Account account)	//--> Kalles hver gang en bruker i oversiktslisten blir trykket på, tegner og viser senterpanelet med valgt bruker.
	{
		//--------------------------------------------------------------------------------TopLeft (overskrift, bilde, knapp)
			JPanel leftTopGrid = new JPanel(new BorderLayout());
			leftTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

			//Overskrift
			JLabel accountName = new JLabel(account.getName());
			accountName.setFont(new Font("Arial",Font.BOLD, 20 ));
			accountName.setBorder(BorderFactory.createEmptyBorder(0,0,15,0));
			leftTopGrid.add(accountName, BorderLayout.PAGE_START);

			//Bilde av account
			ImageIcon accountImage = account.getThumbImage();
			JLabel accountThumb = new JLabel(accountImage);
			accountThumb.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			leftTopGrid.add(accountThumb, BorderLayout.CENTER);

			//Knapper for å legge til nye bilder/video
			JPanel buttonGridTopLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
			buttonGridTopLeft.setBackground(EPlannerMainWindow.BASE_COLOR);
			buttonGridTopLeft.add(changeCoverimage);
			leftTopGrid.add(buttonGridTopLeft, BorderLayout.PAGE_END);
			centerPanel.add(leftTopGrid);

		//--------------------------------------------------------------------------------TopRight (Felter for innfylling av ny brukers personalia)
			Box rightTopBox = new Box(BoxLayout.LINE_AXIS);

			rightTopBox.setMaximumSize(new Dimension(1000,150 ));
			rightTopBox.setPreferredSize(new Dimension(350, 150));
			rightTopBox.setBackground(EPlannerMainWindow.BASE_COLOR);
			rightTopBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Legg til personalia"));


			JPanel rightLeftTopGrid = new JPanel(new GridLayout(6, 1));
			rightLeftTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

			JPanel rightRightTopGrid = new JPanel(new GridLayout(6, 1));
			rightRightTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

			//rightLeftTopGrid
			JPanel accountLabelFlow1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			accountLabelFlow1.setBackground(EPlannerMainWindow.BASE_COLOR);
			accountLabelFlow1.add(new JLabel("Brukernavn:"));
			JPanel accountLabelFlow2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			accountLabelFlow2.setBackground(EPlannerMainWindow.BASE_COLOR);
			accountLabelFlow2.add(new JLabel("Navn:"));
			JPanel accountLabelFlow3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			accountLabelFlow3.setBackground(EPlannerMainWindow.BASE_COLOR);
			accountLabelFlow3.add(new JLabel("Adr:"));
			JPanel accountLabelFlow4 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			accountLabelFlow4.setBackground(EPlannerMainWindow.BASE_COLOR);
			accountLabelFlow4.add(new JLabel("Tlf:"));
			JPanel accountLabelFlow5 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			accountLabelFlow5.setBackground(EPlannerMainWindow.BASE_COLOR);
			accountLabelFlow5.add(new JLabel("Epost:"));
			JPanel accountLabelFlow6 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			accountLabelFlow6.setBackground(EPlannerMainWindow.BASE_COLOR);
			accountLabelFlow6.add(new JLabel("Repeter Epost:"));

			//rightRightTopGrid
			JPanel accountFlow1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			accountFlow1.setBackground(EPlannerMainWindow.BASE_COLOR);
			accountFlow1.add(usernameField);
			JPanel accountFlow2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			accountFlow2.setBackground(EPlannerMainWindow.BASE_COLOR);
			accountFlow2.add(nameField);
			JPanel accountFlow3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			accountFlow3.setBackground(EPlannerMainWindow.BASE_COLOR);
			accountFlow3.add(adrField);
			JPanel accountFlow4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			accountFlow4.setBackground(EPlannerMainWindow.BASE_COLOR);
			accountFlow4.add(telField);
			JPanel accountFlow5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			accountFlow5.setBackground(EPlannerMainWindow.BASE_COLOR);
			accountFlow5.add(emailField);
			JPanel accountFlow6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			accountFlow6.setBackground(EPlannerMainWindow.BASE_COLOR);
			accountFlow6.add(emailConfirmField);

			//Adding Left
			rightLeftTopGrid.add(accountLabelFlow1);
			rightLeftTopGrid.add(accountLabelFlow2);
			rightLeftTopGrid.add(accountLabelFlow3);
			rightLeftTopGrid.add(accountLabelFlow4);
			rightLeftTopGrid.add(accountLabelFlow5);
			rightLeftTopGrid.add(accountLabelFlow6);

			//Adding Right
			rightRightTopGrid.add(accountFlow1);
			rightRightTopGrid.add(accountFlow2);
			rightRightTopGrid.add(accountFlow3);
			rightRightTopGrid.add(accountFlow4);
			rightRightTopGrid.add(accountFlow5);
			rightRightTopGrid.add(accountFlow6);


			rightTopBox.add(rightLeftTopGrid);
			rightTopBox.add(rightRightTopGrid);

			centerPanel.add(rightTopBox);

		//--------------------------------------------------------------------------------BottomLeft (Visning av valgt brukers info)
			Box leftBottomBox = new Box(BoxLayout.LINE_AXIS);

			leftBottomBox.setMaximumSize(new Dimension(1000,150 ));
			leftBottomBox.setBackground(EPlannerMainWindow.BASE_COLOR);

			leftBottomBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Brukerinfo"));

			JPanel leftLeftBottomGrid = new JPanel(new GridLayout(7, 1));
			leftLeftBottomGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

			JPanel leftRightBottomGrid = new JPanel(new GridLayout(7, 1));
			leftRightBottomGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

			//leftLeftBottomGrid
			JPanel labelFlow0 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			labelFlow0.setBackground(EPlannerMainWindow.BASE_COLOR);
			labelFlow0.add(new JLabel("Adgangsnivå"));
			JPanel labelFlow1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			labelFlow1.setBackground(EPlannerMainWindow.BASE_COLOR);
			labelFlow1.add(new JLabel("Brukernavn:"));
			JPanel labelFlow2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			labelFlow2.setBackground(EPlannerMainWindow.BASE_COLOR);
			labelFlow2.add(new JLabel("Navn:"));
			JPanel labelFlow3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			labelFlow3.setBackground(EPlannerMainWindow.BASE_COLOR);
			labelFlow3.add(new JLabel("Adr:"));
			JPanel labelFlow4 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			labelFlow4.setBackground(EPlannerMainWindow.BASE_COLOR);
			labelFlow4.add(new JLabel("Tlf:"));
			JPanel labelFlow5 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			labelFlow5.setBackground(EPlannerMainWindow.BASE_COLOR);
			labelFlow5.add(new JLabel("Epost:"));
			JPanel labelFlow6 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			labelFlow6.setBackground(EPlannerMainWindow.BASE_COLOR);
			labelFlow6.add(new JLabel("Passord:"));


			//leftRightBottomGrid
			JPanel flow0 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			flow0.setBackground(EPlannerMainWindow.BASE_COLOR);
			flow0.add(accessEditBox);
			JPanel flow1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			flow1.setBackground(EPlannerMainWindow.BASE_COLOR);
			flow1.add(usernameEditField);
			JPanel flow2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			flow2.setBackground(EPlannerMainWindow.BASE_COLOR);
			flow2.add(nameEditField);
			JPanel flow3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			flow3.setBackground(EPlannerMainWindow.BASE_COLOR);
			flow3.add(adrEditField);
			JPanel flow4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			flow4.setBackground(EPlannerMainWindow.BASE_COLOR);
			flow4.add(telEditField);
			JPanel flow5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			flow5.setBackground(EPlannerMainWindow.BASE_COLOR);
			flow5.add(emailEditField);
			JPanel flow6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			flow6.setBackground(EPlannerMainWindow.BASE_COLOR);
			flow6.add(passEditField);
			flow6.add(resetPassButton);


			//Adding Left
			leftLeftBottomGrid.add(labelFlow0);
			leftLeftBottomGrid.add(labelFlow1);
			leftLeftBottomGrid.add(labelFlow2);
			leftLeftBottomGrid.add(labelFlow3);
			leftLeftBottomGrid.add(labelFlow4);
			leftLeftBottomGrid.add(labelFlow5);
			leftLeftBottomGrid.add(labelFlow6);


			//Adding Right
			leftRightBottomGrid.add(flow0);
			leftRightBottomGrid.add(flow1);
			leftRightBottomGrid.add(flow2);
			leftRightBottomGrid.add(flow3);
			leftRightBottomGrid.add(flow4);
			leftRightBottomGrid.add(flow5);
			leftRightBottomGrid.add(flow6);


			nameEditField.setText(account.getRealName());
			usernameEditField.setText(account.getUsername());
			adrEditField.setText(account.getAdr());
			telEditField.setText(account.getTel());
			emailEditField.setText(account.getEmail());
			passEditField.setText("***********");

			if(account.getAccess() == Account.SUPERADMIN)
			{
				accessEditBox.addItem(AccountList.ADMIN_NAME);
				accessEditBox.setSelectedItem(AccountList.ADMIN_NAME);
			}
			else
			{
				accessEditBox.removeItem(AccountList.ADMIN_NAME);
				accessEditBox.setSelectedItem(account.getStringAccess());
			}


			leftBottomBox.add(leftLeftBottomGrid);
			leftBottomBox.add(leftRightBottomGrid);

			centerPanel.add(leftBottomBox);

		//--------------------------------------------------------------------------------BottomRight (Felter for passord og tilgangsnivå)
			Box rightBottomBox = new Box(BoxLayout.LINE_AXIS);
			rightBottomBox.setMaximumSize(new Dimension(1000,150 ));
			rightBottomBox.setBackground(EPlannerMainWindow.BASE_COLOR);
			rightBottomBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Sett adgang"));

			JPanel innerRightBottomBox = new JPanel(new BorderLayout());
			innerRightBottomBox.setBackground(EPlannerMainWindow.BASE_COLOR);
			innerRightBottomBox.setPreferredSize(new Dimension(350, 100));


			JPanel rightLeftBottomGrid = new JPanel(new GridLayout(2, 1));
			rightLeftBottomGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

			JPanel rightRightBottomGrid = new JPanel(new GridLayout(2, 1));
			rightRightBottomGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

			//rightLeftBottomGrid
			JPanel accountInfoLabelFlow1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			accountInfoLabelFlow1.setBackground(EPlannerMainWindow.BASE_COLOR);
			accountInfoLabelFlow1.add(new JLabel("Passord:"));
			JPanel accountInfoLabelFlow2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			accountInfoLabelFlow2.setBackground(EPlannerMainWindow.BASE_COLOR);
			accountInfoLabelFlow2.add(new JLabel("Tilgang:"));


			//rightRightBottomGrid
			JPanel accountInfoFlow1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			accountInfoFlow1.setBackground(EPlannerMainWindow.BASE_COLOR);
			accountInfoFlow1.add(passField);
			JPanel accountInfoFlow2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			accountInfoFlow2.setBackground(EPlannerMainWindow.BASE_COLOR);
			accountInfoFlow2.add(accessBox);

			//Adding Left
			rightLeftBottomGrid.add(accountInfoLabelFlow1);
			rightLeftBottomGrid.add(accountInfoLabelFlow2);


			//Adding Right
			rightRightBottomGrid.add(accountInfoFlow1);
			rightRightBottomGrid.add(accountInfoFlow2);

			Box innerFlow = new Box(BoxLayout.LINE_AXIS);

			innerFlow.add(rightLeftBottomGrid);
			innerFlow.add(rightRightBottomGrid);
			innerRightBottomBox.add( innerFlow, BorderLayout.PAGE_START);
			rightBottomBox.add(innerRightBottomBox);

			innerRightBottomBox.add(Box.createVerticalGlue());
			centerPanel.add(rightBottomBox);
	}

// ------------------------------------------------------------------------------------------------------------------------------Funksjons Metoder:

	public void repaintAccountView() //--> Retegner Brukeroversikt-panelet(dette)
	{
		centerPanel.removeAll();
		displayAccount(accountJList.getSelectedValue());
		centerPanel.getRootPane().revalidate();
		setActiveEditFields(false);
		setActiveFields(false);
	}

	public void updateAccountJList()	//--> Oppdaterer brukeroversikt-listen
	{
		accountJList.setContent(accountList.getActiveArray());
	}

	private void setActiveEditFields(boolean active)	//--> Setter brukerinfo-feltene aktive/ikke-aktive
	{
		nameEditField.setEditable(active);
		adrEditField.setEditable(active);
		telEditField.setEditable(active);
		emailEditField.setEditable(active);
		resetPassButton.setEnabled(active);

		Account tmp = accountJList.getSelectedValue();

		if(tmp != null && tmp.getAccess() == Account.SUPERADMIN) //Er ikke lov å endre tilgangsnivået til Admin.
			accessEditBox.setEnabled(false);
		else
			accessEditBox.setEnabled(active);

		if(active)
		{
			editButton.setText("Oppdater");
			deleteButton.setText("Avbryt");
		}
		else
		{
			editButton.setText("Endre");
			deleteButton.setText("Deaktiver");
		}
	}

	private void setActiveFields(boolean active)	//--> Setter ny-bruker-feltene aktive/ikke-aktive, tømmer de hvis ikke aktiv.
	{
		nameField.setEditable(active);
		usernameField.setEditable(active);
		adrField.setEditable(active);
		telField.setEditable(active);
		emailField.setEditable(active);
		emailConfirmField.setEditable(active);
		passField.setEditable(active);
		accessBox.setEnabled(active);


		if(active)
		{
			newButton.setText("Lagre");
			deleteButton.setText("Avbryt");
		}
		else
		{
			nameField.setText("");
			usernameField.setText("");
			adrField.setText("");
			telField.setText("");
			emailField.setText("");
			emailConfirmField.setText("");
			passField.setText("");
			newButton.setText("Ny");
			deleteButton.setText("Deaktiver");
		}
	}

	public void setDefaultSelectedAccount()
	{
			accountJList.setSelectedIndex(0);
	}

	/*
	Kontor slettes ikke, men deaktiveres slik at man kan se hvilke
	planleggere som har opprettet hva. Kun admin skal kunne slette
	brukere helt, men det har vi ikke rukket å implementere.
	public void deleteAccount()
	{
		Account del = accountJList.getSelectedValue();

		if(del != null)
		{
			int answer = Utilities.showOption(parent, "Er du sikker på at du vil slette " + del.getName());
			if(answer == JOptionPane.YES_OPTION)
			{
				accountList.removeAccount(del, null);
				updateAccountJList();
				centerPanel.removeAll();
				centerPanel.getRootPane().revalidate();
				parent.accountTabs.setSelectedComponent(parent.accountNewPanel);
				parent.accountTabs.setSelectedComponent(parent.accountPanel);
				Utilities.showInfo(parent, del.getName() + " er slettet.");
			}
		}
	}
	*/

	private void resetPwd()	//--> Resetter og genererer et nytt tilfeldig passord til valgt bruker.
	{
		Account user = accountJList.getSelectedValue();
		passEditField.setText(user.resetPwd());
	}

	private void editAccount()	//--> Oppdaterer valgt bruker med innskrevet data.
	{
		Account user = accountJList.getSelectedValue();

		user.setRealName(nameEditField.getText());
		user.setAdr(adrEditField.getText());
		user.setTel(telEditField.getText());
		user.setEmail(emailEditField.getText());

		int accessLevel = 0;

		if(accessEditBox.getSelectedItem().equals(Account.ACCESS_NAMES[0]))
			user.setAccess(Account.MANAGER);
		else if(accessEditBox.getSelectedItem().equals(Account.ACCESS_NAMES[1]))
			user.setAccess(Account.EVENTPLANNER);
		else if(accessEditBox.getSelectedItem().equals(Account.ACCESS_NAMES[2]))
			user.setAccess(Account.SALESMAN);

		updateAccountJList();

		Utilities.showInfo(parent, "Bruker oppdatert.");
		setActiveEditFields(false);
	}

	private void addCoverImage()	//--> Legger til et coverbilde på brukeren.
	{
		File cover = Utilities.setPath(parent);
		Account user = accountJList.getSelectedValue();
		if(cover == null)
			return;

		IOActions.createMediaPath(user.getName(), Database.ACCOUNT);	//Oppretter en mappe med artistens navn.
		IOActions.copyImage(cover, user.getName(), Database.ACCOUNT); //kopierer valgt bilde til artistens mappe.
		Utilities.showInfo(parent, "Bilde oppdateres etter omstart.");
		repaintAccountView();

	}

	private void createNewAccount()	//--> Oppretter en ny bruker av programmet.
	{
		String username = usernameField.getText().trim();
		String pwd = passField.getText();
		String email = emailField.getText();
		String emailConf = emailConfirmField.getText();

		if(username.trim().equals(""))
		{
			Utilities.showError(parent, "Ugylig brukernavn.");
			return;
		}
		else if(pwd.length() < 3)
		{
			Utilities.showError(parent, "Ugylig passord, må være minimum 4 tegn.");
			return;
		}
		else if(!email.equals(emailConf))
		{
			Utilities.showError(parent, "Epost feltene samstemmer ikke.");
			return;
		}

		int accessLevel = 0;

		if(accessBox.getSelectedItem().equals(Account.ACCESS_NAMES[0]))
			accessLevel = Account.MANAGER;
		else if(accessBox.getSelectedItem().equals(Account.ACCESS_NAMES[1]))
			accessLevel = Account.EVENTPLANNER;
		else if(accessBox.getSelectedItem().equals(Account.ACCESS_NAMES[2]))
			accessLevel = Account.SALESMAN;
		else
		{
			Utilities.showError(parent, null);
			return;
		}

		Account user = new Account(accessLevel, nameField.getText(), username, email, adrField.getText(), telField.getText(), pwd);

		if(!accountList.addAccount(user))
		{
			Utilities.showError(parent, "Brukernavn finnes allerede, prøv igjen.");
			return;
		}

		Utilities.showInfo(parent, "Bruker registrert.");
		setActiveFields(false);
		updateAccountJList();
	}

	private void deactivate()	//--> Deaktiverer en konto, slik at den ikke synes i listen over brukere og ikke kan logges inn med.
	{
		if(accountJList.getSelectedValue().deactivate())
		{
			updateAccountJList();
			Utilities.showInfo(parent, "Bruker deaktivert.");
		}
		else
			Utilities.showError(parent, "Admin kan ikke deaktiveres.");
	}


// ------------------------------------------------------------------------------------------------------------------------------INDRE KLASSER

	//Hensikt: Lytter på valg i listene.
	private class ListListener implements ListSelectionListener
	{
		@Override
		public void valueChanged(ListSelectionEvent e)
		{
			if (e.getValueIsAdjusting() == false)
			{
				int selected = accountJList.getSelectedIndex();
				if (selected == -1)
				{
				}
				else
				{
					repaintAccountView();
				}
			}
		}
	}//End of ListListener class

	//Hensikt: Lytter på knapper lagt til i brukerpanelet
	private class Listener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(accountJList.getSelectedValue() == null)
				return;

			if( e.getSource() == newButton)
			{
				if(newButton.getText().equals("Lagre"))
				{
					createNewAccount();
				}
				else
					setActiveFields(true);
			}
			if( e.getSource() == deleteButton)
			{
				if(deleteButton.getText().equals("Avbryt"))
				{
					setActiveFields(false);
					setActiveEditFields(false);
				}
				else
					deactivate();
			}
			else if(e.getSource() == editButton)
			{
				if(editButton.getText().equals("Oppdater"))
				{
					editAccount();
				}
				else
				{
					if(accountJList.getSelectedValue().getAccess() == Account.SUPERADMIN)
					{
						if(parent.user.getAccess() != Account.SUPERADMIN)	//Kun superadmin kan redigere superadmin.
						{
							Utilities.showError(parent, "Ingen tilgang.");
							setActiveEditFields(false);
							return;
						}
					}

					setActiveEditFields(true);
				}
			}
			else if(e.getSource() == resetPassButton)
			{
				resetPwd();
			}
			else if(e.getSource() == changeCoverimage)
				addCoverImage();
		}
	}//End of Listener class

} // End of class AccountPanel

