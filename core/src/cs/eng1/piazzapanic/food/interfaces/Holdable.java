package cs.eng1.piazzapanic.food.interfaces;

import com.badlogic.gdx.graphics.Texture;

/**
 * Represents any item that can be held by the chefs.
 *
 * @author Andrey Samoilov
 * @author Ross Holmes
 */
public interface Holdable {
    /**
     * Get the type of this Holdable.
     * @return string representing the type
     */
    String getType();

    /**
     * Get the texture of this Holdable.
     * @return {@link Texture} used to render the Holdable.
     */
    Texture getTexture();
}
