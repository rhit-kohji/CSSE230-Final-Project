import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


public class MyComponent extends JComponent {
	private ArrayList<Line2D.Double> pathLines = new ArrayList<Line2D.Double>();
	
	protected void addPathLines(ArrayList<Graph.Vertex> vertices) {
		for (int i = 0; i < vertices.size() - 1; i++) {
			pathLines.add(new Line2D.Double(vertices.get(i).getPosX(), vertices.get(i).getPosY(), vertices.get(i + 1).getPosX(), vertices.get(i + 1).getPosY()));
		}
	}
	
	protected void clear() {
		pathLines.clear();
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

//		for (int i = 0; i < 829; i++) {
//			for (int q = 0; q < 808; q++) {
//				g2.drawRect(i * 1, q * 1, 1, 1);
//			}
//		}
		Color zeldaBlue = new Color(52, 198, 235);
		Color grey = new Color(212, 212, 212);
		Color darkGrey = new Color(133, 133, 133);

		g2.setColor(zeldaBlue);
		g2.fillRect(610, 0, 367, 111);

		g2.setColor(grey);
		g2.fillRect(610, 112, 367, 325);

		g2.setColor(darkGrey);
		g2.fillRect(610, 325, 367, 274);

		g2.setColor(Color.white);
		g2.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		g2.drawString("Hyrule Trip Planner", 675, 65);

		g2.setColor(Color.black);
		g2.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		g2.drawString("Plan Trip By Location", 665, 150);
		g2.drawString("Plan Trip By Cost", 680, 370);

		Toolkit t = Toolkit.getDefaultToolkit();
		Image i = t.getImage("MapFor230WithCorrectSize.png");
		g.drawImage(i, 0, 0, this);
		
		for (int j = 0; j < pathLines.size(); j++) {
			g2.setColor(Color.RED);
			g2.fill(pathLines.get(j));
		}

	}
}
