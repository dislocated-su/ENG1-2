package cs.eng1.piazzapanic.food.ingredients;

import cs.eng1.piazzapanic.food.FoodTextureManager;

/**
 * Dough. Used in making pizza.
 *
 * @author Andrey Samoilov
 */
public class Dough extends Ingredient {

    /**
     * Create a new {@link Dough}
     * @param textureManager to find the correct texture.
     */
    public Dough(FoodTextureManager textureManager) {
        super("dough", textureManager);
    }
}
