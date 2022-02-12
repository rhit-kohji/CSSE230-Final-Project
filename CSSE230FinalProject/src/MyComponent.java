import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class MyComponent extends JComponent {

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		for (int i = 0; i < 829; i++) {
			for (int q = 0; q < 808; q++) {
				g2.drawRect(i * 1, q * 1, 1, 1);
			}
		}
		Color zeldaBlue = new Color(52, 198, 235);
		Color grey = new Color(212, 212, 212);
		Color darkGrey = new Color(133, 133, 133);

		g2.setColor(zeldaBlue);
		g2.fillRect(830, 0, 367, 111);

		g2.setColor(grey);
		g2.fillRect(830, 112, 367, 325);

		g2.setColor(darkGrey);
		g2.fillRect(830, 425, 367, 405);

		g2.setColor(Color.white);
		g2.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		g2.drawString("Hyrule Trip Planner", 895, 65);

		g2.setColor(Color.black);
		g2.setFont(new Font("TimesRoman", Font.PLAIN, 30));
		g2.drawString("Plan Trip By Location", 875, 150);
		g2.drawString("Plan Trip By Cost", 895, 460);

	}
}
