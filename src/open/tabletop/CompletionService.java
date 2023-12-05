package open.tabletop;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;

import open.tabletop.dto.ApplicationData;
import open.tabletop.generation.Completion;
import open.tabletop.generation.Config;
import open.tabletop.generation.decorators.Primer;
import open.tabletop.generation.decorators.Format;
import open.tabletop.generation.decorators.UserInput;

public class CompletionService {
    public CompletableFuture<String> requestBasicWorld(String format, String question) {
        try {
            String openAPIKey = Files.readString(Paths.get(ApplicationData.get().getOpenaiApiKey()), StandardCharsets.UTF_8);

            Config config = Config.builder()
                .model(ApplicationData.get().getTextModel())
                .openAiAPIKey(openAPIKey)
            .build();

            Completion comp = new Completion(config)
                .with(new Primer("You are a creative narrator for a tabletop role playing game. You are narrating in the style of "+ApplicationData.get().getStyle()))
                .with(new Format(format))
                .with(new UserInput(question));

            return comp.getCompletion().thenApply((r) -> r.getChoices().get(0).getMessage().getContent());
        } catch (Exception e) {
            e.printStackTrace();
            return CompletableFuture.failedFuture(e);
        }
    }
}
