/*
 *	Innehold:  Konstrukt�rer for rammel�se knapper med ikon p�.
 *
 *	Laget av: Jonas Moltzau og Martin L�kkeberg.
 *	Sist endret: 13.05.2012.
 */

package eplanner.windows.components;

import javax.swing.*;
import java.awt.*;

// Hensikt: Klassen fungerer som en bekvemlighetsklasse for � opprette JButtons
//			uten rammer og tekst, kun et ikon p�.
@SuppressWarnings("serial")
public class IconButton extends JButton
{
	//Konstrukt�r for knapp med gr� bakgrunn og str. 30x27. brukes hovedsakelig til v�rkt�ylinjeknapper.
	public IconButton(Icon icon)
	{
		super(icon);
		setBackground(new Color(230, 230, 230, 230));
		setBorderPainted(false);
		setFocusPainted(false);
		setPreferredSize(new Dimension(30, 27));
	}

	// Konstrukt�r, som over, men tar imot bg farge.
	public IconButton(Icon icon, Color bgColor)
	{
		super(icon);
		setBackground(bgColor);
		setBorderPainted(false);
		setFocusPainted(false);
		setPreferredSize(new Dimension(30, 27));
	}

	// Konstrukt�r, som over, men tar imot bg farge og str.
	public IconButton(Icon icon, Color bgColor, Dimension size)
	{
		super(icon);
		setBackground(bgColor);
		setBorderPainted(false);
		setFocusPainted(false);
		setPreferredSize(size);
	}

} // End of class IconButton