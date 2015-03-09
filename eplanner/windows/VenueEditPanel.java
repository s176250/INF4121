/*
 *	Innehold: Kode for å opprette og drive panelet som lar deg endre Loakler<Venue> i programmet. Er underklasse av VenueNewPanel
 *
 *				Konstruktør						--> Setter tilstander på GUI-elementer og sender nødvendige variabler til super.
 *				setGenreSelection()				--> Overrides slik at ingen ting er valgt i JListen.
 *	 |Override| createConcert() 				--> Oppdaterer selve lokalet.
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
public class VenueEditPanel extends VenueNewPanel
{
	private EPlannerMainWindow grandParent;
	private Venue venue;
	private Person con;
	protected String[] genreVenue;

	public VenueEditPanel(EPlannerMainWindow parent, VenueList venueList, GenreList genreList, Venue venue)
	{
		//Sender variabler til super og tar vare på en peker til hovedvinduet. samt lagrer event variabel fra super.
			super(parent, venueList, genreList);
			grandParent = parent;
			this.venue = venue;


		JButton cancelButton = new JButton("Avbryt");// --> For å lukke vinduet uten å lagre, har sin egen anonyme lytter.
		cancelButton.addActionListener(new ActionListener()
										   {
												public void actionPerformed(ActionEvent e)
												{
													grandParent.repaintVenueView();
												}
											});
		super.southPanel.add(cancelButton);


		//Setter tilstanden på de forskjellige feltene slik at det som skal endres kan endres og visa versa.
			super.venueNameField.setEditable(false);
			super.venueNameField.setText(venue.getName());

			super.adrField.setText(venue.getAdr());
			super.countryField.setText(venue.getLand());
			super.telField.setText(venue.getTel());
			super.emailField.setText(venue.getEmail());
			super.maxCapField.setText(venue.getMaxCapacity() + "");
			super.defaultPrice.setText(venue.getStandardPrice() + "");
			super.rentPrice.setText(venue.getRentPrice() + "");
			super.isNumbered.setSelected(venue.getNumberedSeats());

			con = venue.getContact();
			super.contactNameField.setText(con.getName());
			super.contactAdrField.setText(con.getAdr());
			super.contactCountryField.setText(con.getLand());
			super.contactTelField.setText(con.getTel());
			super.contactEmailField.setText(con.getEmail());

			super.webField.setText(venue.getWebsite() + "");
			super.coverImageField.setText(venue.getThumbImagePath());
			super.infoInput.setText(venue.getInfo());
			super.descInput.setText(venue.getDesc());

			genreVenue = venue.getGenres();
			super.genreJList.setSelections(genreVenue);

	}

//------------------------------------------------------------------------------------- Overridede funksjonsmetoder

	@Override
	protected void createVenue()// --> Oppdaterer selve lokalet.
	{
		try
		{
			URI webTmp = new URI(super.webField.getText());
			venue.setWebsite(webTmp);
		}
		catch(Exception e)
		{
			Utilities.showError(grandParent, "Ugyldig Nettadresse");
		}

		BigDecimal stdTmp = null;
		BigDecimal rentTmp = null;
		int maxTmp = 0;

		try
		{
			maxTmp = Integer.parseInt(super.maxCapField.getText());
			stdTmp = new BigDecimal(super.defaultPrice.getText());
			rentTmp = new BigDecimal(super.rentPrice.getText());
		}
		catch(NumberFormatException nfe)
		{
			Utilities.showError(grandParent, "Skriv kun tall i tallfeltene!");
			return;
		}

		venue.setInfo(super.infoInput.getText());
		venue.setDesc(super.descInput.getText());


		venue.setAdr(super.adrField.getText());
		venue.setLand(super.countryField.getText());
		venue.setTel(super.telField.getText());
		venue.setEmail(super.emailField.getText());

		venue.setMaxCapacity(maxTmp);
		venue.setStandardPrice(stdTmp);
		venue.setRentPrice(rentTmp);
		venue.setNumberedSeats(super.isNumbered.isSelected());

		con.setName(super.contactNameField.getText());
		con.setAdr(super.contactAdrField.getText());
		con.setLand(super.contactCountryField.getText());
		con.setTel(super.contactTelField.getText());
		con.setEmail(super.contactEmailField.getText());


		int[] selectedGenres = super.genreJList.getSelectedIndices();

		ArrayList<String> newGenres = new ArrayList<>(selectedGenres.length);

		for(int i = 0; i < selectedGenres.length; i++)
		{
			newGenres.add(i, genreList.getGenreOnIndex(selectedGenres[i]));
		}
		venue.modifyGenres(newGenres);

		String coverTmp = super.coverImageField.getText();
		if(!coverTmp.equals("") || !coverTmp.equals(venue.getThumbImagePath()) )
		{
			File coverArt = new File(coverTmp);
			if(!coverArt.exists())
				Utilities.showError(grandParent, "Fant ikke bildefil, setter standardbilde.");
			else
			{
				IOActions.copyImage(coverArt, venue.getName(), Database.VENUE);
				Utilities.showInfo(grandParent, "Oppdatert bilde vises etter omstart.");
			}
		}
		grandParent.repaintVenueView();
		Utilities.showInfo(grandParent, "Lokale Oppdatert!");
	}

	@Override
	protected void setGenreSelection()// --> Overrides slik at ingen ting er valgt i JListen.
	{
	}
}