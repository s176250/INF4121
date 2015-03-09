/*
 *	Innhold:  Metoder som oppretter og tegner elementene i artistPanel, og lytter på slett, ny og endre knapper.
 *
 *				drawWestPanel()						--> Lager og legger til vestre panel med artistlisten
 *				drawSouthPanel()					--> Lager og legger til søndre panel med knapper
 *				displayArtist()						--> Kalles hver gang en artist i oversiktslisten blir trykket på, tegner og viser senterpanelet med valgt artist.
 *				repaintArtistView()					--> Retegner Artistoversikt-panelet(dette)
 *				updateArtistJList()					--> Oppdaterer artistoversikt-listen
 *				resizeImage()						--> Reskalerer bilde
 *				setDefaultSelectedArtist()			--> Setter valgt artist til første i listen.
 *				setSelected()						--> Setter valgt artist til innkommen parameter.
 *				getSelected()						--> Returnerer artisten som er valgt i oversiktslisten.
 *				editSelected()						--> Åpner endre panelet med valgt artist som parameter.
 *				deleteArtist()						--> Sletter valgt artist
 *				disableButtons()					--> Kobler ut ny, endre og slett knappene.
 *				openWebsite()						--> Åpner artistens nettside.
 *
 *				Indre klasser:
 *				ArtistListListener					--> Lytter på valg i listene.
 *				ArtistListener						--> Lytter på knapper lagt til i artistpanelet
 *				ArtistMouseListener					--> Lytter på musehandlinger over webside-JLablene.
 *
 *	Laget av: Jonas Moltzau og Martin Løkkeberg.
 *	Sist endret: 13.05.2012.
 */

package eplanner.windows;

import eplanner.*;
import eplanner.utilities.IOActions;
import eplanner.utilities.Utilities;
import eplanner.windows.components.ExtendedJList;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.DefaultStyledDocument;

//Hensikt: Tegne artist oversikt panelet
@SuppressWarnings("serial")
public class ArtistPanel extends JPanel
{
	//Lister
		private ExtendedJList<Artist> artistJList;
		private ExtendedJList<String> concertJList;

	//Tekstbokser
		private JTextArea descOutput;
		private JTextPane reviewOutput;

	//Data
		private ArtistList artistList;
		private Concert[] concertArray;

	//Paneler
		private JPanel centerPanel, westPanel, southPanel;

	//Knapper og Labels
		private JButton picsButton, vidsButton, editButton, newButton, deleteButton, infoButton;
		private JLabel websiteLabel;
		protected JLabel artistThumb;

	//Lyttere
		private ArtistListListener listListener;
		private ArtistListener actionListener;
		private ArtistMouseListener mouseListener;

	//Annet
		private EPlannerMainWindow parent;
		private	ImageIcon artistImage;
		private	double imageH, imageW;

	//Konstruktør
	public ArtistPanel(EPlannerMainWindow parent, ArtistList artistList)
	{
		super(new BorderLayout());

		//Lagrer inkommne parametre
		this.artistList = artistList;
		this.parent = parent;

		//Setter preferanser
		setPreferredSize(new Dimension(810, 700));
		setBackground(EPlannerMainWindow.BASE_COLOR);
		setVisible(true);

		//Oppretter Lyttere
		actionListener = new ArtistListener();
		listListener = new ArtistListListener();
		mouseListener = new ArtistMouseListener();

		//Oppretter JLabel
		artistThumb = new JLabel(artistImage);

		//Oppretter tekstbokser
        reviewOutput = new JTextPane();
		descOutput = new JTextArea(16,20);
		descOutput.setLineWrap(true);
		descOutput.setWrapStyleWord(true);
		descOutput.setEditable(false);

		//Oppretter Knapper
		picsButton = new JButton("Bilder");
		picsButton.addActionListener(actionListener);
		picsButton.setEnabled(false); //Ikke laget
		vidsButton = new JButton("Video");
		vidsButton.addActionListener(actionListener);
		vidsButton.setEnabled(false); //Ikke laget
		infoButton = new JButton("Info");
		infoButton.addActionListener(actionListener);
		editButton = new JButton("Endre");
		editButton.addActionListener(actionListener);
		newButton = new JButton("Ny");
		newButton.addActionListener(actionListener);
		deleteButton = new JButton("Slett");
		deleteButton.addActionListener(actionListener);


	//------------------------------------------------------------------------------------- LINE START (West Panel)

		drawWestPanel();	// Lager og legger til vestre panel med liste over artister.


	//------------------------------------------------------------------------------------- CENTER

		centerPanel = new JPanel(new GridLayout(2,2,15,20));
		centerPanel.setMinimumSize(new Dimension(500, 500));
		centerPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
		centerPanel.setBorder(BorderFactory.createEmptyBorder(10,0,15,15));

		add(centerPanel, BorderLayout.CENTER);


	//------------------------------------------------------------------------------------- PAGE END (South Panel)

		drawSouthPanel();	// Lager og legger til søndre panel med knapper i.

	}
	//Slutt konstruktør


// ------------------------------------------------------------------------------------------------------------------------------GUI Metoder:
		// Alle metodene som slutter på panel er paneler som opprettes i tilsvarende posisjon til navnet.

