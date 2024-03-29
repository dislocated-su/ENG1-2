package cs.eng1.piazzapanic.stations;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import cs.eng1.piazzapanic.food.ingredients.Ingredient;
import cs.eng1.piazzapanic.food.ingredients.Patty;
import cs.eng1.piazzapanic.food.interfaces.Grillable;
import cs.eng1.piazzapanic.food.interfaces.Holdable;
import cs.eng1.piazzapanic.ui.StationActionUI;
import cs.eng1.piazzapanic.ui.StationUIController;
import java.util.LinkedList;

/**
 * The GrillingStation class is a station representing the place in the kitchen
 * where you grill {@link Patty} to be used in making burgers.
 * @author Ross Holmes
 */
public class GrillingStation extends Station {

    public Grillable currentIngredient;
    public boolean progressVisible = false;
    private boolean checkUpdateUI = false;

    /**
     * The constructor method for the class
     *
     * @param id                  The unique identifier of the station
     * @param textureRegion       The rectangular area of the texture
     * @param uiController        The controller from which we can get show and hide
     *                            the action
     *                            buttons belonging to the station
     * @param alignment           Dictates where the action buttons are shown
     * @param locked              Whether the station is locked and has to be purchased before it can be used.
     */
    public GrillingStation(
        int id,
        TextureRegion textureRegion,
        StationUIController uiController,
        StationActionUI.ActionAlignment alignment,
        Boolean locked
    ) {
        super(id, textureRegion, uiController, alignment, locked);
    }

    @Override
    public void reset() {
        currentIngredient = null;
        progressVisible = false;
        super.reset();
    }

    /**
     * Called every frame. Used to update the progress bar and check if enough time
     * has passed for the
     * ingredient to be changed to its half cooked or cooked variant
     *
     * @param delta Time in seconds since the last frame.
     */
    @Override
    public void act(float delta) {
        if (inUse) {
            currentIngredient.grillTick(delta);

            if (currentIngredient != null && !((Ingredient) currentIngredient).getUseable() && !checkUpdateUI) {
                uiController.showActions(this, getActionTypes());
                checkUpdateUI = true;
            }
            assert currentIngredient != null;
            uiController.updateProgressValue(this, currentIngredient.getGrillProgress());

            if (currentIngredient.grillStepComplete() && progressVisible) {
                uiController.hideProgressBar(this);
                progressVisible = false;
                uiController.showActions(this, getActionTypes());
            }
        }
        super.act(delta);
    }

    /**
     * Checks the presented ingredient with the list of valid ingredients to see if
     * it can be cooked
     *
     * @param itemToCheck The item presented by the chef to be checked
     *                          if it can be used
     *                          by the station
     * @return true if the ingredient is in the validIngredients array; false
     *         otherwise
     */
    private boolean isCorrectIngredient(Holdable itemToCheck) {
        if (itemToCheck instanceof Ingredient) {
            if (itemToCheck instanceof Grillable) {
                return (!((Grillable) itemToCheck).getGrilled() && ((Ingredient) itemToCheck).getUseable());
            }
        }
        return false;
    }

    /**
     * Obtains the actions that can be currently performed depending on the states
     * of the station
     * itself and the selected chef
     *
     * @return actionTypes - the list of actions the station can currently perform.
     */
    @Override
    public LinkedList<StationAction.ActionType> getActionTypes() {
        LinkedList<StationAction.ActionType> actionTypes = super.getActionTypes();
        if (nearbyChef == null) {
            return new LinkedList<>();
        }
        if (locked) {
            return actionTypes;
        }
        if (currentIngredient == null) {
            if (nearbyChef.hasIngredient() && isCorrectIngredient(nearbyChef.getStack().peek())) {
                actionTypes.add(StationAction.ActionType.PLACE_INGREDIENT);
            }
        } else {
            // check to see if total number of seconds has passed to progress the state of
            // the patty.
            if (
                currentIngredient.grillStepComplete() &&
                !currentIngredient.getGrilled() &&
                (((Ingredient) currentIngredient).getUseable())
            ) {
                actionTypes.add(StationAction.ActionType.FLIP_ACTION);
            }

            if (currentIngredient.getGrilled() || !(((Ingredient) currentIngredient).getUseable())) {
                actionTypes.add(StationAction.ActionType.GRAB_INGREDIENT);
            }

            if (!inUse) {
                actionTypes.add(StationAction.ActionType.COOK_ACTION);
            }
        }
        return actionTypes;
    }

    @Override
    public void doStationAction(StationAction.ActionType action) {
        super.doStationAction(action);
        switch (action) {
            case COOK_ACTION:
                // timeCooked is used to track how long the
                // ingredient has been cooking for.
                inUse = true;
                uiController.hideActions(this);
                uiController.showProgressBar(this);
                progressVisible = true;
                break;
            case FLIP_ACTION:
                currentIngredient.flip();
                uiController.hideActions(this);
                uiController.showProgressBar(this);
                progressVisible = true;
                break;
            case PLACE_INGREDIENT:
                if (this.nearbyChef != null && nearbyChef.hasIngredient() && currentIngredient == null) {
                    if (this.isCorrectIngredient(nearbyChef.getStack().peek())) {
                        currentIngredient = (Grillable) nearbyChef.popFood();
                    }
                }
                uiController.showActions(this, getActionTypes());
                break;
            case GRAB_INGREDIENT:
                if (nearbyChef.canGrabIngredient()) {
                    nearbyChef.grabItem(currentIngredient.getGrillResult());
                    currentIngredient = null;
                    inUse = false;
                }
                checkUpdateUI = false;
                uiController.showActions(this, getActionTypes());
                break;
            default:
                break;
        }
    }

    /**
     * Displays ingredients that have been placed on the station
     *
     * @param batch       Used to display a 2D texture
     * @param parentAlpha The parent alpha, to be multiplied with this actor's
     *                    alpha, allowing the
     *                    parent's alpha to affect all children.
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (currentIngredient != null) {
            drawFoodTexture(batch, currentIngredient.getTexture());
        }
    }
}
