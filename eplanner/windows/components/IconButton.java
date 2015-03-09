/*
 *	Innehold:  Konstruktører for rammeløse knapper med ikon på.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 13.05.2012.
 */

package eplanner.windows.components;

import javax.swing.*;
import java.awt.*;

// Hensikt: Klassen fungerer som en bekvemlighetsklasse for å opprette JButtons
//			uten rammer og tekst, kun et ikon på.
@SuppressWarnings("serial")
public class IconButton extends JButton
{
	//Konstruktør for knapp med grå bakgrunn og str. 30x27. brukes hovedsakelig til værktøylinjeknapper.
	public IconButton(Icon icon)
	{
		super(icon);
		setBackground(new Color(230, 230, 230, 230));
		setBorderPainted(false);
		setFocusPainted(false);
		setPreferredSize(new Dimension(30, 27));
	}

	// Konstruktør, som over, men tar imot bg farge.
	public IconButton(Icon icon, Color bgColor)
	{
		super(icon);
		setBackground(bgColor);
		setBorderPainted(false);
		setFocusPainted(false);
		setPreferredSize(new Dimension(30, 27));
	}

	// Konstruktør, som over, men tar imot bg farge og str.
	public IconButton(Icon icon, Color bgColor, Dimension size)
	{
		super(icon);
		setBackground(bgColor);
		setBorderPainted(false);
		setFocusPainted(false);
		setPreferredSize(size);
	}

} // End of class IconButton