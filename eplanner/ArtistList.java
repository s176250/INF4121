/*
 *	Innehold:  Metoder for behandlig av en liste med artistobjekter:
 *
 *			 	getArtists							--> Returnerer hele artist listen.
 *				isEmpty()	                    	--> For å sjekke om listen er tom.
 *				addArtist()
 *				removeArtist()
 *				removeConcertsFromArtists()			--> Fjerner en konsert fra alle artister
 *				removeConcertsFromArtists()			--> Fjerner en konsert fra en spesifik artist
 *				findArtist(String name)				--> Eksakt finn metode.
 *				findArtistsOnPartialName			--> Søker igjennom hele listen for å finne artister med navn som delvis matcher.
 *				findArtistsOnPartialNameSet			--> Søker igjennom inkommen liste for å finne artister med navn som delvis matcher.
 *				getArtistsActive					--> Returerer alle aktive eller ikke aktive artister
 *				getArtistOnGenre					--> Returnerer alle artister som passer til angitt sjanger.
 *				getArtistOnGenre					--> Returnerer alle artister i inkommen liste som passer til angitt sjanger.
 *				getSubsetToFrom						--> Søker gjennom hele listen og finner artister som har navn fra og med den ene strengen til og med den andre strengen.
 *				getSubsetToFrom						--> Søker gjennom et innkommet set og finner artister som har navn fra og med den ene strengen til og med den andre strengen.
 *				getArray()							--> Returerer alle artisterer i en array.
 *				getNameArray()						--> Returerer navnene på alle artisterer i en array.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 12.05.2012.
 */

package eplanner;

import eplanner.comparators.NameComparator;
import eplanner.utilities.IOActions;
import eplanner.utilities.Utilities;
import java.io.Serializable;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JFrame;

// Hensikt: Klasse for organisering av artister, og søking blant de.
public class ArtistList implements Serializable
{
	private static final long serialVersionUID = 1337L;
	private TreeSet<Artist> list;							//Liste med artister
	private NameComparator<Artist> comp;					//Sammenligner for artister


	public ArtistList()
	{
		comp = new NameComparator<>();
		list = new TreeSet<>(comp);
	}


// --------------------------------------------------------------------------- Metoder
	public TreeSet<Artist> getArtists()
	{
		return list;
	}

	public boolean isEmpty()
	{
		return list.isEmpty();
	}


// ------------------------------------------------ Legg til og fjern artister, og konsert fra artist:
	public boolean addArtist(Artist newArtist)
	{
		return list.add(newArtist);
	}

	public void removeArtist(Artist oldArtist, JFrame window)
	{
		if(oldArtist.getConcerts() == null)	//Sjekker om Artisten har blitt brukt, isåfall er det ikke lov å slette Artisten.
		{
			if(!list.remove(oldArtist))
				Utilities.showError(window, "Angitt artist finnes ikke.");
			return;
		}
		Utilities.showError(window, "Kan ikke slettes pga. artist har registrerte konserter.\nSlett evt. konserter for å få lov til dette.");
	}

	public int removeConcertsFromArtists(Concert c)	//Fjerner en konsert fra alle artister
	{
		int del = 0;
		for( Artist ar : list)
		{
			if(ar.removeConcert(c))
				del++;
		}
		return del;
	}

	public int removeConcertsFromArtists(Concert c, Artist a)	//Fjerner en konsert fra en spesifik artist
	{
		int del = 0;
		for( Artist ar : list)
		{
			if((ar == a) && (ar.removeConcert(c)))
				del++;
		}
		return del;
	}


// ------------------------------------------------ Metoder for å finne artister og hente ut speifikke undersett fra artisttlisten:

	public Artist findArtist(String name)	// Eksakt finn metode.
	{
		Band find = new Band(name);
		Artist found = list.tailSet(find).first();
		if ((found != null) && (found.getName().equals(name)))
			return found;
		return null;
	}

	public TreeSet<Artist> findArtistsOnPartialName(String name)	//--> Søker igjennom hele listen for å finne artister med navn som delvis matcher.
	{
		TreeSet<Artist> match = new TreeSet<>(comp);

		for(Artist a : list)
		{
			String aName = a.getName().toUpperCase();
			String sName = name.toUpperCase();

			if(aName.indexOf(sName) > -1)
				match.add(a);
		}

		return match;
	}

