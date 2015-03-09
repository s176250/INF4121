/*
 *	Innehold: Metoder for behandling av kontoer(Accounts).
 *
 *		Spesifikke metoder:
 *			addAccount()				 --> Legger til en konto i listen hvis den ikke finnes fra før.
 *			deactivate()				 --> Fjerner en koto fra listen som matcher innkommen parameter(navn).
 *			delAccount() 				 --> Fjerner en konto fra listen som matcher innkommen parameter(navn).
 *			findAccount()				 --> Finner en konto i listen som matcher innkommen parameter(navn) og returnerer denne.
 *			login()					 	 --> Sjekker om navnet som kommer inn som parameter matcher en registret konto og sjekker deretter passordet mot denne kontoen.
 *			getActiveArray()		 	 --> Returnerer en array-representasjon av alle aktike kontoer.
 *			getArray()					 --> Returnerer en array-representasjon av listen.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 13.05.2012.
 */

package eplanner.users;

import java.io.*;
import eplanner.utilities.Utilities;
import java.util.*;
import javax.swing.*;

//	Hensikt: Å opptre som listeklasse for alle objekter av typen <Account>, og utføre nødvendige metoder/operasjoner på disse objektene.

public class AccountList implements Serializable
{
	private HashMap<String, Account> list;					//Listen som inneholder alle kontoene.
	private Account superAdmin;								// Kontoen til Superadmin som er hardkodet inn i programmet.
	public static String ADMIN_NAME = "Admin";				// Admin sitt navn.
	private static final long serialVersionUID = 1337L;

	public AccountList() // --> Kontruktør.
	{
		list = new HashMap<>();
		superAdmin = new Account(Account.SUPERADMIN, ADMIN_NAME, ADMIN_NAME, "", "", "", "");
		list.put(ADMIN_NAME, superAdmin);
	}

	public boolean addAccount(Account a)// --> Legger til en konto i listen hvis den ikke finnes fra før.
	{
		if( !list.containsKey(a.getName()) )
		{
			list.put(a.getName(), a);
			return true;
		}
		return false;
	}

	public void deactivate(Account toDel)// --> Fjerner en koto fra listen som matcher innkommen parameter(navn).
	{
		toDel.deactivate();
	}

	public Account delAccount(String name)// --> Fjerner en koto fra listen som matcher innkommen parameter(navn).
	{
		Account toDel = list.remove(name);

		return toDel;
	}

	public Account findAccount(String name)// --> Finner en konto i listen som matcher innkommen parameter(navn) og returnerer denne.
	{
		return list.get(name);
	}

	public Account login(String name, String pwd, JFrame window) // --> Sjekker om navnet som kommer inn som parameter matcher en registret konto og sjekker deretter passordet mot denne kontoen.
	{
		Account login = findAccount(name);

		if(login == null || !login.getActive())
		{
			Utilities.showError(window, "Bruker finnes ikke! " + name);
			return null;
		}

		if(login.verifyPwd(pwd))
			return login;

		Utilities.showError(window, "Feil passord, prøv igjen!");
		return null;
	}

	public Account[] getActiveArray() // --> Returnerer en array-representasjon av alle aktike kontoer.
	{
		Collection<Account> oArray = list.values();
		ArrayList<Account> arrayList = new ArrayList<>();

		for(Account a : oArray)
		{
				if(a.getActive())
					arrayList.add(a);
		}

		Account[] array = new Account[arrayList.size()];

		for(int i = 0; i < array.length; i++)
			array[i] = arrayList.get(i);

		return array;
	}

	public Account[] getArray() // --> Returnerer en array-representasjon av listen.
	{
		Collection<Account> oArray = list.values();
		Account[] array = new Account[oArray.size()];

		int count = 0;

		for(Account a : oArray)
		{
				array[count++] = a;
		}

		return array;
	}




} // End of class AccountList