	private void drawWestPanel()	//--> Lager og legger til vestre panel med artistlisten
	{
		westPanel = new JPanel(new GridLayout(1,1));

		JScrollPane listScroll = new JScrollPane(westPanel);
		listScroll.setBackground(EPlannerMainWindow.BASE_COLOR);
		listScroll.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));

		artistJList = new ExtendedJList<>();
		updateArtistJList();

		artistJList.addListSelectionListener(listListener);
		artistJList.setFixedCellHeight(20);
		artistJList.setFixedCellWidth(190);
		artistJList.setFont(new Font("Arial",Font.PLAIN, 14 ));
		artistJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		artistJList.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 1), "Events"));

		westPanel.add(artistJList);

		add(listScroll, BorderLayout.LINE_START);
	}


	private void drawSouthPanel()	//--> Lager og legger til søndre panel med knapper
	{
		southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		southPanel.setBackground(EPlannerMainWindow.BASE_COLOR);
		southPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,60));

		southPanel.add(newButton);
		southPanel.add(editButton);
		southPanel.add(deleteButton);

		add(southPanel, BorderLayout.PAGE_END);
	}


	protected void displayArtist(Artist artist)	//--> Kalles hver gang en artist i oversiktslisten blir trykket på, tegner og viser senterpanelet med valgt artist.
	{
		//--------------------------------------------------------------------------------TopLeft (overskrift, bilde, knapper)
			JPanel leftTopGrid = new JPanel(new BorderLayout());
			leftTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

			//Overskrift
			JLabel artistName = new JLabel(artist.getName());
			artistName.setFont(new Font("Arial",Font.BOLD, 20 ));
			artistName.setBorder(BorderFactory.createEmptyBorder(0,0,15,0));
			leftTopGrid.add(artistName, BorderLayout.PAGE_START);

			//Bilde
			resizeImage(artistThumb, 0, 0);
			artistThumb = new JLabel(artistImage);
			artistThumb.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
			leftTopGrid.add(artistThumb, BorderLayout.CENTER);

			//Knapper for å legge til nye bilder/video
			JPanel buttonGridTopLeft = new JPanel(new FlowLayout(FlowLayout.LEFT));
			buttonGridTopLeft.setBackground(EPlannerMainWindow.BASE_COLOR);
			buttonGridTopLeft.add(picsButton);
			buttonGridTopLeft.add(vidsButton);
			buttonGridTopLeft.add(infoButton);
			leftTopGrid.add(buttonGridTopLeft, BorderLayout.PAGE_END);
			centerPanel.add(leftTopGrid);

		//--------------------------------------------------------------------------------TopRight (Beskrivelse og Misc info)
			JPanel rightTopGrid = new JPanel(new BorderLayout());
			rightTopGrid.setBackground(EPlannerMainWindow.BASE_COLOR);

			descOutput.setText(artist.getDesc());
			descOutput.setCaretPosition(0);
			JScrollPane scrollDescOutput = new JScrollPane(descOutput);
			scrollDescOutput.setBackground(EPlannerMainWindow.BASE_COLOR);
			scrollDescOutput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Beskrivelse"));

			Box southRightTop = new Box(BoxLayout.PAGE_AXIS);
			southRightTop.setBackground(EPlannerMainWindow.BASE_COLOR);
			southRightTop.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Misc"));

			String website = artist.getWebsite() + "";
			websiteLabel = new JLabel("<html>Webside: <font color='blue'><u>" + website + "</u></font> </html>");
			if(!website.equals(""))
			{
				websiteLabel.addMouseListener(mouseListener);
				websiteLabel.setToolTipText("Åpne i standard nettleser..");
			}

			if(artist instanceof Band)
			{
				Band tmp = (Band) artist;
				southRightTop.add(new JLabel("Bandmedlemmer:" + tmp.bandmembersToString()));
				southRightTop.add(new JLabel("Nationalitet:" + tmp.getNationality()));
				southRightTop.add(websiteLabel);
			}
			else
			{
				Soloartist tmp = (Soloartist) artist;
				southRightTop.add(new JLabel("Instrumenter: " + tmp.getInstruments()));
				southRightTop.add(websiteLabel);
			}

			southRightTop.add(new JLabel("Sjangere: " + artist.genresToString()));

			rightTopGrid.add(southRightTop, BorderLayout.PAGE_END);
			rightTopGrid.add(scrollDescOutput, BorderLayout.CENTER);

			centerPanel.add(rightTopGrid);

		//--------------------------------------------------------------------------------BottomLeft (Liste med konserter)
			concertArray = artist.getConcertArray();
			String[] concertNames = artist.getConcertNames();

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

		//--------------------------------------------------------------------------------BottomRight (Tekstboks med anmeldelse)
			reviewOutput =  new JTextPane();
			reviewOutput.setEditable(false);
			JScrollPane scrollOutput = new JScrollPane(reviewOutput);
			scrollOutput.setBackground(EPlannerMainWindow.BASE_COLOR);
			scrollOutput.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Anmeldelse"));
			centerPanel.add(scrollOutput);
	}

