/** FIXME: Loaded from an old attempt to make a Chess game,
 * requires updating and modernization.
 */

import java.awt.*;

import javax.swing.JPanel;

public class TTTPanel extends JPanel {

    //ChessGame game;
    Image img;
    public TTTPanel(int w, int h){
        super();
        setSize(w, h);
    }

    public void paint(Graphics g){
        g.setColor(Color.BLACK);

        g.drawLine(200, 0, 200, 600);
        g.drawLine(400, 0, 400, 600);

        g.drawLine(0, 200, 600, 200);
        g.drawLine(0, 400, 600, 400);
    }



}