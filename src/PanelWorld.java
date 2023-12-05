
import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class PanelWorld extends JPanel implements IModelRefresher {

    JTextArea regenWorldArea;
    private DataService dataService = new DataService();

    public PanelWorld() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JPanel worldRow1 = new JPanel(); worldRow1.setMaximumSize(OpenTabletop.hugeRow);
        regenWorldArea = new JTextArea();
        regenWorldArea.setLineWrap(true);
        regenWorldArea.setRows(20);
        regenWorldArea.setColumns(80);
        regenWorldArea.setMaximumSize(new Dimension(640, 480));
        Runnable saveModel = () -> {
            DataModel model = dataService.getCurrentModel();
            model.worldDescription = regenWorldArea.getText();
            dataService.setCurrentModel(model);
            dataService.saveDataToFile(model);
        };
        regenWorldArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                saveModel.run();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                saveModel.run();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                saveModel.run();
            }
        });
        JButton regenWorldButton = new JButton("Regen");
        regenWorldButton.setMaximumSize(new Dimension(120, 30));
        regenWorldButton.addActionListener(e -> {
            String format = "Rewrite and expand on the next world description. Be sure to format your next answer in a few paragraphs.";
            String currentDescription = regenWorldArea.getText();
            if (currentDescription.trim().length() == 0) {
                currentDescription = "A random world of curiousity.";
            }
            
            String response = OpenTabletop.app.request(format, currentDescription);
            regenWorldArea.setText(response);
            dataService.saveDataToFile(dataService.getCurrentModel());
        });
        worldRow1.add(regenWorldButton);
        worldRow1.add(regenWorldArea);
        this.add(worldRow1);
        this.add(Box.createGlue());
    }

    @Override
    public void refreshUIFromModel(DataModel model) {
        regenWorldArea.setText(model.worldDescription);
    }
}
