/*
 *	Innehold:  Metoder for behandlig av et bandobjekt:
 *
 *				get-metoder	                    --> For å hente ut datafelter.
 * 				getBandmembersArray()			--> Returnerer bandmedlemmer i en array
 *				getThumbImagePath()				--> Returnerer coverbildes filsti hvis det finnes.
 *				getThumbImage()					--> Returnerer coverbilde hvis det finnes, hvis ikke returnerer et standard bilde.
 *				set-metoder	                    --> For å sette datafelter.
 *				addBandmember()					--> Legger til et bandmedlem
 *				removeBandmember()				--> Fjerner et bandmedlem
 *				bandmembersToString()			--> Returnerer en string med navnene til alle i bandet.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 12.05.2012.
 */

package eplanner;

import eplanner.utilities.Utilities;
import java.io.File;
import java.util.LinkedList;
import javax.swing.ImageIcon;

// Hensikt: Klasse for oppretting av band objekter, for å lagre info om et band og utføre operasjoner/hente informasjon om de.
public class Band extends Artist
{
	private static final long serialVersionUID = 1337L;
	private String bandname, nationality;				//Bandnavn og nasjonalitet
	private LinkedList<Person> bandmembers;				//Bandets medlemmer

	//Konstruktør for nytt band
	public Band(String website, String internalInfo, String description, Person contact,			//Superklasse variabler
				String bandname, String nationality, LinkedList<Person> bandmembers)								//Bandklasse variabler
	{
		super(website, internalInfo, description, contact);
		this.bandname = bandname;
		this.nationality = nationality;
		this.bandmembers = bandmembers;
	}

	//Konstruktør for midl artister i sortering.
	public Band(String name)
	{
		super();
		bandname = name;
	}


//-------------------------------------------------------------------------------------------------------- GET - metoder
	@Override
	public String getName()
	{
		return bandname;
	}

	public String getNationality()
	{
		return nationality;
	}

	public LinkedList<Person> getBandmembers()
	{
		return bandmembers;
	}

	public Person[] getBandmembersArray()	//--> Returnerer bandmedlemmer i en array
	{
		Object[] tmp = bandmembers.toArray();
		Person[] tmpPers = new Person[tmp.length];

		for(int i = 0; i < tmp.length; i++)
		{
			tmpPers[i] = (Person) tmp[i];
		}

		return tmpPers;
	}

    @Override
	public ImageIcon getThumbImage()	//--> Returnerer coverbildes filsti hvis det finnes.
	{
		ImageIcon artistImage = Utilities.getImage("Images/Artists/" + getName() + "/" + getName() + ".jpg");

		if(artistImage == null)
		{
			return Utilities.getIcon("/Images/no_avatar.gif");
		}
		else
			return artistImage;
	}

    @Override
	public String getThumbImagePath()	//--> Returnerer coverbilde hvis det finnes, hvis ikke returnerer et standard bilde.
	{
		File image = new File("Images/Artists/" + getName() + "/" + getName() + ".jpg");
		if(image.exists())
			return image.getAbsolutePath();
		return "";
	}

//-------------------------------------------------------------------------------------------------------- SET/ADD/REMOVE - metoder

	public String setName()
	{
		return bandname;
	}

	public void setNationality(String nationality)
	{
		this.nationality = nationality;
	}

	public boolean addBandmember(Person p)	//--> Legger til et bandmedlem
	{
		return bandmembers.add(p);
	}

	public boolean removeBandmember(Person p)	//--> Fjerner et bandmedlem
	{
		return bandmembers.remove(p);
	}

	public String bandmembersToString()	//-->Returnerer en string med navnene til alle i bandet.
	{
		String output = "";

		int i = bandmembers.size();

		for(Person p : bandmembers)
		{
			if(i == 1)
			{
				output += p.getName();
				continue;
			}
			output += p.getName() + ", ";
			i--;
		}
		return output;
	}

    @Override
	public String toString()
	{
		return getName();
	}

} // End of class Band