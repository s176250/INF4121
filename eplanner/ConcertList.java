/*
 *	Innehold:  Metoder for behandlig av en liste med konsertobjekter:
 *
 *				getConcerts()                   	--> Returnerer hele konsertlisten.
 *				isEmpty()	                    	--> For å sjekke om listen er tom.
 *				addConcert()
 *				removeConcert()
 *				findConcert()						--> Returnerer et subSet med alle Concert objektene som matcher en spesifikk dato.
 *				findConcert()						--> Returnerer et subSet med alle Concert objektene som er mellom(inklusiv) to datoer.
 *				findConcertsOnPartialName()			--> Søker igjennom hele listen for å finne konserter med navn som delvis matcher.
 *				findConcertOnName()					--> Søker listen til(hvis) den finner konserten med navn som matcher.
 *				findConcertsOnPartialNameSet()		--> Søker igjennom inkommen liste for å finne konserter med navn som delvis matcher.
 *				getConcertsOnGenre()				--> Returnerer alle konserter som passer til angitt sjanger.
 *				getConcertsOnGenre()				--> Returnerer alle konserter i inkommen liste som passer til angitt sjanger.
 *				getActiveConcerts()					--> Returerer alle aktive eller ikke aktive konserter
 *				getActiveConcertsArray()			--> Returerer alle aktive konserter i en array.
 *				getConcertsByPlanner()				--> Returerer alle konserter av en spesifikk planner.
 *				getArray()							--> Returerer alle konserterer i en array.
 *				getSubsetToFrom() 					--> Søker gjennom et innkommet set og finner konserter som har dato fra og med den ene datoen til og med den andre datoen.
 *				getSubsetToFrom() 					--> Søker gjennom et innkommet set og finner konserter som har navn fra og med den ene strengen til og med den andre strengen.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 12.05.2012.
 */

package eplanner;

import eplanner.comparators.NameComparator;
import eplanner.users.Account;
import eplanner.utilities.IOActions;
import eplanner.utilities.Utilities;
import java.io.Serializable;
import java.util.*;
import javax.swing.JFrame;


// Hensikt: Klasse for organisering av konserter, og søking blant de.
public class ConcertList implements Serializable
{
	private static final long serialVersionUID = 1337L;
	private TreeSet<Concert> list;									// Listen med konserter
	private NameComparator<Concert> comp;	// Sammenligner for konserter.

	// Konstruktør, oppretter ny liste.
	public ConcertList()
	{
		comp = new NameComparator<>();
		list = new TreeSet<>(comp);
	}

//--------------------------------------------------------------------------- Metoder

	public TreeSet<Concert> getConcerts()
	{
		return list;
	}

	public boolean isEmpty()
	{
		return list.isEmpty();
	}

// ------------------------------------------------ Legg til og fjern konserter:
	public boolean addConcert(Concert newConcert)
	{
		return list.add(newConcert);
	}

	public boolean removeConcert(VenueList venues, Concert oldConcert, ArtistList artists)	//--> Fjerner en konsert fra listen.
	{
		venues.removeConcertsFromVenues(oldConcert);	// Konserten må først fjernes fra lokaler den er holdt på, slik at de ikke holder referanser til konserten etter den blir slettet.
		artists.removeConcertsFromArtists(oldConcert);	// Samme som over men for artister.
		return list.remove(oldConcert);
	}


// ------------------------------------------------ Metoder for å finne konserter og hente ut speifikke undersett fra konsertlisten:

	public SortedSet<Concert> findConcert(GregorianCalendar findDate)	//--> Bruker binærsøk i TreeSet for å returnere et subSet med alle Concert objektene som har dato = findDate.
	{
		//Fra og med dato:
			Concert from_inclusive = new Concert(findDate);

		//Legger til en dag fordi til datoen er eksludert i subSet metoden.
			GregorianCalendar temp = Utilities.getCleanDate(findDate);
			temp.add(Calendar.DAY_OF_MONTH, 1);
			Concert to_exclusive = new Concert(temp);

		SortedSet<Concert> found = list.subSet(from_inclusive, to_exclusive);
		if (found.isEmpty())
			return null;
		return found;
	}

	public SortedSet<Concert> findConcert(GregorianCalendar fromDate, GregorianCalendar toDate)	//--> Bruker binærsøk i TreeSet for å returnere et subSet med alle Concert objektene som er mellom(inklusiv) to datoer.
	{
		Concert from_inclusive = new Concert(fromDate);

		toDate.add(Calendar.DAY_OF_MONTH, 1);
		Concert to_exclusive = new Concert(toDate);

		SortedSet<Concert> found = list.subSet(from_inclusive, to_exclusive);
		if (found.isEmpty())
			return null;
		return found;
	}

	public TreeSet<Concert> findConcertsOnPartialName(String name)	//--> Søker igjennom hele listen for å finne konserter med navn som delvis matcher.
	{
		TreeSet<Concert> match = new TreeSet<>(comp);

		for(Concert c : list)
		{
			String cName = c.getName().toUpperCase();
			String sName = name.toUpperCase();

			if(cName.indexOf(sName) > -1)
				match.add(c);
		}
		return match;
	}

	public Concert findConcertOnName(String name)	//--> Søker listen til(hvis) den finner konserten med navn som matcher.
	{
		for(Concert c : list)
		{
			if(c.getName().equals(name))
				return c;
		}
		return null;
	}

