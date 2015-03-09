/*
 *	Innehold: Metoder for behandling av sjangere (Genres).
 *
 *		Spesifikke metoder:
 *			addGenre()		 		--> Legger til en sjanger i listen hvis den ikke allerede er i den predefinerte Arrayen.
 *			getGenres() 			--> Returnerer hele sjangerlisten.
 *			getGenreNames() 		--> Returnerer en array med navnene på alle sjangerene.
 *			getGenreOnIndex()		--> Returnerer en sjanger som ligger på en bestemt index.
 *			removeGenre() 	 		--> Fjerner en sjanger hvis den ikke er medlem i den predefinerte Arrayen.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 13.05.2012.
 */

package eplanner;

import java.util.*;
import java.io.Serializable;

/*
 *	Hensikt: Å opptre som listeklasse for alle objekter av typen <Genre>, og utføre nødvendige metoder/operasjoner på disse objektene.
 *	Skal hovedsakelig oppre som samleklasse for alle sjangerene.
 */

public class GenreList implements Serializable
{
	private static final long serialVersionUID = 1337L;

	// Konstant array som representerer grunnsjangerene i programmet.
	private static final String[] GENRE_ARRAY = {"Rock","Jazz","Pop","Reggea","Klassisk","Blues","Humor","Country","Techno","Hip Hop","Rap","Opera","Folk", "Alternative", "Indie", "Punk"};

	private ArrayList<String> genres;

	public GenreList( ) // --> Konstruktør. Legger alle de predefinerte sjangerene til en ny ArrayList som huser alle sjangere som lagres i klassen.
	{
		genres = new ArrayList<>(GENRE_ARRAY.length);

		for(String g : GENRE_ARRAY)
		{
			genres.add(g);
		}
	}

//---------------------------------------------------- Liste - metoder /Start

	public boolean addGenre(String g)// --> Legger til en sjanger i listen hvis den ikke allerede er i den predefinerte Arrayen.
	{
		String s = g.trim();
		for( String i : GENRE_ARRAY)
			if(i.equals(s))
				return false;

		return genres.add(s);
	}

	public ArrayList<String> getGenres()// --> Returnerer hele sjangerlisten.
	{
		return genres;
	}

	public String[] getGenreNames()// --> Returnerer en array med navnene på alle sjangerene.
	{
		Object[] tmp = genres.toArray();
		String[] stringTmp = new String[tmp.length];

		for(int i = 0; i < stringTmp.length; i++)
		{
			stringTmp[i] = (String)tmp[i];
		}

		return stringTmp;
	}

	public String getGenreOnIndex(int index)// --> Returnerer en sjanger som ligger på en bestemt index.
	{
		return genres.get(index);
	}

	public boolean removeGenre(String g)// --> Fjerner en sjanger hvis den ikke er medlem i den predefinerte Arrayen.
	{
		String s = g.trim();
		for( String i : GENRE_ARRAY)
			if(i.equals(s))
				return false;

		return genres.remove(s);
	}

//---------------------------- Liste - metoder /END

} // End of class Myclass