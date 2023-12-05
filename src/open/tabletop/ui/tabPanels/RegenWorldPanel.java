package open.tabletop.ui.tabPanels;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.util.concurrent.CompletableFuture;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import open.tabletop.CompletionService;
import open.tabletop.dto.ApplicationData;
import open.tabletop.ui.PanelUtils;

public class RegenWorldPanel {
    
    JComponent worldPanel;
    JTextArea regenWorldArea;
    private CompletionService completionService;


    public RegenWorldPanel(JTabbedPane tabbedPane, ImageIcon icon) {
        this.completionService = new CompletionService();

        worldPanel = PanelUtils.makeTextPanel("Panel #2");

        JPanel worldRow1 = new JPanel(); worldRow1.setMaximumSize(PanelUtils.hugeRow);
        regenWorldArea = new JTextArea();
        regenWorldArea.setLineWrap(true);
        regenWorldArea.setRows(20);
        regenWorldArea.setColumns(80);
        regenWorldArea.setMaximumSize(new Dimension(640, 480));
        regenWorldArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                ApplicationData.get().setWorld(regenWorldArea.getText());
                ApplicationData.save();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                ApplicationData.get().setWorld(regenWorldArea.getText());
                ApplicationData.save();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                ApplicationData.get().setWorld(regenWorldArea.getText());
                ApplicationData.save();
            }
        });

        String world = ApplicationData.get().getWorld();
        if (world != null) {
            regenWorldArea.setText(world);
        }
        
        JButton regenWorldButton = new JButton("Regen");
        regenWorldButton.setMaximumSize(new Dimension(120, 30));
        regenWorldButton.addActionListener(e -> {
            String format = "Rewrite and expand on the next world description. Be sure to format your next answer in a few paragraphs.";
            String currentDescription = regenWorldArea.getText();
            if (currentDescription.trim().length() == 0) {
                currentDescription = "A random world of curiousity.";
            }

            CompletableFuture<String> completionFuture = this.completionService.requestBasicWorld(format, currentDescription);

            completionFuture.whenComplete((response, ex) -> {
                regenWorldArea.setText(response);
                ApplicationData.get().setWorld(response);
                ApplicationData.save();
            });

            while (!completionFuture.isDone()) {
                //TODO: Present loading animation of some kind. 
            }
        });

        worldRow1.add(regenWorldButton);
        worldRow1.add(regenWorldArea);
        worldPanel.add(worldRow1);
        worldPanel.add(Box.createGlue());

        worldPanel.setLayout(new BoxLayout(worldPanel, BoxLayout.PAGE_AXIS));
        tabbedPane.addTab("World", icon, worldPanel,"Does twice as much nothing");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
    }
}
