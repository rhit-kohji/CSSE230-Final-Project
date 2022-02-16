import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


// tiny change
public class mouseClickListener implements MouseListener {

	@Override
	public void mouseClicked(MouseEvent e) {	
		int localX = e.getX();
		int localY = e.getY();
		
		System.out.println(localX + ", " +  localY);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

}
