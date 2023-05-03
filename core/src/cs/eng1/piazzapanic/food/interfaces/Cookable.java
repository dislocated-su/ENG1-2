package cs.eng1.piazzapanic.food.interfaces;

/**
 * A {@link Holdable} that can be cooked in an oven.
 * Ingredients that implement this must have 2-step processing logic.
 *
 * @author Andrey Samoilov
 * @author Ross Holmes
 */
public interface Cookable extends Holdable {
    /**
     * Perform one tick of cooking
     * @param delta time elapsed since the previous frame in seconds.
     * @return {@code boolean} indicating whether the cooking process is complete.
     */
    boolean cookingTick(float delta);

    /**
     * @return whether the first step of cooking is complete and the second step is ongoing.
     */
    boolean getHalfCooked();

    /**
     * @return whether both steps of cooking are complete.
     */
    boolean getCooked();

    /**
     * @return get the result of cooking. This can be any other Holdable for flexibility.
     */
    Holdable getCookingResult();

    //TODO: figure this out
    boolean cookingStepComplete();

    float getCookingProgress();

    /**
     * This action is done by the player and prevents the object from becoming burnt.
     */
    void flip();
}