// ------------------------------------------------------------------------------------------------------------------------------Funksjons Metoder:

	public void repaintArtistView()	//--> Retegner Artistoversikt-panelet(dette)
	{
		centerPanel.removeAll();
		displayArtist(artistJList.getSelectedValue());
		centerPanel.getRootPane().revalidate();
	}

	public void updateArtistJList()	//--> Oppdaterer artistoversikt-listen
	{
		artistJList.setContent(artistList.getArray());
	}

	/*
	Reskalerer bilde, kalkulerer høyde og breddeforhold slik at bildet blir forstørret med originalforholdet,
	hvis høyden på bilde er større enn bredden bestemmer høyden forholdet og vica versa.
	Størrelsen blir ikke større enn størrelsen til originalbildet.
	Setter også parameterene for å tegne bildet slik at bildet kommer ut med best mulig kvalitet.
	*/
	protected void resizeImage(JLabel container, int height, int width)
	{

		if(artistJList.getSelectedValue().getThumbImagePath().equals(""))
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
			File img = new File("Images/Artists/" + artistJList.getSelectedValue().getName() + "/" +  artistJList.getSelectedValue().getName() + ".jpg");
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

		if(imageW > imageH)
		{
			// Bredde bestemmer størrelsen

			if(containerW < imageW)
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

			if(containerH < imageH)
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

		artistImage = artistJList.getSelectedValue().getThumbImage();

		Image image = artistImage.getImage();

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

		artistImage = resizedImage;
	}

	public void setDefaultSelectedArtist()	//--> Setter valgt artist til første i listen.
	{
		if(!artistList.isEmpty())
			artistJList.setSelectedIndex(0);
	}

	public void setSelected(Artist selected)	//--> Setter valgt artist til innkommen parameter.
	{
		if(!artistList.isEmpty())
			artistJList.setSelectedValue(selected, true);
	}

	public Artist getSelected()	 //--> Returnerer artisten som er valgt i oversiktslisten.
	{
		return artistJList.getSelectedValue();
	}

	protected void editSelected()	//--> Åpner endre panelet med valgt artist som parameter.
	{
		Artist aTmp = getSelected();
		if(aTmp != null)
			parent.addArtistEditTab(aTmp);
	}

	public void deleteArtist()	//--> Sletter valgt artist
	{
		Artist del = artistJList.getSelectedValue();

		if(del != null)
		{
			int answer = Utilities.showOption(parent, "Er du sikker på at du vil slette " + del.getName());
			if(answer == JOptionPane.YES_OPTION)
			{
				artistList.removeArtist(del, null);
				updateArtistJList();
				centerPanel.removeAll();
				centerPanel.getRootPane().revalidate();
				parent.artistTabs.setSelectedComponent(parent.artistNewPanel);
				parent.artistTabs.setSelectedComponent(parent.artistPanel);
				Utilities.showInfo(parent, del.getName() + " er slettet.");
			}
		}
	}

	protected void disableButtons()	//--> Kobler ut ny, endre og slett knappene.
	{
		editButton.setEnabled(false);
		newButton.setEnabled(false);
		deleteButton.setEnabled(false);
	}

	public void openWebsite()	//--> Åpner artistens nettside.
	{
		try
		{
			Desktop desktop = Desktop.getDesktop();
            desktop.browse(artistJList.getSelectedValue().getWebsite());
		}
		catch(Exception l){Utilities.showError(parent, "Ugyldig nettadresse.");}
	}


// ------------------------------------------------------------------------------------------------------------------------------INDRE KLASSER
	//Hensikt: Lytter på valg i listene.
	private class ArtistListListener implements ListSelectionListener
	{
		@Override
		public void valueChanged(ListSelectionEvent e)
		{
			if(e.getSource() == concertJList)	//Viser anmeldelse av valgt konsert
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
			}//end-if
			else	//Viser valgt artist
			{
				if (e.getValueIsAdjusting() == false)
				{
					int selected = artistJList.getSelectedIndex();
					if (selected == -1)
					{
					}
					else
					{
						artistImage = artistJList.getSelectedValue().getThumbImage();
						repaintArtistView();
					}
				}
			}//end-else
		}
	}//End of ArtistListListener class


	//Hensikt: Lytter på knapper lagt til i artistpanelet
	private class ArtistListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			if( e.getSource() == deleteButton)
			{
				deleteArtist();
			}
			else if(e.getSource() == editButton)
			{
				editSelected();
			}
			else if(e.getSource() == infoButton)
			{
				new InternalInfoWindow<Artist>(parent ,artistJList.getSelectedValue());
			}
		}
	}//End of ArtistListener class


	//Hensikt: Lytter på musehandlinger over webside-JLablene.
	private class ArtistMouseListener implements MouseListener
	{
		@Override
		public void	mouseClicked(MouseEvent e)
		{
			openWebsite();
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
	}// End of class ArtistMouseListener

} // End of class ArtistPanel

