package cs.eng1.piazzapanic.food.interfaces;

/**
 * A {@link Holdable} that can be chopped on a cutting board. Contrary to {@link Cookable} and {@link Grillable},
 * a Choppable can be processed in one step i.e. doesn't need to be flipped.
 *
 * @author Andrey Samoilov
 * @author Ross Holmes
 */
public interface Choppable extends Holdable {
    /**
     * Perform one tick of chopping
     * @param delta time elapsed since the previous frame in seconds.
     * @return {@code boolean} indicating whether the chopping process is complete.
     */
    boolean choppingTick(float delta);

    boolean getChopped();

    Holdable getChoppingResult();

    float getChoppingProgress();
}
