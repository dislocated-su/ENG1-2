package cs.eng1.tests.stations;

import static org.junit.Assert.*;

import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.Patty;
import cs.eng1.piazzapanic.stations.IngredientStation;
import cs.eng1.piazzapanic.stations.StationAction;
import cs.eng1.tests.GdxTestRunner;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the behaviour of ingredientStation, being getActionTypes returning correct values.
 *
 * @author Sabid Hossain
 */
@RunWith(GdxTestRunner.class)
public class IngredientStationTests {

    /**
     * Asserts getActionTypes as empty with no nearby chef.
     */
    @Test
    public void testGetActionTypesNoChef() {
        IngredientStation station = new IngredientStation(
            1,
            null,
            null,
            null,
            null,
            null
        );
        List<StationAction.ActionType> actionTypes = station.getActionTypes();
        assertTrue(
            "nothing is added to action types if no chef is nearby",
            actionTypes.isEmpty()
        );
    }

    /**
     * Asserts getActionTypes for grab ingredient.
     */
    @Test
    public void testGetActionTypesWithChef() {
        IngredientStation station = new IngredientStation(
            1,
            null,
            null,
            null,
            null,
            null
        );
        Chef chef = new Chef(null, null, null);
        station.nearbyChef = chef;
        List<StationAction.ActionType> actionTypes = station.getActionTypes();
        assertTrue(
            "adds GRAB_INGREDIENT to actionTypes when a chef is nearby",
            actionTypes.contains(StationAction.ActionType.GRAB_INGREDIENT)
        );
    }

    /**
     * Asserts getActionTypes and action for grab ingredient.
     */
    @Test
    public void testGettingIngredients() {
        IngredientStation station = new IngredientStation(
            1,
            null,
            null,
            null,
            null,
            new Patty(new FoodTextureManager())
        );
        Chef chef = new Chef(null, null, new ChefManager(0, null, null, null));
        station.nearbyChef = chef;
        station.doStationAction(StationAction.ActionType.GRAB_INGREDIENT);
        assertTrue(
            "The chef can pick up ingredients from the ingredients station",
            chef.hasIngredient()
        );
    }
}
