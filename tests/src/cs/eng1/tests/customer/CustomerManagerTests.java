package cs.eng1.tests.customer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import cs.eng1.piazzapanic.PlayerState;
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.customer.CustomerManager;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.ui.StationUIController;
import cs.eng1.piazzapanic.ui.UIOverlay;
import cs.eng1.piazzapanic.utility.KeyboardInput;
import cs.eng1.piazzapanic.utility.MapLoader;
import cs.eng1.tests.GdxTestRunner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Vector;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class CustomerManagerTests {

    UIOverlay overlay = mock(UIOverlay.class);
    Stage stage = mock(Stage.class);
    World world = new World(Vector2.Zero, true);
    KeyboardInput kbInput = new KeyboardInput();
    FoodTextureManager textureManager = new FoodTextureManager();
    StationUIController stationUIController = new StationUIController(
        stage,
        null
    );
    CustomerManager customerManager = new CustomerManager(
        1,
        overlay,
        world,
        5,
        0
    );

    ChefManager chefManager = new ChefManager(1, overlay, world, kbInput);

    Texture fake = new Texture(
        Gdx.files.internal(
            "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"
        )
    );
    Chef chef = new Chef(fake, Vector2.Zero, chefManager);

    MapLoader mapLoader = new MapLoader("test-map.tmx");

    @Test
    public void initTests() {
        mapLoader.loadWaypoints(
            "Waypoints",
            "cookspawnid",
            "aispawnid",
            "lightid",
            "aiobjective"
        );
        mapLoader.createStations(
            "Stations",
            "Sensors",
            chefManager,
            stage,
            stationUIController,
            textureManager,
            customerManager
        );
        int defaultTime = customerManager.getTimer().getRemainingTime();
        PlayerState.getInstance().setDifficulty(0);
        assertNull(
            "customerManager should have no objectives by default.",
            customerManager.getObjectives()
        );
        assertNull(
            "customerManager should have no spawns by default.",
            customerManager.getSpawnLocations()
        );
        customerManager.init(
            textureManager,
            stage,
            mapLoader.aiObjectives,
            mapLoader.aiSpawnpoints
        );
        int objectives = customerManager.getObjectives().size();
        assertEquals(
            "Init should create 1 customer.",
            1,
            customerManager.getCustomerQueue().size()
        );
        assertEquals(
            "Init should identify the correct number of objectives.",
            3,
            objectives
        );
        assertEquals(
            "Init should identify objectives correctly.",
            new Vector2(1, 23),
            customerManager.getObjective(0).getPosition()
        );
        assertEquals(
            "Init should identify objectives correctly.",
            new Vector2(11, 4),
            customerManager.getObjective(1).getPosition()
        );
        assertEquals(
            "Init should identify objectives correctly.",
            new Vector2(7, 10),
            customerManager.getObjective(-1).getPosition()
        );
        assertEquals(
            "Init should have the correct number of spawns.",
            2,
            customerManager.getSpawnLocations().size()
        );
        assertEquals(
            "Init should identify spawns correctly.",
            new Vector2(13, 7),
            customerManager.getCustomerQueue().get(0).getPosition()
        );
        assertEquals(
            "A PlayerState difficulty of 0 should multiply the difficulty by 2",
            2f * defaultTime,
            customerManager.getTimer().getRemainingTime(),
            0.1
        );
        assertFalse(
            "The endless timer should not run if in scenario mode.",
            customerManager.getEndlessTimer().getRunning()
        );

        PlayerState.getInstance().setDifficulty(2);
        customerManager.init(
            textureManager,
            stage,
            mapLoader.aiObjectives,
            mapLoader.aiSpawnpoints
        );

        assertEquals(
            "Calling init multiple times should not duplicate objectives.",
            objectives,
            customerManager.getObjectives().size()
        );
        assertEquals(
            "Init should reset customers.",
            1,
            customerManager.getCustomerQueue().size()
        );
        assertEquals(
            "A PlayerState difficulty of 2 should multiply the difficulty by 0.75",
            1.5f * defaultTime,
            customerManager.getTimer().getRemainingTime(),
            0.1
        );

        PlayerState.getInstance().setDifficulty(17);
        customerManager.init(
            textureManager,
            stage,
            mapLoader.aiObjectives,
            mapLoader.aiSpawnpoints
        );

        assertEquals(
            "A non-recognised playerstate difficulty should not multiply difficulty.",
            1.5f * defaultTime,
            customerManager.getTimer().getRemainingTime(),
            0.1
        );
    }

    @Test
    public void endlessTests() {
        customerManager = new CustomerManager(1, overlay, world, 0, 0);
        mapLoader.loadWaypoints(
            "Waypoints",
            "cookspawnid",
            "aispawnid",
            "lightid",
            "aiobjective"
        );
        mapLoader.createStations(
            "Stations",
            "Sensors",
            chefManager,
            stage,
            stationUIController,
            textureManager,
            customerManager
        );
        customerManager.init(
            textureManager,
            stage,
            mapLoader.aiObjectives,
            mapLoader.aiSpawnpoints
        );
        assertTrue(customerManager.getEndlessTimer().getRunning());
        // customerManager.checkSpawn(999f);
        // assertNull(customerManager.getCustomerQueue().size());
    }

    @Test
    public void checkSpawnTests() {
        customerManager = new CustomerManager(1, overlay, world, 0, 0);
        mapLoader.loadWaypoints(
            "Waypoints",
            "cookspawnid",
            "aispawnid",
            "lightid",
            "aiobjective"
        );
        mapLoader.createStations(
            "Stations",
            "Sensors",
            chefManager,
            stage,
            stationUIController,
            textureManager,
            customerManager
        );
        customerManager.init(
            textureManager,
            stage,
            mapLoader.aiObjectives,
            mapLoader.aiSpawnpoints
        );
        float spawnTime = (customerManager.getTimer().getRemainingTime() / 500);
        for (int i = 2; i < 500; i++) {
            customerManager.checkSpawn(spawnTime);
            assertEquals(i, customerManager.getCustomerQueue().size());
        }
    }
    // @Test
    // public void addStationTests() {
    //     LinkedList<SubmitStation> list = new LinkedList<>();
    //     assertEquals(
    //         "customerManager should start without any stations.",
    //         list,
    //         customerManager.getRecipeStations()
    //     );
    //     customerManager.addStation(submitStation);
    //     list.add(submitStation);
    //     assertEquals(
    //         "addStation should add a station to customerManager.",
    //         list,
    //         customerManager.getRecipeStations()
    //     );
    // }

    // @Test
    // public void checkSpawnTests() {
    //     customerManager.init(
    //         textureManager,
    //         stage,
    //         new HashMap<>(),
    //         new ArrayList<>()
    //     );
    //     customerManager.checkSpawn(1f / 60);
    //     assertEquals(
    //         "checkSpawn shouldn't change customerQueue until time equal to delay is passed.",
    //         1,
    //         customerManager.getCustomerQueue().size
    //     );
    //     for (int i = 0; i <= 4000; i++) {
    //         customerManager.checkSpawn(1f / 60);
    //     }
    //     assertEquals(
    //         "checkSpawn should add a customer when enough time has passed.",
    //         2,
    //         customerManager.getCustomerQueue().size
    //     );
    // }

    // @Test
    // public void actTests() {
    //     customerManager.init(
    //         textureManager,
    //         stage,
    //         new HashMap<>(),
    //         new ArrayList<>()
    //     );
    //     for (int i = 0; i <= 4000; i++) {
    //         customerManager.act(1f / 60);
    //     }
    //     assertEquals(
    //         "act should add a customer when enough time has passed.",
    //         2,
    //         customerManager.getCustomerQueue().size
    //     );
    //     assertEquals(
    //         "act should reduce reputation when a customer's order hasn't been met for long enough.",
    //         2,
    //         customerManager.getReputation()
    //     );
    //     customerManager.loseReputation();
    //     assertEquals(
    //         "loseReputation should decrease reputation.",
    //         1,
    //         customerManager.getReputation()
    //     );
    // }

    // @Test
    // public void checkRecipeTests() {
    //     assertFalse(
    //         "checkRecipe should return false when there is no recipe.",
    //         customerManager.checkRecipe(new Pizza(textureManager))
    //     );
    //     customerManager.init(
    //         textureManager,
    //         stage,
    //         new HashMap<>(),
    //         new ArrayList<>()
    //     );
    //     assertTrue(
    //         "checkRecipe should return the top recipe of customerQueue (in customerManager).",
    //         customerManager.checkRecipe(new Pizza(textureManager))
    //     );
    // }

    // @Test
    // public void nextRecipeTests() {
    //     customerManager.init(
    //         textureManager,
    //         stage,
    //         new HashMap<Integer, Box2dLocation>(),
    //         new ArrayList<>()
    //     );
    //     Customer first = customerManager.getCustomerQueue().first();
    //     customerManager.nextRecipe(chef);
    //     assertTrue(
    //         "nextRecipe should complete the current order.",
    //         first.isOrderCompleted()
    //     );
    //     assertNotEquals(
    //         "nextRecipe should change the current order.",
    //         first.getOrder(),
    //         customerManager.getFirstOrder()
    //     );
    //     for (int i = 0; i < 4; i++) {
    //         customerManager.generateCustomer();
    //         customerManager.nextRecipe(chef);
    //     }
    //     assertFalse(
    //         "nextRecipe should stop the timer when all orders have been delivered.",
    //         customerManager.getTimer().getRunning()
    //     );
    // }
}
