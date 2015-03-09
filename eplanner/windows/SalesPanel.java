/*
 *	Innhold:  Metoder for å vise salgsinfo og selge billetter til konserter:
 *
 *
 *					drawWestPanel()					--> Tegner det vestre panelet med liste over aktive konserter.
 *					displaySale()					--> Tegner et salgspanel med info fra valgt konsert.
 *					drawReciept()					--> Setter oppsumeringsfeltene.
 *					repaintSalesView()				--> Tegner salgsvisningen(centerpanel) på nytt.
 *					updateConcertJList()			--> Oppdaterer listen over aktive konserter.
 *					sellTickets()					--> Registrerer salg av billetter på valgt konsert.
 *
 *					Indre klasser:
 *					FocusLostListener				--> Lytte på salgsfeltene og så oppdatere summeringsfeltene.
 *					ConcertListListener				--> Lytte på listen med aktive konserter.
 *					SalesListener					--> Lytte på knappene.
 *
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 13.05.2012.
 */

package eplanner.windows;

import eplanner.Concert;
import eplanner.ConcertList;
import eplanner.utilities.*;
import eplanner.windows.components.ExtendedJList;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.Calendar;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

//Hensikt: Tegne et panel for salg, og håndere salg.
@SuppressWarnings("serial")
public class SalesPanel extends JPanel
{
	//Liste
	private ExtendedJList<Concert> concertJList;

	//Paneler
	private JPanel westPanel, topPanel, leftLeftTopGrid, leftRightTopGrid, rightLeftTopGrid, rightRightTopGrid, leftLeftBottomGrid, leftRightBottomGrid;
	private Box centerPanel, bottomBox, centerBox;

	//Knapper og Labels
	private JButton printButton, addButton;
	private JLabel mainArtistLabel, mainArtistLabelSmall, secArtistLabel, vNameLabel;

	//Tekstfelter og Tekstbokser
	private JTextField availableField, soldField, reservedField, standardField, mainArtistField, secArtistField, venueField, startTimeField, totalTicketsField, pricePerTicketField, totalPriceField;
	private JTextField nameField, emailField, accountField;
	private JTextArea reciept;

	//Lyttere
	private ConcertListListener listListener;
	private SalesListener actionListener;
	private FocusLostListener focusLostListener;

	//Annet
	private EPlannerMainWindow parent;
	private ConcertList concertList;

	//Konstruktør
	public SalesPanel(EPlannerMainWindow parent, ConcertList concertList)
	{
		super(new BorderLayout());

		//Setter preferanser
			setPreferredSize(new Dimension(810, 700));
			setBackground(Color.WHITE);
			setVisible(true);

		//Lagrer inkommne variabler
			this.concertList = concertList;
			this.parent = parent;

		//Lyttere
			actionListener = new SalesListener();
			listListener = new ConcertListListener();
			focusLostListener = new FocusLostListener();

		//Buttons
			addButton = new JButton("Regisrer Salg");
			addButton.addActionListener(actionListener);
			printButton = new JButton("Print kvittering");
			printButton.addActionListener(actionListener);

		//TextFields
			availableField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
			availableField.setEditable(false);
			soldField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
			soldField.setEditable(false);
			reservedField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
			reservedField.setEditable(false);
			standardField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
			standardField.setEditable(false);

			mainArtistField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
			mainArtistField.setEditable(false);
			secArtistField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
			secArtistField.setEditable(false);
			venueField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
			venueField.setEditable(false);
			startTimeField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
			startTimeField.setEditable(false);

			totalTicketsField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
			totalTicketsField.addFocusListener(focusLostListener);
			pricePerTicketField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
			pricePerTicketField.addFocusListener(focusLostListener);
			totalPriceField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
			totalPriceField.setEditable(false);

			nameField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
			emailField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);
			accountField = new JTextField(ConcertNewPanel.DEFAULT_FIELD_SIZE);


		//------------------------------------------------------------------------------------- LINE START (West Panel)

			drawWestPanel();	// JList med AKTIVE konserter.


