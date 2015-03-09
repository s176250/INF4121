/*
 *	Innehold: Kode for å opprette og drive visningen av Loakler<Venue> i programmet.
 *
 *				Konstruktør						--> Inneholder den grunnleggende borderlayouten og er delt inn i de forskjellige seksjonene med kommentarer.
 *				displayVenue()					--> Endrer visningen av et lokale i centerpanelet avhengig av hva du har valgt i JListen i vestre panel.
 *				drawWestPanel()					--> Lager panelet for JListen på vestre side av vinduet
 *				deleteVenue()					--> Sletter lokalet som er valgt i JListen.
 *				disableButtons()				--> Setter knappene i sørpanelet til å være disabled.
 *				getSelected()					--> Returnerer valgt lokale i JListen.
 *				editSelected()					--> Oppretter en endre-fane for den lokalet som er valgt i JListen når man trykker på endreknappen.
 *				openWebsite()					--> Åpner websiden med innkommen URI.
 *				repaintVenueView()				--> Fjerner, putter inn og revaliderer innholdet i center-panelet.
 *				setDefaultSelectedVenue()		--> Setter det øvertste lokalet i listen til å være valgt når panelet tegnes.
 *				setSelected()					--> Setter det lokalet som kommer inn som parameter til å være valgt i JListen.
 *				updateVenueJList()				--> Oppdaterer innholdet i JListen.
 *				resizeImage()					--> Resizer bildet til eventen etter størrelse til JLabelen det ligger inni.
 *													Kalkulerer høyde go breddeforhold slik at bildet blir forstørret med originalforholdet,
 *													og ikke større enn størrelsen til originalbildet.
 *													Setter også parameterene for å tegne bildet slik at bildet kommer ut med best mulig kvalitet.
 *		|class| VenueListListener 				--> Lytteklasse for JListen.
 *		|class| VenueListener  					--> ActionListener
 *		|class| VenueMouseListener  			--> Muselytter som hovedsakelig endre utseandet på musen når man kommer over noe som leder til en nettside,
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
import java.awt.Desktop;
import javax.swing.text.DefaultStyledDocument;
import java.awt.image.*;
import javax.imageio.ImageIO;
import java.util.Date;

//--------------------------------- Eplanner import-setninger
import eplanner.users.*;
import eplanner.utilities.*;
import eplanner.windows.components.*;
import eplanner.*;

//	Hensikt: Å vise panelet som viser informasjon om et lokale.
@SuppressWarnings("serial")
public class VenuePanel extends JPanel
{
	//JList
		private ExtendedJList<String> concertJList;
		private ExtendedJList<Venue> venueJList;
	//TextArea
		private JTextArea descOutput;
		private JTextPane reviewOutput;
	//Lister
		private VenueList venueList;

	//GUI komponenter
		private JPanel centerPanel, westPanel;
		private JButton picsButton, extraButton, editButton, newButton, deleteButton;
		private Concert[] concertArray;
		protected JLabel venueThumb;

	//Lyttere
		private VenueListListener listListener;
		private VenueListener actionListener;
		private VenueMouseListener mouseListener;

	//Parent
		private EPlannerMainWindow parent;
		private JLabel websiteLabel;

	//Lokalets bilde og origanal bredde og høyde på det.
		protected ImageIcon venueImage;
		private double imageH, imageW;


	public VenuePanel(EPlannerMainWindow parent, VenueList venueList)// --> Konstruktør
	{
		super(new BorderLayout());

		//Mottak av variabler fra parameterne.
			this.venueList = venueList;
			this.parent = parent;

		//Setter størrelse og farge på panelet.
			setPreferredSize(new Dimension(810, 700));
			setBackground(EPlannerMainWindow.BASE_COLOR);
			setVisible(true);

		//Lyttere
			actionListener = new VenueListener();
			listListener = new VenueListListener();
			mouseListener = new VenueMouseListener();

		//JLabel
			venueThumb = new JLabel(venueImage);

		//JTextArea
			reviewOutput = new JTextPane();
			descOutput = new JTextArea(16,20);
			descOutput.setLineWrap(true);
			descOutput.setWrapStyleWord(true);
			descOutput.setEditable(false);


		//JButtons
			picsButton = new JButton("Bilder");
			picsButton.addActionListener(actionListener);
			picsButton.setEnabled(false); //Ikke laget
			extraButton = new JButton("Info");
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
		centerPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10,0,15,15));

		add(centerPanel, BorderLayout.CENTER);

	//--------------------------------------------------------------------------------PAGE END (South Panel) --> buttons
		JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			southPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
			southPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,60));

		southPanel.add(newButton);
		southPanel.add(editButton);
		southPanel.add(deleteButton);

		add(southPanel, BorderLayout.PAGE_END);
	}


	protected void displayVenue(Venue venue)
	{

		//--------------------------------------------------------------------------------TopLeft
			JPanel leftTopGrid = new JPanel(new BorderLayout());
				leftTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

			//Overskrift
				JLabel venueName = new JLabel(venue.getName());
					venueName.setFont(new Font("Arial",Font.BOLD, 20 ));
					venueName.setBorder(BorderFactory.createEmptyBorder(0,0,15,0));

			leftTopGrid.add(venueName, BorderLayout.PAGE_START);

			//Bilde av venue
				resizeImage(venueThumb, 0, 0);
				venueThumb = new JLabel(venueImage);
					venueThumb.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

			leftTopGrid.add(venueThumb, BorderLayout.CENTER);

			//Knapper for å legge til nye bilder/video
				JPanel buttonGridTopLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
					buttonGridTopLeft.setBackground(EPlannerMainWindow.BASE_COLOR);
					buttonGridTopLeft.add(picsButton);
					buttonGridTopLeft.add(extraButton);

			leftTopGrid.add(buttonGridTopLeft, BorderLayout.PAGE_END);
			centerPanel.add(leftTopGrid);

		//--------------------------------------------------------------------------------TopRight
			JPanel rightTopGrid = new JPanel(new BorderLayout());

			descOutput.setText(venue.getDesc());
			descOutput.setCaretPosition(0);
			JScrollPane scrollDescOutput = new JScrollPane(descOutput);
				scrollDescOutput.setBackground(EPlannerMainWindow.BASE_COLOR);
				scrollDescOutput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Beskrivelse"));

			Box southRightTop = new Box(BoxLayout.PAGE_AXIS);
				southRightTop.setBackground(EPlannerMainWindow.BASE_COLOR);
				southRightTop.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Misc"));

			String website = venue.getWebsite() + "";
			websiteLabel = new JLabel("<html>Webside: <font color='blue'><u>" + website + "</u></font> </html>");
			if(!website.equals(""))
			{
				websiteLabel.addMouseListener(mouseListener);
				websiteLabel.setToolTipText("Åpne i standard nettleser..");
			}

			southRightTop.add(new JLabel("Adresse: " + venue.getAdr()));
			southRightTop.add(new JLabel("Telefon: " + venue.getTel()));
			southRightTop.add(new JLabel("Epost: " + venue.getEmail()));
			southRightTop.add(websiteLabel);
			southRightTop.add(new JLabel("Maks Kapasitet: " + venue.getMaxCapacity()));
			southRightTop.add(new JLabel("Sjangere: " + venue.genresToString()));
			southRightTop.add(new JLabel("Leiepris: " + venue.getRentPrice() + " NOK"));

			rightTopGrid.add(southRightTop, BorderLayout.PAGE_END);
			rightTopGrid.add(scrollDescOutput, BorderLayout.CENTER);

			rightTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);
			centerPanel.add(rightTopGrid);

		//--------------------------------------------------------------------------------BottomLeft --> JList med konserter
			concertArray = venue.getConcertArray();
			String[] concertNames = venue.getConcertNames();

			if(concertNames == null)
			{
				String[] tmp = {"Ingen konserter"};
				concertJList = new ExtendedJList<>(tmp);
			}
			else
				concertJList = new ExtendedJList<>(concertNames);

			concertJList.setFixedCellHeight(20);
			concertJList.setFixedCellWidth(250);
			concertJList.setPreferredSize(new Dimension(250, 200));
			concertJList.setFont(new Font("Arial",Font.PLAIN, 14 ));
			concertJList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Konserter"));
			concertJList.addListSelectionListener(listListener);
			centerPanel.add(concertJList);

		//--------------------------------------------------------------------------------BottomRight --> JTextArea med anmeldelse
			reviewOutput =  new JTextPane();
			reviewOutput.setText("");
			reviewOutput.setEditable(false);
			JScrollPane scrollOutput = new JScrollPane(reviewOutput);
			scrollOutput.setBackground(EPlannerMainWindow.BASE_COLOR);
			scrollOutput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Anmeldelse"));
			centerPanel.add(scrollOutput);


	}

	private void drawWestPanel()// --> Lager panelet for JListen på vestre side av vinduet.
	{
		westPanel = new JPanel(new GridLayout(1,1));

		JScrollPane listScroll = new JScrollPane(westPanel);
		listScroll.setBackground(EPlannerMainWindow.BASE_COLOR);
		listScroll.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

		venueJList = new ExtendedJList<>();
		updateVenueJList();

		venueJList.addListSelectionListener(listListener);
		venueJList.setFixedCellHeight(20);
		venueJList.setFixedCellWidth(190);
		venueJList.setFont(new Font("Arial",Font.PLAIN, 14 ));
		venueJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		venueJList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1), "Events"));

		westPanel.add(venueJList);

		add(listScroll, BorderLayout.LINE_START);
	}

//------------------------------------------------------------------------------------------------------------------ Funkjsonsmetoder

	public void deleteVenue()// --> Sletter lokalet som er valgt i JListen.
	{
		Venue del = venueJList.getSelectedValue();

		if(del != null)
		{
			int answer = Utilities.showOption(parent, "Er du sikker på at du vil slette " + del.getName());
			if(answer == JOptionPane.YES_OPTION)
			{
				venueList.removeVenue(del, null);
				updateVenueJList();
				centerPanel.removeAll();
				centerPanel.getRootPane().revalidate();
				parent.venueTabs.setSelectedComponent(parent.venueNewPanel);
				parent.venueTabs.setSelectedComponent(parent.venuePanel);
				Utilities.showInfo(parent, del.getName() + " er slettet.");
			}
		}
	}

	protected void disableButtons()// --> Setter knappene i sørpanelet til å være disabled.
	{
		editButton.setEnabled(false);
		newButton.setEnabled(false);
		deleteButton.setEnabled(false);
	}

	protected void editSelected()// --> Oppretter en endre-fane for det lokalet som er valgt i JListen når man trykker på endreknappen.
	{
		Venue tmp = getSelected();
		if(tmp != null)
			parent.addVenueEditTab(tmp);
	}

	public Venue getSelected()// --> Returnerer valgt lokale i JListen.
	{
		return venueJList.getSelectedValue();
	}

	public void openVenue(Venue venue)
	{
		venueJList.setSelection(venue);
	}

	public void openWebsite()// --> Åpner websiden med innkommen URI.
	{
		try
		{
			Desktop desktop = Desktop.getDesktop();
            desktop.browse(venueJList.getSelectedValue().getWebsite());
		}
		catch(Exception l){Utilities.showError(parent, "Ugyldig nettadresse.");}
	}

	public void repaintVenueView()// --> Fjerner, putter inn og revaliderer innholdet i center-panelet.
	{
		Venue tmpVenue = venueJList.getSelectedValue();

		if(tmpVenue == null)
			return;

		centerPanel.removeAll();
		displayVenue(tmpVenue);
		centerPanel.getRootPane().revalidate();
	}

	public void setDefaultSelectedVenue()// --> Setter det øvertste lokalet i listen til å være valgt når panelet tegnes.
	{
		if(!venueList.isEmpty())
			venueJList.setSelectedIndex(0);
	}

	public void setSelected(Venue selected)// --> Setter det lokalet som kommer inn som parameter til å være valgt i JListen.
	{
		if(!venueList.isEmpty())
			venueJList.setSelectedValue(selected, true);
	}
	public void updateVenueJList()// --> Oppdaterer innholdet i JListen.
	{
		venueJList.setContent(venueList.getArray());
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
		Venue tmpVenue = venueJList.getSelectedValue();

		if(tmpVenue.getThumbImagePath().equals(""))
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
			File img = new File("Images/Venues/" + tmpVenue.getName() + "/" + tmpVenue.getName() + ".jpg");
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

		venueImage =tmpVenue.getThumbImage();

		Image image = venueImage.getImage();

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

		venueImage = resizedImage;
	}


	private class VenueListListener implements ListSelectionListener// --> Lytteklasse for JListen.
	{
		public void valueChanged(ListSelectionEvent e)
		{
			if(e.getSource() == concertJList)
			{
				if (e.getValueIsAdjusting() == false)
				{
					int selected = concertJList.getSelectedIndex();
					if (selected == -1)
					{
					}
					else
					{
						if(concertArray != null && (concertArray[selected].getReview() != null))
						{
							reviewOutput.setDocument(concertArray[selected].getReview());
						}
						else
						{
							reviewOutput.setDocument(new DefaultStyledDocument());
							reviewOutput.setText("Ingen Anmeldelser");
						}
					}
				}
			}
			else
			{

				if (e.getValueIsAdjusting() == false)
				{
					int selected = venueJList.getSelectedIndex();
					if (selected == -1)
					{
					}
					else
					{
						venueImage = venueJList.getSelectedValue().getThumbImage();
						repaintVenueView();
					}
				}
			}
		}
	}//End of Listener class



	private class VenueListener implements ActionListener// --> ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if( e.getSource() == deleteButton)
			{
				deleteVenue();
			}
			else if(e.getSource() == editButton)
			{
				editSelected();
			}
			else if(e.getSource() == extraButton)
			{
				new InternalInfoWindow<Venue>(parent ,venueJList.getSelectedValue());
			}
		}
	}//End of Listener class


	/*
	 * Muselytter som hovedsakelig endrer utseandet på musen når man kommer over noe som leder til en nettside,
	 * og som deretter kaller opp metoden som åpner nettsiden.
	 */
	private class VenueMouseListener implements MouseListener
	{
		public void	mouseClicked(MouseEvent e)
		{
			openWebsite();
		}
		public void mouseEntered(MouseEvent e)
		{
			parent.setCursor(EPlannerMainWindow.HANDCURSOR);
		}
		public void mouseExited(MouseEvent e)
		{
			parent.setCursor(EPlannerMainWindow.NORMALCURSOR);
		}
		public void mousePressed(MouseEvent e)
		{
		}
		public void mouseReleased(MouseEvent e)
		{
		}
	}


} // End of class Myclass

