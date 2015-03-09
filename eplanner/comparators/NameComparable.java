/*
 * Innhold: Dette interfacet må implementeres hvis klassen skal kunne bruke den
 * 			generiske Comparator klassen StringComp for å sorteres på navn(som tar hensyn til æøå).
 * 			Krever at klassene som implementerer den har en getName() metode.
 *
 *
 *
 *	Laget av: Martin WL og Jonas M
 *  Sist Endret: 04.05.2012
 *
 */
package eplanner.comparators;

public interface NameComparable // --> Kontruktør.
{
	public String getName();

} // End of interface NameComparable