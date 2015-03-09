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
public class TicketInfoWindow extends JFrame
{
	private JTable recieptTable;
	private JTabbedPane ticketsTabs;
	private JPanel userInfoPanel, ticketsPanel, southPanel;
	private JLabel revenueLabel, ticketsSoldPercent;
	private EPlannerMainWindow parent;
	private Concert concert;
	public static final String[] COLUMN_NAMES = {"Dato", "Kunde", "Epost", "Kontonr", "Antall Bill.", "Pris", "Total"};

	public TicketInfoWindow(EPlannerMainWindow parent, Concert concert)
	{
		super("Billettinfo");

		this.parent = parent;
		this.concert = concert;


		//Setter preferanser
			setSize(600, 400);
			setVisible(true);
			setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			setLocationRelativeTo(parent);
			Utilities.setIcon(this);

		revenueLabel = new JLabel("Inntjent: " + concert.getRevenue() + " NOK.         ");

		String sold = "";

		try
		{
			double onePercent = (double) concert.getMaxTickets()/100.00;
			double soldPercent = (double) concert.getSoldTickets()/onePercent;
			sold = "Prosentvis solgte billetter: " + soldPercent + "%";
		}
		catch(ArithmeticException ae)
		{
			sold = "Ingen billetter solgt";
		}

		ticketsSoldPercent = new JLabel(sold);


		ticketsPanel = new JPanel(new BorderLayout());

		//---------------------------------------------------------CENTER
		ticketsTabs = new JTabbedPane();

		recieptTable = new JTable(concert.getReceipts() ,COLUMN_NAMES);
		JScrollPane scroll = new JScrollPane(recieptTable);

		ticketsTabs.addTab("Kvitteringer", scroll);

		ticketsPanel.add(ticketsTabs, BorderLayout.CENTER);


		//--------------------------------------------------------SOUTH
		southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		southPanel.add(revenueLabel);
		southPanel.add(ticketsSoldPercent);
		ticketsPanel.add(southPanel, BorderLayout.PAGE_END);


		setContentPane(ticketsPanel);
	}

}// End of class TicketInfoWindow


