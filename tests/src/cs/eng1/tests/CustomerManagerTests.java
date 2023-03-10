package cs.eng1.tests;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import cs.eng1.piazzapanic.food.CustomerManager;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.recipes.Burger;
import cs.eng1.piazzapanic.food.recipes.Pizza;
import cs.eng1.piazzapanic.food.recipes.Recipe;

import com.badlogic.gdx.utils.Queue;

import cs.eng1.piazzapanic.stations.SubmitStation;
import cs.eng1.piazzapanic.ui.UIOverlay;
import java.util.LinkedList;

@RunWith(GdxTestRunner.class)
public class CustomerManagerTests {
    UIOverlay overlay;
    public FoodTextureManager foodTextureManager;
    public CustomerManager managerNoState = new CustomerManager(overlay);
    //Testing CustomerManager assertions
    @Test
    public void overlayAssertionTest() {
        assertEquals("recipeStations should be an empty linked list.", overlay, managerNoState.overlay);
    }
    
    @Test
    public void recipeStationsAssertionTest() {
        assertEquals("recipeStations should be an empty linked list.", new LinkedList<>(), managerNoState.recipeStations);
    }

    @Test
    public void customerOrdersAssertionTest() {
        assertEquals("customerOrders should be an empty queue.", new Queue<>(), managerNoState.customerOrders);
    }
    
    // this is an attempt to test init, idk if i can do that. it would need to be random anyway so look at this later.
    // @Test
    // public void whatever() {
    //     assertEquals(new int[] { 3, 2, 1, 0, 2 }, customerManager.possibleRecipes);
    // }

    //Tests checkRecipe for output True when the recipe is the currentOrder and False when it isn't
    @Test
    public void checkRecipeTest(){
        CustomerManager customerManager = new CustomerManager(overlay);
        Recipe pizza = new Pizza(foodTextureManager);
        Recipe burger = new Burger(foodTextureManager);

        customerManager.customerOrders.addLast(pizza);
        customerManager.currentOrder = customerManager.customerOrders.first();

        Recipe pizza2 = new Pizza(foodTextureManager);
        assertTrue(customerManager.checkRecipe(pizza2));
        assertFalse(customerManager.checkRecipe(burger));
    }

    @Test
    public void addStationTest(){
        CustomerManager customerManager = new CustomerManager(overlay);
        SubmitStation station = new SubmitStation(0, null, null, null, customerManager);
        customerManager.addStation(station);
        assertEquals("Station should equal station added to an empty list", station, customerManager.recipeStations.remove(0));
        customerManager.addStation(station);
        customerManager.addStation(station);
        assertEquals("Station should have another station added to the list", station, customerManager.recipeStations.remove(1));
    }

    // @Test
    // public void notifySubmitStationsTest(){
    //     CustomerManager customerManager = new CustomerManager(overlay);
    //     SubmitStation station = new SubmitStation(0, null, null, null, customerManager);
    //     customerManager.addStation(station);
    //     assertEquals(station.updateOrderActions(), customerManager.notifySubmitStations());
    // }
    //
    // nothing can be tested for submitStations as it returns a void and the elements reference UI so untestable.
}
