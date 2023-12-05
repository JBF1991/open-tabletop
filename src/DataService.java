
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import org.bspfsystems.simplejson.JSONObject;
import org.bspfsystems.simplejson.parser.JSONParser;

public class DataService {

    private static DataModel currentModel;

    public DataModel getCurrentModel() {
        return currentModel;
    }

    public void setCurrentModel(DataModel model) {
        currentModel = model;
    }

    public String base64Encode(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes());
    }

    public String base64Decode(String str) {
        return new String(Base64.getDecoder().decode(str));
    }


    public DataModel loadDataFromFile() {
        try {
            DataModel newModel = new DataModel();
            String jsonString = Files.readString(Paths.get("data.json"), StandardCharsets.UTF_8);
            JSONObject obj = (JSONObject)JSONParser.deserialize(jsonString);
            newModel.style = obj.getString("style");
            newModel.worldDescription = obj.getString("world");
            newModel.textModel = obj.getString("text-model");
            newModel.imageModel = obj.getString("image-model");
            newModel.keyfileName = obj.getString("openai-api-key");
            return newModel;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveDataToFile(DataModel model) {
        if (OpenTabletop.isAppReady == false) {
            return;
        }
        try {
            PrintWriter out = new PrintWriter("data.json");
            String jsonTemplate = """
            {
                "style" : "%s",
                "world" : "%s",
                "text-model" : "%s",
                "image-model" : "%s",
                "openai-api-key" : "%s"
            }
            """;
            String json = String.format(
                jsonTemplate,
                model.style,
                model.worldDescription.replaceAll("\n", "\\\\n"),
                model.textModel,
                model.imageModel,
                model.keyfileName
            );
            out.println(json);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
