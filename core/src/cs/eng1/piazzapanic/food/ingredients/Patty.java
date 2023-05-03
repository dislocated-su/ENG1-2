package cs.eng1.piazzapanic.food.ingredients;

import cs.eng1.piazzapanic.food.FoodTextureManager;

/**
 * Patty. Used in making burgers. Can be grilled.
 */
public class Patty extends BasicGrillable {

    public Patty(FoodTextureManager textureManager) {
        super("patty", textureManager);
    }
}
