/*
 *	Innehold: Kode for å opprette og drive panelet som lar deg endre Events<Concert> i programmet. Er underklasse av ConcertNewPanel
 *
 *				Konstruktør						--> Setter tilstander på GUI-elementer og sender nødvendige variabler til super.
 *				extendSummaryPanel()			--> Utvider oppsummeringen slik at man får informasjon om hvor mange bill.
 *													som er solgt osv. slik at man kan endre verdiene innenfor gyldige rammer
 *				setEventTime()					--> Setter tiden til den som allerede er lagret i event variabelen.
 *	 |Override| checkVenueOccupied()			--> Sjekker om lokalet er opptatt på den datoen brukeren har valgt. Setter teksten i JTextFieldet rød i så fall.
 *	 |Override| createConcert() 				--> Oppdaterer selve eventen.
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
import java.net.*;
import java.util.*;
import java.math.BigDecimal;


//--------------------------------- Eplanner import-setninger
import eplanner.users.*;
import eplanner.utilities.*;
import eplanner.windows.components.*;
import eplanner.*;

//	Hensikt: Å vise nødvendig GUI for å kunne endre en Event, og å sette inn tekst i alle felter som tidligere er fyllt ut.
@SuppressWarnings("serial")
public class ConcertEditPanel extends ConcertNewPanel
{
	private EPlannerMainWindow grandParent;
	private Concert concert;

	public ConcertEditPanel(EPlannerMainWindow parent, VenueList venueList, ArtistList artistList, ConcertList concertList, Concert concert) //--> Konstruktør
	{
		//Sender variabler til super og tar vare på en peker til hovedvinduet. samt lagrer event variabel fra super.
			super(parent, venueList, artistList, concertList);
			grandParent = parent;
			this.concert = concert;


		JButton cancelButton = new JButton("Avbryt");// --> For å lukke vinduet uten å lagre, har sin egen anonyme lytter.
		cancelButton.addActionListener(new ActionListener()
										   {
												public void actionPerformed(ActionEvent e)
												{
													grandParent.repaintConcertView();
												}
											});
		super.southPanel.add(cancelButton);


		//Setter tilstanden på de forskjellige feltene slik at det som skal endres kan endres og visa versa.

			setEventTime();

			super.concertNameField.setEditable(false);
			super.concertNameField.setText(concert.getName());

			super.ticketField.setText(concert.getMaxTickets() + "");
			super.defaultPriceField.setText(concert.getDefaultPrice() + "");
			super.reservedField.setText(concert.getReservedTickets() + "");

			super.descInput.setText(concert.getDescription());

			super.venueJList.setSelection(concert.getVenue());

			super.mainArtistJList.setSelection(concert.getArtist());
			super.secondaryArtistJList.setSelection(concert.getSecArtist());

			super.coverImageField.setText(concert.getThumbImagePath());

			super.summaryTicketField.setText(super.ticketField.getText());
			super.summaryPriceField.setText(super.defaultPriceField.getText());
			super.summaryResField.setText(super.reservedField.getText());
			super.summaryNameField.setText(super.concertNameField.getText());

			extendSummaryPanel();
	}

	//Utvider oppsummeringen slik at man får informasjon om hvor mange bill. som er solgt osv. slik at man kan endre verdiene innenfor gyldige rammer
	private void extendSummaryPanel()
	{
		super.summaryBox.setMinimumSize(new Dimension(250, 280));
		super.summaryBox.setMaximumSize(new Dimension(500, 280));
		super.summaryBox.setPreferredSize(new Dimension(250, 280));
		super.summaryLeftGridLayout.setRows(11);
		super.summaryRightGridLayout.setRows(11);

		JPanel summaryLeftFlow9 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		summaryLeftFlow9.setBackground(EPlannerMainWindow.BASE_COLOR);
		summaryLeftFlow9.add(new JLabel("Solgte bill."));
		JPanel summaryLeftFlow10 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		summaryLeftFlow10.setBackground(EPlannerMainWindow.BASE_COLOR);
		summaryLeftFlow10.add(new JLabel("Utgitt res.:"));

		super.summaryLeftGrid.add(summaryLeftFlow9);
		super.summaryLeftGrid.add(summaryLeftFlow10);


		JPanel summaryRightFlow9 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		summaryRightFlow9.setBackground(EPlannerMainWindow.BASE_COLOR);
			JTextField soldTicketsField = new JTextField(concert.getSoldTickets() + "", ConcertNewPanel.DEFAULT_FIELD_SIZE+3);
			soldTicketsField.setEditable(false);
		summaryRightFlow9.add(soldTicketsField);

		JPanel summaryRightFlow10 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		summaryRightFlow10.setBackground(EPlannerMainWindow.BASE_COLOR);
			JTextField soldDistributedTicketsField = new JTextField(concert.getDistributedReservedTickets() + "", ConcertNewPanel.DEFAULT_FIELD_SIZE+3);
			soldDistributedTicketsField.setEditable(false);
		summaryRightFlow10.add(soldDistributedTicketsField);

		super.summaryRightGrid.add(summaryRightFlow9);
		super.summaryRightGrid.add(summaryRightFlow10);
	}

//------------------------------------------------------------------------------------- Funksjonsmetoder

	protected void setEventTime()// --> Setter tiden til den som allerede er lagret i event variabelen.
	{
		super.eventTime = Utilities.getTimeDate(concert.getDate(), concert.getStartTime());
		super.setListenerActive(false);

		yearSpinner.setValue(super.eventTime.get(Calendar.YEAR));
		monthSpinner.setValue(super.eventTime.get(Calendar.MONTH)+1);
		daySpinner.setValue(super.eventTime.get(Calendar.DAY_OF_MONTH));
		hourSpinner.setValue(super.eventTime.get(Calendar.HOUR_OF_DAY));
		minSpinner.setValue(super.eventTime.get(Calendar.MINUTE));

		super.setListenerActive(true);
		super.updateDateFields();
	}

//------------------------------------------------------------------------------------- Overridede funksjonsmetoder
	@Override
	protected void checkVenueOccupied()// --> Sjekker om lokalet er opptatt på den datoen brukeren har valgt. Setter teksten i JTextFieldet rød i så fall.
	{
		Venue current = venueJList.getSelectedValue();
		if(current != null)
		{
			if(current.getOccupied(eventTime) && !current.containsConcert(concert))
			{
				summaryVenueField.setText("Ikke ledig");
				summaryVenueField.setForeground(Color.RED);
				addButton.setEnabled(false);
			}
			else
			{
				summaryVenueField.setText(venueJList.getSelectedValue().getName());
				summaryVenueField.setForeground(Color.BLACK);
				addButton.setEnabled(true);
			}
		}
		super.updateArtistJList();
	}

	@Override
	protected void createConcert()// --> Oppdaterer selve eventen.
	{
		int maxTick = 0;
		int resTick = 0;
		BigDecimal price = null;
		int maxReserved = (concert.getAvailableTickets() + concert.getAvailableReserved());

		try
		{
			maxTick = Integer.parseInt(super.summaryTicketField.getText());
			price =	new BigDecimal(super.summaryPriceField.getText());
			resTick = Integer.parseInt(super.summaryResField.getText());
		}
		catch(Exception ex)
		{
			Utilities.showError(grandParent, "Skriv kun tall i tallfeltene.");
			return;
		}

		if(maxTick > concert.getVenue().getMaxCapacity() || resTick > maxReserved)
		{
			Utilities.showError(grandParent, "Totalt antall billetter kan ikke overstige lokalets kapasitet: " + concert.getVenue().getMaxCapacity() + "\nReserverte billetter kan ikke overstige maks antall billetter til salgs." );
			return;
		}

		if(!concert.setMaxTickets(maxTick))
		{
			Utilities.showError(grandParent, "Ugyldig maksverdi.");
			return;
		}

		concert.setReservedTickets(resTick);
		concert.setDefaultPrice(price);

		concert.setDate(super.eventTime);
		concert.setDescription(super.descInput.getText());

		concert.setVenue(super.venueJList.getSelectedValue(), venueList);
		concert.setArtist(super.mainArtistJList.getSelectedValue(), artistList);
		concert.setSecArtist(super.secondaryArtistJList.getSelectedValuesList(), artistList);


		String coverTmp = super.coverImageField.getText();
		if(!(coverTmp.equals("") || coverTmp.equals(concert.getThumbImagePath())) )
		{
			File coverArt = new File(coverTmp);
			if(!coverArt.exists())
				Utilities.showError(grandParent, "Fant ikke bildefil, setter standardbilde.");
			else
			{
				IOActions.copyImage(coverArt, concert.getName(), Database.CONCERT);
				Utilities.showInfo(grandParent, "Oppdatert bilde vises etter omstart.");
			}
		}
		grandParent.repaintConcertView();
		Utilities.showInfo(grandParent, "Event Oppdatert!");
	}
} // ConcertEditPanel /End