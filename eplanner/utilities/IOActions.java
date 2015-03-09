/*
 *	Innehold: Metoder for interaktering med filsystemet på datatamaskinen.
 *
 *		Spesifikke metoder:
 *			copyImage()					--> Kopierer et bilde fra et brukervalgt sted på PCen og legger det i den
 *											korresponderende mappen til det objektet som defineres av typeparameteren.
 *			createMediaPath()			-->	Oppretter mappestrukturen i filsystemet for lagring av media for brukeren med innkommen type og navn.
 *			readFromFile() 				--> Leser databasefilene inn i programmet.
 *			writeToFile()				--> Lagrer databsen til programmet på en fil.
 *			writeErrorLog()				--> Skriver en logg av feilmeldingen til en innkommen exception på en loggfil,
 *											med en bestemt formatering. Oppretter loggfilen hvis nødvendig.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 13.05.2012.
 */

package eplanner.utilities;

import javax.swing.*;
import java.io.*;
import javax.imageio.*;
import java.awt.image.*;
import eplanner.utilities.*;
import java.util.Calendar;
import eplanner.*;

//	Hensikt: Å interaktere med filsystemet for å lagre databsen til programmet og all annen nødvendig data.

public class IOActions implements Serializable
{
	private static final long serialVersionUID = 1337L;

	/*
	 * Kopierer et bilde fra et brukervalgt sted på PCen og legger det i
	 * den korresponderende mappen til det objektet som defineres av typeparameteren.
	 */
	public static void copyImage(File from, String name, String type)
	{
		File newFile = new File("Images/" + type + "/" + name + "/" + name + ".jpg");

		if(newFile.exists())
			newFile.delete();

		if(newFile.getParentFile().exists() && from.exists())
		{
			try
			{
				BufferedImage newImage = ImageIO.read(from);
				ImageIO.write(newImage, "jpg", newFile);
			}
			catch(IOException eio)
			{
				Utilities.showError(null, "Kunne ikke kopiere bilde.");
				writeErrorLog(eio);
			}
		}
	}

	/*
	 * Oppretter mappestrukturen i filsystemet for lagring av media for
	 * brukeren med innkommen type og navn.
	 */
	public static boolean createMediaPath(String name, String type)
	{
		File newFolder = new File("Images/" + type + "/" + name);

		if(!newFolder.exists())
		{
			newFolder.mkdirs();
			return true;
		}
		return false;
	}

	public static Database readFromFile(File path)// --> Leser databasefilene inn i programmet.
	{
		Database data = null;

		try( ObjectInputStream inFile = new ObjectInputStream(new FileInputStream(path)))
		{
			data = (Database) inFile.readObject();
			return data;
		}
		catch(ClassNotFoundException cnfe)
		{
			Utilities.showError(null, "Finner ikke kildefiler til programmet.");
			writeErrorLog(cnfe);
			System.exit(1);
		}
		catch(FileNotFoundException fnfe)
		{
			Utilities.showInfo(null, "Velkommen til ePlanner!\nOppretter ny database.");
			writeErrorLog(fnfe);
			data = new Database();
		}
		catch(IOException e)
		{
			writeErrorLog(e);
			int result = Utilities.showOption(null, "Feil under lesing av lagret fil. Vil du opprette ny database?");

			if(result == JOptionPane.YES_OPTION)
			{
				data = new Database();
			}
			else
				System.exit(1);
		}
		catch(Exception e)
		{
			Utilities.showError(null, null);
			writeErrorLog(e);
			System.exit(1);
		}
		finally
		{
			return data;
		}
	}

	public static void writeToFile(File path, Database data)// --> Lagrer databsen til programmet på en fil.
	{
		try(ObjectOutputStream write= new ObjectOutputStream (new FileOutputStream(path)))
		{
			write.writeObject(data);
		}
		catch(NotSerializableException nse)
		{
			Utilities.showError(null, "Strukturell feil på indre filstuktur! Ingen data lagret.");
			Utilities.showError(null, nse.getMessage());
			writeErrorLog(nse);
		}
		catch(IOException eio)
		{
			Utilities.showError(null, "Feil i lagring til fil.");
			writeErrorLog(eio);
		}
	}

	/*
	 * Skriver en logg av feilmeldingen til en innkommen exception på en loggfil,
	 * med en bestemt formatering.
	 * Oppretter loggfilen hvis nødvendig.
	 */
	public static void writeErrorLog(Throwable throwable)
	{
  		Writer writer = new StringWriter();
  		PrintWriter printWriter = new PrintWriter(writer);
  		throwable.printStackTrace(printWriter);

		File tmpFile = new File("Log/logFile.txt");

		if(!tmpFile.exists())
		{
			try
			{
				tmpFile.getParentFile().mkdirs();
				tmpFile.createNewFile();
			}
			catch(IOException p)
			{
				Utilities.showError(null, "Kunne ikke lage logg-fil");
			}
		}

		String logMessage = Utilities.getStringDate(Calendar.getInstance()) + " [" + Utilities.getStringTime(Calendar.getInstance()) + "]  -  "
							+ throwable.getMessage() + System.getProperty("line.separator")
							+ "-----------------------------" + System.getProperty("line.separator");

		logMessage += writer.toString() + System.getProperty("line.separator") + System.getProperty("line.separator") + System.getProperty("line.separator") ;

		try(BufferedWriter out = new BufferedWriter(new FileWriter(tmpFile, true)))
		{
			out.write(logMessage);
		}
		catch(IOException eio){}
	}

}