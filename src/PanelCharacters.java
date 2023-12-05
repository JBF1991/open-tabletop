
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class PanelCharacters extends JPanel implements IModelRefresher {

    public PanelCharacters() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    @Override
    public void refreshUIFromModel(DataModel model) {
        
    }
    
}