	public TreeSet<Concert> findConcertsOnPartialNameSet(String name, Set<Concert> subSet)	//--> Søker igjennom inkommen liste for å finne konserter med navn som delvis matcher.
	{
		TreeSet<Concert> match = new TreeSet<>(comp);

		for(Concert c : subSet)
		{
			String cName = c.getName().toUpperCase();
			String sName = name.toUpperCase();

			if(cName.indexOf(sName) > -1)
				match.add(c);
		}
		return match;
	}

	public TreeSet<Concert> getConcertsOnGenre(String gen)	//--> Returnerer alle konserter som passer til angitt sjanger.
	{
		TreeSet<Concert> match = new TreeSet<>(comp);

		for(Concert c : list)
		{
			if(c.getArtist().hasGenre(gen))
				match.add(c);
		}

		if(match.isEmpty())
			return null;

		return match;
	}

	public TreeSet<Concert> getConcertsOnGenre(String gen, TreeSet<Concert> limitedSet)	//--> Returnerer alle konserter i inkommen liste som passer til angitt sjanger.
	{
		TreeSet<Concert> match = new TreeSet<>(comp);

		for(Concert c : limitedSet)
		{
			if(c.getArtist().hasGenre(gen))
				match.add(c);
		}
		return match;
	}

	public TreeSet<Concert> getActiveConcerts(boolean active)	//--> Returerer alle aktive eller ikke aktive konserter
	{
		GregorianCalendar calender = Utilities.getCleanDate(Calendar.getInstance());
		SortedSet<Concert> activeConcerts;

		if(active)
		{
			Concert c = new Concert(calender);
			activeConcerts = list.tailSet(c);
		}
		else
		{
			calender.roll(Calendar.DAY_OF_MONTH, 1);
			Concert c = new Concert(calender);
			activeConcerts = list.headSet(c);
		}

		if(activeConcerts.isEmpty())
			return null;

		TreeSet<Concert> aConcerts = new TreeSet<>(comp);
		aConcerts.addAll(activeConcerts);

		return aConcerts;
	}

	public Concert[] getActiveConcertsArray()	//--> Returerer alle aktive konserter i en array.
	{
		SortedSet<Concert> active = getActiveConcerts(true);

		if(active == null)
			return null;

		Concert[] tmp = new Concert[active.size()];

		int count = 0;

		for(Concert c : active)
		{
			tmp[count++] = c;
		}
		return tmp;
	}

	public TreeSet<Concert> getConcertsByPlanner(Account account)	//--> Returerer alle konserter av en spesifikk planner.
	{
		if(account.getAccess() < Account.EVENTPLANNER) return null;

		TreeSet<Concert> match = new TreeSet<>(comp);

		for(Concert c : list)
		{
			if(c.getPlanner() == account)
			{
				match.add(c);
			}
		}
		if(match.isEmpty())
			return null;

		return match;
	}

	public Concert[] getArray()	//--> Returerer alle konserterer i en array.
	{
		if(list.isEmpty())
			return null;

		Object[] oArray = list.toArray();
		Concert[] array = new Concert[oArray.length];

		for(int i = 0; i<array.length; i++)
		{
			array[i] = (Concert) oArray[i];
		}

		return array;
	}

	public TreeSet<Concert> getSubsetToFrom(GregorianCalendar a, GregorianCalendar b, JFrame win, TreeSet<Concert> limited) //--> Søker gjennom et innkommet set og finner konserter som har navn fra og med den ene datoen til og med den andre datoen.
	{
		if(a == null)
			a = new GregorianCalendar(1970,0,1);

		if(b == null)
			b = new GregorianCalendar(2200,0,1);

		b.roll(Calendar.DAY_OF_MONTH, 1);	// Gjør til-datoen inklusiv.

		Concert from = new Concert(a);
		Concert to = new Concert(b);

		NavigableSet<Concert> subset = new TreeSet<>(comp);
		TreeSet<Concert> subsetTree = new TreeSet<>(comp);

		try
		{
			subset = limited.subSet(from , true, to, true);
			subsetTree.addAll(subset);
		}
		catch(IllegalArgumentException iae)
		{
			Utilities.showError(win, "Ugyldig datorekkefølge. Prøv på nytt.");
			IOActions.writeErrorLog(iae);
			return null;
		}
		catch(NullPointerException npe){IOActions.writeErrorLog(npe);}

		return subsetTree;
	}

	public TreeSet<Concert> getSubsetToFrom(String a, String b, JFrame win, TreeSet<Concert> limited) //--> Søker gjennom et innkommet set og finner konserter som har navn fra og med den ene bokstaven til og med den andre bokstaven.
	{
		if(a.equals(""))
			a = "a";

		b += "å";	//legger til å slik at til-strengen blir inklusiv.

		Concert from = new Concert(a);
		Concert to = new Concert(b);

		TreeSet<Concert> subsetTree = new TreeSet<>(comp);

		for(Concert c : limited)
		{
			if( (from.getName().compareToIgnoreCase(c.getName()) < 1)  && (to.getName().compareToIgnoreCase(c.getName()) > -1) )
			{
				subsetTree.add(c);
			}
		}
		return subsetTree;
	}

} // End of class ConcertList