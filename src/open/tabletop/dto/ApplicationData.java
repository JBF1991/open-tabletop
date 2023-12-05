package open.tabletop.dto;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.GsonBuilder;

import lombok.Data;

@Data
public final class ApplicationData {
    private static final ApplicationData INSTANCE = new ApplicationData();
    private String style;
    private String world;
    private String textModel;
    private String imageModel;
    private String openaiApiKey;


    public static ApplicationData get() {
        return INSTANCE;
    }

    public static void save() {
        try {
            PrintWriter out = new PrintWriter("data.json");
            String json = new GsonBuilder()
                .setPrettyPrinting()
                .serializeNulls()
                .create()
                .toJson(INSTANCE);
        
            out.println(json);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        try {
            Path path = Paths.get("data.json");
            
            String jsonString = Files.readString(path, StandardCharsets.UTF_8);
            ApplicationData loadedData = new GsonBuilder().setPrettyPrinting().create().fromJson(jsonString, ApplicationData.class);
            
            ApplicationData.INSTANCE.style = loadedData.style;
            ApplicationData.INSTANCE.world = loadedData.world;
            ApplicationData.INSTANCE.textModel = loadedData.textModel;
            ApplicationData.INSTANCE.imageModel = loadedData.imageModel;
            ApplicationData.INSTANCE.openaiApiKey = loadedData.openaiApiKey;
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
