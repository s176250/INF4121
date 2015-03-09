/*
 *	Innehold: Kode for å opprette og drive visningen av Events<Concert> i programmet.
 *
 *				Konstruktør						--> Inneholder den grunnleggende borderlayouten og er delt inn i de forskjellige seksjonene med kommentarer.
 *				displayConcert()				--> Endrer visningen av en konsert i centerpanelet avhengig av hva du har valgt i JListen i vestre panel.
 *				drawWestPanel()					--> Lager panelet for JListen på vestre side av vinduet
 *				deleteConcert()					--> Sletter konserten som er valgt i JListen.
 *				disableButtons()				--> Setter knappene i sørpanelet til å være disabled.
 *				getSelected()					--> Returnerer valgt event i JListen.
 *				editSelected()					--> Oppretter en endre-fane for den konserten som er valgt i JListen når man trykker på endreknappen.
 *				openWebsite()					--> Åpner websiden med innkommen URI.
 *				repaintConcertView()			--> Fjerner, putter inn og revaliderer innholdet i center-panelet.
 *				setDefaultSelectedConcert()		--> Setter den øvertste konserten i listen til å være valgt når panelet tegnes.
 *				setSelected()					--> Setter den konsertensom kommer inn som parameter til å være valgt i JListen.
 *				updateConcertJList()			--> Oppdaterer innholdet i JListen.
 *				resizeImage()					--> Resizer bildet til eventen etter størrelse til JLabelen det ligger inni.
 *													Kalkulerer høyde go breddeforhold slik at bildet blir forstørret med originalforholdet,
 *													og ikke større enn størrelsen til originalbildet.
 *													Setter også parameterene for å tegne bildet slik at bildet kommer ut med best mulig kvalitet.
 *		|class| ConcertListListener 			--> Lytteklasse for JListen.
 *		|class| ConcertListener  				--> ActionListener
 *		|class| ConcertMouseListener  			--> Muselytter som hovedsakelig endre utseandet på musen når man kommer over noe som leder til en nettside,
 *													og som deretter kaller opp metoden som åpner nettsiden.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 13.05.2012.
 */

package eplanner.windows;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import java.net.URI;
import java.awt.Desktop;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.util.Date;

//--------------------------------- Eplanner import-setninger
import eplanner.users.*;
import eplanner.utilities.*;
import eplanner.windows.components.*;
import eplanner.*;

//	Hensikt: Å vise panelet som viser informasjon om en event.
@SuppressWarnings("serial")
public class ConcertPanel extends JPanel
{
	// GUI komponenter
		private ExtendedJList<Concert> concertJList;
		private JButton editButton, newButton, deleteButton;
		protected JButton reviewButton, extraButton;
		private JLabel websiteLabel;
		private JLabel mainArtistLabel, mainArtistLabelSmall, secArtistLabel, vNameLabel;
		private JLabel artistWebsiteLabel, venueWebsiteLabel;
		protected JLabel concertThumb;
		private JPanel centerPanel, westPanel;
		private JTextArea reviewOutput, descOutput;

	// Lister
		private ArtistList artistList;
		private ConcertList concertList;
		private VenueList venueList;
		private Concert[] concertArray;

	// Lyttere
		private ConcertListListener listListener;
		private ConcertListener actionListener;
		private ConcertMouseListener mouseListener;
		private DefaultListModel<String> venueJLModel;
		private EPlannerMainWindow parent;

	//Eventens bilde og origanal bredde og høyde på det.
		protected ImageIcon concertImage;
		private double imageH, imageW;