	public TreeSet<Artist> findArtistsOnPartialNameSet(String name, Set<Artist> subSet)	//--> Søker igjennom inkommen liste for å finne artister med navn som delvis matcher.
	{
		TreeSet<Artist> match = new TreeSet<>(comp);

		for(Artist a : subSet)
		{
			String aName = a.getName().toUpperCase();
			String sName = name.toUpperCase();

			if(aName.indexOf(sName) > -1)
				match.add(a);
		}

		return match;
	}

	public TreeSet<Artist> getArtistsActive(boolean status) //--> Returerer alle aktive eller ikke aktive artister
	{
		TreeSet<Artist> match = new TreeSet<>(comp);

		if(status)
		{
			for(Artist a : list)
			{
				if(a.getActive() > 0)
					match.add(a);
			}
		}
		else
		{
			for(Artist ar : list)
			{
				if(ar.getActive() == 0)
					match.add(ar);
			}
		}

		return match;
	}

	public TreeSet<Artist> getArtistOnGenre(String gen)	//--> Returnerer alle artister som passer til angitt sjanger.
	{
		TreeSet<Artist> match = new TreeSet<>(comp);

		for(Artist a : list)
		{
			if(a.hasGenre(gen))
				match.add(a);
		}

		if(match.isEmpty())
			return null;

		return match;
	}

	public TreeSet<Artist> getArtistOnGenres(String[] genres)	//--> Returnerer alle artister som passer til angitte sjangere.
	{
		TreeSet<Artist> match = new TreeSet<>(comp);

		for(Artist a : list)
		{
			for(String s: genres)
			{
				if(a.hasGenre(s))
				{
					match.add(a);
					break;
				}
			}
		}

		if(match.isEmpty())
			return null;

		return match;
	}

	public TreeSet<Artist> getArtistOnGenre(String gen, TreeSet<Artist> limitedSet)	//--> Returnerer alle artister i inkommen liste som passer til angitt sjanger.
	{
		TreeSet<Artist> match = new TreeSet<>(comp);

		for(Artist a : limitedSet)
		{
			if(a.hasGenre(gen))
				match.add(a);
		}

		return match;
	}

	public NavigableSet<Artist> getSubsetToFrom(String a, String b, JFrame win)//--> Søker gjennom hele listen og finner artister som har navn fra og med den ene strengen til og med den andre strengen.
	{
		Artist from = new Band(a);
		Artist to = new Band(b);
		NavigableSet<Artist> subset;

		try
		{
			subset = list.subSet(from , true, to, true);

			if(subset.isEmpty())
				return null;
		}
		catch(IllegalArgumentException iae)
		{
			Utilities.showError(win, "Ugyldig bokstavrekkefølge. Prøv på nytt.");
			IOActions.writeErrorLog(iae);
			return null;
		}
		return subset;
	}

	public TreeSet<Artist> getSubsetToFrom(String a, String b, JFrame win, TreeSet<Artist> limit)	//--> Søker gjennom et innkommet set og finner artister som har navn fra og med den ene strengen til og med den andre strengen.
	{
		if(a.equals(""))
			a = "a";

		b += "  ";	//legger til å slik at til-strengen blir inklusiv.

		Artist from = new Band(a);
		Artist to = new Band(b);

		if(to == null || from == null)
			return null;

		NavigableSet<Artist> subset = new TreeSet<>(comp);
		TreeSet<Artist> subsetTree = new TreeSet<>(comp);

		try
		{
			subset = limit.subSet(from , true, to, true);
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

	public Artist[] getArray()	//--> Returerer alle artisterer i en array.
	{
		if(list.isEmpty())
			return null;

		Object[] oArray = list.toArray();
		Artist[] array = new Artist[oArray.length];

		for(int i = 0; i<array.length; i++)
		{
			if( oArray[i] instanceof Band)
				array[i] = (Band) oArray[i];
			else
				array[i] = (Soloartist) oArray[i];
		}

		return array;
	}

	public String[] getNameArray()	//--> Returerer navnene på alle artisterer i en array.
	{
		Artist[] tmp = getArray();

		if(tmp == null)
			return null;

		String[] names = new String[tmp.length];

		for(int i = 0; i < tmp.length; i++)
		{
			names[i] = tmp[i].getName();
		}

		return names;
	}

} // End of class ArtistList