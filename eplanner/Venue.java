/*
 *	Innehold: Metoder for interaktering med variablene i lokalet(venue).
 *
 *		Spesifikke metoder:
 *			generiske get-metoder						--> For å hente ut datafelter.
 *			getGenres()									--> Returnerer lokalets sjangere i en array.
 *			getActiveConcerts()	 						--> Returnerer alle aktive konserter registrert på dette lokalet.
 *			getAllConcerts()							--> Returnerer TreeSet<Concert>.
 *			getConcertArray()							--> Returnerer TreeSet<Concert> som en array.
 *			getConcertNames()							--> Returnerer TreeSet<Concert> som en array av String.
 *			getOccupied()								--> Sjekker om lokalet er ledig på den datoen som blir tatt imot som parameter.
 *			getReservedDates()							--> Returnerer en array av GregorianCalendar over alle datoene hvor dette lokalet er reservert.
 *			Listemetoder for TreeSet<Concert> concerts 	--> Legger til/fjerner en konsert fra listen over konserter på lokalet.
 *			generiske set-metoder						--> For å sette datafelter.
 *			modifyGenres()			 					--> Bytter ut Genre-arrayen med en ny array over de nye sjangerene for lokalet.
 *			hasGenre()				 					--> Sjekker om lokalet har sjangeren som kommer med som parameter.
 *			getThumbImage()								--> Returnerer coverbildets filsti hvis det finnes.
 *			getThumbImagePath()							--> Returnerer coverbilde hvis det finnes, hvis ikke returnerer et standard bilde.
 *			genresToString()							--> Returner et string representasjon av artistens sjangere.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 12.05.2012.
 */

package eplanner;

import javax.swing.*;
import java.net.URI;
import java.util.*;
import java.math.BigDecimal;
import java.io.*;
import eplanner.comparators.*;
import eplanner.utilities.*;

//	Hensikt: Klasse for oppretting av lokaler(venue) objekter, for å lagre all informasjon om et lokale og utføre operasjoner/hente informasjon om dem.

public class Venue implements NameComparable, Serializable, HasContact // --> Kontruktør.
{
	private String name, adr, tel, email, land, description, internalInfo;		// Info og beskrivelse.
	private Person contact;														// Lokalets kontaktperson.
	private URI website;														// Nettadresse.
	private int maxCapacity;													// Maks kapasitet for lokalet.
	private boolean numberedSeats;												// Om lokalet har nummererte plasser eller ikke.
	private ArrayList<String> genres;											// Lokalets sjangere.
	private TreeSet<Concert> concerts;											// Lokalets konserter.
	private BigDecimal standardPrice;											// Lokalets standardpris
	private BigDecimal rentPrice;

	private static final long serialVersionUID = 1337L;
/*
	~ 2.prioritet String createdByAccount
	~ 2.prioritet Date lastModified, createdDate
*/

	// Primærkonstruktør
	public Venue(String name, String adr, String tel, String email, String description, String web, int maxCapacity, // --> Konstruktør.
					boolean numberedSeats,ArrayList<String> genres, BigDecimal standardPrice, Person contact, String internalInfo, BigDecimal rentPrice)
	{
		this.name = name;
		this.adr = adr;
		this.tel = tel;
		this.email = email;
		this.land = "";
		this.description = description;
		this.contact = contact;
		this.internalInfo = internalInfo;
		try
		{
			this.website = new URI(web);
		}
		catch(Exception use){IOActions.writeErrorLog(use);}

		this.maxCapacity = maxCapacity;
		this.numberedSeats = numberedSeats;
		this.genres = genres;
		this.standardPrice = standardPrice;
		this.rentPrice = rentPrice;

		concerts = new TreeSet<>(new NameComparator<Concert>());
	}

	public Venue (String name) // --> Enkel konstruktør for søking og sammenlikning av <Venue> - objekter.
	{
		this.name = name;
	}

//---------------------------------------------------- GET - metoder /Start 	--> For å hente ut datafelter.

	public int getActive()
	{
		SortedSet<Concert> set = getActiveConcerts();
		if(set == null)
			return 0;

		return set.size();
	}

	public String getAdr()
	{
		return adr;
	}

	@Override
	public Person getContact()
	{
		return contact;
	}

	public String getDesc()
	{
		return description;
	}

	public String getEmail()
	{
		return email;
	}

	public BigDecimal getRentPrice()
	{
		return rentPrice;
	}

	public String[] getGenres()
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

	@Override
	public String getInfo()
	{
		return internalInfo;
	}

	public String getLand()
	{
		return land;
	}

	public int getMaxCapacity()
	{
		return maxCapacity;
	}

	public String getName()
	{
		return name;
	}

	public boolean getNumberedSeats()
	{
		return numberedSeats;
	}

	public BigDecimal getStandardPrice()
	{
		return standardPrice;
	}