	//Konstruktør
	public ConcertPanel(EPlannerMainWindow parent, VenueList venueList, ArtistList artistList, ConcertList concertList)
	{
		super(new BorderLayout());

		//Mottak av variabler fra parameterene.
			this.venueList = venueList;
			this.artistList = artistList;
			this.concertList = concertList;
			this.parent = parent;

		//Setter størrelse og farge på panelet.
			setPreferredSize(new Dimension(810, 700));
			setBackground(Color.WHITE);
			setVisible(true);

		//JLabel
			concertThumb = new JLabel(concertImage);

		//Lyttere
			actionListener = new ConcertListener();
			listListener = new ConcertListListener();
			mouseListener = new ConcertMouseListener();

		//JTextArea
			reviewOutput = new JTextArea(13,20);
			reviewOutput.setLineWrap(true);
			reviewOutput.setWrapStyleWord(true);
			descOutput = new JTextArea(16,20);
			descOutput.setLineWrap(true);
			descOutput.setWrapStyleWord(true);
			descOutput.setEditable(false);


		//Buttons
			reviewButton = new JButton("Anmeldelse");
			reviewButton.addActionListener(actionListener);
			extraButton = new JButton("Billettinfo");
			extraButton.addActionListener(actionListener);
			editButton = new JButton("Endre");
			editButton.addActionListener(actionListener);
			newButton = new JButton("Ny");
			newButton.addActionListener(actionListener);
			deleteButton = new JButton("Slett");
			deleteButton.addActionListener(actionListener);

	//------------------------------------------------------------------------------------- LINE START (West Panel)

		drawWestPanel();

	//------------------------------------------------------------------------------------- Center Panel
		centerPanel = new JPanel(new GridLayout(2,2,15,20));

		centerPanel.setMinimumSize(new Dimension(500, 500));
		centerPanel.setBackground(Color.WHITE);
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10,0,15,15));

		add(centerPanel, BorderLayout.CENTER);

	//--------------------------------------------------------------------------------PAGE END (South Panel) --> buttons
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		southPanel.setBackground(Color.WHITE);
		southPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,60));

		southPanel.add(newButton);
		southPanel.add(editButton);
		southPanel.add(deleteButton);


		add(southPanel, BorderLayout.PAGE_END);
	}// END konstruktør.

