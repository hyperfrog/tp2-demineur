package appDemineur.form;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import appDemineur.model.BestTimes;
import appDemineur.model.Game;

/**
 * La classe AppScoresDialog sert de boîte de dialogue pour les meilleurs temps. 
 *
 * @author Christian Lesage
 * @author Alexandre Tremblay
 *
 */

public class AppScoresDialog extends JDialog implements ActionListener, WindowListener
{
    private JPanel buttonsPanel;
    private JButton eraseButton;
    private JTextPane levelsTextPane;
    private JButton okButton;
    private JTextPane playersTextPane;
    private JPanel scoresPanel;
    private JTextPane timesTextPane;
    
//    private AppFrame parent;
	
    private BestTimes bestTimes;
    
	/**
	 * 
	 * @param parent
	 */
	public AppScoresDialog(AppFrame parent)
	{
		super(parent);
		
//		this.parent = parent;
		this.bestTimes = new BestTimes();
		
		this.setTitle("Démineurs les plus rapides");
		this.setResizable(false);
		this.setModal(true);
		
		this.initComponents();
		this.showScores();
		this.pack();
		
		this.addWindowListener(this);
	}

    private void initComponents() {

        this.buttonsPanel = new JPanel();
        this.eraseButton = new JButton();
        this.okButton = new JButton();
        this.scoresPanel = new JPanel();
        this.levelsTextPane = new JTextPane();
        this.timesTextPane = new JTextPane();
        this.playersTextPane = new JTextPane();

        this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

        this.buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        this.eraseButton.setText("Effacer les scores");
        this.eraseButton.setActionCommand("ERASE");
        this.buttonsPanel.add(this.eraseButton);
        this.eraseButton.addActionListener(this);

        this.okButton.setText("OK");
        this.okButton.setActionCommand("CLOSE");
        this.buttonsPanel.add(this.okButton);
        this.okButton.addActionListener(this);

        this.getContentPane().add(this.buttonsPanel, BorderLayout.PAGE_END);

        this.scoresPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));

        this.levelsTextPane.setMaximumSize(null);
        this.levelsTextPane.setMinimumSize(null);
        this.levelsTextPane.setPreferredSize(new Dimension(100, 60));
        this.scoresPanel.add(this.levelsTextPane);

        this.timesTextPane.setMaximumSize(null);
        this.timesTextPane.setMinimumSize(null);
        this.timesTextPane.setPreferredSize(new Dimension(100, 60));
        this.scoresPanel.add(this.timesTextPane);

        this.playersTextPane.setMaximumSize(null);
        this.playersTextPane.setMinimumSize(null);
        this.playersTextPane.setPreferredSize(new Dimension(100, 60));
        this.scoresPanel.add(this.playersTextPane);

        this.levelsTextPane.setEditable(false);
        this.levelsTextPane.setOpaque(false);

        this.playersTextPane.setEditable(false);
        this.playersTextPane.setOpaque(false);

        this.timesTextPane.setEditable(false);
        this.timesTextPane.setOpaque(false);

        this.getContentPane().add(this.scoresPanel, BorderLayout.CENTER);
    }
    
    private void showScores()
    {
    	String levels = "";
    	String times = "";
    	String players = "";
    	
		for (int i = 0; i < Game.LEVELS.length; i++)
		{
			levels += Game.LEVELS[i].displayName + " :\n";
			
			String time =  this.bestTimes.getTime(i);
			
			if (time != "")
			{
				times += time + " seconde(s)";
			}
			else
			{
				times += "Aucun record";
			}
			times += "\n";
			
			String player = this.bestTimes.getPlayer(i);

			if (player != "")
			{
				players += player;
			}
			players += "\n";
		}
		
		this.levelsTextPane.setText(levels);
		this.timesTextPane.setText(times);
		this.playersTextPane.setText(players);
    }
	
    private void eraseScores()
    {
		for (int i = 0; i < Game.LEVELS.length; i++)
    	{
			this.bestTimes.setPlayer(i, "");
			this.bestTimes.setTime(i, "");
    	}
		this.showScores();
		this.bestTimes.write();
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
		if (evt.getActionCommand().equals("ERASE"))
		{
			this.eraseScores();
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
