package cs.eng1.piazzapanic.food.ingredients;

import cs.eng1.piazzapanic.food.FoodTextureManager;

/**
 * Tomato. Used in making salads and pizza. Can be chopped.
 */
public class Tomato extends BasicChoppable {

    public Tomato(FoodTextureManager textureManager) {
        super("tomato", textureManager);
    }
}
