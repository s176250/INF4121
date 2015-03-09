/*
 *	Innehold: Metoder for interaktering med variablene i en Person.
 *
 *		Spesifikke metoder:
 *			generiske set og get-metoder	--> For å hente ut/sette datafelter.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 12.05.2012.
 */


package eplanner;

import java.io.*;
import eplanner.comparators.*;
/*
 *	Hensikt: Klasse for oppretting av person objekter, for å lagre all informasjon om en person og
 *	utføre operasjoner/hente informasjon om de. Personer lagres som bandmedlemmer og kan, hvis programmet skal utvides,
 *  bli brukt som en genrell klasse for lagring av tilfeldige personer brukere av programmet vil lagre.
 */

public class Person implements Serializable, NameComparable
{
	private static final long serialVersionUID = 1337L;
	private String name, adr, land, tel, email;

	public Person(String name, String adr, String land, String tel, String email) // --> Kontruktør.
	{
		this.name = name;
		this.adr = adr;
		this.land = land;
		this.tel = tel;
		this.email = email;
	}

	public Person(String name)
	{
		this.name = name;
	}
//---------------------------------------------------- GET - metoder /Start 	--> For å hente ut datafelter.


	public String getAdr()//	--> Konstruktør for oppretting av objekter for søking
	{
		return adr;
	}

	public String getEmail()
	{
		return email;
	}

	public String getLand()
	{
		return land;
	}

	public String getName()
	{
		return name;
	}

	public String getTel()
	{
		return tel;
	}

//---------------------------------------------------- SET - metoder 	--> For å sette datafelter.

	public void setAdr(String adr)
	{
		this.adr = adr;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public void setLand(String land)
	{
		this.land = land;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public void setTel(String tel)
	{
		this.tel = tel;
	}

	public String toString()
	{
		return name;
	}

} // End of class Person