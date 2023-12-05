package open.tabletop.ui.tabPanels;

import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import open.tabletop.ui.PanelUtils;

public class QuestsPanel {
    JComponent questsPanel;

    public QuestsPanel(JTabbedPane tabbedPane, ImageIcon icon) {
        questsPanel = PanelUtils.makeTextPanel("Panel #3");
        
        questsPanel.setLayout(new BoxLayout(questsPanel, BoxLayout.PAGE_AXIS));
        tabbedPane.addTab("Quests", icon, questsPanel, "Still does nothing");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
    }
}
