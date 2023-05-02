package cs.eng1.piazzapanic.chef;

import java.util.Stack;

/**
 * A wrapper over java's builtin Stack for type T
 *
 * @param <T> The type that should be used for all the stack elements
 *
 * @author Matt Fitzpatrick
 */
public class FixedStack<T> extends Stack<T> {

    public final int maxSize;

    /**
     * Create a new instance of FixedStack
     * @param size maximum size of the stack
     */
    public FixedStack(int size) {
        super();
        this.maxSize = size;
    }


    /**
     * @param item the item to be pushed onto this stack.
     * @return the item that was pushed.
     */
    @Override
    public T push(T item) {
        if (!hasSpace()) {
            return null;
        }
        return super.push(item);
    }

    /**
     * @return whether the stack has space for at least 1 more element.
     */
    public boolean hasSpace() {
        return this.size() != maxSize;
    }
}
