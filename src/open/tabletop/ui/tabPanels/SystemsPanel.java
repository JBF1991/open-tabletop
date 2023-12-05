package open.tabletop.ui.tabPanels;

import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import open.tabletop.dto.ApplicationData;
import open.tabletop.ui.PanelUtils;

public class SystemsPanel {

    JComponent systemsPanel;
    JComboBox<String> styleCombo;

    public SystemsPanel(JTabbedPane tabbedPane, ImageIcon icon) {
        systemsPanel = PanelUtils.makeTextPanel("Panel #1");

        JPanel systemsRow1 = new JPanel(); systemsRow1.setMaximumSize(PanelUtils.shortRow);
        String[] choices = { 
            "Arthur C. Clarke",
            "C3PO",
            "Carl Jung",
            "C. S. Lewis",
            "Donald Trump",
            "Friedrich Nietzshe",
            "George R. R. Martin",
            "H. G. Wells",
            "H. P. Lovecraft",
            "Isaac Asimov",
            "J. K. Rowling",
            "John C. Maxwell",
            "John Grisham",
            "Lewis Carroll",
            "Philip K. Dick",
            "Richard Dawkins",
            "Sam Harris",
            "Soren Kierkegaard",
            "Stephen King",
            "The Apostle Paul",
            "Thomas Merton",
            "T. S. Eliot",
            "Voltaire",
            "William Shakespeare"
        };
        
        styleCombo = new JComboBox<String>(choices);
        styleCombo.addActionListener(e -> {
            ApplicationData.get().setStyle(styleCombo.getSelectedItem().toString());
            ApplicationData.save();
        });
        
        String style = ApplicationData.get().getStyle();
        if (style != null) {
            styleCombo.setSelectedItem(style);
        }

        systemsRow1.add(new JLabel("In The Style Of:"));
        systemsRow1.add(styleCombo);
        systemsPanel.add(systemsRow1);
        systemsPanel.add(Box.createGlue());

        
        systemsPanel.setLayout(new BoxLayout(systemsPanel, BoxLayout.PAGE_AXIS));
        tabbedPane.addTab("Systems", icon, systemsPanel,"Does nothing");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
    }
}
