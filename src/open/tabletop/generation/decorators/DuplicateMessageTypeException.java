package open.tabletop.generation.decorators;

public class DuplicateMessageTypeException extends Exception {
    public DuplicateMessageTypeException(String messageType) {
        super("Completion can not contain multiple "+messageType+" messages.");
    }
}
