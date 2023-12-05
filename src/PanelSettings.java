
import java.awt.Dimension;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PanelSettings extends JPanel implements IModelRefresher {

    JComboBox<String> textModelBox;
    JComboBox<String> imageModelBox;
    JTextField keyfilenameField;

    private DataService dataService = new DataService();
    
    public PanelSettings() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JPanel settingsRow1 = new JPanel(); settingsRow1.setMaximumSize(OpenTabletop.shortRow);
        JPanel settingsRow2 = new JPanel(); settingsRow2.setMaximumSize(OpenTabletop.shortRow);
        JPanel settingsRow3 = new JPanel(); settingsRow3.setMaximumSize(OpenTabletop.shortRow);
        String[] textModelChoices = { 
            "gpt-3.5",
            "gpt-3.5-turbo",
            "gpt-4",
            "gpt-4-32k"
        };
        textModelBox = new JComboBox<String>(textModelChoices);
        textModelBox.setSelectedIndex(0);
        textModelBox.addActionListener(e -> {
            dataService.saveDataToFile(dataService.getCurrentModel());
        });
        String[] imageModelChoices = { 
            "dall-e-2",
            "dall-e-3"
        };
        imageModelBox = new JComboBox<String>(imageModelChoices);
        imageModelBox.setSelectedIndex(0);
        imageModelBox.addActionListener(e -> {
            dataService.saveDataToFile(dataService.getCurrentModel());
        });
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
                int returnVal = fc.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    keyfilenameField.setText(file.getPath());
                    dataService.saveDataToFile(dataService.getCurrentModel());
                }
        });
        settingsRow3.add(openButton);
        settingsRow3.add(keyfilenameField);
        this.add(settingsRow1);
        this.add(settingsRow2);
        this.add(settingsRow3);
        this.add(Box.createVerticalGlue());
    }

    @Override
    public void refreshUIFromModel(DataModel model) {
        textModelBox.setSelectedItem(model.textModel);
        imageModelBox.setSelectedItem(model.imageModel);
        keyfilenameField.setText(model.keyfileName);
    }
}
