
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class PanelQuests extends JPanel implements IModelRefresher {

    public PanelQuests() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    @Override
    public void refreshUIFromModel(DataModel model) {
        
    }
    
}