//------------------------------------------------------------------------------------------------------------------ GUI metoder

	protected void displayConcert(Concert concert)// --> Endrer visningen av en konsert i centerpanelet avhengig av hva du har valgt i JListen i vestre panel.
	{
			Artist artist = concert.getArtist();
			String secArtist = concert.getSecArtistNames();
			Venue venue = concert.getVenue();

		//--------------------------------------------------------------------------------TopLeft
			JPanel leftTopGrid = new JPanel(new BorderLayout());
			leftTopGrid.setBackground(Color.WHITE);

			//Overskrift
			JLabel concertName = new JLabel(concert.getName() + "  -  " + Utilities.getStringDate(concert.getDate()));
			concertName.setFont(new Font("Arial",Font.BOLD, 20 ));
			concertName.setBorder(BorderFactory.createEmptyBorder(0,0,15,0));
			leftTopGrid.add(concertName, BorderLayout.PAGE_START);

			//Bilde av venue
			resizeImage(concertThumb, 0, 0);
			concertThumb = new JLabel(concertImage);
			concertThumb.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			leftTopGrid.add(concertThumb, BorderLayout.CENTER);

			//Knapper for å legge til nye bilder/video
			JPanel buttonGridTopLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
			buttonGridTopLeft.setBackground(Color.WHITE);
			buttonGridTopLeft.add(reviewButton);
			buttonGridTopLeft.add(extraButton);
			leftTopGrid.add(buttonGridTopLeft, BorderLayout.PAGE_END);
			centerPanel.add(leftTopGrid);

		//--------------------------------------------------------------------------------TopRight
			JPanel rightTopGrid = new JPanel(new BorderLayout());

			//Beskivelse feltet
			descOutput.setText(concert.getDescription());
			descOutput.setCaretPosition(0);
			JScrollPane scrollDescOutput = new JScrollPane(descOutput);
			scrollDescOutput.setBackground(Color.WHITE);
			scrollDescOutput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Beskrivelse"));

			//Oppsummering feltet i panelet
			Box southRightTop = new Box(BoxLayout.PAGE_AXIS);
			southRightTop.setBackground(Color.WHITE);
			southRightTop.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Oppsummering"));

			southRightTop.add(new JLabel("Dato:   " + Utilities.getStringDate(concert.getDate() )));
			southRightTop.add(new JLabel("Tid:   " + concert.getStartTime()));
			southRightTop.add(new JLabel("Pris:   " + concert.getDefaultPrice()));
			southRightTop.add(new JLabel("Ledige bill.   " + concert.getAvailableTickets()));
			southRightTop.add(new JLabel("Ledige res. bill.:   " + concert.getAvailableReserved()));
			southRightTop.add(new JLabel("Ansvarlig:   " + concert.getPlanner().getName()));

			mainArtistLabelSmall =  new JLabel("Hovedartist:   " + artist.getName());
			mainArtistLabelSmall.addMouseListener(mouseListener);
			southRightTop.add(mainArtistLabelSmall);

			secArtistLabel =  new JLabel("Andre Artister:   " + secArtist);
			southRightTop.add(secArtistLabel);

			rightTopGrid.add(southRightTop, BorderLayout.PAGE_END);
			rightTopGrid.add(scrollDescOutput, BorderLayout.CENTER);

			rightTopGrid.setBackground(Color.WHITE);
			centerPanel.add(rightTopGrid);

		//--------------------------------------------------------------------------------BottomLeft -> Viser info om lokalet knyttet til eventen.
			Box bottomLeftBox = new Box(BoxLayout.PAGE_AXIS);
			bottomLeftBox.setBackground(Color.WHITE);
			bottomLeftBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Lokale"));

			String website = venue.getWebsite() + "";
			venueWebsiteLabel = new JLabel("<html>Webside:   <font color='blue'><u>" + website + "</u></font> </html>");
			if(!website.equals(""))
			{
				venueWebsiteLabel.addMouseListener(mouseListener);
				venueWebsiteLabel.setToolTipText("Åpne i standard nettleser..");
			}

			vNameLabel = new JLabel(venue.getName());
			vNameLabel.setFont(new Font("Arial",Font.BOLD, 17 ));
			vNameLabel.addMouseListener(mouseListener);
			vNameLabel.setBorder(BorderFactory.createEmptyBorder(5,0,10,0));
			vNameLabel.setToolTipText("Vis");

			bottomLeftBox.add(vNameLabel);
			bottomLeftBox.add(new JLabel("Adresse:   " + venue.getAdr()));
			bottomLeftBox.add(new JLabel("Telefon:   " + venue.getTel()));
			bottomLeftBox.add(new JLabel("Epost:   " + venue.getEmail()));
			bottomLeftBox.add(venueWebsiteLabel);
			bottomLeftBox.add(new JLabel("Maks Kapasitet:   " + venue.getMaxCapacity()));
			bottomLeftBox.add(new JLabel("Sjangere:   " + venue.genresToString()));

			centerPanel.add(bottomLeftBox);

		//--------------------------------------------------------------------------------BottomRight -> Viser info om artisten knyttet til eventen.
			Box bottomRightBox = new Box(BoxLayout.PAGE_AXIS);
			bottomRightBox.setBackground(Color.WHITE);
			bottomRightBox.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Hovedartist"));

			String websiteA = artist.getWebsite() + "";
			artistWebsiteLabel = new JLabel("<html>Webside:   <font color='blue'><u>" + websiteA + "</u></font> </html>");
			if(!websiteA.equals(""))
			{
				artistWebsiteLabel.addMouseListener(mouseListener);
				artistWebsiteLabel.setToolTipText("Åpne i standard nettleser..");
			}

			mainArtistLabel =  new JLabel(artist.getName());
			mainArtistLabel.setFont(new Font("Arial",Font.BOLD, 17 ));
			mainArtistLabel.addMouseListener(mouseListener);
			mainArtistLabel.setBorder(BorderFactory.createEmptyBorder(5,0,10,0));
			mainArtistLabel.setToolTipText("Vis");
			bottomRightBox.add(mainArtistLabel);

			if(artist instanceof Band)
			{
				Band tmp = (Band) artist;
				bottomRightBox.add(new JLabel("Bandmedlemmer:   " + tmp.bandmembersToString()));
				bottomRightBox.add(new JLabel("Nationalitet:   " + tmp.getNationality()));
				bottomRightBox.add(artistWebsiteLabel);
			}
			else
			{
				Soloartist tmp = (Soloartist) artist;
				bottomRightBox.add(new JLabel("Instrumenter:   " + tmp.getInstruments()));
				bottomRightBox.add(artistWebsiteLabel);
			}

			bottomRightBox.add(new JLabel("Sjangere:   " + artist.genresToString()));
			centerPanel.add(bottomRightBox);


	}

	private void drawWestPanel()// --> Lager panelet for JListen på vestre side av vinduet
	{
		westPanel = new JPanel(new GridLayout(1,1));

		JScrollPane listScroll = new JScrollPane(westPanel);
		listScroll.setBackground(Color.WHITE);
		listScroll.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

		concertJList = new ExtendedJList<>();
		updateConcertJList();

		concertJList.addListSelectionListener(listListener);
		concertJList.setFixedCellHeight(20);
		concertJList.setFixedCellWidth(190);
		concertJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		concertJList.setFont(new Font("Arial",Font.PLAIN, 14 ));
		concertJList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1), "Events"));

		westPanel.add(concertJList);

		add(listScroll, BorderLayout.LINE_START);
	}

