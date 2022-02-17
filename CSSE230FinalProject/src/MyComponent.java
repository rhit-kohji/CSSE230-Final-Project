import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;


// tiny change
public class MyComponent extends JComponent {
	public boolean end = false;
	public boolean start = false;
	public boolean time = false; 
	public boolean dist = false;
	public String startStr = ""; 
	public String endStr = ""; 
	public ArrayList<Line2D.Double> pathLines = new ArrayList<Line2D.Double>();
	public String distStr = "";
	public String timeStr = "";
	public String distOnlyStr = "";
	public String timeOnlyStr = "";
	public String startStrTimeOrDist = "";
	public Font fancyFont;
	
	protected void addPathLines(ArrayList<Graph<String>.Vertex> al) {
		for (int i = 0; i < al.size() - 1; i++) {
			pathLines.add(new Line2D.Double(al.get(i).getPosX(), al.get(i).getPosY(), al.get(i + 1).getPosX(), al.get(i + 1).getPosY()));
		}
		System.out.println(pathLines.toString());
	}
	
	protected void clear() {
		pathLines.clear();
		startStr = "";
		endStr = "";
	}

	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
//		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//		try {
//			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("EnchantedLand-jnX9.ttf")));
//		} catch (FontFormatException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		//TODO: uncomment this later
//		InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("EnchantedLand-jnX9.ttf");
//		try {
//			fancyFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(48f);
//		} catch (FontFormatException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	
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
		g2.setFont(fancyFont);
		g2.drawString("Plan Trip By Location", 665, 150);
		g2.drawString("Plan Trip By Cost", 680, 370);

		Toolkit t = Toolkit.getDefaultToolkit();
		Image i = t.getImage("MapFor230WithCorrectSize.png");
		g.drawImage(i, 0, 0, this);
		
		for (int j = 0; j < pathLines.size(); j++) {
			g2.setColor(zeldaBlue);
			g2.setStroke(new BasicStroke(3));
			g2.draw(pathLines.get(j));
			g2.fill(pathLines.get(j));
		}
		
		if(startStr != "" || endStr != ""){
			g2.setColor(Color.white);
			g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			g2.setColor(Color.BLACK);
			g2.drawString("Dist: " + distStr, 630, 190);
			g2.drawString("Time: " + timeStr, 630, 230);
		}
		
		if(distStr != "" || timeStr != ""){
			g2.setColor(Color.BLACK);
			g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
			g2.drawString("Starting Point: " + startStrTimeOrDist, 630, 410);
			g2.drawString("Ending Point: ", 630, 440);
			g2.drawString("Time: ", 630, 470);
			g2.drawString("Dist: ", 630, 490);
		}

	}

}