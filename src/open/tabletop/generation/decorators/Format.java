package open.tabletop.generation.decorators;

import java.util.Map;

import com.theokanning.openai.completion.chat.ChatMessage;

public class Format implements Decorator<String, ChatMessage> {
    private static final String MESSAGE_TYPE = "format";
    private String content;

    public Format(String content) {
        this.content = content;
    }

    @Override
    public void apply(Map<String, ChatMessage> messages) throws DuplicateMessageTypeException {
        if (messages.containsKey(MESSAGE_TYPE)) {
            throw new DuplicateMessageTypeException(MESSAGE_TYPE);
        } else {
            messages.put(MESSAGE_TYPE, new ChatMessage("user", content));
        }
    }
}
