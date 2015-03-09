/*
 *	Innehold:  Konstruktører og metoder for å forenkle opprettelsen og bruke av JSpinner
 				Konstruktører						--> Multiple kostruktører for enkel oppretting av objekter.
 * 				getInt()							--> Returnerer verdien i JSpinneren i form av en int.
 * 				setMaximum()						--> Setter maksimumsverdien tillatt på spinneren.
 *				setMinimum()						--> Setter minimumsverdien tillatt på spinneren.
 *				setValue()							--> Setter verdien på spinneren.
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 13.05.2012.
 */

package eplanner.windows.components;

import javax.swing.*;

// Hensikt: Klassen fungerer som en bekvemlighetsklasse for å opprette JSpinners

public class ExtendedJSpinner extends JSpinner
{
	private SpinnerNumberModel model;
	private static final long serialVersionUID = 1337L;

	public ExtendedJSpinner(int value, int min, int max, int interval)
	{
		super();
		model = new SpinnerNumberModel(value, min, max, interval);
		setModel(model);
	}

	public ExtendedJSpinner(int value, int min, int max)
	{
		super();
		model = new SpinnerNumberModel(value, min, max, 1);
		setModel(model);
	}

	public ExtendedJSpinner(int min, int max)
	{
		super();
		model = new SpinnerNumberModel(min, min, max, 1);
		setModel(model);
	}

	public int getInt()// --> Returnerer verdien i JSpinneren i form av en int.
	{
		return model.getNumber().intValue();
	}

	public void setMaximum(int max)// --> Setter maksimumsverdien tillatt på spinneren.
	{
		model.setMaximum(max);
	}

	public void setMinimum(int min)// --> Setter minimumsverdien tillatt på spinneren.
	{
		model.setMinimum(min);
	}

	public void setValue(int value)// --> Setter verdien på spinneren.
	{

		model.setValue(value);
	}
}