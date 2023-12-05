package open.tabletop.generation.decorators;

import java.util.Map;

@FunctionalInterface
public interface Decorator<S, C> {
    void apply(Map<S, C> messages) throws DuplicateMessageTypeException;
}
