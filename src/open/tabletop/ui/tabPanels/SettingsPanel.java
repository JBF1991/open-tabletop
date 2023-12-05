package open.tabletop.ui.tabPanels;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import open.tabletop.dto.ApplicationData;
import open.tabletop.ui.PanelUtils;

public class SettingsPanel {

    JComponent settingsPanel;

    JComboBox<String> textModelBox;
    JComboBox<String> imageModelBox;
    JTextField keyfilenameField;

    public SettingsPanel(JTabbedPane tabbedPane, ImageIcon icon) {
        settingsPanel = PanelUtils.makeTextPanel("Panel #6");

        JPanel settingsRow1 = new JPanel(); settingsRow1.setMaximumSize(PanelUtils.shortRow);
        JPanel settingsRow2 = new JPanel(); settingsRow2.setMaximumSize(PanelUtils.shortRow);
        JPanel settingsRow3 = new JPanel(); settingsRow3.setMaximumSize(PanelUtils.shortRow);
        String[] textModelChoices = { 
            "gpt-3.5",
            "gpt-3.5-turbo",
            "gpt-4",
            "gpt-4-32k"
        };
        textModelBox = new JComboBox<String>(textModelChoices);
        textModelBox.setSelectedIndex(0);
        textModelBox.addActionListener(e -> {
            ApplicationData.get().setTextModel(textModelBox.getSelectedItem().toString());
            ApplicationData.save();
        }); 

        String textModel = ApplicationData.get().getTextModel();
        if (textModel != null) {
            textModelBox.setSelectedItem(textModel);
        }

        String[] imageModelChoices = { 
            "dall-e-2",
            "dall-e-3"
        };
        imageModelBox = new JComboBox<String>(imageModelChoices);
        imageModelBox.setSelectedIndex(0);
        imageModelBox.addActionListener(e -> {
            ApplicationData.get().setImageModel(imageModelBox.getSelectedItem().toString());
            ApplicationData.save();
        }); 

        String imageModel = ApplicationData.get().getImageModel();
        if (imageModel != null) {
            imageModelBox.setSelectedItem(imageModel);
        }
        
        settingsRow1.add(new JLabel("Text Model:"));
        settingsRow1.add(textModelBox);
        settingsRow2.add(new JLabel("Image Model:"));
        settingsRow2.add(imageModelBox);
        
        JFileChooser fc = new JFileChooser();
        fc.setFileHidingEnabled(false);
        
        JButton openButton = new JButton("OpenAI API Keyfile");
        openButton.setMaximumSize(new Dimension(640, 30));
        keyfilenameField = new JTextField();
        keyfilenameField.setMaximumSize(new Dimension(640, 30));
        keyfilenameField.setEditable(false);
        openButton.addActionListener(e -> {
            //Handle open button action.
            int returnVal = fc.showOpenDialog(settingsPanel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                keyfilenameField.setText(file.getPath());
                ApplicationData.get().setOpenaiApiKey(keyfilenameField.getText());
                ApplicationData.save();
            } 
        });

        String keyFileName = ApplicationData.get().getOpenaiApiKey();
        if (keyFileName != null) {
            keyfilenameField.setText(keyFileName);
        }

        settingsRow3.add(openButton);
        settingsRow3.add(keyfilenameField);
        settingsPanel.add(settingsRow1);
        settingsPanel.add(settingsRow2);
        settingsPanel.add(settingsRow3);
        settingsPanel.add(Box.createVerticalGlue());

        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.PAGE_AXIS));
        tabbedPane.addTab("Settings", icon, settingsPanel, "Does nothing at all");
        tabbedPane.setMnemonicAt(5, KeyEvent.VK_6);
    }
}
