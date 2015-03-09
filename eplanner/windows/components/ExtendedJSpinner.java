/*
 *	Innehold:  Konstrukt�rer og metoder for � forenkle opprettelsen og bruke av JSpinner
 				Konstrukt�rer						--> Multiple kostrukt�rer for enkel oppretting av objekter.
 * 				getInt()							--> Returnerer verdien i JSpinneren i form av en int.
 * 				setMaximum()						--> Setter maksimumsverdien tillatt p� spinneren.
 *				setMinimum()						--> Setter minimumsverdien tillatt p� spinneren.
 *				setValue()							--> Setter verdien p� spinneren.
 *	Laget av: Jonas Moltzau og Martin L�kkeberg.
 *	Sist endret: 13.05.2012.
 */

package eplanner.windows.components;

import javax.swing.*;

// Hensikt: Klassen fungerer som en bekvemlighetsklasse for � opprette JSpinners

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

	public void setMaximum(int max)// --> Setter maksimumsverdien tillatt p� spinneren.
	{
		model.setMaximum(max);
	}

	public void setMinimum(int min)// --> Setter minimumsverdien tillatt p� spinneren.
	{
		model.setMinimum(min);
	}

	public void setValue(int value)// --> Setter verdien p� spinneren.
	{

		model.setValue(value);
	}
}