package cs.eng1.piazzapanic.stations;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import cs.eng1.piazzapanic.food.ingredients.Ingredient;
import cs.eng1.piazzapanic.ui.StationActionUI;
import cs.eng1.piazzapanic.ui.StationUIController;
import java.util.LinkedList;

/**
 * The IngredientStation class is a station representing the place in the pantry where you can pick up a specific {@link Ingredient}
 * @author Matt Fitzpatrick
 * @author Alistair Foggin
 * @author Andrey Samoilov
 * @author Ross Holmes
 */
public class IngredientStation extends Station {

    protected final Ingredient ingredientDispensed;

    /**
     * The constructor method for the class
     *
     * @param id           The unique identifier of the station
     * @param image        The rectangular area of the texture
     * @param uiController The controller from which we can get show and hide the
     *                     action
     *                     buttons belonging to the station
     * @param alignment    Dictates where the action buttons are shown
     * @param locked       Whether the station is locked and has to be purchased before it can be used.
     * @param ingredient   The ingredient that this station will dispense.
     */
    public IngredientStation(
        int id,
        TextureRegion image,
        StationUIController uiController,
        StationActionUI.ActionAlignment alignment,
        Boolean locked,
        Ingredient ingredient
    ) {
        super(id, image, uiController, alignment, locked);
        ingredientDispensed = ingredient; // What ingredient the station will give to the player.
    }

    @Override
    public LinkedList<StationAction.ActionType> getActionTypes() {
        LinkedList<StationAction.ActionType> actionTypes =
            super.getActionTypes();
        if (nearbyChef == null) {
            return new LinkedList<>();
        }
        if (locked) {
            return actionTypes;
        }
        if (nearbyChef.canGrabIngredient()) {
            actionTypes.add(StationAction.ActionType.GRAB_INGREDIENT);
        }
        return actionTypes;
    }

    @Override
    public void doStationAction(StationAction.ActionType action) {
        super.doStationAction(action);
        if (action == StationAction.ActionType.GRAB_INGREDIENT) {
            if (this.nearbyChef != null && nearbyChef.canGrabIngredient()) {
                nearbyChef.grabItem(
                    Ingredient.fromString(
                        ingredientDispensed.getType(),
                        ingredientDispensed.getTextureManager()
                    )
                );
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (ingredientDispensed != null) {
            drawFoodTexture(batch, ingredientDispensed.getTexture(), 1.5f);
        }
    }
}
