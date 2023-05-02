package cs.eng1.piazzapanic.utility.saving;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import cs.eng1.piazzapanic.chef.FixedStack;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.Ingredient;
import cs.eng1.piazzapanic.stations.IngredientStack;

/**
 * SavedIngredientStack
 */
public class SavedIngredientStack {

    // Figure out a way to not use SavedFood[] as it does not get stored correctly
    SerializeableMap<String, SavedFood[]> stack;
    int maxStackSize;

    SavedIngredientStack(IngredientStack stack) {
        this.maxStackSize = stack.maxStackSize;

        Map<String, SavedFood[]> map = new HashMap<String, SavedFood[]>();

        for (String type : stack.keySet()) {
            FixedStack<Ingredient> fixedStack = stack.get(type);

            SavedFood[] ingredients = new SavedFood[fixedStack.size()];
            for (int i = 0; i < ingredients.length; i++) {
                ingredients[i] = new SavedFood(fixedStack.get(i));
            }

            map.put(type, ingredients);
        }

        this.stack = new SerializeableMap<>(map);
    }

    SavedIngredientStack() {

    }

    public IngredientStack get(FoodTextureManager manager) {
        Map<String, SavedFood[]> savedFoodMap = stack.get();

        IngredientStack ingredientStack = new IngredientStack(maxStackSize);

        // for (String key : savedFoodMap.keySet()) {
        // Object o = savedFoodMap.get(key);
        // if (o instanceof JsonValue) {
        // JsonValue val = (JsonValue) o;
        // Gdx.app.log("", (val.toString()));
        // }
        Gdx.app.log("", "");
        // for (SavedFood savedFood : arr) {
        // ingredientStack.addIngredient(entry.getKey(), (Ingredient)
        // savedFood.get(manager));
        // }

        return ingredientStack;
    }

}