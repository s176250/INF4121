/*
 *	Innehold:  Metoder for behandlig av et soloartistobjekt:
 *
 *				get-metoder	                    --> For å hente ut datafelter.
 *				getThumbImagePath()				--> Returnerer coverbildes filsti hvis det finnes.
 *				getThumbImage()					--> Returnerer coverbilde hvis det finnes, hvis ikke returnerer et standard bilde.
 *				set-metode	                    --> For å sette datafelter.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 12.05.2012.
 */

package eplanner;

import eplanner.utilities.Utilities;
import java.io.File;
import javax.swing.ImageIcon;


// Hensikt: Klasse for oppretting av soloartist objekter, for å lagre info om en soloartist og utføre operasjoner/hente informasjon om de.
public class Soloartist extends Artist
{
	private static final long serialVersionUID = 1337L;
	private String instruments;							// Soloartistens instrument(er)
	private Person artist;								// Selve artisten

	//Konstruktør for ny soloartist
	public Soloartist(String website, String internalInfo, String description, Person contact,			//Superklasse variabler
					  String instruments, Person artist)				 													//Soloartistklasse variabler
	{
		super(website, internalInfo, description, contact);
		this.instruments = instruments;
		this.artist = artist;
	}

	//Konstruktør for midl artister i sortering.
	public Soloartist(String name)
	{
		super();
		artist = new Person(name);
	}


//-------------------------------------------------------------------------------------------------------- GET - metoder
	@Override
	public String getName()
	{
		return artist.getName();
	}

	public String getInstruments()
	{
		return instruments;
	}

	public Person getArtistPerson()
	{
		return artist;
	}

    @Override
	public String getThumbImagePath()	//--> Returnerer coverbildes filsti hvis det finnes.
	{
		File image = new File("Images/Artists/" + getName() + "/" + getName() + ".jpg");
		if(image.exists())
			return image.getAbsolutePath();
		return "";
	}

    @Override
	public ImageIcon getThumbImage()	//--> Returnerer coverbilde hvis det finnes, hvis ikke returnerer et standard bilde.
	{
		ImageIcon artistImage = Utilities.getImage("Images/Artists/" + getName() + "/" + getName() + ".jpg");

		if(artistImage == null)
		{
			return Utilities.getIcon("/Images/no_avatar.gif");
		}
		else
			return artistImage;
	}

//-------------------------------------------------------------------------------------------------------- SET - metoder
	public void setInstruments(String in)
	{
		instruments = in;
	}


//---------------------------------------------------------------- Andre metoder:
	@Override
	public String toString()
	{
		return getName();
	}

} // End of class Soloartist