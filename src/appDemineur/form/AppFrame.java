package appDemineur.form;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * La classe AppFrame permet de cr�er une fen�tre qui sert de contenant
 * pour le plateau de jeu du D�mineur.
 * 
 * @author Christian Lesage
 * @author Alexandre Tremblay
 * 
 */
public class AppFrame extends JFrame implements ComponentListener, WindowListener
{
	// Sert � l'impl�mentation du singleton
	private static AppFrame instance = null;
	
	// Dimension initiale de la fen�tre
	private static final Dimension INIT_SIZE = new Dimension(640, 745);
	
	// Largeur minimale de la fen�tre
	private static final int MIN_WIDTH = 640;
	
	// Hauteur minimale de la fen�tre
	private static final int MIN_HEIGHT = 480;
	
	// Titre de la fen�tre
	private static final String INIT_TITLE = "D�mineur par Alexandre Tremblay et Christian Lesage";
	
	// Messages pour la bo�te de dialogue de confirmation de sortie
	private static final String QUIT_MESSAGE = "Voulez-vous quitter le jeu ?";
	private static final String QUIT_TITLE = "Quitter";

	// Objet du plateau de jeu
	private Board gameBoard;
	
	// Menu de l'application
	private AppMenu appMenu;
	
	/*
	 * Cr�e la fen�tre du jeu, qui contient les menus et le plateau de jeu
	 */
	protected AppFrame()
	{
		super();
		this.setNativeLookAndFeel();

		// Initialise le menu
		this.appMenu = new AppMenu(this);
		
		this.setJMenuBar(this.appMenu);
		
		this.setTitle(AppFrame.INIT_TITLE);
		this.setSize(AppFrame.INIT_SIZE);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		this.gameBoard = new Board(this, true);
		this.getContentPane().add(this.gameBoard);
		
		// Sp�cifie les �couteurs pour la fen�tre
		this.addComponentListener(this);
		this.addFocusListener(this.gameBoard);
		
		this.addWindowListener(this);
	}
	
	// Confirme que l'utilisateur veut vraiment sortir de l'application
	private void quitApplication()
	{
		int confirm = JOptionPane.showConfirmDialog(this,
				AppFrame.QUIT_MESSAGE, AppFrame.QUIT_TITLE,
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		if (confirm == JOptionPane.YES_OPTION)
		{
			this.dispose();
			System.exit(0);
		}

	}	
	
	private void setNativeLookAndFeel()
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			System.out.println("Incapable de changer l'apparence de l'application.");
		}
	}
	
	/**
	 * Cr�e et retourne la fen�tre du jeu en tant que singleton.
	 *  
	 * La fen�tre du jeu contient les menus et un plateau de jeu de type Board.
	 * 
	 * @see appDemineur.form.Board
	 * 
	 * @return l'instance du singleton
	 */
	public static AppFrame getInstance()
	{
		if (AppFrame.instance == null)
		{
			AppFrame.instance = new AppFrame();
		}
		return AppFrame.instance;
	}
	
	/**
	 * Retourne l'objet gameBoard qui contient la partie en cours.
	 * 
	 * @return l'objet gameBoard qui contient la partie en cours
	 */
	public Board getBoard()
	{
		return this.gameBoard;
	}
	
	/**
	 * M�thode appel�e quand la fen�tre est redimensionn�e.
	 * On s'assure que ses dimensions respectent la largeur et la hauteur minimales permises.
	 * Cette m�thode doit �tre publique mais ne devrait pas �tre appel�e directement.
	 * 
	 * @see java.awt.event.ComponentListener#componentResized(java.awt.event.ComponentEvent)
	 * 
	 * @param e �v�nement d�clencheur
	 */
	@Override
	public void componentResized(ComponentEvent e)
	{
		int width = getWidth();
		int height = getHeight();
		// V�rifie si la largeur et la hauteur sont inf�rieures 
		// � la valeur minimale permise pour chacune
		boolean resize = false;
		if (width < AppFrame.MIN_WIDTH)
		{
			resize = true;
			width = AppFrame.MIN_WIDTH;
		}
		if (height < AppFrame.MIN_HEIGHT)
		{
			resize = true;
			height = AppFrame.MIN_HEIGHT;
		}
		if (resize)
		{
			setSize(width, height);
		}
	}

	/** 
	 * M�thode appel�e quand la fen�tre va �tre ferm�e.
	 * Cette m�thode doit �tre publique mais ne devrait pas �tre appel�e directement.
	 * 
	 * @param e �v�nement d�clencheur
	 */
	@Override
	public void windowClosing(WindowEvent e)
	{
		this.quitApplication();
	}
	
	@Override
	public void componentHidden(ComponentEvent e)
	{
	}
	
	@Override
	public void componentMoved(ComponentEvent e)
	{
	}
	
	@Override
	public void componentShown(ComponentEvent e)
	{
	}

	@Override
	public void windowActivated(WindowEvent arg0)
	{
	}

	@Override
	public void windowClosed(WindowEvent e)
	{
	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{
	}

	@Override
	public void windowDeiconified(WindowEvent e)
	{
	}

	@Override
	public void windowIconified(WindowEvent e)
	{
	}

	@Override
	public void windowOpened(WindowEvent e)
	{
	}
}
