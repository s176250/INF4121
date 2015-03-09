/*
 *	Innehold: Metoder for interaktering med variablene i en Person.
 *
 *		Spesifikke metoder:
 *			generiske get-metoder		--> For å hente ut datafelter.
 *			receiptToArray()			--> Returnerer en array med denne kvitteringens verdier.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 14.05.2012.
 */


package eplanner;

import eplanner.comparators.*;
import eplanner.Concert;
import eplanner.ConcertList;
import eplanner.utilities.Utilities;
import eplanner.windows.components.ExtendedJList;

import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.*;

/*
 *	Hensikt: Klasse for oppretting av kvitteringer.
 */
public class Receipt implements Serializable, NameComparable
{
	private static final long serialVersionUID = 1337L;
	private String name, accountNr, email;
	private int tickets;
	private BigDecimal ticketPrice, total;
	private GregorianCalendar date;


	public Receipt(String name, String email, String accountNr, int tickets, BigDecimal ticketPrice) // --> Kontruktør.
	{
		this.name = name;
		this.tickets = tickets;
		this.accountNr = accountNr;
		this.ticketPrice = ticketPrice;
		this.email = email;
		date = (GregorianCalendar) Calendar.getInstance();
		total = ticketPrice.multiply(new BigDecimal(tickets));
	}

//---------------------------------------------------- GET - metoder /Start 	--> For å hente ut datafelter.


	public String accountNr()
	{
		return accountNr;
	}

	public String getEmail()
	{
		return email;
	}

	public String getName()
	{
		return name;
	}

	public int getTickets()
	{
		return tickets;
	}

	public BigDecimal getPrice()
	{
		return ticketPrice;
	}

	public BigDecimal getTotal()
	{
		return total;
	}

	public GregorianCalendar getDate()
	{
		return date;
	}

	public String[] receiptToArray()	//--> Returnerer en array med denne kvitteringens verdier.
	{
		String[] tmp = {Utilities.getStringDate(date), name, email, accountNr, tickets + "", ticketPrice + "", total + ""};
		return tmp;
	}

	public String toString()
	{
		return name;
	}

} // End of class Person