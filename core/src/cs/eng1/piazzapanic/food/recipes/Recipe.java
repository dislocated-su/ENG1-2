package cs.eng1.piazzapanic.food.recipes;

import com.badlogic.gdx.graphics.Texture;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.interfaces.Holdable;
import cs.eng1.piazzapanic.stations.StationAction.ActionType;
import java.util.LinkedList;
import java.util.List;

/**
 * The Recipe class is the parent class of the food classes that
 * dictates what ingredients are needed to make them
 *
 * @author Alistair Foggin
 */
public class Recipe implements Holdable {

    private final FoodTextureManager textureManager;
    private final String type;

    /**
     * The constructor method for the class
     *
     * @param type           The food object that inherits the class
     * @param textureManager The controller from which we can get information on
     *                       what texture
     *                       each food object should have
     */
    public Recipe(String type, FoodTextureManager textureManager) {
        this.type = type;
        this.textureManager = textureManager;
    }

    /**
     * @inheritDoc
     */
    public Texture getTexture() {
        return textureManager.getTexture(type);
    }

    /**
     * @inheritDoc
     */
    public String getType() {
        return type;
    }

    /**
     * Gets what ingredients are needed to make the food item
     *
     * @return The list of ingredients that are needed
     */
    public List<String> getRecipeIngredients() {
        LinkedList<String> ingredientTypes = new LinkedList<>();
        switch (getType()) {
            case "burger":
                ingredientTypes.add("bun");
                ingredientTypes.add("patty_grilled");
                break;
            case "salad":
                ingredientTypes.add("tomato_chopped");
                ingredientTypes.add("lettuce_chopped");
                break;
            case "pizza":
                ingredientTypes.add("dough");
                ingredientTypes.add("tomato_chopped");
                ingredientTypes.add("cheese_sliced");
                break;
            case "jacket_potato":
                ingredientTypes.add("potato_cooked");
                ingredientTypes.add("cheese_sliced");
                break;
        }
        return ingredientTypes;
    }

    /**
     * Creates a new {@link Recipe} instance based on the specified {@link ActionType}.
     * This method is used to convert compatible action types into their corresponding recipe when interacting with a station.
     * @param action The {@link ActionType} to create the {@link Recipe} for.
     * @param textureManager The {@link FoodTextureManager} instance to use for loading textures.
     * @return The created {@link Recipe} instance.
     * @throws IllegalArgumentException If the specified {@link ActionType} is not valid.
     */
    public static Recipe fromAction(
        ActionType action,
        FoodTextureManager textureManager
    ) {
        switch (action) {
            case MAKE_BURGER:
                return new Burger(textureManager);
            case MAKE_SALAD:
                return new Salad(textureManager);
            case MAKE_JACKET:
                return new JacketPotato(textureManager);
            case MAKE_PIZZA:
                return new Pizza(textureManager);
            default:
                throw new IllegalArgumentException(
                    action.name() + "is not a valid recipe type"
                );
        }
    }

    /**
     * @see Recipe#fromAction(ActionType, FoodTextureManager)
     */
    public static Recipe fromString(
        String type,
        FoodTextureManager textureManager
    ) {
        switch (type) {
            case "burger":
                return new Burger(textureManager);
            case "salad":
                return new Salad(textureManager);
            case "jacket_potato":
                return new JacketPotato(textureManager);
            case "pizza":
                return new Pizza(textureManager);
            default:
                throw new IllegalArgumentException(
                    type + "is not a valid recipe type"
                );
        }
    }

    public FoodTextureManager getTextureManager() {
        return textureManager;
    }
}
