/*
 *	Innehold:  Metoder for oppretting av anmeldelser, samt visning av input-/konfigurasjonspanel for anmeldelser:
 *
 *				Konstruktør						--> Inneholder den grunnleggende borderlayouten og er delt inn i de forskjellige seksjonene med kommentarer.
 *				addStylesToDocument() 			--> Lager stiler som legges til dokumentet slik at tekstens attributter kan endres i henhold til satildefinisjonen.
   				close()							--> Lukker vinduet.
 *				save()							--> Lagrer anmeldelsen.
 *				setStyle()						--> Finner teksten som er merket og erstatter denne med samme tekst formatert med innkommen stil.
 *		|class| Listener					--> Actionlistener koblet til toolbarknappene
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 13.05.2012.
 */

package eplanner.windows;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyleConstants;
import javax.swing.text.Style;
import javax.swing.text.StyleContext;
import javax.swing.text.BadLocationException;
import eplanner.users.*;
import eplanner.windows.components.*;
import eplanner.utilities.*;
import eplanner.*;
import java.net.URL;

// Hensikt: Vise et vindu hvor brukeren kan endre/lage anmeldelser, hvor det er mulig å redigere tekstattributter;
@SuppressWarnings("serial")
public class ReviewWindow extends JFrame
{
	private Concert concert;
	private JTextPane textPane;
	private StyledDocument document;
	private JButton save, close, bold, underline, italic, regular, dice1, dice2, dice3, dice4, dice5, dice6;
	private EPlannerMainWindow parent;
	private JToolBar toolbar;
	private Listener listener;

	public ReviewWindow(EPlannerMainWindow parent, Concert concert) // --> Konstruktør
	{
		//Sender variabler til super og tar vare på en peker til hovedvinduet. samt lagrer event variabel fra super.
		super( concert.getName() + " - Lag/Endre anmeldelse");

		this.concert = concert;
		this.parent = parent;

		//Lytter
		listener = new Listener();

		//Layout
		setLayout(new BorderLayout());



	// IconButtons
		bold = new IconButton(Utilities.getIcon("/Images/Icons/bold.png"));
		bold.addActionListener(listener);
		italic = new IconButton(Utilities.getIcon("/Images/Icons/italic.png"));
		italic.addActionListener(listener);
		underline = new IconButton(Utilities.getIcon("/Images/Icons/underline.png"));
		underline.addActionListener(listener);
		save = new IconButton(Utilities.getIcon("/Images/Icons/saveIcon.png"));
		save.addActionListener(listener);
		close = new IconButton(Utilities.getIcon("/Images/Icons/closeIcon.png"));
		close.addActionListener(listener);
		regular = new IconButton(Utilities.getIcon("/Images/Icons/changeFont.png"));
		regular.addActionListener(listener);
		dice1 = new IconButton(Utilities.getIcon("/Images/Icons/terning_1.png"));
		dice1.addActionListener(listener);
		dice2 = new IconButton(Utilities.getIcon("/Images/Icons/terning_2.png"));
		dice2.addActionListener(listener);
		dice3 = new IconButton(Utilities.getIcon("/Images/Icons/terning_3.png"));
		dice3.addActionListener(listener);
		dice4 = new IconButton(Utilities.getIcon("/Images/Icons/terning_4.png"));
		dice4.addActionListener(listener);
		dice5 = new IconButton(Utilities.getIcon("/Images/Icons/terning_5.png"));
		dice5.addActionListener(listener);
		dice6 = new IconButton(Utilities.getIcon("/Images/Icons/terning_6.png"));
		dice6.addActionListener(listener);


	// JToolbar
		toolbar = new JToolBar("Verktøy");
		toolbar.add(save);
		toolbar.add(close);
		toolbar.add(bold);
		toolbar.add(italic);
		toolbar.add(underline);
		toolbar.add(regular);
		toolbar.add(dice1);
		toolbar.add(dice2);
		toolbar.add(dice3);
		toolbar.add(dice4);
		toolbar.add(dice5);
		toolbar.add(dice6);


		add(toolbar, BorderLayout.PAGE_START);

	// -------------------------------------------------------------------------------- Centerpanel

        textPane = new JTextPane();
        document = concert.getReview();

        if(document == null)
        {
        	document = textPane.getStyledDocument();
	        addStylesToDocument(document);
		}
		else
			textPane.setDocument(document);


		JScrollPane scroll = new JScrollPane(textPane);
		add(scroll, BorderLayout.CENTER);

//------------------------------------------------------------------------------------- Default Attributes / Vindusplassering
        setSize(450, 400);

		Utilities.setIcon(this);  // --> Her utnyttes metoden i Utilities-klassen for å sette et ikon på dette viduet.

        setLocationRelativeTo(parent);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

	}

//------------------------------------------------------------------------------------- Funksjonsmetoder

