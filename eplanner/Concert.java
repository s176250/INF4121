/*
 *	Innehold:  Metoder for behandlig av et konsertobjekt:
 *
 *				get-metoder	                    --> For å hente ut datafelter.
 *				getAvailableTickets() 			--> Regner ut og returnerer antall ledige billetter til konserten.
 *				getAvailableReserved()			--> Regner ut og returnerer antall ledige billetter av de reserverte billettene..
 *				getConcertGenres()				--> Returnerer alle sjangerene assosiert med artisten som spiller konserten.
 *				getThumbImage()					--> Returnerer coverbilde hvis det finnes, hvis ikke returnerer et standard bilde.
 *				getThumbImagePath()				--> Returnerer coverbildes filsti.
 *				set-metoder	                    --> For å sette datafelter.
 *				sellTickets						--> (Selger billetter) Minsker antallet ledige billetter og øker antalle solgte billetter samt omsettningen, hvis det er nok ledige.
 *				sellReservedTickets				--> Minsker antallet ledige reserverte billetter og øker antalle solgte reserverte billetter samt omsetningen, hvis det er nok ledige.
 *				toString						--> Returerer eventens string representasjon(dato - navn), brukes primært i oversiktslistene.
 *				getReceipts()					--> Returnerer en matrise med alle kvitteringer for denne konserten.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 12.05.2012.
 */

package eplanner;

import eplanner.*;
import eplanner.windows.*;
import eplanner.comparators.NameComparable;
import eplanner.users.Account;
import eplanner.utilities.Utilities;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.ImageIcon;
import javax.swing.text.StyledDocument;
import eplanner.comparators.NameComparator;
import eplanner.users.Account;
import java.util.*;


// Hensikt: Klasse for oppretting av konsert(arrangement) objekter, for å lagre all info om en konsert og utføre operasjoner/hente informasjon om de.
public class Concert implements Serializable, NameComparable
{
	private static final long serialVersionUID = 1337L;
	private GregorianCalendar eventDate;				// Eventens dato.
	private String name, description, startTime;		// Eventens navn, beskrivelse og starttid.
	private StyledDocument review;						// Eventens anmeldelse.
	private Account eventplanner;						// Eventens ansvarlige.
	private Artist mainArtist;							// Eventens hovedartist.
	private List<Artist> secondaryArtist;				// Eventens sekundærartister.
	private Venue venue;								// Eventens lokale
	private BigDecimal defaultPrice, revenue;			// Eventens standardpris per billett og inntjening.
	private int maxTickets, reservedTickets;			// Eventens maksimale antall billetter til distribusjon, og reserverte billetter.
	private int	distributedReservedTickets, soldTickets; //Eventens distribuerte reserverte billetter og vanlig solgte billetter.
	private TreeSet<Receipt> receipts;					 //Kvitteringer for solgte billetter
	private NameComparator<Receipt> comp;				 //Comparator til kvitteringssettet.


	// Konstruktør, brukes for å opprette eventer.
	public Concert(GregorianCalendar eventDate, String name, Account eplanner, Artist mainArtist, List<Artist> secondaryArtist, Venue venue, BigDecimal defaultPrice,
				   int maxTickets, int reservedTickets, String description)
	{
		setDate(eventDate);
		this.name = name;
		this.mainArtist = mainArtist;
		this.secondaryArtist = secondaryArtist;
		this.venue = venue;
		this.defaultPrice = defaultPrice;
		setAccount(eplanner);
		setMaxTickets(maxTickets);
		revenue = new BigDecimal(0);
		this.reservedTickets = reservedTickets;
		this.description = description;
		mainArtist.addConcert(this);
		for(Artist a:secondaryArtist)
			a.addConcert(this);
		venue.addConcert(this);
		comp = new NameComparator<>();
		receipts = new TreeSet<>(comp);
	}

	// Konstruktør, brukes kun i ConcertList for sortering
	public Concert(GregorianCalendar eventDate)
	{
		this.eventDate = eventDate;
		name = "";
		venue = null;
	}

