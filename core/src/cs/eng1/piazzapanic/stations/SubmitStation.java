package cs.eng1.piazzapanic.stations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import cs.eng1.piazzapanic.customer.Customer;
import cs.eng1.piazzapanic.customer.CustomerManager;
import cs.eng1.piazzapanic.food.interfaces.Holdable;
import cs.eng1.piazzapanic.food.recipes.Recipe;
import cs.eng1.piazzapanic.stations.StationAction.ActionType;
import cs.eng1.piazzapanic.ui.StationActionUI.ActionAlignment;
import cs.eng1.piazzapanic.ui.StationUIController;
import java.util.LinkedList;
import java.util.Objects;

/**
 * AssembleStation
 */
public class SubmitStation extends Station {

    private final CustomerManager customerManager;

    public Customer customer;

    public SubmitStation(
        int id,
        TextureRegion image,
        StationUIController uiController,
        ActionAlignment alignment,
        Boolean locked,
        CustomerManager customerManager
    ) {
        super(id, image, uiController, alignment, locked);
        this.customerManager = customerManager;
    }

    @Override
    public LinkedList<ActionType> getActionTypes() {
        LinkedList<ActionType> actionTypes = new LinkedList<>();
        if (nearbyChef == null || nearbyChef.getStack().isEmpty()) {
            return new LinkedList<>();
        }
        Holdable topItem = nearbyChef.getStack().peek();
        if (checkCorrectRecipe(topItem)) {
            actionTypes.add(ActionType.SUBMIT_ORDER);
        }
        return actionTypes;
    }

    private boolean checkCorrectRecipe(Holdable item) {
        if (customer != null && item instanceof Recipe) {
            return ((Recipe) item).getType()
                .equals(customer.getOrder().getType());
        }
        return false;
    }

    @Override
    public void doStationAction(ActionType action) {
        super.doStationAction(action);
        if (Objects.requireNonNull(action) == ActionType.SUBMIT_ORDER) {
            Holdable topItem = nearbyChef.popFood();
            if (!checkCorrectRecipe(topItem)) {
                return;
            }
            Customer c = this.customer;
            customer = null;
            customerManager.nextRecipe(nearbyChef, c);
        }

        uiController.showActions(this, getActionTypes());
    }

    /**
     * Updates the current available actions based on the new customer's order
     */
    public void updateOrderActions() {
        uiController.showActions(this, getActionTypes());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (this.customer != null) {
            Texture desiredOrder = this.customer.getOrder().getTexture();
            Color color = batch.getColor();
            color.a = 0.6f;
            batch.setColor(color);
            drawFoodTexture(batch, desiredOrder, 1.2f);
            color.a = 1f;
            batch.setColor(color);
        }
    }
}
