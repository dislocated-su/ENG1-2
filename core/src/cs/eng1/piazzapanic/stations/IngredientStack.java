package cs.eng1.piazzapanic.stations;

import cs.eng1.piazzapanic.chef.FixedStack;
import cs.eng1.piazzapanic.food.ingredients.Ingredient;
import cs.eng1.piazzapanic.food.recipes.Recipe;
import java.util.HashMap;

/**
 * An ingredient stack acts like a collection of {@link FixedStack<Ingredient>},
 * where there can be many ingredients of different types,
 * but only a specified maximum amount of ingredient per type.
 *
 * This allows {@link RecipeStation} to create a {@link Recipe} out of {@link Ingredient}s that are out of order.
 *
 * @author Andrey Samoilov
 */
public class IngredientStack extends HashMap<String, FixedStack<Ingredient>> {

    public final int maxStackSize;

    /**
     * Create a new {@link IngredientStack} given a maximum stack size.
     * @param maxStackSize how much of a specific ingredient is permitted to be stored within one stack.
     */
    public IngredientStack(int maxStackSize) {
        super();
        this.maxStackSize = maxStackSize;
    }

    /**
     * Remove an ingredient from the collection.
     * @param type The type of ingredient to be removed. Can be the same as {@link Ingredient#getType()}
     * @return the {@link Ingredient} that was removed, or null if an ingredient of specified type was not found.
     */
    public Ingredient removeIngredient(String type) {
        if (this.containsKey(type)) {
            FixedStack<Ingredient> stack = this.get(type);
            if (stack.size() > 0) {
                Ingredient ingredient = stack.pop();
                if (stack.size() == 0) {
                    this.remove(type);
                }
                return ingredient;
            }
        }
        return null;
    }

    /**
     * Add a new {@link Ingredient} to the collection.
     * @param type The type of ingredient to be added. Can be the same as {@link Ingredient#getType()}
     * @param ingredient {@link Ingredient} to add.
     */
    public void addIngredient(String type, Ingredient ingredient) {
        if (!this.containsKey(type)) {
            FixedStack<Ingredient> newStack = new FixedStack<>(maxStackSize);
            newStack.add(ingredient);
            this.put(type, newStack);
        } else {
            this.get(type).add(ingredient);
        }
    }

    public boolean contains(String type) {
        if (!this.containsKey(type)) {
            return false;
        }

        return this.get(type).size() > 0;
    }

    public void reset() {
        this.clear();
    }
}
