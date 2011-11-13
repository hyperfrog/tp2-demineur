package appDemineur.form;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * La classe AppAboutDialog sert de boîte de dialogue pour la fenêtre À Propos... 
 *
 * @author Christian Lesage
 * @author Alexandre Tremblay
 *
 */

public class AppAboutDialog extends JDialog implements ActionListener, WindowListener
{
	private static final String APP_NAME		= "Travail Pratique 2 : Démineur";
	private static final String APP_VERSION 	= "v 1.0";
	private static final String APP_AUTHOR_ONE 	= "Christian Lesage";
	private static final String APP_AUTHOR_TWO 	= "Alexandre Tremblay";
	
	// Images utilisées pour la boîte de dialogue À propos
	private static ImageIcon aboutLogo = null;
	
	// Initialisation des images
	static
	{
		try
		{
			//AppAboutDialog.aboutLogo = new ImageIcon(AppAboutDialog.class.getResource("../../demineur_logo.png"));
		}
		catch (IllegalArgumentException e)
		{
			System.out.println("Incapable de trouver une ou plusieurs image(s) de la classe AppAboutDialog.");
		}
	}
	
	// 
	private JPanel container;
	
	// 
	private JPanel containerInfo;
	
	//
	private JLabel logo;
	
	// 
	private JLabel title;
	
	//
	private JLabel version;
	
	//
	private JLabel authors;
	
	//
	private JButton closeButton;
	
	/**
	 * 
	 * @param owner
	 */
	public AppAboutDialog(AppFrame owner)
	{
		super(owner);
		
		this.setTitle("À propos...");
		this.setResizable(false);
		this.setModal(true);
		
		this.container = new JPanel();
		this.containerInfo = new JPanel();
		this.logo = new JLabel();
		this.title = new JLabel();
		this.version = new JLabel();
		this.authors = new JLabel();
		this.closeButton = new JButton();
		
//		this.container.setLayout(new GridLayout(0, 2));
		this.container.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
		
		this.containerInfo.setLayout(new GridLayout(3, 0));
		
		this.logo = new JLabel();
		this.logo.setIcon(AppAboutDialog.aboutLogo);
		this.logo.setPreferredSize(new Dimension(200, 200));
		
		this.title.setText(AppAboutDialog.APP_NAME);
		this.title.setFont(new Font(null, Font.BOLD, 20));
		
		this.version.setText(AppAboutDialog.APP_VERSION);
		this.version.setFont(new Font(null, Font.ITALIC, 10));
		
		this.authors.setText("Auteurs : " + AppAboutDialog.APP_AUTHOR_ONE + " et " + AppAboutDialog.APP_AUTHOR_TWO);
		this.authors.setFont(new Font(null, Font.PLAIN, 14));
		
		this.closeButton.setText("Fermer");
		this.closeButton.setActionCommand("CLOSE");
		
		JPanel closeButtonPanel = new JPanel();
		closeButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 10));
		closeButtonPanel.add(this.closeButton);
		
		this.containerInfo.add(this.title);
		this.containerInfo.add(this.version);
		this.containerInfo.add(this.authors);
		
		this.container.add(this.logo);
		this.container.add(this.containerInfo);
		
		this.getContentPane().add(this.container, BorderLayout.CENTER);
		this.getContentPane().add(closeButtonPanel, BorderLayout.PAGE_END);
		
		this.closeButton.addActionListener(this);
		this.addWindowListener(this);
		
		this.pack();
	}

	private void close()
	{
		this.setVisible(false);
		this.dispose();
	}
	
	@Override
	public void actionPerformed(ActionEvent evt)
	{
		if (evt.getActionCommand().equals("CLOSE"))
		{
			this.close();
		}
	}
	
	@Override
	public void windowClosing(WindowEvent evt)
	{
		this.close();
	}
	
	@Override
	public void windowActivated(WindowEvent evt) {}

	@Override
	public void windowClosed(WindowEvent evt) {}

	@Override
	public void windowDeactivated(WindowEvent evt) {}

	@Override
	public void windowDeiconified(WindowEvent evt) {}

	@Override
	public void windowIconified(WindowEvent evt) {}

	@Override
	public void windowOpened(WindowEvent evt) {}
	
}
