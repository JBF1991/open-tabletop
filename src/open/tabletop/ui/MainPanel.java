package open.tabletop.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

import open.tabletop.ui.tabPanels.CharactersPanel;
import open.tabletop.ui.tabPanels.ItemsPanel;
import open.tabletop.ui.tabPanels.QuestsPanel;
import open.tabletop.ui.tabPanels.RegenWorldPanel;
import open.tabletop.ui.tabPanels.SettingsPanel;
import open.tabletop.ui.tabPanels.SystemsPanel;

public class MainPanel extends JPanel {
    
    public static void createAndShowGUI() {
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        //Create and set up the window.
        JFrame frame = new JFrame("TabbedPaneDemo");
        frame.setMinimumSize(new Dimension(1200, 720));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Add content to the window.
        frame.add(new MainPanel(), BorderLayout.CENTER);
        //Display the window.
        frame.pack();
        frame.setSize(new Dimension(1200, 720));
        frame.setVisible(true);
    }

    MainPanel() {
        super(new GridLayout(1, 1));

        JTabbedPane tabbedPane = new JTabbedPane();
        
        ImageIcon icon = PanelUtils.createImageIcon("images/middle.gif");

        new SystemsPanel(tabbedPane, icon);
        new RegenWorldPanel(tabbedPane, icon);
        new QuestsPanel(tabbedPane, icon);
        new CharactersPanel(tabbedPane, icon);
        new ItemsPanel(tabbedPane, icon);
        new SettingsPanel(tabbedPane, icon);

        //Add the tabbed pane to this panel.
        add(tabbedPane);

        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

}
