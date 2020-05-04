import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JFrame;

public class TTTFrame extends JFrame
{
    public TTTFrame(int width, int height)
    {
        // creates the JFrame with the given name
        super("Chess");

        // Sets the close button to exit the program
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // makes the window not able to be resized
        setResizable(false);

        // creates the window
        pack();

        // creates the panel
        TTTPanel p = new TTTPanel(width, height);

        // gets the frames insets
        Insets frameInsets = getInsets();

        // calculates panel size
        int frameWidth = width
                + (frameInsets.left + frameInsets.right);
        int frameHeight = height
                + (frameInsets.top + frameInsets.bottom);

        // sets the frame's size
        setPreferredSize(new Dimension(frameWidth, frameHeight));

        // turns off the layout options
        setLayout(null);

        // adds the panel to the frame
        add(p);

        // adjusts the window to meet its new preferred size
        pack();

        // shows the frame
        setVisible(true);

    }
}