//------------------------------------------------------------------------------------------------------------------ Funkjsonsmetoder


	public void deleteConcert()// --> Sletter konserten som er valgt i JListen.
	{
		Concert del = concertJList.getSelectedValue();

		if(del != null)
		{
			int answer = Utilities.showOption(parent, "Er du sikker på at du vil slette " + del.getName() + "\nDette vil slett eventen fra ALLE artister og lokaler denne er knyttet til.");
			if(answer == JOptionPane.YES_OPTION)
			{
				concertList.removeConcert(venueList, del, artistList);
				parent.updateConcertJList();
				centerPanel.removeAll();
				centerPanel.getRootPane().revalidate();
				parent.concertTabs.setSelectedComponent(parent.concertNewPanel);
				parent.concertTabs.setSelectedComponent(parent.concertPanel);
				Utilities.showInfo(parent, del.getName() + " er slettet.");
				new TicketInfoWindow(parent, del);
			}
		}
	}

	protected void disableButtons()// --> Setter knappene i sørpanelet til å være disabled.
	{
		editButton.setEnabled(false);
		newButton.setEnabled(false);
		deleteButton.setEnabled(false);
	}

	protected void editSelected()// --> Oppretter en endre-fane for den konserten som er valgt i JListen når man trykker på endreknappen.
	{
		Concert tmp = concertJList.getSelectedValue();;
		if(tmp != null)
			parent.addConcertEditTab(tmp);
	}

	protected Concert getSelected()// --> Returnerer valgt event i JListen.
	{
		return concertJList.getSelectedValue();
	}

	public void openWebsite(URI web)// --> Åpner websiden med innkommen URI.
	{
		try
		{
			Desktop desktop = Desktop.getDesktop();
            desktop.browse(web);
		}
		catch(Exception l){Utilities.showError(parent, "Ugyldig nettadresse.");}
	}

	public void repaintConcertView()// --> Fjerner, putter inn og revaliderer innholdet i center-panelet.
	{
		Concert tmpConcert = concertJList.getSelectedValue();

		if(tmpConcert == null)
			return;

		centerPanel.removeAll();
		displayConcert(concertJList.getSelectedValue());
		centerPanel.getRootPane().revalidate();
	}


	public void setDefaultSelectedConcert()// --> Setter den øvertste konserten i listen til å være valgt når panelet tegnes.
	{
		if(!concertList.isEmpty())
			concertJList.setSelectedIndex(0);
	}

	public void setSelected(Concert selected)// --> Setter den konsertensom kommer inn som parameter til å være valgt i JListen.
	{
		if(!concertList.isEmpty())
			concertJList.setSelectedValue(selected, true);
	}

	public void updateConcertJList()// --> Oppdaterer innholdet i JListen.
	{
		concertJList.setContent(concertList.getArray());
	}

