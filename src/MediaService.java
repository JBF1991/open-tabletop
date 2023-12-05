
import javax.swing.ImageIcon;

public class MediaService {
    
    
    public ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = OpenTabletop.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

}
