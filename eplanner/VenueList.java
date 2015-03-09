/*
 *	Innehold: Metoder for behandling av lokaler<Venue>.
 *
 *		Spesifikke metoder:
 *			addVenue()				 							--> Legger til et nytt lokale i listen.
 *			isEmpty()											--> Sjekker om listen er tom.
 *			removeConcertsFromVenues()							--> Fjerner en konsert fra alle lokaler i listen.
 *			removeConcertsFromVenues()							--> Fjerner en konsert fra et lokale i listen.
 *			removeVenue				 							--> Fjerner et lokale hvis lokale IKKE har blitt brukt.
 *			findVenue				 							--> Søker gjennom listen etter ett lokale og returnerer dette.
 *			findVenuesOnPartialName()							--> Finner alle lokaler med et navn som inneholder søkestrengen, og returnerer disse i et TreeSet.
 *			findVenuesOnPartialNameSet()		 				--> Finner alle lokaler med et navn som inneholder søkestrengen i subsettet, og returnerer disse i et TreeSet.
 *			getArray()											--> Returnerer en array med alle lokalene i lokale-listen.
 *			getNameArray()										--> Returnerer en array med navnene på alle lokalene i lokale-listen.
 *			getSubsetToFrom()									--> Returnerer et subSet med lokaler som har navn fra og med den ene bokstaven til og med den andre bokstaven.
 *			getSubsetToFrom()									--> Returnerer et subSet med lokaler som har navn fra og med den ene bokstaven
 *																	til og med den andre bokstaven innenfor begrensnings-settet.
 *			getVenuesActive()		 							--> Returnerer alle aktive lokaler.
 *			getVenuesAvailable()	 							--> Returnerer alle lokalene som er ledig eller opptatt, på angitt dato.
 *			getVenuesOnGenre()		 							--> Returnerer alle lokalene som har samme sjanger som innkomne parameter.
 *			getVenuesOnGenre()									--> Returnerer alle lokalene innenfor begrensnings-settet som har samme sjanger som innkomne parameter.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 12.05.2012.
 */

package eplanner;

import java.util.*;
import java.io.*;
import javax.swing.*;
import eplanner.comparators.*;
import eplanner.utilities.*;

// Hensikt: Å opptre som listeklasse for alle objekter av typen <Venue>, og utføre nødvendige metoder/operasjoner på disse objektene.

public class VenueList implements Serializable
{
	private TreeSet<Venue> list;							// Selve listen over lokaler.
	private NameComparator<Venue> comp;						// Referanse til Comparatoren for listen.
	private static final long serialVersionUID = 1337L;

	public VenueList() // --> Konstruktør.
	{
		comp = new NameComparator<Venue>();
		list = new TreeSet<>(comp);
	}

//---------------------------------------------------- Metoder for manipulering av innholdet i listen og innholdet i underlistene til denne listen.

	public boolean addVenue(Venue newVenue) //  --> Legger til et nytt lokale i listen.
	{
		return list.add(newVenue);
	}

	public boolean isEmpty() // --> Sjekker om listen er tom.
	{
		return list.isEmpty();
	}

	public int removeConcertsFromVenues(Concert c) // --> Fjerner en konsert fra alle lokaler i listen.
	{
		int del = 0;
		for( Venue v : list)
		{
			if(v.removeConcert(c))
				del++;
		}
		return del;
	}

	public int removeConcertsFromVenues(Concert c, Venue ve) // --> Fjerner en konsert fra et lokale i listen.
	{
		int del = 0;
		for( Venue v : list)
		{
			if((v == ve) && (v.removeConcert(c)))
				del++;
		}
		return del;
	}



	public void removeVenue(Venue oldVenue, JFrame window) //  --> Fjerner et lokale hvis lokale IKKE har blitt brukt.
	{
		if(oldVenue.getAllConcerts() == null)
		{
			if(!list.remove(oldVenue))
				Utilities.showError(window, "Angitt lokale finnes ikke.");
			return;
		}

		Utilities.showError(window, "Kan ikke slettes pga. lokale har registrerte konserter.\nSlett evt. konserter for å få lov til dette.");
	}


// ------------------------------------------------ Metoder for å finne lokaler og for å hente ut speifikke undersett fra lokale-listen:

	public Venue findVenue(String name) //  --> Søker gjennom listen etter ett lokale og returnerer dette.
	{
		Venue find = new Venue(name);
		Venue found = list.tailSet(find).first();
		if ((found != null) && (found.getName().equals(name)))
			return found;
		return null;
	}

	public TreeSet<Venue> findVenuesOnPartialName(String name) //  --> Finner alle lokaler med et navn som inneholder søkestrengen, og returnerer disse i et TreeSet.
	{
		TreeSet<Venue> match = new TreeSet<>(comp);

		for(Venue v : list)
		{
			String vName = v.getName().toUpperCase();
			String sName = name.toUpperCase();

			if(vName.indexOf(sName) > -1)
				match.add(v);
		}

		return match;
	}

