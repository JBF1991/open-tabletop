
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelSystems extends JPanel implements IModelRefresher {

    public JComboBox<String> styleCombo;
    private DataService dataService = new DataService();

    public PanelSystems() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JPanel systemsRow1 = new JPanel(); systemsRow1.setMaximumSize(OpenTabletop.shortRow);
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
            DataModel model = dataService.getCurrentModel();
            model.style = styleCombo.getSelectedItem().toString();
            dataService.setCurrentModel(model);
            dataService.saveDataToFile(model);
        });
        systemsRow1.add(new JLabel("In The Style Of:"));
        systemsRow1.add(styleCombo);
        this.add(systemsRow1);
        this.add(Box.createGlue());
    }

    @Override
    public void refreshUIFromModel(DataModel model) {
        styleCombo.setSelectedItem(model.style);
    }

    
}
