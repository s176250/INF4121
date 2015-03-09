/*
 *	Innehold:  Metoder for å utføre mye brukte funksjoner og ofte visste meldinger:
 *
 *				showError()				--> Viser en feilmelding med inkommen parameter som tekst, eller en standard tekst.
 *				showInfo()				--> Viser en infomelding med inkommen parameter som tekst
 *				showOption() 			--> Viser en valgmelding med inkommen parameter som tekst.
 *				getCleanDate()			--> Tar imot et calender objekt og returnerer det med nullstilt klokkeslett.
 *				getStringDate()			--> Tar imot en dato og returnerer en string representasjon av den.
 *				getStringTime()			--> Tar imot en dato og returnerer en string representasjon av klokkeslettet.
 *				getTimeDate()			--> Tar imot et calender objekt uten tid og et klokkeslett i string format, og returnerer det som et komplett calender objekt.
 *				stringToDate()			--> Tar imot en streng og returnerer den som en dato.
 *				getIcon()				--> Tar imot en filsti til et bilde og returnerer et bilde opprettet ved hjelp av class.getResource().
 *				getImage()				--> Tar imot en filsti til et bilde og returnerer et bilde opprettet ved hjelp av Toolkit.
 *				setIcon()				--> Tar imot et vindu og setter standard ikon på det.
 *				setPath()				--> Åpner en filvelger og et tekstfelt og setter valgt filsti inn i tekstfeltet.
 *				setPath()				--> Åpner en filvelger, og returnerer valgt fil.
 *				google()				--> Åpner standard nettleser og søker google etter parameter.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 12.05.2012.
 */

package eplanner.utilities;

import eplanner.comparators.NameComparable;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.*;
import javax.swing.filechooser.*;


// Hensikt: Klassen er en bekvemmelighetsklasse for ofte utførte operasjoner som ikke nødvendigvis relaterer til spesifikke objekter.
public class Utilities
{

// ------------------------------------------------------------------------------------------MELDINGS-METODER:

	public static void showError(JFrame window, String message)		//--> Viser en feilmelding med inkommen parameter som tekst, eller en standard tekst.
	{
		if(message == null)
		{
			JOptionPane.showMessageDialog(window,"Oops, en feil oppstod.","Error",JOptionPane.ERROR_MESSAGE);
			return;
		}
	   	JOptionPane.showMessageDialog(window,message,"Error",JOptionPane.ERROR_MESSAGE);
	}

	public static void showInfo(JFrame window, String message)	//--> Viser en infomelding med inkommen parameter som tekst.
	{
		JOptionPane.showMessageDialog(window,message,"Info",JOptionPane.INFORMATION_MESSAGE);
	}

	public static int showOption(JFrame window, String message) //--> Viser en valgmelding med inkommen parameter som tekst.
	{
		String[] options = {"Ja", "Nei", "Avbryt"};
		int result = JOptionPane.showOptionDialog(window, message,"Option", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);

		return result;
	}

// ------------------------------------------------------------------------------------------CALENDER-METODER:

	public static GregorianCalendar getCleanDate(Calendar date)	// Tar imot et calender objekt og returnerer det med nullstilt klokkeslett.
	{
		return new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
	}

	public static String getStringDate(Calendar date)	//--> Tar imot en dato og returnerer en string representasjon av den.
	{
		String month = (date.get(Calendar.MONTH)+1) + "";
		String day = date.get(Calendar.DAY_OF_MONTH) + "";

		if(month.length() == 1)
			month = "0" + month;

		if(day.length() == 1)
			day = "0" + day;

		return day + "." + month + "." + date.get(Calendar.YEAR);
	}

	public static String getStringTime(Calendar date)	//--> Tar imot en dato og returnerer en string representasjon av klokkeslettet.
	{
		String min = date.get(Calendar.MINUTE) + "";
		String hour = date.get(Calendar.HOUR_OF_DAY) + "";

		if(min.length() == 1)
			min = "0" + min;

		if(hour.length() == 1)
			hour = "0" + hour;

		return hour + ":" + min;
	}

	public static GregorianCalendar getTimeDate(GregorianCalendar date, String time)	//--> Tar imot et calender objekt uten tid og et klokkeslett i string format, og returnerer det som et komplett calender objekt.
	{
		String[] times = time.split(":");
		int hour = Integer.parseInt(times[0]);
		int minute = Integer.parseInt(times[1]);
		return new GregorianCalendar(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH), hour, minute);
	}

	public static GregorianCalendar stringToDate(String date)	//--> Tar imot en streng og returnerer den som en dato.
	{
		String[] dates = date.split("\\Q.\\E");

		if(dates.length != 3)
			return null;

		int day = 0;
		int month = 0;
		int year = 0;

		try
		{
			day = Integer.parseInt(dates[0]);
			month = Integer.parseInt(dates[1]) - 1;
			year = Integer.parseInt(dates[2]);
		}
		catch(NumberFormatException nfe)
		{
			return null;
		}
		return new GregorianCalendar(year, month, day);
	}

