package cs.eng1.tests.stations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.food.ingredients.Bun;
import cs.eng1.piazzapanic.food.ingredients.Cheese;
import cs.eng1.piazzapanic.food.ingredients.Dough;
import cs.eng1.piazzapanic.food.ingredients.Lettuce;
import cs.eng1.piazzapanic.food.ingredients.Patty;
import cs.eng1.piazzapanic.food.ingredients.Potato;
import cs.eng1.piazzapanic.food.ingredients.Tomato;
import cs.eng1.piazzapanic.food.ingredients.UncookedPizza;
import cs.eng1.piazzapanic.food.recipes.Burger;
import cs.eng1.piazzapanic.food.recipes.JacketPotato;
import cs.eng1.piazzapanic.food.recipes.Salad;
import cs.eng1.piazzapanic.stations.RecipeStation;
import cs.eng1.piazzapanic.stations.StationAction;
import cs.eng1.piazzapanic.stations.StationAction.ActionType;
import cs.eng1.piazzapanic.ui.StationUIController;
import cs.eng1.tests.GdxTestRunner;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the behaviour of ingredientStation, being getActionTypes returning correct values and performing those actions, as well as reset.
 *
 * @author Sabid Hossain
 */
@RunWith(GdxTestRunner.class)
public class RecipeStationTests {

    private final ChefManager chefManager = mock(ChefManager.class);
    private final StationUIController mockUI = mock(StationUIController.class);

    /**
     * Tests that getActionTypes returns nothing when nearbyChef is null
     */
    @Test
    public void testGetActionTypesNoChef() {
        RecipeStation station = new RecipeStation(
            1,
            null,
            mockUI,
            null,
            null,
            null
        );
        List<StationAction.ActionType> actionTypes = station.getActionTypes();
        assertTrue(
            "Nothing is added to action types if no chef is nearby.",
            actionTypes.isEmpty()
        );
    }

    /**
     * Tests that getActionTypes returns nothing when nearbyChef is not holding
     * anything
     */
    @Test
    public void testGetActionTypesWithNoIngredient() {
        RecipeStation station = new RecipeStation(
            1,
            null,
            mockUI,
            null,
            null,
            null
        );
        Chef chef = new Chef(null, null, null);
        station.nearbyChef = chef;
        List<StationAction.ActionType> actionTypes = station.getActionTypes();
        assertTrue(
            "Nothing is added to action types if the chef has nothing in their inventory.",
            actionTypes.isEmpty()
        );
    }

    /**
     * Tests that getActionTypes returns nothing when nearbyChef is not holiding a
     * processed ingredient
     */
    @Test
    public void testGetActionTypesWithIncorrectIngredient() {
        RecipeStation station = new RecipeStation(
            1,
            null,
            mockUI,
            null,
            null,
            null
        );
        Chef chef = new Chef(null, null, chefManager);
        chef.grabItem(new Potato(null));
        station.nearbyChef = chef;
        List<StationAction.ActionType> actionTypes = station.getActionTypes();
        assertTrue(
            "Nothing is added to action types if the chef has nothing in their inventory.",
            actionTypes.isEmpty()
        );
    }

    /**
     * Tests that getActionTypes returns PLACE_INGREDIENT when nearbyChef is holding
     * a processed ingredient (chopped lettuce)
     */
    @Test
    public void testGetActionTypesWithCorrectIngredient() {
        RecipeStation station = new RecipeStation(
            1,
            null,
            mockUI,
            null,
            null,
            null
        );
        Chef chef = new Chef(null, null, chefManager);
        Lettuce lettuce = new Lettuce(null);
        station.nearbyChef = chef;
        lettuce.setChopped(true);
        chef.grabItem(lettuce);
        List<StationAction.ActionType> actionTypes = station.getActionTypes();
        assertTrue(
            "Returns PLACE_INGREDIENT if the chef has a processed ingredient.",
            actionTypes.contains(ActionType.PLACE_INGREDIENT)
        );
    }