		//------------------------------------------------------------------------------------- Center Panel - Tegnes først når man trykker på en konsert.
			centerPanel = new Box(BoxLayout.LINE_AXIS);

			add(centerPanel, BorderLayout.CENTER);


		//--------------------------------------------------------------------------------PAGE END (South Panel) --> knapper.
			JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			southPanel.setBackground(Color.WHITE);
			southPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,60));

			southPanel.add(addButton);
			southPanel.add(printButton);

			add(southPanel, BorderLayout.PAGE_END);
	}
	//Slutt konstruktør

// ------------------------------------------------------------------------------------------------------------------------------GUI Metoder:


	private void drawWestPanel()	//--> Tegner det vestre panelet med liste over aktive konserter.
	{
		westPanel = new JPanel(new GridLayout(1,1));

		JScrollPane listScroll = new JScrollPane(westPanel);
		listScroll.setBackground(Color.WHITE);
		listScroll.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

		concertJList = new ExtendedJList<>();
		updateConcertJList();

		concertJList.addListSelectionListener(listListener);
		concertJList.setFixedCellHeight(20);
		concertJList.setFixedCellWidth(190);
		concertJList.setFont(new Font("Arial",Font.PLAIN, 14 ));
		concertJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		concertJList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1), "Aktive Events"));

		westPanel.add(concertJList);

		add(listScroll, BorderLayout.LINE_START);
	}


	private void displaySale(Concert concert)	//-- Tegner et salgspanel med info fra valgt konsert.
	{
		centerBox = new Box(BoxLayout.PAGE_AXIS);
			centerBox.setMinimumSize(new Dimension(500, 500));
			centerBox.setMaximumSize(new Dimension(1000, 100000));
			centerBox.setBackground(EPlannerMainWindow.BASE_COLOR);
			centerBox.setBorder(BorderFactory.createEmptyBorder(10,0,15,15));

		centerPanel.add(centerBox);
		centerPanel.add(Box.createHorizontalGlue());

	//------------------------------------------------------------------------------------- Top Box	(overskrift billettsalg)
		topPanel = new JPanel();
		topPanel.setBackground(EPlannerMainWindow.BASE_COLOR);

		JLabel ticketTopLabel = new JLabel("Billettsalg");
		ticketTopLabel.setFont(new Font("Arial", Font.BOLD, 28));

		topPanel.add(ticketTopLabel);

		centerBox.add(topPanel);
	//------------------------------------------------------------------------------------- Middle Left Box (felter for billettinfo)
		Box middleBox = new Box(BoxLayout.LINE_AXIS);

		Box leftTopBox = new Box(BoxLayout.LINE_AXIS);

		leftTopBox.setMaximumSize(new Dimension(1000,150 ));
		leftTopBox.setPreferredSize(new Dimension(350, 150));
		leftTopBox.setBackground(EPlannerMainWindow.BASE_COLOR);
		leftTopBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Billettinfo"));


		leftLeftTopGrid = new JPanel(new GridLayout(4, 1));
		leftLeftTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

		leftRightTopGrid = new JPanel(new GridLayout(4, 1));
		leftRightTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

		//leftLeftTopGrid
		JPanel ticketInfoLabelFlow1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		ticketInfoLabelFlow1.setBackground(EPlannerMainWindow.BASE_COLOR);
		ticketInfoLabelFlow1.add(new JLabel("Ledige Bill.:"));
		JPanel ticketInfoLabelFlow2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		ticketInfoLabelFlow2.setBackground(EPlannerMainWindow.BASE_COLOR);
		ticketInfoLabelFlow2.add(new JLabel("Solgte Bill.:"));
		JPanel ticketInfoLabelFlow3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		ticketInfoLabelFlow3.setBackground(EPlannerMainWindow.BASE_COLOR);
		ticketInfoLabelFlow3.add(new JLabel("Ledig Res. Bill.:"));
		JPanel ticketInfoLabelFlow4 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		ticketInfoLabelFlow4.setBackground(EPlannerMainWindow.BASE_COLOR);
		ticketInfoLabelFlow4.add(new JLabel("Veil. Bill. pris:"));


		//leftRightTopGrid
		JPanel ticketInfoFlow1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ticketInfoFlow1.setBackground(EPlannerMainWindow.BASE_COLOR);
		ticketInfoFlow1.add(availableField);
		availableField.setText(concert.getAvailableTickets() + "");
		JPanel ticketInfoFlow2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ticketInfoFlow2.setBackground(EPlannerMainWindow.BASE_COLOR);
		ticketInfoFlow2.add(soldField);
		soldField.setText(concert.getSoldTickets() + "");
		JPanel ticketInfoFlow3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ticketInfoFlow3.setBackground(EPlannerMainWindow.BASE_COLOR);
		ticketInfoFlow3.add(reservedField);
		reservedField.setText(concert.getAvailableReserved() + "");
		JPanel ticketInfoFlow4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ticketInfoFlow4.setBackground(EPlannerMainWindow.BASE_COLOR);
		ticketInfoFlow4.add(standardField);
		standardField.setText(concert.getDefaultPrice() + "");

		//Adding Left
		leftLeftTopGrid.add(ticketInfoLabelFlow1);
		leftLeftTopGrid.add(ticketInfoLabelFlow2);
		leftLeftTopGrid.add(ticketInfoLabelFlow3);
		leftLeftTopGrid.add(ticketInfoLabelFlow4);

		//Adding Right
		leftRightTopGrid.add(ticketInfoFlow1);
		leftRightTopGrid.add(ticketInfoFlow2);
		leftRightTopGrid.add(ticketInfoFlow3);
		leftRightTopGrid.add(ticketInfoFlow4);


		leftTopBox.add(leftLeftTopGrid);
		leftTopBox.add(leftRightTopGrid);

		middleBox.add(leftTopBox);

		//------------------------------------------------------------------------------------- Middle Right Box (Felter for eventinfo)

		Box rightTopBox = new Box(BoxLayout.LINE_AXIS);

		rightTopBox.setMaximumSize(new Dimension(1000,150 ));
		rightTopBox.setPreferredSize(new Dimension(350, 150));
		rightTopBox.setBackground(EPlannerMainWindow.BASE_COLOR);
		rightTopBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Eventinfo"));


		rightLeftTopGrid = new JPanel(new GridLayout(4, 1));
		rightLeftTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

		rightRightTopGrid = new JPanel(new GridLayout(4, 1));
		rightRightTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

		//leftLeftTopGrid
		JPanel concertInfoLabelFlow1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		concertInfoLabelFlow1.setBackground(EPlannerMainWindow.BASE_COLOR);
		concertInfoLabelFlow1.add(new JLabel("Hovedartist:"));
		JPanel concertInfoLabelFlow2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		concertInfoLabelFlow2.setBackground(EPlannerMainWindow.BASE_COLOR);
		concertInfoLabelFlow2.add(new JLabel("Andre artister:"));
		JPanel concertInfoLabelFlow3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		concertInfoLabelFlow3.setBackground(EPlannerMainWindow.BASE_COLOR);
		concertInfoLabelFlow3.add(new JLabel("Lokale:"));
		JPanel concertInfoLabelFlow4 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		concertInfoLabelFlow4.setBackground(EPlannerMainWindow.BASE_COLOR);
		concertInfoLabelFlow4.add(new JLabel("Dato/Tid"));

		//leftRightTopGrid
		JPanel concertInfoFlow1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		concertInfoFlow1.setBackground(EPlannerMainWindow.BASE_COLOR);
		concertInfoFlow1.add(mainArtistField);
		mainArtistField.setText(concert.getArtist().getName());
		JPanel concertInfoFlow2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		concertInfoFlow2.setBackground(EPlannerMainWindow.BASE_COLOR);
		concertInfoFlow2.add(secArtistField);
		secArtistField.setText(concert.getSecArtistNames());
		secArtistField.setCaretPosition(0);
		JPanel concertInfoFlow3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		concertInfoFlow3.setBackground(EPlannerMainWindow.BASE_COLOR);
		concertInfoFlow3.add(venueField);
		venueField.setText(concert.getVenue().getName());
		JPanel concertInfoFlow4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		concertInfoFlow4.setBackground(EPlannerMainWindow.BASE_COLOR);
		concertInfoFlow4.add(startTimeField);
		startTimeField.setText(Utilities.getStringDate(concert.getDate()) + " " + concert.getStartTime());

		//Adding Left
		rightLeftTopGrid.add(concertInfoLabelFlow1);
		rightLeftTopGrid.add(concertInfoLabelFlow2);
		rightLeftTopGrid.add(concertInfoLabelFlow3);
		rightLeftTopGrid.add(concertInfoLabelFlow4);

		//Adding Right
		rightRightTopGrid.add(concertInfoFlow1);
		rightRightTopGrid.add(concertInfoFlow2);
		rightRightTopGrid.add(concertInfoFlow3);
		rightRightTopGrid.add(concertInfoFlow4);


		rightTopBox.add(rightLeftTopGrid);
		rightTopBox.add(rightRightTopGrid);

		middleBox.add(rightTopBox);

		centerBox.add(middleBox);

	//------------------------------------------------------------------------------------- Bottom Box (Salgsfelter og kvitteringspanel)

		bottomBox = new Box(BoxLayout.LINE_AXIS);

		Border tileBM = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Salg");
		Border emptyBM = BorderFactory.createEmptyBorder(10,10,10,10);
		bottomBox.setBorder(BorderFactory.createCompoundBorder(tileBM, emptyBM));

		//------------------------------------------------------------------------------------- Bottom Box Left (Salgsfelter)
			Box leftBottomOuterBox = new Box(BoxLayout.PAGE_AXIS);
			Box leftBottomBox = new Box(BoxLayout.LINE_AXIS);

			leftBottomBox.setMaximumSize(new Dimension(1000,180 ));
			leftBottomBox.setPreferredSize(new Dimension(350, 180));
			leftBottomBox.setBackground(EPlannerMainWindow.BASE_COLOR);
			leftBottomBox.setBorder(BorderFactory.createEmptyBorder(10,0,0,0));

			leftLeftBottomGrid = new JPanel(new GridLayout(6, 1));
			leftLeftBottomGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

			leftRightBottomGrid = new JPanel(new GridLayout(6, 1));
			leftRightBottomGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

			//leftLeftTopGrid sales
			JPanel salesLabelFlow0 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			salesLabelFlow0.setBackground(EPlannerMainWindow.BASE_COLOR);
			salesLabelFlow0.add(new JLabel("Fullt navn:"));
			JPanel salesLabelFlow1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			salesLabelFlow1.setBackground(EPlannerMainWindow.BASE_COLOR);
			salesLabelFlow1.add(new JLabel("Epost:"));
			JPanel salesLabelFlow2 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			salesLabelFlow2.setBackground(EPlannerMainWindow.BASE_COLOR);
			salesLabelFlow2.add(new JLabel("Kontonr.:"));


			JPanel salesLabelFlow3 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			salesLabelFlow3.setBackground(EPlannerMainWindow.BASE_COLOR);
			salesLabelFlow3.add(new JLabel("Antall billetter:"));
			JPanel salesLabelFlow4 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			salesLabelFlow4.setBackground(EPlannerMainWindow.BASE_COLOR);
			salesLabelFlow4.add(new JLabel("Pris per Billett:"));
			JPanel salesLabelFlow5 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			salesLabelFlow5.setBackground(EPlannerMainWindow.BASE_COLOR);
			salesLabelFlow5.add(new JLabel("Totalt:"));

			//leftRightTopGrid
			JPanel salesFlow0 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			salesFlow0.setBackground(EPlannerMainWindow.BASE_COLOR);
			salesFlow0.add(nameField);
			totalTicketsField.setText("");
			JPanel salesFlow1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			salesFlow1.setBackground(EPlannerMainWindow.BASE_COLOR);
			salesFlow1.add(emailField);
			totalTicketsField.setText("");
			JPanel salesFlow2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			salesFlow2.setBackground(EPlannerMainWindow.BASE_COLOR);
			salesFlow2.add(accountField);
			totalTicketsField.setText("");
			JPanel salesFlow3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			salesFlow3.setBackground(EPlannerMainWindow.BASE_COLOR);
			salesFlow3.add(totalTicketsField);
			totalTicketsField.setText("0");
			JPanel salesFlow4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			salesFlow4.setBackground(EPlannerMainWindow.BASE_COLOR);
			salesFlow4.add(pricePerTicketField);
			pricePerTicketField.setText(concert.getDefaultPrice() + "");
			JPanel salesFlow5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
			salesFlow5.setBackground(EPlannerMainWindow.BASE_COLOR);
			salesFlow5.add(totalPriceField);
			totalPriceField.setText("0");


			//Adding Left
			leftLeftBottomGrid.add(salesLabelFlow0);
			leftLeftBottomGrid.add(salesLabelFlow1);
			leftLeftBottomGrid.add(salesLabelFlow2);
			leftLeftBottomGrid.add(salesLabelFlow3);
			leftLeftBottomGrid.add(salesLabelFlow4);
			leftLeftBottomGrid.add(salesLabelFlow5);

			//Adding Right
			leftRightBottomGrid.add(salesFlow0);
			leftRightBottomGrid.add(salesFlow1);
			leftRightBottomGrid.add(salesFlow2);
			leftRightBottomGrid.add(salesFlow3);
			leftRightBottomGrid.add(salesFlow4);
			leftRightBottomGrid.add(salesFlow5);


			leftBottomBox.add(leftLeftBottomGrid);
			leftBottomBox.add(leftRightBottomGrid);

			leftBottomOuterBox.add(leftBottomBox);
			leftBottomOuterBox.add(Box.createVerticalGlue());
			bottomBox.add(leftBottomOuterBox);


		//------------------------------------------------------------------------------------- Bottom Box Right (Salg Oppsummeringsinfo)

		Box rightBottomBox = new Box(BoxLayout.LINE_AXIS);

		Border tileBR = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Kvittering");
		Border emptyBR = BorderFactory.createEmptyBorder(10,10,5,10);
		Border comBR = BorderFactory.createCompoundBorder(tileBR, emptyBR);
		Border lineBR = BorderFactory.createLineBorder(Color.black, 1);

		rightBottomBox.setBorder(BorderFactory.createCompoundBorder(comBR, lineBR));

		rightBottomBox.setBackground(EPlannerMainWindow.BASE_COLOR);

		reciept = new JTextArea(20,20);
		reciept.setFont(new Font("Lucida Console", Font.PLAIN, 12));
		reciept.setEditable(false);

		drawReciept(concert);

		rightBottomBox.add(reciept);
		bottomBox.add(rightBottomBox);

		centerBox.add(bottomBox);
		centerBox.add(Box.createVerticalGlue());
	} //Slutt displaySale()


	private void drawReciept(Concert concert)	//--> Setter oppsumeringsfeltene.
	{
		reciept.setText("\n  " + concert.getName() + "  \n");
		reciept.append("  Eventdato: " + Utilities.getStringDate(concert.getDate()) + "  \n");
		reciept.append("  Lokale: " + concert.getVenue().getName() + "  \n");
		reciept.append("  Utstedt: " + Utilities.getStringDate(Calendar.getInstance()) + "\n");
		reciept.append("  Dørene åpner: " + concert.getStartTime() + "\n\n\n");

		reciept.append("  Pris per billett: " + pricePerTicketField.getText() + "  \n");
		reciept.append("  Antall billetter: " + totalTicketsField.getText() + "  \n\n");
		reciept.append("  Totalt: " + totalPriceField.getText() + " NOK \n");
	}


