/*
 *	Innehold:  Metoder som er felles for alle underklasser og metoder som må implemteres:
 *
 *				get-metoder	                    --> For å hente ut datafelter.
 *				getGenres()						--> Returnerer artistens sjangere i en array.
 *				genresToString()				--> Returner et string representasjon av artistens sjangere.
 *				getConcertArray()				--> Returnerer artisten konserter i en array.
 *				getConcertNames()				--> Returner et string representasjon av artistens konserter.
 *				getActiveConcerts()				--> Returnerer artistens aktive konserter.
 *				getActive()						--> Returnerer antallet aktive konserter artisten har.
 *				set-metoder	                    --> For å sette datafelter.
 *				modifyGenres()					--> Setter artistens sjangere til inkommen sjanger liste.
 *				hasGenre()						--> Sjekker om artisten har angitt sjanger
 *				addConcert() 					--> Legger til concert til artisten
 *				removeConcert() 				--> Fjerner concert fra artisten
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 12.05.2012.
 */

package eplanner;

import eplanner.comparators.*;
import eplanner.utilities.IOActions;
import java.io.Serializable;
import java.net.URI;
import java.util.*;
import javax.swing.ImageIcon;

// Hensikt: Klasse for lagring av Band og Soloartister, for å lagre all felles info og metoder om de.
public abstract class Artist implements NameComparable, Serializable, HasContact
{
	private static final long serialVersionUID = 1337L;
	private URI website;								// Nettadresse
	private Person contact;								// Artistens kontaktperson
	private String internalInfo, description;			// interninfo og beskrivelse.
	private ArrayList<String> genres;					// Artistens sjangere.
	private TreeSet<Concert> concerts;					// Artistens konserter.

	// Primær Konstruktør
	public Artist(String web, String internalInfo, String description, Person contact)
	{
		try{
			this.website = new URI(web);
		}
		catch(Exception use){IOActions.writeErrorLog(use);}

		this.internalInfo = internalInfo;
		this.description = description;
		this.contact = contact;
		concerts = new TreeSet<>(new NameComparator<Concert>());
	}

	// Konstruktør for midl. artister.
	public Artist()
	{
	}

//-------------------------------------------------------------------------------------------------------- GET - metoder
    @Override
	public abstract String getName();


	public abstract ImageIcon getThumbImage();


	public abstract String getThumbImagePath();

	@Override
	public String getInfo()
	{
		return internalInfo;
	}

	public String getDesc()
	{
		return description;
	}

	public URI getWebsite()
	{
		return website;
	}

	@Override
	public Person getContact()
	{
		return contact;
	}

	public String[] getGenres()	//--> Returnerer artisten sjangere i en array.
	{
		if(genres.isEmpty())
			return null;

		Object[] tmp = genres.toArray();

		String[] stringTmp = new String[tmp.length];

		for(int i = 0; i < stringTmp.length; i++)
		{
			stringTmp[i] = (String)tmp[i];
		}
		return stringTmp;
	}

	public String genresToString()	//--> Returner et string representasjon av artistens sjangere.
	{
		String[] genreNames = getGenres();
		String tmpGenre = "";
		int i = genreNames.length;

		for(String s : genreNames)
		{
			if(i == 1)
			{
				tmpGenre += s;
				continue;
			}
			tmpGenre += s + ", ";
			i--;
		}
		return tmpGenre;
	}

	public TreeSet<Concert> getConcerts()
	{
		if(concerts.isEmpty())
			return null;

		return concerts;
	}

	public Concert[] getConcertArray()	//--> Returnerer artisten konserter i en array.
	{
		if(concerts.isEmpty())
			return null;

		Object[] oArray = concerts.toArray();
		Concert[] array = new Concert[oArray.length];

		for(int i = 0; i<array.length; i++)
		{
			array[i] = (Concert) oArray[i];
		}

		return array;
	}

	public String[] getConcertNames()	//--> Returner et string representasjon av artistens konserter.
	{
		Concert[] tmp = getConcertArray();

		if(tmp == null)
			return null;

		String[] names = new String[tmp.length];

		for(int i = 0; i < tmp.length; i++)
		{
			names[i] = tmp[i].toString();
		}

		return names;
	}

	public SortedSet<Concert> getActiveConcerts()	//--> Returnerer artisten aktive konserter.
	{
		GregorianCalendar calender = (GregorianCalendar)Calendar.getInstance();

		Concert c = new Concert(calender);
		SortedSet<Concert> active = concerts.tailSet(c);

		if(active.isEmpty())
			return null;

		return active;
	}

	public int getActive()	//--> Returnerer antallet aktive konserter artisten har.
	{
		SortedSet<Concert> set = getActiveConcerts();
		if(set == null)
			return 0;

		return set.size();
	}


//-------------------------------------------------------------------------------------------------------- SET - metoder
	public void setInfo(String internalInfo)
	{
		this.internalInfo = internalInfo;
	}

	public void setDesc(String description)
	{
		this.description = description;
	}

	public void setWebsite(URI website)
	{
		this.website = website;
	}

	public void setContact(Person contact)
	{
		this.contact = contact;
	}


//-------------------------------------------------------------------------------------------------------- Andre metoder

//---------------------------------------------------- Metoder for ArrayList<String> genres
	public void modifyGenres(ArrayList<String> genres)	//--> Setter artistens sjangere til inkommen sjanger liste.
	{
		this.genres = genres;
	}

	public boolean hasGenre(String genre)	//--> Sjekker om artisten har angitt sjanger
	{
		return genres.contains(genre);
	}


//---------------------------------------------------- Metoder for TreeSet<Concert> concerts
	public boolean addConcert(Concert c) //--> Legger til concert til artisten
	{
		return concerts.add(c);
	}

	public boolean removeConcert(Concert c) //--> Fjerner concert fra artisten
	{
		return concerts.remove(c);
	}

//---------------------------------------------------- toString
	@Override
	public abstract String toString();

} // End of class Artist


