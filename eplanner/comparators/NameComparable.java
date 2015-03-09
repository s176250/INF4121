/*
 * Innhold: Dette interfacet m� implementeres hvis klassen skal kunne bruke den
 * 			generiske Comparator klassen StringComp for � sorteres p� navn(som tar hensyn til ���).
 * 			Krever at klassene som implementerer den har en getName() metode.
 *
 *
 *
 *	Laget av: Martin WL og Jonas M
 *  Sist Endret: 04.05.2012
 *
 */
package eplanner.comparators;

public interface NameComparable // --> Kontrukt�r.
{
	public String getName();

} // End of interface NameComparable