//------------------------------------------------------------------------------------------------------------------ Bildemetode
	/*
	 * Resizer bildet til eventen etter størrelse til JLabelen det ligger inni.
	 * Kalkulerer høyde go breddeforhold slik at bildet blir forstørret med originalforholdet,
	 * og ikke større enn størrelsen til originalbildet.
	 * Setter også parameterene for å tegne bildet slik at bildet kommer ut med best mulig kvalitet.
	 */
	protected void resizeImage(JLabel container, int height, int width)
	{
		Concert tmpConcert = concertJList.getSelectedValue();

		if(tmpConcert.getThumbImagePath().equals(""))
		{
			return;
		}

		if(height == 0)
			height = container.getHeight();

		if(width == 0)
			width = container.getWidth();

		if( height == 0 || width == 0)
			return;

        BufferedImage readImage = null;
        try
        {
			File img = new File("Images/Concerts/" + tmpConcert.getName() + "/" + tmpConcert.getName() + ".jpg");
			if(img.exists())
				readImage = ImageIO.read(img);
			imageH = readImage.getHeight();
			imageW = readImage.getWidth();
			if(imageH < 290 && imageW < 215)
				return;
		}
		catch (Exception e)
		{IOActions.writeErrorLog(e);}

		double containerH = height;
		double containerW = width;

		if(imageW > imageH)	// --------------------------------Hvis bildets orginalbredde er større enn høyden skal bredde bestemme størrelsen og vica versa.
		{
			// Bredde bestemmer størrelsen

			if(containerW < imageW) // --------------------------Er rammens størrelse større enn bildets orginalstørrelse?
			{	// -----------------------------------------------Bildet blir skalert.
				containerH = (containerW/imageW) * imageH;
				height = (int) containerH;
			}
			else
			{	//------------------------------------------------Bildets orginale størrelse blir brukt.
				height = (int) imageH;
				width = (int) imageW;
			}
		}
		else
		{
			// Høyde bestemmer Størrelsen

			if(containerH < imageH) // --------------------------Er rammens størrelse større enn bildets orginalstørrelse?
			{	// -----------------------------------------------Bildet blir skalert.
				containerW = (containerH/imageH) * imageW;
				width = (int) containerW;
			}
			else
			{	//------------------------------------------------Bildets orginale størrelse blir brukt.
				height = (int) imageH;
				width = (int) imageW;
			}
		}

		concertImage =tmpConcert.getThumbImage();

		Image image = concertImage.getImage();

		// Create empty BufferedImage, sized to Image
		BufferedImage buffImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw Image into BufferedImage
		Graphics g = buffImage.getGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();

		BufferedImage resizedImageBeffered = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB );
		Graphics2D tmpGraphic = resizedImageBeffered.createGraphics();

		tmpGraphic.setComposite(AlphaComposite.Src);																//-------> For å få bedre bildekvalitet.
		tmpGraphic.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);	//
		tmpGraphic.setRenderingHint(RenderingHints.KEY_RENDERING,RenderingHints.VALUE_RENDER_QUALITY);				//
		tmpGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);				//

		tmpGraphic.drawImage(buffImage, 0, 0, width, height, null);
		tmpGraphic.dispose();

		ImageIcon resizedImage = new ImageIcon(resizedImageBeffered);

		concertImage = resizedImage;
	}

//------------------------------------------------------------------------------------------------------------------ Lyttere

	private class ConcertListListener implements ListSelectionListener// --> Lytteklasse for JListen.
	{
		public void valueChanged(ListSelectionEvent e)
		{
			if (e.getValueIsAdjusting() == false)
			{
				int selected = concertJList.getSelectedIndex();
				if (selected == -1)
				{
				}
				else
				{
					concertImage = concertJList.getSelectedValue().getThumbImage();
					repaintConcertView();
				}
			}
		}
	}//End of Listener class



	private class ConcertListener implements ActionListener// --> ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if( e.getSource() == deleteButton)
			{
				deleteConcert();
			}
			else if(e.getSource() == editButton)
			{
				editSelected();
			}
			else if(e.getSource() == reviewButton)
			{
				new ReviewWindow(parent, concertJList.getSelectedValue());
			}
			else if( e.getSource() == extraButton)
			{
				new TicketInfoWindow(parent, concertJList.getSelectedValue());
			}
		}
	}//End of Listener class

	/*
	 * Muselytter som hovedsakelig endrer utseandet på musen når man kommer over noe som leder til en nettside,
	 * og som deretter kaller opp metoden som åpner nettsiden.
	 */
	private class ConcertMouseListener implements MouseListener
	{
		@Override
		public void	mouseClicked(MouseEvent e)
		{
			if(e.getSource() == artistWebsiteLabel)
				openWebsite(concertJList.getSelectedValue().getArtist().getWebsite());
			else if(e.getSource() == venueWebsiteLabel)
				openWebsite(concertJList.getSelectedValue().getVenue().getWebsite());
			else if(e.getSource() == mainArtistLabel || e.getSource() == mainArtistLabelSmall)
				parent.openArtist(concertJList.getSelectedValue().getArtist());
			else if(e.getSource() == vNameLabel)
				parent.openVenue(concertJList.getSelectedValue().getVenue());
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{
			parent.setCursor(EPlannerMainWindow.HANDCURSOR);
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
			parent.setCursor(EPlannerMainWindow.NORMALCURSOR);
		}

		@Override
		public void mousePressed(MouseEvent e)
		{
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
		}
	}


} // End of class Myclass