    /**
     * Tests that doStationAction (PLACE_INGREDIENT) put the nearbyChef's top
     * ingredient onto the station
     */
    @Test
    public void testDoPlaceAction() {
        RecipeStation station = new RecipeStation(
            1,
            null,
            mockUI,
            null,
            null,
            null
        );
        Chef chef = new Chef(null, null, chefManager);
        Lettuce lettuce = new Lettuce(null);
        station.nearbyChef = chef;
        lettuce.setChopped(true);
        chef.grabItem(lettuce);
        station.doStationAction(ActionType.PLACE_INGREDIENT);
        assertTrue(
            "The chef puts lettuce on the station.",
            station.ingredientStack.contains(lettuce.getType())
        );
    }

    /**
     * Tests that doStationAction (GRAB_INGREDIENT) puts the completedRecipe onto
     * the chef's foodStack
     */
    @Test
    public void testDoGrabIngredientAction() {
        RecipeStation station = new RecipeStation(
            1,
            null,
            mockUI,
            null,
            null,
            null
        );
        Chef chef = new Chef(null, null, chefManager);
        station.nearbyChef = chef;
        station.completedRecipe = new Burger(null);
        station.doStationAction(ActionType.GRAB_INGREDIENT);
        assertFalse(
            "A burger is taken by the chef.",
            chef.getStack().isEmpty()
        );
    }

    /**
     * Tests that doStationAction (MAKE_BURGER) creates a Burger in completedRecipe
     * if given a Bun and Patty
     */
    @Test
    public void testDoMakeBurgerAction() {
        RecipeStation station = new RecipeStation(
            1,
            null,
            mockUI,
            null,
            null,
            null
        );
        station.placeIngredient(new Bun(null));
        station.placeIngredient(new Patty(null));
        station.doStationAction(ActionType.MAKE_BURGER);
        assertTrue(
            "A salad is made when the appropriate ingredients are given.",
            station.completedRecipe instanceof Burger
        );
    }

    /**
     * Tests that doStationAction (MAKE_SALAD) creates a Salad in completedRecipe
     * if given Lettuce and Tomato
     */
    @Test
    public void testDoMakeSaladAction() {
        RecipeStation station = new RecipeStation(
            1,
            null,
            mock(StationUIController.class),
            null,
            null,
            null
        );
        station.placeIngredient(new Lettuce(null));
        station.placeIngredient(new Tomato(null));
        station.doStationAction(ActionType.MAKE_SALAD);
        assertTrue(
            "A salad is made when the appropriate ingredients are given.",
            station.completedRecipe instanceof Salad
        );
    }

    /**
     * Tests that doStationAction (MAKE_JACKET) creates a JacketPotato if given
     * Potato and Cheese
     */
    @Test
    public void testDoMakeJacketAction() {
        RecipeStation station = new RecipeStation(
            1,
            null,
            mockUI,
            null,
            null,
            null
        );
        station.placeIngredient(new Potato(null));
        station.placeIngredient(new Cheese(null));
        station.doStationAction(ActionType.MAKE_JACKET);
        assertTrue(
            "A jacket potato is made when the appropriate ingredients are given.",
            station.completedRecipe instanceof JacketPotato
        );
    }

    /**
     * Tests that doStationAction (ASSEMBLE_PIZZA) creates an UncookedPizza if given
     * Dough, Cheese and Tomato
     */
    @Test
    public void doAssemblePizzaAction() {
        RecipeStation station = new RecipeStation(
            1,
            null,
            mockUI,
            null,
            null,
            null
        );
        station.placeIngredient(new Dough(null));
        station.placeIngredient(new Cheese(null));
        station.placeIngredient(new Tomato(null));
        station.doStationAction(ActionType.ASSEMBLE_PIZZA);
        assertTrue(
            "A salad is made when the appropriate ingredients are given.",
            station.completedRecipe instanceof UncookedPizza
        );
    }

    /**
     * Tests the reset method appropriately resets all values.
     */
    @Test
    public void testResetMethod() {
        RecipeStation station = new RecipeStation(
            1,
            null,
            mockUI,
            null,
            null,
            null
        );
        station.completedRecipe = new Salad(null);
        station.placeIngredient(new Potato(null));
        station.reset();
        assertTrue(
            (station.completedRecipe == null) &&
            station.displayIngredient.isEmpty() &&
            !station.ingredientStack.contains(new Potato(null).getType())
        );
    }
}