	public TreeSet<Venue> findVenuesOnPartialNameSet(String name, Set<Venue> subSet) //  --> Finner alle lokaler med et navn som inneholder søkestrengen i subsettet, og returnerer disse i et TreeSet.
	{
		TreeSet<Venue> match = new TreeSet<>(comp);

		for(Venue v : subSet)
		{
			String vName = v.getName().toUpperCase();
			String sName = name.toUpperCase();

			if(vName.indexOf(sName) > -1)
				match.add(v);
		}

		return match;
	}

	public Venue[] getArray() //  --> Returnerer en array med alle lokalene i lokale-listen.
	{
		if(list.isEmpty())
			return null;

		Object[] oArray = list.toArray();
		Venue[] array = new Venue[oArray.length];

		for(int i = 0; i<array.length; i++)
		{
			array[i] = (Venue) oArray[i];
		}

		return array;
	}

	public String[] getNameArray() //  --> Returnerer en array med navnene på alle lokalene i lokale-listen.
	{
		Venue[] tmp = getArray();

		if(tmp == null)
			return null;

		String[] names = new String[tmp.length];

		for(int i = 0; i < tmp.length; i++)
		{
			names[i] = tmp[i].getName();
		}

		return names;
	}

	public TreeSet<Venue> getSubsetToFrom(String a, String b, JFrame win) //  --> Returnerer et subSet med lokaler som har navn fra og med den ene bokstaven til og med den andre bokstaven.
	{
		if(a.equals(""))
			a = "a";

		b += "  ";

		Venue from = new Venue(a);
		Venue to = new Venue(b);

		NavigableSet<Venue> subset = new TreeSet<>(comp);
		TreeSet<Venue> subsetTree = new TreeSet<>(comp);

		try
		{
			subset = list.subSet(from , true, to, true);
			subsetTree.addAll(subset);
		}
		catch(IllegalArgumentException iae)
		{
			Utilities.showError(win, "Ugyldig bokstavrekkefølge. Prøv på nytt.");
			IOActions.writeErrorLog(iae);
			return null;
		}
		catch(NullPointerException npe){IOActions.writeErrorLog(npe);}

		return subsetTree;
	}

	public TreeSet<Venue> getSubsetToFrom(String a, String b, JFrame win, TreeSet<Venue> limited) //  --> Returnerer et subSet med lokaler som har navn fra og med den ene bokstaven
																								  //  til og med den andre bokstaven innenfor begrensnings-settet.
	{
		if(a.equals(""))
			a = "a";

		b += "  ";

		Venue from = new Venue(a);
		Venue to = new Venue(b);

		NavigableSet<Venue> subset = new TreeSet<>(comp);
		TreeSet<Venue> subsetTree = new TreeSet<>(comp);

		try
		{
			subset = limited.subSet(from , true, to, true);
			subsetTree.addAll(subset);
		}
		catch(IllegalArgumentException iae)
		{
			Utilities.showError(win, "Ugyldig bokstavrekkefølge. Prøv på nytt.");
			IOActions.writeErrorLog(iae);
			return null;
		}
		catch(NullPointerException npe){IOActions.writeErrorLog(npe);}

		return subsetTree;
	}

	public TreeSet<Venue> getVenues() //  --> Returnerer hele lokale-listen.
	{
		return list;
	}

	public TreeSet<Venue> getVenuesActive(boolean status) //  --> Returnerer alle aktive lokaler.
	{
		TreeSet<Venue> match = new TreeSet<>(comp);

		if(status)
		{
			for(Venue v : list)
			{
				if(v.getActive() > 0)
					match.add(v);
			}
		}
		else
		{
			for(Venue ve : list)
			{
				if(ve.getActive() == 0)
					match.add(ve);
			}
		}

		return match;
	}

	public TreeSet<Venue> getVenuesAvailable(boolean getAvailable, GregorianCalendar date) //  --> Returnerer alle lokalene som er ledig eller opptatt, på angitt dato.
	{
		TreeSet<Venue> match = new TreeSet<>(comp);

		if(getAvailable)
		{
			for(Venue v : list)
			{
				if(!v.getOccupied(date))
					match.add(v);
			}
		}
		else
		{
			for(Venue v : list)
			{
				if(v.getOccupied(date))
					match.add(v);
			}
		}

		if(match.isEmpty())
			return null;

		return match;
	}


	public TreeSet<Venue> getVenuesOnGenre(String gen) //  --> Returnerer alle lokalene som har samme sjanger som innkomne parameter.
	{
		TreeSet<Venue> match = new TreeSet<>(comp);

		for(Venue v : list)
		{
			if(v.hasGenre(gen))
				match.add(v);
		}

		if(match.isEmpty())
			return null;

		return match;
	}

	public TreeSet<Venue> getVenuesOnGenre(String gen, TreeSet<Venue> limitedSet) //  --> Returnerer alle lokalene innenfor begrensnings-settet som har samme sjanger som innkomne parameter.
	{
		TreeSet<Venue> match = new TreeSet<>(comp);

		for(Venue v : limitedSet)
		{
			if(v.hasGenre(gen))
				match.add(v);
		}
		return match;
	}

//---------------------------- Liste - metoder /END

} // End of class VenueList