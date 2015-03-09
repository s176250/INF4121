/*
 * Innhold: Dette interfacet kan implementeres hvis klassen har metodene
 *
 *	Laget av: Martin WL og Jonas M
 *  Sist Endret: 15.05.2012
 *
 */
package eplanner.comparators;

import eplanner.Person;

public interface HasContact // --> Kontruktør.
{
	public Person getContact();
	public String getInfo();

} // End of interface HasContact