	// Konstruktør, brukes kun i ConcertList for sortering
	public Concert(String name)
	{
		eventDate = (GregorianCalendar) Calendar.getInstance();
		this.name = name;
		venue = null;
	}

//---------------------------------------------------- GET - metoder
	public GregorianCalendar getDate()
	{
		return eventDate;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public boolean getActive()
	{
		if(Utilities.getCleanDate(Calendar.getInstance()).compareTo(eventDate) > 0)
			return true;
		return false;
	}

	public StyledDocument getReview()
	{
		return review;
	}

	public Artist getArtist()
	{
		return mainArtist;
	}

	public List<Artist> getSecArtist()
	{
		return secondaryArtist;
	}

	public String getSecArtistNames()
	{
		String names = "";
		for(Artist a: secondaryArtist)
			names += a.getName() + ", ";
		return names;
	}

	public String getDescription()
	{
		return description;
	}

	public Venue getVenue()
	{
		return venue;
	}

	public BigDecimal getDefaultPrice()
	{
		return defaultPrice;
	}

	public int getMaxTickets()
	{
		return maxTickets;
	}

	public int getReservedTickets()
	{
		return reservedTickets;
	}

	public Account getPlanner()
	{
		return eventplanner;
	}

	public BigDecimal getRevenue()
	{
		return revenue;
	}

	public String[][] getReceipts()	//--> Returnerer en matrise med alle kvitteringer for denne konserten.
	{
		String[][] matrix = new String[receipts.size()][TicketInfoWindow.COLUMN_NAMES.length];

		/*if(receipts.isEmpty())
			return matrix;
*/
		int row = 0;
		for(Receipt r : receipts)
		{
			String[] rec = r.receiptToArray();

			for(int j = 0; j < rec.length; j++)
			{
				matrix[row][j] = rec[j];
			}
			row++;
		}
		return matrix;
	}

	public int getAvailableTickets()	// --> Returnerer antall ledige billetter
	{
		return (maxTickets - reservedTickets - soldTickets);
	}

	public int getAvailableReserved()	// --> Returnerer antall ledige billetter av de reserverte.
	{
		return (reservedTickets - distributedReservedTickets);
	}

	public int getSoldTickets()
	{
		return soldTickets;
	}

	public int getDistributedReservedTickets()
	{
		return distributedReservedTickets;
	}

	public String getStartTime()
	{
		return startTime;
	}

	public String[] getConcertGenres()	//--> Returnerer alle sjangerene assosiert med artisten som spiller konserten.
	{
		return mainArtist.getGenres();
	}


	public ImageIcon getThumbImage()	// --> Returnerer coverbilde hvis det finnes, hvis ikke returnerer et standard bilde.
	{
		ImageIcon concertImage = Utilities.getImage("Images/Concerts/" + getName() + "/" + getName() + ".jpg");

		if(concertImage == null)
		{
			return Utilities.getIcon("/Images/no_avatar.gif");
		}
		else
			return concertImage;
	}

	public String getThumbImagePath()	// --> Returnerer coverbildes filsti hvis det finnes.
	{
		File image = new File("Images/Concerts/" + getName() + "/" + getName() + ".jpg");
		if(image.exists())
			return image.getAbsolutePath();
		return "";
	}
	//GET metoder slutt.


//----------------------------------------------------------------SET Metoder:

	public void setDate(GregorianCalendar e)	//--> Setter eventens dato, etter å ha renset den for klokkelsett.
	{
		startTime = Utilities.getStringTime(e);
		this.eventDate = Utilities.getCleanDate(e);
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setReview(StyledDocument r)
	{
		review = r;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public boolean setAccount(Account e)	//--> Setter ansvarlig planlegger hvis den kontoen har tilgangsrettigheter lik eller større enn eventplanner.
	{
		if(e.getAccess() >= Account.EVENTPLANNER)
		{
			eventplanner = e;
			return true;
		}
		return false;
	}

	public void setArtist(Artist mainArtist, ArtistList artistList)	//--> Fjerner denne konserten fra eksisterende hovedartist, legger den til på innkommende artist, og til slutt setter denne konsertens artist.
	{
		artistList.removeConcertsFromArtists(this, this.mainArtist);
		mainArtist.addConcert(this);
		this.mainArtist = mainArtist;
	}

	public void setSecArtist(List<Artist> secondaryArtist, ArtistList artistList) //--> Samme som setArtist(), men for oppvarmingsartisten.
	{
		for(Artist a: this.secondaryArtist)
			artistList.removeConcertsFromArtists(this, a);

		for(Artist b: secondaryArtist)
			b.addConcert(this);

		this.secondaryArtist = secondaryArtist;
	}

	public void setVenue(Venue venue, VenueList venueList)	//--> Samme som setArtist(), men for lokalet.
	{
		venueList.removeConcertsFromVenues(this);
		venue.addConcert(this);
		this.venue = venue;
	}


	public void setDefaultPrice(BigDecimal defaultPrice)
	{
		this.defaultPrice = defaultPrice;
	}

	public boolean setMaxTickets(int maxTickets)	//--> Angir maks antall billetter som kan selges/reserveres for konserten kan ikke være flere enn kapasiteten til et lokale.
	{
		if( (maxTickets > venue.getMaxCapacity()) || (maxTickets < 1) || (maxTickets < soldTickets+reservedTickets) )
		{
			return false;
		}
		else
		{
			this.maxTickets = maxTickets;
			return true;
		}
	}

	public boolean setReservedTickets(int reservedTickets)	// --> Setter antallet reserverte billetter, kan ikke være mer enn antall ledige pluss antall ledig reserverte.
	{
		if( reservedTickets <= (getAvailableTickets()+getAvailableReserved()))
		{
			this.reservedTickets = reservedTickets;
			return true;
		}
		return false;
	}
	//SET metoder slutt.

//---------------------------------------------------------------- Metoder for salg av billetter:

	public boolean sellTickets(int numberOfTickets, BigDecimal price, String name, String email, String account)	// --> Minsker antallet ledige billetter og øker antalle solgte billetter samt omsettningen, hvis det er nok ledige.
	{
		if( getAvailableTickets() >= numberOfTickets)
		{
			revenue = revenue.add(price.multiply((new BigDecimal(numberOfTickets))));
			soldTickets += numberOfTickets;
			receipts.add(new Receipt(name, email, account, numberOfTickets, price));
			return true;
		}
		return false;
	}

	public boolean sellReservedTickets(int numberOfTickets, BigDecimal price)	// --> Minsker antallet ledige reserverte billetter og øker antalle solgte reserverte billetter samt omsetningen, hvis det er nok ledige.
	{
		if( getAvailableReserved() >= numberOfTickets)
		{
			revenue = revenue.add(price.multiply((new BigDecimal(numberOfTickets))));
			distributedReservedTickets += numberOfTickets;
			return true;
		}
		return false;
	}


//---------------------------------------------------------------- Andre metoder:

    @Override
	public String toString()
	{
		return Utilities.getStringDate(eventDate) + "-" + name;
	}

} // End of class Concert