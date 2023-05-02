package cs.eng1.piazzapanic.food.recipes;

import cs.eng1.piazzapanic.food.FoodTextureManager;
/**
 * Pizza
 *
 * @see Recipe
 * @see cs.eng1.piazzapanic.food.ingredients.UncookedPizza
 * @author Andrey Samoilov
 */
public class Pizza extends Recipe {

    /**
     * @see Recipe
     */
    public Pizza(FoodTextureManager textureManager) {
        super("pizza", textureManager);
    }
}
