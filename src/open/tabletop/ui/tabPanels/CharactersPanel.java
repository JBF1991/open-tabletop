package open.tabletop.ui.tabPanels;

import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import open.tabletop.ui.PanelUtils;

public class CharactersPanel {
    JComponent charactersPanel;

    public CharactersPanel(JTabbedPane tabbedPane, ImageIcon icon) {
        charactersPanel = PanelUtils.makeTextPanel("Panel #4 (has a preferred size of 410 x 50).");

        charactersPanel.setLayout(new BoxLayout(charactersPanel, BoxLayout.PAGE_AXIS));
        tabbedPane.addTab("Characters", icon, charactersPanel, "Does nothing at all");
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
    }
}