   // --> Lager stiler som legges til dokumentet slik at tekstens attributter kan endres i henhold til satildefinisjonen.
   	protected void addStylesToDocument(StyledDocument doc)
    {
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "Arial");

        Style style = doc.addStyle("italic", regular);
        StyleConstants.setItalic(style, true);

        style = doc.addStyle("bold", regular);
        StyleConstants.setBold(style, true);

        style = doc.addStyle("underline", regular);
        StyleConstants.setUnderline(style, true);

        style = doc.addStyle("small", regular);
        StyleConstants.setFontSize(style, 12);

		int count = 0;

		for(int i = 0; i < 6; i++)
		{
			String icon = "icon" + ++count;
			ImageIcon ic = Utilities.getIcon("/Images/Icons/terning_" + (count) + ".png");
			if(ic == null)
				System.err.println("/Images/Icons/terning_" + (count) + ".png");
			style = doc.addStyle(icon, regular);
			StyleConstants.setAlignment(style, StyleConstants.ALIGN_CENTER);
			StyleConstants.setIcon(style, ic);
		}

    }

	private void close()// --> Lukker vinduet.
	{
		this.dispose();
	}

	private void save()// --> Lagrer anmeldelsen.
	{
		try
		{
			setCursor(EPlannerMainWindow.WAITCURSOR);
			concert.setReview(document);
			Thread.sleep(100);
		}
		catch(Exception k){IOActions.writeErrorLog(k);}
		finally
		{
			setCursor(EPlannerMainWindow.NORMALCURSOR);
		}
	}

	private void setStyle(String style)// --> Finner teksten som er merket og erstatter denne med samme tekst formatert med innkommen stil.
	{
		String selected = " ";

		if(style.indexOf("icon") < 0)
			selected = textPane.getSelectedText();

		textPane.replaceSelection("");
		int caret = textPane.getCaretPosition();

		try
		{
			document.insertString(caret, selected, document.getStyle(style));
		}
		catch (BadLocationException ble)
		{
			Utilities.showError(parent, null);
			IOActions.writeErrorLog(ble);
		}

		textPane.requestFocusInWindow();

	}

//------------------------------------------------------------------------------------- Lytterklasser

	private class Listener implements ActionListener // --> Actionlistener koblet til toolbarknappene
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == bold)
			{
				setStyle("bold");
			}
			else if(e.getSource() == italic)
			{
				setStyle("italic");
			}
			else if(e.getSource() == underline)
			{
				setStyle("underline");
			}
			else if(e.getSource() == regular)
			{
				setStyle("regular");
			}
			else if(e.getSource() == dice1)
			{
				setStyle("icon1");
			}
			else if(e.getSource() == dice2)
			{
				setStyle("icon2");
			}
			else if(e.getSource() == dice3)
			{
				setStyle("icon3");
			}
			else if(e.getSource() == dice4)
			{
				setStyle("icon4");
			}
			else if(e.getSource() == dice5)
			{
				setStyle("icon5");
			}
			else if(e.getSource() == dice6)
			{
				setStyle("icon6");
			}
			else if(e.getSource() == close)
			{
				close();
			}
			else if(e.getSource() == save)
			{
				save();
			}
		}
	}// Class CommandListener /END

}// Class ReviewWindow /END