package cs.eng1.piazzapanic.food.ingredients;

import cs.eng1.piazzapanic.food.FoodTextureManager;

/**
 * Lettuce. Used in making salads. Can be chopped.
 * @author Alistair Foggin
 */
public class Lettuce extends BasicChoppable {

    /**
     * Create a new {@link Lettuce}
     * @param textureManager to find the correct texture.
     */
    public Lettuce(FoodTextureManager textureManager) {
        super("lettuce", textureManager);
    }
}