// ------------------------------------------------------------------------------------------------------------------------------Funksjons Metoder:

	public void repaintSalesView()	//--> Tegner salgsvisningen(centerpanel) på nytt.
	{
		centerPanel.removeAll();
		displaySale(concertJList.getSelectedValue());
		centerPanel.getRootPane().revalidate();
	}

	public void updateConcertJList()	//--> Oppdaterer listen over aktive konserter.
	{
		concertJList.setContent(concertList.getActiveConcertsArray());
	}

	private void sellTickets()	//--> Registrerer salg av billetter på valgt konsert.
	{
		Concert con = concertJList.getSelectedValue();

		if(con == null)
			return;

		int tickets = 0;
		BigDecimal price = null;
		String name = nameField.getText();
		String email = emailField.getText();
		String account = accountField.getText();


		if(name.length() < 1 || email.length() < 5)
		{
			Utilities.showError(parent, "Fyll inn gyldig navn og epost.");
			return;
		}
		try
		{
			tickets = Integer.parseInt(totalTicketsField.getText());
			price = new BigDecimal(pricePerTicketField.getText());
			Long.parseLong(account);
		}
		catch(NumberFormatException nfe)
		{
			Utilities.showError(parent, "Fyll inn kun tall i tall-felt.");
			return;
		}

		if(tickets < 1)
		{
			Utilities.showError(parent, "Antall billetter kan ikke være mindre enn en.");
			return;
		}
		if(price.intValue() < 0)
		{
			Utilities.showError(parent, "Negativ pris ikke akseptert.");
			return;
		}
		if(con.sellTickets(tickets, price, name, email, account))
		{
			Utilities.showInfo(parent, "Billettsalg registrert.");

			availableField.setText(con.getAvailableTickets() + "");
			soldField.setText(con.getSoldTickets() + "");
			reservedField.setText(con.getAvailableReserved() + "");
			totalTicketsField.setText("0");
			totalPriceField.setText("0");
			nameField.setText("");
			emailField.setText("");
			accountField.setText("");
		}
		else
			Utilities.showError(parent, "Ikke nok ledige billetter.");
	}


