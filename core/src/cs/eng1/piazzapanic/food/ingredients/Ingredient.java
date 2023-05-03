package cs.eng1.piazzapanic.food.ingredients;

import com.badlogic.gdx.graphics.Texture;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.interfaces.Holdable;
import cs.eng1.piazzapanic.food.recipes.Recipe;

/**
 * Base class for all ingredients. Used to distinguish ingredients used in the preparation process and Recipes.
 * @see Recipe
 * @author Alistair Foggin
 * @author Andrey Samoilov
 * @author Ross Holmes
 */
public abstract class Ingredient implements Holdable {

    private final String type;
    protected final FoodTextureManager textureManager;
    protected boolean cooked = false;
    protected boolean chopped = false;
    protected boolean grilled = false;
    protected boolean useable = true;

    public Ingredient(String type, FoodTextureManager textureManager) {
        this.type = type;
        this.textureManager = textureManager;
    }

    @Override
    public String toString() {
        String output = getType() + "_";
        if (!useable) {
            output += "ruined";
        } else if (cooked) {
            output += "cooked";
        } else if (grilled) {
            output += "grilled";
        } else if (chopped) {
            output += "chopped";
        } else {
            output += "raw";
        }
        return output;
    }

    /**
     * Initialize an Ingredient based on a string name
     *
     * @param ingredientName the name of the ingredient which can be defined from
     *                       Tiled
     * @return the Ingredient of the type defined by the input
     */
    public static Ingredient fromString(String ingredientName, FoodTextureManager textureManager) {
        switch (ingredientName) {
            case "patty":
                return new Patty(textureManager);
            case "tomato":
                return new Tomato(textureManager);
            case "lettuce":
                return new Lettuce(textureManager);
            case "bun":
                return new Bun(textureManager);
            case "cheese":
                return new Cheese(textureManager);
            case "potato":
                return new Potato(textureManager);
            case "dough":
                return new Dough(textureManager);
            case "uncooked_pizza":
                return new UncookedPizza(textureManager);
            default:
                return null;
        }
    }

    /**
     * Initialize an array of ingredients based on the input string.
     *
     * @param csvIngredientNames A string containing a list of ingredient names
     *                           separated by commas
     *                           with no whitespace as defined in Tiled
     * @return An array of Ingredient based on the input string
     */
    public static Ingredient[] arrayFromString(
        String csvIngredientNames,
        FoodTextureManager textureManager
    ) {
        String[] ingredientNames = csvIngredientNames.split(",");
        Ingredient[] ingredients = new Ingredient[ingredientNames.length];
        for (int i = 0; i < ingredientNames.length; i++) {
            ingredients[i] = fromString(ingredientNames[i], textureManager);
        }
        return ingredients;
    }

    public String getType() {
        return type;
    }

    public Texture getTexture() {
        return textureManager.getTexture(getType());
    }

    public void setIsCooked(boolean value) {
        cooked = value;
    }

    public boolean getCooked() {
        return cooked;
    }

    public void setIsGrilled(boolean value) {
        grilled = value;
    }

    public boolean getGrilled() {
        return grilled;
    }

    public void setChopped(boolean value) {
        chopped = value;
    }

    public boolean getChopped() {
        return chopped;
    }

    public void setUseable(boolean value) {
        useable = value;
    }

    public boolean getUseable() {
        return useable;
    }

    public FoodTextureManager getTextureManager() {
        return textureManager;
    }
}
