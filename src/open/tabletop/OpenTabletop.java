package open.tabletop;

import javax.swing.SwingUtilities;

import open.tabletop.dto.ApplicationData;
import open.tabletop.ui.MainPanel;

public class OpenTabletop {

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        ApplicationData.load();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainPanel.createAndShowGUI();
            }
        });
    }
}