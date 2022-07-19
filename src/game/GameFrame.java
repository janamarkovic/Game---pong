package game;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameFrame extends JFrame{
	
	GamePanel panel;
	
	public GameFrame() {
		 panel = new GamePanel(this);//Kreira se novi JPanel
		 this.add(panel);//Na formu se dodaje prethodno kreirani JPanel(GamePanel)
		 this.setTitle("Pong game");//Naslov
		 this.setResizable(false);//Ne moze da se menja velicina prozora
		 this.setBackground(Color.black);//Postavlja se boja pozadine
		 this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Zatvaranje na X
		 this.pack();//Postavljanje velicine
		 this.setVisible(true);//GameFrame postaje vidljiv
		 this.setLocationRelativeTo(null);//Postavlja GameFrame na sredinu ekrana
		
	}

    public GamePanel getPanel() {
        return panel;
    }
        
        
}
