/*
 *	Innehold:  Metoder/klasser i LoginWindow:
 *
 *				Konstruktør						--> Inneholder den grunnleggende borderlayouten og er delt inn i de forskjellige seksjonene med kommentarer.
 *				changeLook()					--> Restarter hovedviduet for å sette ny SystemLookAndFeel.
 *				logout()						--> Logger ut en bruker.
 *				logInAccoun() 					--> Logger inn den riktige brukeren og sender informasjonen om denne videre til EPlannerMainWindow.
 *		|class| CommandListener					--> Lytteklasse for feltene i LoginWindow.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 13.05.2012.
 */

package eplanner.windows;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import eplanner.users.*;
import eplanner.utilities.*;
import java.net.URL;

// Hensikt: Klasse som tegner Login - vinduet.
@SuppressWarnings("serial") // --> JFrame implementerer Serializable dette interfacet brukes ikke her og vi trenger derfor ikke denne advarselen.
public class LoginWindow extends JFrame
{
	//GUI variableer
	private AccountList accountList;
	private JTextField userName;
	private JPasswordField password;
	private JButton logIn, cancel;

	//Variabler for funksjon.
	private GridLayout centerGrid, westGrid;
	private Database database;
	private EPlannerMainWindow loggedIn;
	private Account toLogin;

	public LoginWindow() // --> Konstruktør
	{
		super("ePlanner");

		//Lytteklasse
		CommandListener listener = new CommandListener();

		//Henter data fra fil
		database = IOActions.readFromFile(new File("Log/Save.dta"));
		accountList = database.getAccountList();

		JPanel borderPanel = new JPanel(new BorderLayout());

//------------------------------------------------------------------------------------- PAGE START (North Panel)

		JPanel northPanel = new JPanel();
		JLabel info = new JLabel("ePlanner", JLabel.CENTER);
		JLabel infoPic = new JLabel(Utilities.getIcon("/Images/Icons/loginIcon.png"), JLabel.CENTER);

		northPanel.add(info);
		northPanel.add(infoPic);
		northPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5 ,0));
		borderPanel.add(northPanel, BorderLayout.PAGE_START);

//------------------------------------------------------------------------------------- LINE START (West Panel)
	   /*
		*	Her Legges hver JLabel inni hver sin flowlayout som igjen legges inni hver sin
		*	gridlayout som settes inn i LINE_START i BorderPanel-containeren.
		*	Dette gjøres slik at JLabels og tekstområde skal stå på linje.
		*	Dette gjelder også for Center-panelet men her med tekstfelt og i CENTER - posisjon.
		*/

		westGrid = new GridLayout(2, 1);
		JPanel westPanel = new JPanel(westGrid);


		//Brukernavn JLabel
		JLabel userLabel = new JLabel("Brukernavn: ");
		JPanel userLabelFlow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		userLabelFlow.add(userLabel);
		westPanel.add(userLabelFlow);

		//Passord JLabel
		JLabel passLabel = new JLabel("Passord: ");
		JPanel passLabelFlow = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		passLabelFlow.add(passLabel);
		westPanel.add(passLabelFlow);

		westPanel.setBorder(BorderFactory.createEmptyBorder(0, 18, 0 ,0));
		borderPanel.add(westPanel, BorderLayout.LINE_START);

//------------------------------------------------------------------------------------- Center Panel
		//Se kommentar i LINE_START.

		centerGrid = new GridLayout(2, 1);
		JPanel centerPanel = new JPanel(centerGrid);

		//Username tekstfelt
		userName = new JTextField(20);
		userName.addActionListener(listener);
		userName.setText("Admin");	// Settes av hensyn til sensor.
		JPanel userFlow = new JPanel(new FlowLayout(FlowLayout.LEFT));
		userFlow.add(userName);
		centerPanel.add(userFlow);

		//Passord tekstfelt
		password = new JPasswordField(20);
		password.addActionListener(listener);
		JPanel passFlow = new JPanel(new FlowLayout(FlowLayout.LEFT));
		passFlow.add(password);
		centerPanel.add(passFlow);


		borderPanel.add(centerPanel, BorderLayout.CENTER);
//------------------------------------------------------------------------------------- PAGE END (South Panel)

		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		logIn = new JButton("Logg inn");
		cancel = new JButton("Lukk");

		southPanel.add(logIn);
		southPanel.add(cancel);

		logIn.addActionListener(listener);
		cancel.addActionListener(listener);


		southPanel.setBorder(BorderFactory.createEmptyBorder(12, 0, 15 ,0));

		borderPanel.add(southPanel, BorderLayout.PAGE_END);
//------------------------------------------------------------------------------------- Default Attributes / Window placement

        setSize(400, 200);

		Utilities.setIcon(this);  // --> Her utnyttes metoden i Utilities-klassen for å sette et ikon på dette viduet.

        setLocationRelativeTo(null);

		setContentPane(borderPanel);

		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

//------------------------------------------------------------------------------------- Funkjsonsmetoder

	public void changeLook()// --> Restarter hovedviduet for å sette ny SystemLookAndFeel.
	{
		loggedIn.dispose();
		try
		{
			Thread.sleep(100);
		}
		catch(Exception e){IOActions.writeErrorLog(e);}
		loggedIn = new EPlannerMainWindow(toLogin, database, this);
	}

	public void logout()// --> Logger ut en bruker.
	{
		loggedIn.dispose();
		setVisible(true);
	}


	public void logInAccount()// --> Logger inn en bruker.
	{
		String username = userName.getText().trim();
		String pwd = new String(password.getPassword());

		if(username.equals(""))
		{
			toLogin = accountList.login("Admin", "1234", this);	//,
		}
		else
			toLogin = accountList.login(username, pwd, this);	//,

		if(toLogin != null)
		{
			loggedIn = new EPlannerMainWindow(toLogin, database, this);
			this.setVisible(false);
		}

	}

	private class CommandListener implements ActionListener // --> Lytteklasse
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == logIn)
			{
				logInAccount();
			}
			else if(e.getSource() == cancel)
				System.exit(0);
			else if(e.getSource() == userName)
			{
				password.requestFocusInWindow();
			}
			else if(e.getSource() == password)
			{
				logInAccount();
			}

		}
	}// Class CommandListener /END

}// Class LoginWindow /END