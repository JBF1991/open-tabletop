package open.tabletop.ui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

import open.tabletop.OpenTabletop;

public class PanelUtils {
    public static final Dimension shortRow = new Dimension(12000, 40);
    public static final Dimension hugeRow = new Dimension(12000, 480);
    

    public static JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(1, 1));
        return panel;
    }

    public static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = OpenTabletop.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
