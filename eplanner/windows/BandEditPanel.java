/*
 *	Innhold:  Metoder som oppretter og tegner elementene i artistPanel, og lytter på slett, ny og endre knapper.
 *
 *
 *				setSelectedMember()				--> Fyller tekstfeltene til "nytt bandmedlem"-feltene med infoen til valgt bandmedlem
 *				createBand()					--> Opprett band metoden redefineres til å endre bandet isteden.
 *				addBandmembers()				--> Legger til valgt bandmedlem til bandet.
 *				removeBandmembers() 			--> Fjerner valgt bandmedlem fra bandet.
 *
 *				Indre klasse
 *				ArtistListListener				--> Lytter på valg i bandmedlemlisten
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 13.05.2012.
 */

package eplanner.windows;


import eplanner.*;
import eplanner.utilities.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

//Hensikt: Bruke superklassen(BandNewPanel) for å lage et endre-bandpanel.
@SuppressWarnings("serial")
public class BandEditPanel extends BandNewPanel
{
	private EPlannerMainWindow grandParent;
	private Band artist;
	private Person con, art;
	protected String[] genreArtist;
	private ArtistListListener listListener;

	public BandEditPanel(EPlannerMainWindow parent, ArtistList artistList, GenreList genreList, Band artist)
	{
		super(parent, artistList, genreList);	//Tegne superklassens vindu
		grandParent = parent;
		this.artist = artist;


		//Avbryt knapp med anonym lytter som lukker Endre vinduet
		JButton cancelButton = new JButton("Avbryt");
		cancelButton.addActionListener(new ActionListener()
										   {
											   	@Override
												public void actionPerformed(ActionEvent e)
												{
													grandParent.repaintArtistView();
												}
											});
		super.southPanel.add(cancelButton);


		listListener = new ArtistListListener();

		super.bandnameField.setEditable(false);	//Ikke lov å endre navn, så den settes til ikke-trykkbar


		//Hent ut info fra inkomment band og fyll tekstfeltene.
		super.bandnameField.setText(artist.getName());

		con = artist.getContact();
		super.contactNameField.setText(con.getName());
		super.contactAdrField.setText(con.getAdr());
		super.contactCountryField.setText(con.getLand());
		super.contactTelField.setText(con.getTel());
		super.contactEmailField.setText(con.getEmail());

		super.webField.setText(artist.getWebsite() + "");
		super.nationalityField.setText(artist.getNationality());
		super.coverImageField.setText(artist.getThumbImagePath());
		super.infoInput.setText(artist.getInfo());
		super.descInput.setText(artist.getDesc());

		genreArtist = artist.getGenres();
		super.genreJList.setSelections(genreArtist);

		bandmembersList.addListSelectionListener(listListener);
		bandmembersList.addArray(artist.getBandmembersArray());	//Legger til bandets medlemmer i JListen over bandmedlemmer.

	}

	private void setSelectedMember()	//--> Fyller tekstfeltene til "nytt bandmedlem"-feltene med infoen til valgt bandmedlem
	{
		art = bandmembersList.getSelectedValue();
		super.nameField.setText(art.getName());
		super.adrField.setText(art.getAdr());
		super.countryField.setText(art.getLand());
		super.telField.setText(art.getTel());
		super.emailField.setText(art.getEmail());
	}

	@Override
	protected void setGenreSelection()	//--> Denne metoden skal ikke brukes lenger.
	{
	}

	@Override
	protected void createBand()		//--> Opprett band metoden redefineres til å endre bandet isteden.
	{
		try
		{
			URI webTmp = new URI(super.webField.getText());
			artist.setWebsite(webTmp);
		}
		catch(Exception e)
		{
			Utilities.showError(grandParent, "Ugyldig Nettadresse");
		}

		artist.setInfo(super.infoInput.getText());
		artist.setDesc(super.descInput.getText());
		artist.setNationality(super.nationalityField.getText());

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
		artist.modifyGenres(newGenres);

		String coverTmp = super.coverImageField.getText();
		if(!coverTmp.equals("") || !coverTmp.equals(artist.getThumbImagePath()) )
		{
			File coverArt = new File(coverTmp);
			if(!coverArt.exists())
				Utilities.showError(grandParent, "Fant ikke bildefil, setter standardbilde.");
			else
			{
				IOActions.copyImage(coverArt, artist.getName(), Database.ARTIST);
				Utilities.showInfo(grandParent, "Oppdatert bilde vises etter omstart.");
			}
		}
		grandParent.repaintArtistView();
		Utilities.showInfo(grandParent, "Band Oppdatert!");
	}

	@Override
	protected void addBandmembers()	//--> Legger til valgt bandmedlem til bandet.
	{
		if(nameField.getText().equals(""))
			return;

		Person member = new Person(nameField.getText(), adrField.getText(), countryField.getText(), telField.getText(), emailField.getText());
		bandmembersList.add(member);
		artist.addBandmember(member);
	}

	@Override
	protected void removeBandmembers() //--> Fjerner valgt bandmedlem fra bandet.
	{
		artist.removeBandmember(bandmembersList.getSelectedValue());
		bandmembersList.remove(bandmembersList.getSelectedIndex());

		super.nameField.setText("");
		super.adrField.setText("");
		super.countryField.setText("");
		super.telField.setText("");
		super.emailField.setText("");
	}

// ------------------------------------------------------------------------------------------------------------------------------INDRE KLASSER
	//Hensikt: Lytter på valg i bandmedlemlisten
	private class ArtistListListener implements ListSelectionListener
	{
		@Override
		public void valueChanged(ListSelectionEvent e)
		{
			if (e.getValueIsAdjusting() == false)
			{
				int selected = bandmembersList.getSelectedIndex();
				if (selected == -1)
				{
				}
				else
				{
					setSelectedMember();
				}
			}
		}
	}//End of ArtistListListener class
}//End of BandEditPanel