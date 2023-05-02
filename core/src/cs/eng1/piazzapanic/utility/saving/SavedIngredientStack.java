package cs.eng1.piazzapanic.utility.saving;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import cs.eng1.piazzapanic.chef.FixedStack;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.Ingredient;
import cs.eng1.piazzapanic.stations.IngredientStack;
import java.util.HashMap;
import java.util.Map;

/**
 * SavedIngredientStack
 */
public class SavedIngredientStack {

    // Figure out a way to not use SavedFood[] as it does not get stored correctly
    SerializableMap<String, String> stack;
    int maxStackSize;

    SavedIngredientStack(IngredientStack stack) {
        this.maxStackSize = stack.maxStackSize;

        Map<String, String> map = new HashMap<>();

        for (String type : stack.keySet()) {
            FixedStack<Ingredient> fixedStack = stack.get(type);

            SavedFood[] ingredients = new SavedFood[fixedStack.size()];
            for (int i = 0; i < ingredients.length; i++) {
                ingredients[i] = new SavedFood(fixedStack.get(i));
            }

            Json json = new Json();
            String jsonString = json.toJson(ingredients, SavedFood.class);

            Gdx.app.log(ingredients.getClass().getTypeName(), jsonString);

            map.put(type, jsonString);
        }

        this.stack = new SerializableMap<>(map);
    }

    SavedIngredientStack() {}

    public IngredientStack get(FoodTextureManager manager) {
        Map<String, String> savedFoodMap = stack.get();

        IngredientStack ingredientStack = new IngredientStack(maxStackSize);

        Json json = new Json();

        for (String key : savedFoodMap.keySet()) {
            SavedFood[] arr = json.fromJson(
                SavedFood[].class,
                savedFoodMap.get(key)
            );

            for (SavedFood savedFood : arr) {
                ingredientStack.addIngredient(
                    key,
                    (Ingredient) savedFood.get(manager)
                );
            }
        }
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
