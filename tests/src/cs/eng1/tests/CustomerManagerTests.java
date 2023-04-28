package cs.eng1.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import cs.eng1.piazzapanic.box2d.Box2dLocation;
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.customer.Customer;
import cs.eng1.piazzapanic.customer.CustomerManager;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.recipes.Pizza;
import cs.eng1.piazzapanic.stations.SubmitStation;
import cs.eng1.piazzapanic.ui.UIOverlay;
import cs.eng1.piazzapanic.utility.KeyboardInput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class CustomerManagerTests {

    UIOverlay overlay = mock(UIOverlay.class);
    CustomerManager customerManager = new CustomerManager(
        1,
        overlay,
        new World(new Vector2(0, 0), true),
        5,
        0
    );
    FoodTextureManager textureManager = new FoodTextureManager();
    World world = new World(new Vector2(0, 0), true);
    KeyboardInput kbInput = new KeyboardInput();
    Stage stage = mock(Stage.class);
    ChefManager chefManager = new ChefManager(1, overlay, world, kbInput);
    SubmitStation submitStation = new SubmitStation(
        0,
        null,
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
            "customerManager should have no orders before initialising.",
            customerManager.getFirstOrder()
        );
        assertFalse(
            "customerManager timer should not be running before initialising.",
            customerManager.getTimer().getRunning()
        );
        assertEquals(
            "customerManager should have no customers before initialising.",
            0,
            customerManager.getCustomerQueue().size
        );
        customerManager.init(
            textureManager,
            stage,
            new HashMap<>(),
            new ArrayList<>()
        );
        assertEquals(
            "Reputation should initialise as 0.",
            3,
            customerManager.getReputation()
        );
        assertTrue(
            "After initialising, customerManager's timer should be running.",
            customerManager.getTimer().getRunning()
        );
        assertEquals(
            "customerManager should have 1 customer after initialising.",
            1,
            customerManager.getCustomerQueue().size
        );
        assertEquals(
            "customerManager should have a recipe after initialising.",
            new Pizza(textureManager).getTexture(),
            customerManager.getFirstOrder().getTexture()
        );
    }

    @Test
    public void addStationTests() {
        LinkedList<SubmitStation> list = new LinkedList<>();
        assertEquals(
            "customerManager should start without any stations.",
            list,
            customerManager.getRecipeStations()
        );
        customerManager.addStation(submitStation);
        list.add(submitStation);
        assertEquals(
            "addStation should add a station to customerManager.",
            list,
            customerManager.getRecipeStations()
        );
    }

    @Test
    public void checkSpawnTests() {
        customerManager.init(
            textureManager,
            stage,
            new HashMap<>(),
            new ArrayList<>()
        );
        customerManager.checkSpawn((float) 1 / 60);
        assertEquals(
            "checkSpawn shouldn't change customerQueue until time equal to delay is passed.",
            1,
            customerManager.getCustomerQueue().size
        );
        for (int i = 0; i <= 4000; i++) {
            customerManager.checkSpawn((float) 1 / 60);
        }
        assertEquals(
            "checkSpawn should add a customer when enough time has passed.",
            2,
            customerManager.getCustomerQueue().size
        );
    }

    @Test
    public void actTests() {
        customerManager.init(
            textureManager,
            stage,
            new HashMap<>(),
            new ArrayList<>()
        );
        for (int i = 0; i <= 4000; i++) {
            customerManager.act((float) 1 / 60);
        }
        assertEquals(
            "act should add a customer when enough time has passed.",
            2,
            customerManager.getCustomerQueue().size
        );
        assertEquals(
            "act should reduce reputation when a customer's order hasn't been met for long enough.",
            2,
            customerManager.getReputation()
        );
        customerManager.loseReputation();
        assertEquals(
            "loseReputation should decrease reputation.",
            1,
            customerManager.getReputation()
        );
    }

    @Test
    public void checkRecipeTests() {
        assertFalse(
            "checkRecipe should return false when there is no recipe.",
            customerManager.checkRecipe(new Pizza(textureManager))
        );
        customerManager.init(
            textureManager,
            stage,
            new HashMap<>(),
            new ArrayList<>()
        );
        assertTrue(
            "checkRecipe should return the top recipe of customerQueue (in customerManager).",
            customerManager.checkRecipe(new Pizza(textureManager))
        );
    }

    @Test
    public void nextRecipeTests() {
        customerManager.init(
            textureManager,
            stage,
            new HashMap<Integer, Box2dLocation>(),
            new ArrayList<>()
        );
        Customer first = customerManager.getCustomerQueue().first();
        customerManager.nextRecipe(chef);
        assertTrue(
            "nextRecipe should complete the current order.",
            first.isOrderCompleted()
        );
        assertNotEquals(
            "nextRecipe should change the current order.",
            first.getOrder(),
            customerManager.getFirstOrder()
        );
        for (int i = 0; i < 4; i++) {
            customerManager.generateCustomer();
            customerManager.nextRecipe(chef);
        }
        assertFalse(
            "nextRecipe should stop the timer when all orders have been delivered.",
            customerManager.getTimer().getRunning()
        );
    }
}
