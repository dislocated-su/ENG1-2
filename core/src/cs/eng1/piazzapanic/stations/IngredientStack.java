package cs.eng1.piazzapanic.stations;

import cs.eng1.piazzapanic.chef.FixedStack;
import cs.eng1.piazzapanic.food.ingredients.Ingredient;
import java.util.HashMap;

public class IngredientStack extends HashMap<String, FixedStack<Ingredient>> {

    public final int maxStackSize;

    // private Map<String, FixedStack<Ingredient>> map = new HashMap<String,
    // FixedStack<Ingredient>>();

    public IngredientStack(int maxStackSize) {
        super();
        this.maxStackSize = maxStackSize;
    }

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

    public void addIngredient(String type, Ingredient ingredient) {
        if (!this.containsKey(type)) {
            FixedStack<Ingredient> newStack = new FixedStack<Ingredient>(
                maxStackSize
            );
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
