package cs.eng1.piazzapanic.food.ingredients;

import cs.eng1.piazzapanic.food.FoodTextureManager;

/**
 * Bun ingredient. Used in making burgers.
 *
 * @author Alistair Foggin
 */
public class Bun extends Ingredient {

    /**
     * Create a new {@link Bun}
     * @param textureManager to find the correct texture.
     */
    public Bun(FoodTextureManager textureManager) {
        super("bun", textureManager);
    }
}
