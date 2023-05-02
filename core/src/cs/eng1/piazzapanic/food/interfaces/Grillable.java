package cs.eng1.piazzapanic.food.interfaces;

/**
 * A {@link Holdable} that can be grilled on {@link cs.eng1.piazzapanic.stations.GrillingStation}
 * Ingredients that implement this must have 2-step processing logic.
 *
 * @author Andrey Samoilov
 * @author Ross Holmes
 */
public interface Grillable extends Holdable {
    /**
     * Perform one tick of grilling
     * @param delta time elapsed since the previous frame in seconds.
     * @return {@code boolean} indicating whether the grilling process is complete.
     */
    boolean grillTick(float delta);

    boolean getHalfGrilled();

    boolean getGrilled();

    Holdable getGrillResult();

    boolean grillStepComplete();

    float getGrillProgress();

    /**
     * This action is done by the player and prevents the object from becoming burnt.
     */
    void flip();
}
