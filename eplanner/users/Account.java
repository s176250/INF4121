/*
 *	Innehold: Metoder for interaktering med variablene i en konto(Account).
 *
 *		Spesifikke metoder:
 *			verifyPwd()					--> Sjekker om passordet som kommer som parameter matcher det passordet som er lagret i klassen.
 *			getStringAccess()			-->		Returnerer aksessnivået brukeren som en String.
 *			getThumbImagePath()			--> Returnerer coverbildets filsti hvis det finnes.
 *			getThumbImage()				--> Returnerer coverbilde hvis det finnes, hvis ikke returnerer et standard bilde.
 *			deactivate()		 		--> Deaktiverer kontoen ved å sette variabelen active til false.
 *			resetPwd()	 				--> Setter passordet til den innkomne parameteren hvis det andre parameteren matcher det gamle passordet i klassen.
 *			setAccess()					--> Setter aksess-typen på kontoen til inkommen parameter hvis denne matcher en av de predefinerte variablene i klassen.
 *			setPwd()				 	--> Setter passordet til den innkomne parameteren hvis det andre parameteren matcher det gamle passordet i klassen.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 13.05.2012.
 */

package eplanner.users;

import java.io.*;
import javax.swing.*;
import java.util.Random;
import eplanner.utilities.*;

//	Hensikt: Klasse for oppretting av kontoer (Account) objekter, for å lagre all informasjon om en konto i et objekt og for å utføre operasjoner/hente ut informasjon om dem.

public class Account implements Serializable
{
	private static final long serialVersionUID = 1337L;
	public final static int SUPERADMIN = 100;													// Konstanter som representerer brukernivåer
	public final static int MANAGER = 80;														//
	public final static int EVENTPLANNER = 60;													//
	public final static int SALESMAN = 40;														//
	public final static String[] ACCESS_NAMES = {"Manager", "Eventplanlegger", "Selger"};		// Array med Stringrepresantasjonen av de settbare brukernivåene.
	private String realName, username, email, adr, tel, pwd;									// Felter i account.
	private boolean active;																		// En variabel som viser om kontoen er aktiv eller ikke.
	private int access;																			// En variabel som lagrer kontoens aksessnivå.

	//Hensikt: representer og lagre info om brukere.
	public Account(int access, String realName, String username, String email, String adr, String tel, String pwd)// --> Konstruktør.
	{
		if(username.equals(AccountList.ADMIN_NAME))
			this.access = access;
		else
			setAccess(access);

		this.realName = realName;
		this.username = username;
		this.email = email;
		this.adr = adr;
		this.tel = tel;
		this.pwd = pwd;
		active = true;
	}

//---------------------------------------------------- Passord - metoder /Start

	public boolean verifyPwd(String pwd)
	{
		if(this.pwd.equals(pwd))
			return true;
		return false;
	}

//---------------------------------------------------- GET - metoder 	--> For å hente ut datafelter.
	public int getAccess()
	{
		return access;
	}

	public boolean getActive()
	{
		return active;
	}

	public String getAdr()
	{
		return adr;
	}

	public String getEmail()
	{
		return email;
	}

	public String getName()
	{
		return username;
	}

	public String getUsername()
	{
		return username;
	}

	public String getRealName()
	{
		return realName;
	}

	public String getStringAccess()// 	-->Returnerer aksessnivået til brukeren som en String.
	{
		String tmp = "";

		switch(access)
		{
			case 100:
					tmp = AccountList.ADMIN_NAME;
					break;
			case 80:
					tmp = ACCESS_NAMES[0];
					break;
			case 60:
					tmp = ACCESS_NAMES[1];
					break;
			case 40:
					tmp = ACCESS_NAMES[2];
					break;
		}

		return tmp;

	}

	public String getTel()
	{
		return tel;
	}

//---------------------------------------------------- Metoder for å hente ut coverbildeinformasjon.

	public ImageIcon getThumbImage()//	--> Returnerer coverbilde hvis det finnes, hvis ikke returnerer et standard bilde.
	{
		ImageIcon artistImage = Utilities.getImage("Images/Accounts/" + getName() + "/" + getName() + ".jpg");

		if(artistImage == null)
		{
			return Utilities.getIcon("/Images/no_avatar.gif");
		}
		else
			return artistImage;
	}

	public String getThumbImagePath()//	 --> Returnerer coverbildets filsti hvis det finnes.
	{
		File image = new File("Images/Accounts/" + getName() + "/" + getName() + ".jpg");
		if(image.exists())
			return image.getAbsolutePath();
		return "";
	}

//---------------------------------------------------- SET - metoder		--> For å sette datafelter.

	public boolean deactivate()// 	--> Deaktiverer kontoen ved å sette variabelen active til false.
	{
		if(access == SUPERADMIN)
			return false;
		else
		{
			active = false;
			return true;
		}
	}

	public String resetPwd()// --> Setter passordet til den innkomne parameteren hvis det andre parameteren matcher det gamle passordet i klassen.
	{
		Random gen = new Random();
		char[] pass = new char[8];

		for(int i = 0; i < pass.length; i++)
		{
			int number = (48 + gen.nextInt(74));
			pass[i] = (char) number;
		}
		pwd = new String(pass);
		return pwd;
	}

	public boolean setAccess(int i)// 	--> Setter aksess-typen på kontoen til inkommen parameter hvis denne matcher en av de predefinerte variablene i klassen.
	{
		if( (i != MANAGER) && (i != EVENTPLANNER) && (i !=  SALESMAN))
			return false;

		access = i;
		return true;
	}

	public void setActive()
	{
		active = true;
	}

	public void setAdr(String adr)
	{
		this.adr = adr;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public boolean setPwd(String oldPwd, String newPwd)// --> Setter passordet til den innkomne parameteren hvis det andre parameteren matcher det gamle passordet i klassen.
	{
		if(!oldPwd.equals(pwd))
			return false;

		pwd = newPwd;
		return true;
	}

	public void setRealName(String realName)
	{
		this.realName = realName;
	}

	public void setTel(String tel)
	{
		this.tel = tel;
	}

	public String toString()
	{
		if(active)
			return getName() + " (" +  getStringAccess() + ")";
		else
			return getName() + " (Deaktivert)";
	}

//---------------------------- Set - metoder /END

} // End of class Account




