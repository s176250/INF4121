/*
 *	Innehold:  Konstruktører og metoder for å forenkle opprettelsen og bruke av JSpinner
 * 				addArray()							--> Legger til en array i JListen.
 * 				add()								--> Legger til et element i Listen.
 *				clear()								--> Fjerner alle elementer i listen.
 *				getScroll()							--> Returnerer listen i et JScrollPane.
 				setContent()						--> Fjerner alle elementer i listen og legger så til en array av elementer.
 				setSelection()						--> Setter valgt element i listen.
 				setSelections()						--> Setter valgte elementer i listen.
 				toArray()							--> Returnerer innholdet i listen som array.
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 13.05.2012.
 */

package eplanner.windows.components;

import javax.swing.*;
import java.awt.Dimension;
import java.util.*;
import eplanner.comparators.*;
import eplanner.*;

// Hensikt: Klassen fungerer som en bekvemlighetsklasse for å opprette JLists
@SuppressWarnings("serial")
public class ExtendedJList<E> extends JList<E>
{
	private DefaultListModel<E> model;

	public ExtendedJList(E[] data)
	{
		super();
		model = new DefaultListModel<>();
		setModel(model);
		addArray(data);
	}

	public ExtendedJList(E data)
	{
		super();
		model = new DefaultListModel<>();
		setModel(model);
		add(data);
	}

	public ExtendedJList(DefaultListModel<E> model)
	{
		super(model);
		this.model = model;
	}

	public ExtendedJList()
	{
		super();
		model = new DefaultListModel<>();
		setModel(model);
	}

	public void addArray(E[] data)// --> Legger til en array i JListen.
	{
		if(data != null)
		{
			for(E element : data)
				model.addElement(element);
		}
	}

	public void add(E data)// --> Legger til et element i Listen.
	{
		if(data != null)
		{
			model.addElement(data);
		}
	}

	public void clear()// --> Fjerner alle elementer i listen.
	{
		model.clear();
	}

	public JScrollPane getScroll()// --> Returnerer listen i et JScrollPane.
	{
		return new JScrollPane(this);
	}

	@Override
	public void remove(int index)// --> Fjerner inkomment elementer fra listen.
	{
		model.remove(index);
	}

	public void setContent(E[] data)// --> Fjerner alle elementer i listen og legger så til en array av elementer.
	{
		clear();
		addArray(data);
	}

	public void setContent(Set<E> data)// --> Fjerner alle elementer i listen og legger så til et sett av elementer.
	{
		clear();
		for(E element : data)
			add(element);
	}

	public <T extends NameComparable> void setSelection(T a)// --> Setter valgt element i listen.
	{
		Object[] data = model.toArray();
		for(int i = 0; i < data.length; i++)
		{
			if(data[i].toString().equals(a.toString()))
			{
				setSelectedIndex(i);
				return;
			}
		}
	}

	public void setSelection(List<E> data)// --> Setter valgt element i listen.
	{
		if(data.isEmpty())
			return;
		Object[] values = new Object[data.size()];
		int count = 0;
		for(E element: data)
			values[count++] = element;
		setSelections(values);
	}

	public <T> void setSelections(T[] a)// --> Setter valgte elementer i listen.
	{
		int[] tmpIndex = new int[a.length];
		Object[] data = model.toArray();
		String[] dataString = new String[a.length];

		int count = 0;

		for(int i = 0; i < data.length; i++)
		{
			if(count < a.length && data[i].toString().equals(a[count].toString()))
			{
					tmpIndex[count++] = i;
			}
		}

		setSelectedIndices(tmpIndex);
	}

	public Object[] toArray()// --> Returnerer innholdet i listen som array.
	{
		return model.toArray();
	}


} // End of class ExtendedJList