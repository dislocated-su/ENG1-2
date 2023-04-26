package cs.eng1.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Queue;
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.customer.Customer;
import cs.eng1.piazzapanic.customer.CustomerManager;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.recipes.Pizza;
import cs.eng1.piazzapanic.food.recipes.Recipe;
import cs.eng1.piazzapanic.stations.SubmitStation;
import cs.eng1.piazzapanic.ui.UIOverlay;
import cs.eng1.piazzapanic.utility.KeyboardInput;
import java.util.LinkedList;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class CustomerManagerTests {

    UIOverlay overlay = mock(UIOverlay.class);
    CustomerManager customerManager = new CustomerManager(overlay, 5, 0);
    FoodTextureManager textureManager = new FoodTextureManager();
    World world = new World(new Vector2(0, 0), true);
    KeyboardInput kbInput = new KeyboardInput();
    ChefManager chefManager = new ChefManager(1, overlay, world, kbInput);
    SubmitStation submitStation = new SubmitStation(
        0,
        null,
        null,
        null,
        customerManager
    );
    Texture fake = new Texture(
        Gdx.files.internal(
            "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"
        )
    );
    Chef chef = new Chef(fake, new Vector2(1, 1), chefManager);

    @Test
    public void initTests() {
        assertNull(
            "By default, customerManager should have no orders.",
            customerManager.getFirstOrder()
        );
        assertFalse(customerManager.getTimer().getRunning());
        assertEquals(0, customerManager.getCustomerQueue().size);
        customerManager.init(textureManager);
        assertEquals(3, customerManager.getReputation());
        assertTrue(customerManager.getTimer().getRunning());
        assertEquals(1, customerManager.getCustomerQueue().size);
        assertEquals(
            new Pizza(textureManager).getTexture(),
            customerManager.getFirstOrder().getTexture()
        );
    }

    @Test
    public void addStationTests() {
        LinkedList<SubmitStation> list = new LinkedList<>();
        assertEquals(list, customerManager.getRecipeStations());
        customerManager.addStation(submitStation);
        list.add(submitStation);
        assertEquals(list, customerManager.getRecipeStations());
    }

    @Test
    public void checkSpawnTests() {
        customerManager.init(textureManager);
        customerManager.checkSpawn(61);
        assertEquals(2, customerManager.getCustomerQueue().size);
    }

    @Test
    public void actTests() {
        customerManager.init(textureManager);
        for (int i = 0; i <= 4000; i++) {
            customerManager.act((float) 1 / 60);
        }
        assertEquals(2, customerManager.getCustomerQueue().size);
        assertEquals(2, customerManager.getReputation());
        customerManager.loseReputation();
        assertEquals(1, customerManager.getReputation());
    }

    @Test
    public void checkRecipeTests() {
        assertFalse(customerManager.checkRecipe(new Pizza(textureManager)));
        customerManager.init(textureManager);
        assertTrue(customerManager.checkRecipe(new Pizza(textureManager)));
    }

    @Test
    public void nextRecipeTests() {
        customerManager.init(textureManager);
        Customer first = customerManager.getCustomerQueue().first();
        customerManager.nextRecipe(chef);
        assertTrue(first.isOrderCompleted());
        assertNotEquals(first.getOrder(), customerManager.getFirstOrder());
        for (int i = 0; i < 4; i++) {
            customerManager.generateCustomer();
            customerManager.nextRecipe(chef);
        }
        assertFalse(customerManager.getTimer().getRunning());
    }
}
