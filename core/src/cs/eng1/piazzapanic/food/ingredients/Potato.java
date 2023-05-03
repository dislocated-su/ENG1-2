package cs.eng1.piazzapanic.food.ingredients;

import cs.eng1.piazzapanic.food.FoodTextureManager;

/**
 * Potato. Used in making jacket potato. Can be cooked.
 */
public class Potato extends BasicCookable {

    public Potato(FoodTextureManager textureManager) {
        super("potato", textureManager);
    }
}
