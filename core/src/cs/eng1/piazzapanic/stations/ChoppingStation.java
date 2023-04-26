package cs.eng1.piazzapanic.stations;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.food.ingredients.Ingredient;
import cs.eng1.piazzapanic.food.interfaces.Choppable;
import cs.eng1.piazzapanic.food.interfaces.Holdable;
import cs.eng1.piazzapanic.ui.StationActionUI;
import cs.eng1.piazzapanic.ui.StationUIController;
import java.util.LinkedList;

/**
 * The ChoppingStation class is a station representing the place in
 * the kitchen where you chop lettuce and tomatoes to be used in making
 * a salad
 */
public class ChoppingStation extends Station {

    protected final float totalTimeToChop = 5f;

    public Choppable currentIngredient = null;
    protected float timeChopped;
    public boolean progressVisible = false;
    private Chef tempChef;

    /**
     * The constructor method for the class
     *
     * @param id           The unique identifier of the station
     * @param image        The rectangular area of the texture
     * @param uiController The controller from which we can get show and hide the
     *                     action
     *                     buttons belonging to the station
     * @param alignment    Dictates where the action buttons are shown
     * @param locked
     */
    public ChoppingStation(
        int id,
        TextureRegion image,
        StationUIController uiController,
        StationActionUI.ActionAlignment alignment,
        Boolean locked) {
        super(id, image, uiController, alignment, locked);
    }

    /**
     * Called every frame. Used to update the progress bar and
     * check if enough time has passed for the ingredient to be
     * changed to its chopped variant
     *
     * @param delta Time in seconds since the last frame.
     */
    @Override
    public void act(float delta) {
        if (inUse) {
            boolean complete = currentIngredient.choppingTick(delta);

            uiController.updateProgressValue(
                this,
                currentIngredient.getChoppingProgress()
            );

            if (complete && progressVisible) {
                uiController.hideProgressBar(this);
                progressVisible = false;
                uiController.showActions(this, getActionTypes());
                tempChef.setPaused(false);
                tempChef = null;
            }
        }
        super.act(delta);
    }

    /**
     * Checks the presented ingredient with the list of
     * valid ingredients to see if it can be chopped
     *
     * @param itemToCheck The item presented by the
     *                    chef to be checked if it can be used
     *                    by the station
     * @return true if the ingredient is in the validIngredients array; false
     *         otherwise
     */
    private boolean isCorrectIngredient(Holdable itemToCheck) {
        if (itemToCheck instanceof Ingredient) {
            if (itemToCheck instanceof Choppable) {
                return (
                    !((Choppable) itemToCheck).getChopped() &&
                    ((Ingredient) itemToCheck).getUseable()
                );
            }
        }
        return false;
    }

    /**
     * Obtains the actions that can be currently performed depending on
     * the states of the station itself and the selected chef
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
            if (
                nearbyChef.hasIngredient() &&
                isCorrectIngredient(nearbyChef.getStack().peek())
            ) {
                actionTypes.add(StationAction.ActionType.PLACE_INGREDIENT);
            }
        } else {
            if (
                currentIngredient.getChopped() ||
                !(((Ingredient) currentIngredient).getUseable())
            ) {
                actionTypes.add(StationAction.ActionType.GRAB_INGREDIENT);
            }
            if (!inUse) {
                actionTypes.add(StationAction.ActionType.CHOP_ACTION);
            }
        }
        return actionTypes;
    }

    /**
     * Given an action, the station should attempt to do that action based on the
     * chef that is nearby
     * or the state of the ingredient currently on the station.
     *
     * @param action the action that needs to be done by this station if it can.
     */
    @Override
    public void doStationAction(StationAction.ActionType action) {
        super.doStationAction(action);
        switch (action) {
            case CHOP_ACTION:
                inUse = true;
                tempChef = nearbyChef;
                uiController.hideActions(this);
                uiController.showProgressBar(this);
                tempChef.setPaused(true);
                progressVisible = true;
                break;
            case PLACE_INGREDIENT:
                if (
                    this.nearbyChef != null &&
                    nearbyChef.hasIngredient() &&
                    currentIngredient == null
                ) {
                    if (
                        (this.isCorrectIngredient(nearbyChef.getStack().peek()))
                    ) {
                        currentIngredient = (Choppable) nearbyChef.popFood();
                    }
                }
                uiController.showActions(this, getActionTypes());
                break;
            case GRAB_INGREDIENT:
                if (
                    this.nearbyChef != null &&
                    nearbyChef.canGrabIngredient() &&
                    currentIngredient != null
                ) {
                    nearbyChef.grabItem(currentIngredient.getChoppingResult());
                    currentIngredient = null;
                    inUse = false;
                }
                uiController.showActions(this, getActionTypes());
                break;
            default:
                break;
        }
    }

    @Override
    public void reset() {
        currentIngredient = null;
        timeChopped = 0;
        progressVisible = false;
        super.reset();
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
