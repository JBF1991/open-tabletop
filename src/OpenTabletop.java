
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.bspfsystems.simplejson.JSONObject;
import org.bspfsystems.simplejson.parser.JSONParser;


public class OpenTabletop extends JPanel implements IModelRefresher {

    public static OpenTabletop app;
    public static boolean isAppReady = false;
    public static Dimension shortRow = new Dimension(12000, 40);
    public static Dimension tallRow = new Dimension(12000, 120);
    public static Dimension hugeRow = new Dimension(12000, 480);

    // Panels
    public static PanelSystems systemsPanel = new PanelSystems();
    public static PanelWorld worldPanel = new PanelWorld();
    public static PanelCharacters charactersPanel = new PanelCharacters();
    public static PanelItems itemsPanel = new PanelItems();
    public static PanelQuests questsPanel = new PanelQuests();
    public static PanelSettings settingsPanel = new PanelSettings();

    ArrayList<IModelRefresher> refreshers = new ArrayList<IModelRefresher>(
        Arrays.asList(
            systemsPanel,
            worldPanel,
            charactersPanel,
            itemsPanel,
            questsPanel,
            settingsPanel
        )
    );

    private DataService dataService = new DataService();
    private MediaService mediaService = new MediaService();

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
                isAppReady = true;
                
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("TabbedPaneDemo");
        frame.setMinimumSize(new Dimension(1200, 720));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        OpenTabletop app = new OpenTabletop();
        OpenTabletop.app = app;
        frame.add(app, BorderLayout.CENTER);
        frame.pack();
        frame.setSize(new Dimension(1200, 720));
        frame.setVisible(true);
    }

    public OpenTabletop() {
        super(new GridLayout(1, 1));
        JTabbedPane tabbedPane = new JTabbedPane();
        ImageIcon icon = mediaService.createImageIcon("images/middle.gif");
        
        tabbedPane.addTab("Systems", icon, systemsPanel,"Does nothing");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        
        tabbedPane.addTab("World", icon, worldPanel,"Does twice as much nothing");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        tabbedPane.addTab("Characters", icon, charactersPanel, "Does nothing at all");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_4);
        
        tabbedPane.addTab("Items", icon, itemsPanel, "Does nothing at all");
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_5);
        
        tabbedPane.addTab("Quests", icon, questsPanel, "Still does nothing");
        tabbedPane.setMnemonicAt(4, KeyEvent.VK_3);
        
        tabbedPane.addTab("Settings", icon, settingsPanel, "Does nothing at all");
        tabbedPane.setMnemonicAt(5, KeyEvent.VK_6);
        //Add the tabbed pane to this panel.
        add(tabbedPane);
        dataService.setCurrentModel(dataService.loadDataFromFile());
        refresh();
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    private void refresh() {
        refreshUIFromModel(dataService.getCurrentModel());
    }

    public void refreshUIFromModel(DataModel model) {
        refreshers.forEach(r -> {
            r.refreshUIFromModel(model);
        });
    }

    public String request(String format, String question) {
        HttpURLConnection connection = null;
        question = question.replaceAll("\n", "\\\\n");
        question = "\"" + question + "\"";
        format = "\"" + format + "\"";
        DataModel model = dataService.getCurrentModel();
        String textModel = "\""+model.textModel + "\",";
        String styleof = model.style;
        String styleprimer = "\"You are a creative narrator for a tabletop role playing game. You are narrating in the style of "+styleof+".\"";
        String body = """
            {
                "model": """+textModel+"""
                "messages": [
                  {
                    "role": "system",
                    "content": """+styleprimer+"""
                  },
                  {
                    "role": "user",
                    "content": """+format+"""
                  },
                  {
                    "role": "assistant",
                    "content": "Please continue."
                  },
                  {
                    "role": "user",
                    "content": """+question+"""
                  }
                ]
              }
        """;
        System.out.println(body);
        try {
            String openAPIKey = Files.readString(Paths.get(model.keyfileName), StandardCharsets.UTF_8);;
            //Create connection
            URL url = new URL("https://api.openai.com/v1/chat/completions");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Content-Length", Integer.toString(body.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");  
            connection.setRequestProperty("Authorization", "Bearer "+openAPIKey);
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            //Send request
            DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
            wr.writeBytes(body);
            wr.close();
            //Get Response  
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            connection.disconnect();
            String jsonString = response.toString();
            JSONObject obj = (JSONObject)JSONParser.deserialize(jsonString);
            String message = obj.getArray("choices").getObject(0).getObject("message").getString("content");
            //System.out.println(message);
            return message;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
            connection.disconnect();
            }
        }
    }
}