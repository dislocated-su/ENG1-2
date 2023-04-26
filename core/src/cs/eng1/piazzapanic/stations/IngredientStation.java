package cs.eng1.piazzapanic.stations;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import cs.eng1.piazzapanic.food.ingredients.Ingredient;
import cs.eng1.piazzapanic.ui.StationActionUI;
import cs.eng1.piazzapanic.ui.StationUIController;
import java.util.LinkedList;

public class IngredientStation extends Station {

    protected final Ingredient ingredientDispensed;

    public IngredientStation(
            int id,
            TextureRegion image,
            StationUIController uiController,
            StationActionUI.ActionAlignment alignment,
            Boolean locked, Ingredient ingredient
    ) {
        super(id, image, uiController, alignment, locked);
        ingredientDispensed = ingredient; // What ingredient the station will give to the player.
    }

    @Override
    public LinkedList<StationAction.ActionType> getActionTypes() {
        LinkedList<StationAction.ActionType> actionTypes = super.getActionTypes();
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
