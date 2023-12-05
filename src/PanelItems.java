
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class PanelItems extends JPanel implements IModelRefresher {

    public PanelItems() {
        super();
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    }

    @Override
    public void refreshUIFromModel(DataModel model) {
        
    }
    
}
