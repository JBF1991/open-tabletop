package open.tabletop.generation;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import com.google.gson.Gson; 
import com.google.gson.GsonBuilder;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;

import open.tabletop.generation.decorators.Decorator;
import open.tabletop.generation.decorators.DuplicateMessageTypeException;
import open.tabletop.utils.JsonBodyHandler;

public class Completion {
    private Config config; //TODO: Maybe we should extract this as a global "Settings" object?
    private Map<String, ChatMessage> messages;

    public Completion(Config config) {
        this.config = config;
        this.messages = new LinkedHashMap<>(); //Linked to maintain insertion order.
    }

    public <T extends Decorator<String, ChatMessage>> Completion with(T t) throws DuplicateMessageTypeException {
        t.apply(messages);
        return this;
    }

    public CompletableFuture<ChatCompletionResult> getCompletion() {
        ChatCompletionRequest req = ChatCompletionRequest.builder()
                .model(config.getModel())
                .messages(new ArrayList<>(messages.values()))
                .build();
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String body = gson.toJson(req);
            System.out.println(body);
            String openAPIKey = config.getOpenAiAPIKey();

            HttpRequest chatCompletionsPost = HttpRequest.newBuilder()
                .uri(new URI("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Content-Language", "en-US")
                .header("Authorization", "Bearer "+openAPIKey)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

            CompletableFuture<ChatCompletionResult> response = HttpClient
                .newHttpClient()
                .sendAsync(chatCompletionsPost, new JsonBodyHandler<>(ChatCompletionResult.class))
                .thenApply(HttpResponse::body);

            return response;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}