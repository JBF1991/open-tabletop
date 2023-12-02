import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandler;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.bspfsystems.simplejson.JSONObject;
import org.bspfsystems.simplejson.parser.JSONParser;

public class OpenTabletop extends JPanel {

    JComponent systemsPanel, worldPanel, questsPanel, charactersPanel, itemsPanel, settingsPanel;

    // Serial Order
    JComboBox<String> styleCombo;
    JTextField keyfilenameField;
    JTextArea regenWorldArea;
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TabbedPaneDemo");
        frame.setMinimumSize(new Dimension(1200, 720));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Add content to the window.
        frame.add(new OpenTabletop(), BorderLayout.CENTER);
        //Display the window.
        frame.pack();
        frame.setSize(new Dimension(1200, 720));
        frame.setVisible(true);
    }

    public OpenTabletop() {
        super(new GridLayout(1, 1));
        JTabbedPane tabbedPane = new JTabbedPane();
        ImageIcon icon = createImageIcon("images/middle.gif");
        systemsPanel = makeTextPanel("Panel #1");
        tabbedPane.addTab("Systems", icon, systemsPanel,"Does nothing");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        worldPanel = makeTextPanel("Panel #2");
        worldPanel.setLayout(new BoxLayout(worldPanel, BoxLayout.LINE_AXIS));
        tabbedPane.addTab("World", icon, worldPanel,"Does twice as much nothing");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        questsPanel = makeTextPanel("Panel #3");
        tabbedPane.addTab("Quests", icon, questsPanel, "Still does nothing");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
        charactersPanel = makeTextPanel("Panel #4 (has a preferred size of 410 x 50).");
        charactersPanel.setPreferredSize(new Dimension(410, 50));
        tabbedPane.addTab("Characters", icon, charactersPanel, "Does nothing at all");
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
        itemsPanel = makeTextPanel("Panel #5");
        tabbedPane.addTab("Items", icon, itemsPanel, "Does nothing at all");
        tabbedPane.setMnemonicAt(4, KeyEvent.VK_5);
        settingsPanel = makeTextPanel("Panel #6");
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.LINE_AXIS));
        tabbedPane.addTab("Settings", icon, settingsPanel, "Does nothing at all");
        tabbedPane.setMnemonicAt(5, KeyEvent.VK_6);
        //Add the tabbed pane to this panel.
        add(tabbedPane);
        setupPanels();
        loadData();
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    protected String base64Encode(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    protected String base64Decode(String str) {
        return new String(Base64.getDecoder().decode(str));
    }

    protected void loadData() {
        try {
            String jsonString = Files.readString(Paths.get("data.json"), StandardCharsets.UTF_8);
            JSONObject obj = (JSONObject)JSONParser.deserialize(jsonString);
            styleCombo.setSelectedItem(obj.getString("style"));
            regenWorldArea.setText(obj.getString("world"));
            keyfilenameField.setText(obj.getString("openai-api-key"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void saveData() {
        try {
            PrintWriter out = new PrintWriter("data.json");
            String jsonTemplate = """
            {
                "style" : "%s",
                "world" : "%s",
                "openai-api-key" : "%s"
            }
            """;
            String json = String.format(
                jsonTemplate, 
                styleCombo.getSelectedItem().toString(),
                regenWorldArea.getText().replaceAll("\n", "\\\\n"),
                keyfilenameField.getText()
            );
            out.println(json);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected String request(String format, String question) {
        HttpURLConnection connection = null;
        question = question.replaceAll("\n", "\\\\n");
        question = "\"" + question + "\"";
        format = "\"" + format + "\"";
        String styleof = styleCombo.getSelectedItem().toString();
        String styleprimer = "\"You are a creative narrator for a tabletop role playing game. You are narrating in the style of "+styleof+".\"";
        String body = """
            {
                "model": "gpt-3.5-turbo",
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
            String openAPIKey = Files.readString(Paths.get(keyfilenameField.getText()), StandardCharsets.UTF_8);;
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

    protected void setupPanels() {
        // SYSTEM PANEL
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
            saveData();
        });
        systemsPanel.add(new JLabel("In The Style Of:"));
        systemsPanel.add(styleCombo);
        // WORLD PANEL
        regenWorldArea = new JTextArea();
        regenWorldArea.setLineWrap(true);
        regenWorldArea.setMaximumSize(new Dimension(640, 480));
        regenWorldArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                saveData();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                saveData();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                saveData();
            }
        });
        JButton regenWorldButton = new JButton("Regen");
        regenWorldButton.setMaximumSize(new Dimension(640, 30));
        regenWorldButton.addActionListener(e -> {
            String format = "Rewrite and expand on the next world description. Be sure to format your next answer in a few paragraphs.";
            String currentDescription = regenWorldArea.getText();
            if (currentDescription.trim().length() == 0) {
                currentDescription = "A random world of curiousity.";
            }
            String response = request(format, currentDescription);
            regenWorldArea.setText(response);
            saveData();
        });
        worldPanel.add(regenWorldButton);
        worldPanel.add(regenWorldArea);
        // SETTINGS PANEL
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
                    saveData();
                }
        });
        settingsPanel.add(openButton);
        settingsPanel.add(keyfilenameField);
    }

    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        panel.setLayout(new GridLayout(1, 1));
        return panel;
    }
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = OpenTabletop.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

}