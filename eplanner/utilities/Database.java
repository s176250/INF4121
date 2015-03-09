/*
 *	Innehold:  Metoder for å hente ut de ulike hovedlistene:
 *
 *				get-metoder	                    --> For å hente ut datafelter.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 12.05.2012.
 */

package eplanner.utilities;

import eplanner.ArtistList;
import eplanner.ConcertList;
import eplanner.GenreList;
import eplanner.VenueList;
import eplanner.users.AccountList;
import java.io.Serializable;

/* Hensikt: Klassen skal ta vare på alle de ulike listene og er den klassen i programmet som serialiseres til fil,
			og direkte og indirekte holder referanser til alle objekter som skal lagres.
			Klassen husker også hvilket utseende som var sist brukt på programmet.
*/
public class Database implements Serializable
{
	private static final long serialVersionUID = 1337L;
	private AccountList accountList;
	private VenueList venueList;
	private GenreList genreList;
	private ConcertList concertList;
	private ArtistList artistList;
	public static final String ARTIST = "Artists";
	public static final String VENUE = "Venues";
	public static final String CONCERT = "Concerts";
	public static final String ACCOUNT = "Accounts";
	private boolean systemLook;

	//Konstruktør, oppretter nye lister, kalles bare når programmet ikke
	//			   klarer å lese den serialiserte filen.
	public Database()
	{
		accountList = new AccountList();
		venueList = new VenueList();
		genreList = new GenreList();
		concertList = new ConcertList();
		artistList = new ArtistList();
	}

	public AccountList getAccountList()
	{
		return accountList;
	}

	public VenueList getVenueList()
	{
		return venueList;
	}

	public GenreList getGenreList()
	{
		return genreList;
	}

	public ConcertList getConcertList()
	{
		return concertList;
	}

	public ArtistList getArtistList()
	{
		return artistList;
	}

	public boolean getSystemLook()
	{
		return systemLook;
	}

	public void setSystemLook(boolean systemLook)
	{
		this.systemLook = systemLook;
	}


}