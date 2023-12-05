package open.tabletop.generation.decorators;

import java.util.Map;

import com.theokanning.openai.completion.chat.ChatMessage;

public class Primer implements Decorator<String, ChatMessage> {
    private static final String MESSAGE_TYPE = "primer";
    private String content;

    public Primer(String content) {
        this.content = content;
    }

    @Override
    public void apply(Map<String, ChatMessage> messages) throws DuplicateMessageTypeException {
        if (messages.containsKey(MESSAGE_TYPE)) {
            throw new DuplicateMessageTypeException(MESSAGE_TYPE);
        } else {
            messages.put(MESSAGE_TYPE, new ChatMessage("system", content));
        }
    }
    
}
