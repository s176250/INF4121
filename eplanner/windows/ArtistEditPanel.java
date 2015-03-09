/*
 *	Innhold:  Metoder som redefinerer oppførselen til ArtistNewPanel, slik at du oppdaterer en artist istedenfor å lage ny.
 *
 *				createArtist()					--> Opprett artist metoden redefineres til å endre artisten isteden.
 *
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

//Hensikt: Bruke superklassen(ArtistNewPanel) for å lage et endre-artistpanel.
@SuppressWarnings("serial")
public class ArtistEditPanel extends ArtistNewPanel
{
	private EPlannerMainWindow grandParent;
	private Soloartist artist;
	private Person con, art;
	protected String[] genreArtist;

	public ArtistEditPanel(EPlannerMainWindow parent, ArtistList artistList, GenreList genreList, Soloartist artist)
	{
		super(parent, artistList, genreList);	//Tegne superklassens vindu
		grandParent = parent;
		this.artist = artist;


		//Avbryt knapp med anonym lytter som lukker Endre vinduet
		JButton cancelButton = new JButton("Avbryt");
		cancelButton.addActionListener(new ActionListener()
										   {
												public void actionPerformed(ActionEvent e)
												{
													grandParent.repaintArtistView();
												}
											});

		super.southPanel.add(cancelButton);	//Legg til avbryt knapp i sørpanelet.


		super.nameField.setEditable(false);	//Ikke lov å endre navn, så den settes til ikke-trykkbar

		//Hent ut info fra inkommen artist og fyll tekstfeltene.
			art = artist.getArtistPerson();
			super.nameField.setText(artist.getName());
			super.adrField.setText(art.getAdr());
			super.countryField.setText(art.getLand());
			super.telField.setText(art.getTel());
			super.emailField.setText(art.getEmail());

			con = artist.getContact();
			super.contactNameField.setText(con.getName());
			super.contactAdrField.setText(con.getAdr());
			super.contactCountryField.setText(con.getLand());
			super.contactTelField.setText(con.getTel());
			super.contactEmailField.setText(con.getEmail());

			super.webField.setText(artist.getWebsite() + "");
			super.instrumentField.setText(artist.getInstruments());
			super.coverImageField.setText(artist.getThumbImagePath());
			super.infoInput.setText(artist.getInfo());
			super.descInput.setText(artist.getDesc());

			genreArtist = artist.getGenres();
			super.genreJList.setSelections(genreArtist);

	}

	@Override
	protected void setGenreSelection()	//--> Denne metoden skal ikke brukes lenger.
	{
	}

	@Override
	protected void createArtist()	//--> Opprett artist metoden redefineres til å endre artisten isteden.
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
		artist.setInstruments(super.instrumentField.getText());

		art.setAdr(super.adrField.getText());
		art.setLand(super.countryField.getText());
		art.setTel(super.telField.getText());
		art.setEmail(super.emailField.getText());

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
		Utilities.showInfo(grandParent, "Artist Oppdatert!");
	}
}