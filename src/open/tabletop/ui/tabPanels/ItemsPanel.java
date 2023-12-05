package open.tabletop.ui.tabPanels;

import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import open.tabletop.ui.PanelUtils;

public class ItemsPanel {

    JComponent itemsPanel;
    
    public ItemsPanel(JTabbedPane tabbedPane, ImageIcon icon) {
        itemsPanel = PanelUtils.makeTextPanel("Panel #5");

        itemsPanel.setLayout(new BoxLayout(itemsPanel, BoxLayout.PAGE_AXIS));
        tabbedPane.addTab("Items", icon, itemsPanel, "Does nothing at all");
        tabbedPane.setMnemonicAt(4, KeyEvent.VK_5);
    }
}