	public String getTel()
	{
		return tel;
	}

	public URI getWebsite()
	{
		return website;
	}

//---------------------------------------------------- GET - metoder for TreeSet<Concert>


	public SortedSet<Concert> getActiveConcerts()//		--> Returnerer alle aktive konserter registrert på dette lokalet.
	{
		GregorianCalendar calender = (GregorianCalendar)Calendar.getInstance();
		calender = Utilities.getCleanDate(calender);

		Concert c = new Concert(calender);
		SortedSet<Concert> active = concerts.tailSet(c);

		if(active.isEmpty())
			return null;

		return active;
	}

	public TreeSet<Concert> getAllConcerts()//		--> Returnerer TreeSet<Concert>.
	{
		if(concerts.isEmpty())
			return null;

		return concerts;
	}


	public Concert[] getConcertArray()//		--> Returnerer TreeSet<Concert> som en array.
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

	public String[] getConcertNames()//		--> Returnerer TreeSet<Concert> som en array av String.
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

//---------------------------------------------------- GET - metoder for dato i henhold til TreeSet<Concert>.

	public boolean getOccupied(GregorianCalendar date)// --> Sjekker om lokalet er ledig på den datoen som blir tatt imot som parameter.
	{
		GregorianCalendar[] reserved = getReservedDates();

		if(reserved == null)
			return false;

		GregorianCalendar check = Utilities.getCleanDate(date);

		for(GregorianCalendar c : reserved)
			if(check.get(Calendar.YEAR) == c.get(Calendar.YEAR) && check.get(Calendar.MONTH) == c.get(Calendar.MONTH)
				&& check.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH))
				return true;

		return false;
	}

	public GregorianCalendar[] getReservedDates()// --> Returnerer en array av GregorianCalendar over alle datoene hvor dette lokalet er reservert.
	{
		SortedSet<Concert> active = getActiveConcerts();

		if(active == null)
			return null;

		GregorianCalendar[] reserved = new GregorianCalendar[active.size()];

		int i = 0;

		for(Concert c : active)
		{
			reserved[i++] = c.getDate();
		}

		return reserved;
	}

//---------------------------------------------------- Listemetoder for TreeSet<Concert> concerts 	--> Legger til/fjerner en konsert fra listen over konserter på lokalet.

	public boolean addConcert(Concert c)
	{
		return concerts.add(c);
	}

	public boolean containsConcert(Concert c)
	{
		return concerts.contains(c);
	}

	public boolean removeConcert(Concert c)
	{
		return concerts.remove(c);
	}

//---------------------------------------------------- SET - metoder 	--> For å sette datafelter.

	public void setAdr(String adr)
	{
		this.adr = adr;
	}

	public void setDesc(String description)
	{
		this.description = description;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public void setInfo(String internalInfo)
	{
		this.internalInfo = internalInfo;
	}

	public void setLand(String land)
	{
		this.land = land;
	}

	public void setRentPrice(BigDecimal rentPrice)
	{
		this.rentPrice = rentPrice;
	}

	public void setMaxCapacity(int maxCapacity)
	{
		this.maxCapacity = maxCapacity;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setNumberedSeats(boolean numberedSeats)
	{
		this.numberedSeats = numberedSeats;
	}

	public void setStandardPrice(BigDecimal standardPrice)
	{
		this.standardPrice = standardPrice;
	}

	public void setTel(String tel)
	{
		this.tel = tel;
	}

	public void setWebsite(URI website)
	{
		this.website = website;
	}

//---------------------------------------------------- Metoder for ArrayList<String> genres

	public void modifyGenres(ArrayList<String> genreList)// --> Bytter ut Genre-arrayen med en ny array over de nye sjangerene for lokalet.
	{
		genres = genreList;
	}

	public boolean hasGenre(String genre)// --> Sjekker om lokalet har sjangeren som kommer med som parameter.
	{
		return genres.contains(genre);
	}

//---------------------------------------------------- Metoder for å hente ut coverbildeinformasjon.


	public ImageIcon getThumbImage()//	--> Returnerer coverbildets filsti hvis det finnes.
	{
		ImageIcon venueImage = Utilities.getImage("Images/Venues/" + getName() + "/" + getName() + ".jpg");

		if(venueImage == null)
		{
			return Utilities.getIcon("/Images/no_avatar.gif");
		}
		else
			return venueImage;
	}

	public String getThumbImagePath()//		--> Returnerer coverbilde hvis det finnes, hvis ikke returnerer et standard bilde.
	{
		File image = new File("Images/Venues/" + getName() + "/" + getName() + ".jpg");
		if(image.exists())
			return image.getAbsolutePath();
		return "";
	}

//---------------------------------------------------- toString metoder

	public String toString()
	{
		return getName();
	}

	public String genresToString()//	--> Returner et string representasjon av artistens sjangere.
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

} //End of class Venue