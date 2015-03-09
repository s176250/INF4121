/*
 *	Innehold:  Metoder oppdatering av innhold og manipulering av en JTable:
 *
 *				getConcerts()                   	--> Returnerer hele konsertlisten.
 * 				addConcert()						--> Lager en rad ut av data fra en konsert og legger den til i tabellen.
 *				addArtist()							--> Lager en rad ut av data fra en artist og legger den til i tabellen.
 *				addVenue()							--> Lager en rad ut av data fra et lokale og legger den til i tabellen.
 *				add()								--> Legger til en rad i tabellen, hvis den tar imot en Artist, Venue eller Concert.
 *				add()								--> Tar imot en array(med strenger) som representerer en rad og legger den til i tabellen.
 *				addSet()							--> Tar imot et set og forsøker å legge alle elementene til i tabellen.
 *				setSet()							--> Tar imot et set, tømmer tabellen og forsøker så å legge til alle elementene.
 *				clear()								--> Fjerner radene fra tabellen.
 *				clearColumns()						--> Fjerner kolonnener fra visningen av tabellen.
 *				getTableColumns()					--> Returnerer alle kolonnene.
 *				setSelectionInterval()				--> Setter valgte rader fra rad til rad.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 13.05.2012.
 */

package eplanner.windows.components;

import eplanner.Artist;
import eplanner.Concert;
import eplanner.Venue;
import eplanner.utilities.Utilities;
import eplanner.windows.SearchPanel;
import java.util.Set;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/* Hensikt: Klassen har som hensikt å fungere som en bekvemelighetsklasse for å øke effektiviteten
			rundt bruk av JTables. Klassen integrerer en DefaultTableModel slik at man kan oppdatere
			innholdet direkte ved å kalle metoder på en ExtendedJTable. Den er også tilpasset
			slik at man kan legge til hele Set av type Artist, Concert og Venue, og få det
			korrekt vist i tabellen.
*/
@SuppressWarnings("serial")
public class ExtendedJTable extends JTable
{
	private DefaultTableModel model;	//Model for innholdet.

	//Konstruktør 1
	public ExtendedJTable(String[] columnNames)
	{
		super();
		model = new DefaultTableModel(columnNames, 0);
		setModel(model);
	}

	//Konstruktør 2
	public ExtendedJTable()
	{
		super();
		model = new DefaultTableModel();
		setModel(model);
	}

//---------------------------------------------------------------------Legg til/Fjern Metoder:

	/*
	Metodene addConcert, addArtist og addVenue baserer seg på følgende rekkefølge av kolonner:
	{Type, Navn, Dato, Webside, Aktiv, Aktve konserter, Kapasitet, Solgte, Lokale, Hovedartist}

	For å legge til flere typer objekter i tabellen lag en 'addObjekt' metode som følger samme
	mønster som addConcert, deretter legg den til i add(Object data) metoden.
	*/


	public void addConcert(Concert data)	//--> Lager en rad ut av data fra en konsert og legger den til i tabellen.
	{
		if(data != null)
		{
			String active = data.getActive()?"Nei" : "Ja";

			String[] concert =
			{
				SearchPanel.CONCERT,
				data.getName(),
				Utilities.getStringDate(data.getDate()) + " " + data.getStartTime(),
				"",
				active,
				"",
				data.getMaxTickets() + "",
				data.getAvailableTickets() + "",
				data.getVenue().getName(),
				data.getArtist().getName()
			};
			model.addRow(concert);
		}
	}

	public void addArtist(Artist data)	//--> Lager en rad ut av data fra en artist og legger den til i tabellen.
	{
		if(data != null)
		{
			String active = null;
			int acNr = data.getActive();
			if(acNr > 0) active = "Ja";
			else active = "Nei";

			String[] artist =
			{
				SearchPanel.ARTIST,
				data.getName(),
				"",
				data.getWebsite() + "",
				active,
				acNr + "",
				"",
				"",
				"",
				""
			};
			model.addRow(artist);
		}
	}

	public void addVenue(Venue data)	//--> Lager en rad ut av data fra et lokale og legger den til i tabellen.
	{
		if(data != null)
		{
			String active = null;
			int acNr = data.getActive();
			if(acNr > 0) active = "Ja";
			else active = "Nei";

			String[] venue =
			{
				SearchPanel.VENUE,
				data.getName(),
				"",
				data.getWebsite() + "",
				active,
				acNr + "",
				data.getMaxCapacity() + "",
				"",
				"",
				""
			};
			model.addRow(venue);
		}
	}

	public void add(Object data)	//--> Legger til en rad i tabellen, hvis den tar imot en Artist, Venue eller Concert.
	{
		if(data instanceof Artist)
			addArtist((Artist) data);
		else if(data instanceof Concert)
			addConcert((Concert) data);
		else if(data instanceof Venue)
			addVenue((Venue) data);
	}

	public void add(String[] data)	//--> Tar imot en array(med strenger) som representerer en rad og legger den til i tabellen.
	{
		if(data != null)
		{
			model.addRow(data);
		}
	}

	public <E> void addSet(Set<E> data)	//--> Tar imot et set og forsøker å legge alle elementene til i tabellen.
	{
		if(data != null)
		{
			for(E element : data)
				add(element);
		}
	}

	public <E> void setSet(Set<E> data)	//--> Tar imot et set, tømmer tabellen og forsøker så å legge til alle elementene.
	{
		clear();
		if(data != null)
		{
			for(E element : data)
				add(element);
		}
	}


//-----------------------------------------------------------------------------------Andre Metoder
	public void clear()		//--> Fjerner radene fra tabellen.
	{
		model.setRowCount(0);;
	}

	public void clearColumns(TableColumn[] tableColumns)	//--> Fjerner kolonnener fra visningen av tabellen.
	{
		for(int i = 0; i < tableColumns.length; i++)
		{
			removeColumn(tableColumns[i]);
		}
	}

	public TableColumn[] getTableColumns()	//--> Returnerer alle kolonnene.
	{
		TableColumn[] tableColumns = new TableColumn[getColumnCount()];

		for(int i = 0; i < getColumnCount(); i++)
		{
			tableColumns[i] = getColumnModel().getColumn(i);
		}
		return tableColumns;
	}

	public void setSelectionInterval(int rowNr, int rowNr2 )	//--> Setter valgte rader fra rad til rad.
	{
		ListSelectionModel model2 = getSelectionModel();
		model2.setSelectionInterval( rowNr, rowNr2 );
	}

} // End of class ExtendedJTable