import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Zoubida extends JPanel {
	/**
	 * UID 
	 */
	private static final long serialVersionUID = 1L;
	private static final int DELTA =50;
	private JLabel jLabel;
	public Zoubida() {
		this.jLabel = new JLabel();
		this.add(jLabel);
	}
	private String explode(List<String> multiLigne, String string) {
		// back to php :D
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < multiLigne.size(); i++) {
			System.out.println(multiLigne.get(i));
		    if (i > 0) {
		        sb.append(string);
		    }
		    sb.append(multiLigne.get(i));
		}
		return sb.toString();
	}
	public static void main(String [] args) {
		JFrame m = new JFrame();
		final Zoubida z;
		m.add(z=new Zoubida());
		m.setSize(600, 700);
		m.setVisible(true);
		z.paf(z.getSize());
		// un petit listener
		z.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                z.paf(e.getComponent().getSize());
            }
        });
	}
	private void paf(Dimension dimension) {
		List<String> multiLigne = new ArrayList<String>(); 
		String message = "blablablablabla bla";
		// la fonction cool que tu cherchais ?
		FontMetrics mettr = jLabel.getFontMetrics(jLabel.getFont());
		int debut = 0;
		int cumul = 0;
		// c'est lineaire ^^
		int pff = message.length();
		for (int c = 0; c < pff; c++) {
			int fin = c;
			int cw = mettr.charWidth(message.charAt(c));
			System.out.println(cw);
			System.out.println((cumul+cw)+ " vs "+dimension.width);
			if (debut!=fin && cumul+cw > dimension.width-DELTA) {
				multiLigne.add(message.substring(debut, fin));
				debut = fin+1;
				cumul=cw;
			} else {
				cumul+=cw;
			}
		}
		if (cumul>0) {
			multiLigne.add(message.substring(debut, message.length()));
		}
		jLabel.setText("<html>"+explode(multiLigne,"<br/>")+"</html>");
	}
}