// ------------------------------------------------------------------------------------------------------------------------------INDRE KLASSER

	//Hensikt: Lytte på salgsfeltene og så oppdatere summeringsfeltene.
	private class FocusLostListener implements FocusListener
	{
		@Override
		public void focusGained(FocusEvent e)
		{
		}

		@Override
		public void focusLost(FocusEvent e)
		{
			int tickets = 0;
			BigDecimal price = new BigDecimal(0);

			try
			{
				tickets = Integer.parseInt(totalTicketsField.getText());
			}
			catch(NumberFormatException nfe)
			{
				totalTicketsField.setText("0");
			}

			try
			{
				price = new BigDecimal(pricePerTicketField.getText());
			}
			catch(NumberFormatException nfe)
			{
				pricePerTicketField.setText("0");
			}

			totalPriceField.setText( (price.multiply(new BigDecimal(tickets))) + "");
			drawReciept(concertJList.getSelectedValue());
		}
	}//End of FocusLostListener class


	//Hensikt: Lytte på listen med aktive konserter.
	private class ConcertListListener implements ListSelectionListener
	{
		@Override
		public void valueChanged(ListSelectionEvent e)
		{
			if (e.getValueIsAdjusting() == false)
			{
				int selected = concertJList.getSelectedIndex();
				if (selected == -1)
				{
				}
				else
				{
					repaintSalesView();
				}
			}
		}
	}//End of ConcertListListener class


	//Hensikt: Lytte på knappene.
	private class SalesListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if( e.getSource() == addButton)
			{
				sellTickets();
			}
			else if(e.getSource() == printButton)
			{
				try
				{
					reciept.print();
				}
				catch(java.awt.print.PrinterException pe)
				{
					IOActions.writeErrorLog(pe);
				}
				catch(NullPointerException npe)
				{
				}
			}
		}
	}//End of SalesListener class

} // End of SalesPanel class