// ------------------------------------------------------------------------------------------I/O-METODER:

	public static ImageIcon getIcon(String path)	//--> Tar imot en filsti til et bilde og returnerer et bilde opprettet ved hjelp av class.getResource().
	{
		String imageFile = "";

    	if(path.equals(""))
    		imageFile = "/Images/Icon.png";
    	else
    		imageFile = path;

    	URL source = Utilities.class.getResource(imageFile);
    	if(source != null)
    	{
			ImageIcon imageIcon = new ImageIcon(source);
			return imageIcon;
		}
		return null;
	}

	public static ImageIcon getImage(String path)	//--> Tar imot en filsti til et bilde og returnerer et bilde opprettet ved hjelp av Toolkit.
	{
		File file = new File(path);
		if(!file.exists())
			return null;
		return new ImageIcon(Toolkit.getDefaultToolkit().getImage(path));

		// For at programmet skal finne bildefilene utenfor jar filen etter at vi oppretter en kjørbar jar fil
		// er vi nødt til å bruke denne metoden for å hente bilder, og filstien må da være f.eks (Images/bilde.jpg) ikke (/Images/bilde.jpg).
		// Dette blir altså gjeldene for bilder av artiser, lokaler, osv., men IKKE ikonfilene etc. som pakkes med jar-filen.
	}

	public static void setIcon(JFrame window)	//--> Tar imot et vindu og setter standard ikon på det.
	{
    	String imageFile = "/Images/IconSmall.png";
    	URL source = Utilities.class.getResource(imageFile);
    	if(source != null)
    	{
			ImageIcon imageIcon = new ImageIcon(source);
			Image icon = imageIcon.getImage();
			window.setIconImage(icon);
		}
	}

	public static void setPath(JFrame window, JTextField field, int type)	//--> Åpner en filvelger og et tekstfelt og setter valgt filsti inn i tekstfeltet.
	{
		String buttontext = "Velg Fil/Mappe";

		if(type == JFileChooser.DIRECTORIES_ONLY)
			buttontext = "Velg Mappe";
		if(type == JFileChooser.FILES_ONLY)
			buttontext = "Velg Fil";

		JFileChooser filechooser = new JFileChooser();
		filechooser.setFileSelectionMode(type);

 		FileFilter filter = new FileNameExtensionFilter("JPEG fil", "jpg", "jpeg");
 		filechooser.setFileFilter(filter);

		if(field.getText().equals(""))
			filechooser.setCurrentDirectory(new File("."));
		else
			filechooser.setCurrentDirectory(new File(field.getText()));

		int resultat = filechooser.showDialog(window, buttontext);

		if( resultat == JFileChooser.APPROVE_OPTION)
		{
			File file = filechooser.getSelectedFile();
			field.setText(file.getAbsolutePath());
		}
	}

	public static File setPath(JFrame window)	//--> Åpner en filvelger for bilder, og returnerer valgt fil.
	{
		JFileChooser filechooser = new JFileChooser();
		filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

 		FileFilter filter = new FileNameExtensionFilter("JPEG fil", "jpg", "jpeg");
 		filechooser.setFileFilter(filter);

		filechooser.setCurrentDirectory(new File("."));

		int resultat = filechooser.showDialog(window, "Velg Fil");

		if( resultat == JFileChooser.APPROVE_OPTION)
		{
			return filechooser.getSelectedFile();
		}
		return null;
	}

	public static <T extends NameComparable> void google(T a)	//--> Åpner standard nettleser og søker google etter parameter.
	{
		try
		{
			String tmp = a.getName().replace(" ", "+");		// Erstatter mellomrom med + slik at det blir en gyldig søkestreng.
			String searchString = "http://www.google.com/search?hl=en&q=" + tmp;
			Desktop desktop = java.awt.Desktop.getDesktop();
            URI uri = new URI( searchString );
            desktop.browse( uri );
		}
		catch(NullPointerException npe)
		{
			IOActions.writeErrorLog(npe);
		}
		catch(Exception l)
		{
			showError(null, null);
			IOActions.writeErrorLog(l);
		}
	}


} // End of class Utilities