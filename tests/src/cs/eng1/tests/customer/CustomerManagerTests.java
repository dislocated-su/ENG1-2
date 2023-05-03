package cs.eng1.tests.customer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import cs.eng1.piazzapanic.PlayerState;
import cs.eng1.piazzapanic.box2d.Box2dLocation;
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.customer.Customer;
import cs.eng1.piazzapanic.customer.CustomerManager;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.ui.StationUIController;
import cs.eng1.piazzapanic.ui.UIOverlay;
import cs.eng1.piazzapanic.utility.KeyboardInput;
import cs.eng1.piazzapanic.utility.MapLoader;
import cs.eng1.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the functionality of CustomerManager (and for convenience customer) being:
 * init, some of endless mode, act and customer movement.
 *
 * @author Joel Paxman
 */
@RunWith(GdxTestRunner.class)
public class CustomerManagerTests {

    UIOverlay overlay = mock(UIOverlay.class);
    Stage stage = mock(Stage.class);
    World world = new World(Vector2.Zero, true);
    KeyboardInput kbInput = new KeyboardInput();
    FoodTextureManager textureManager = new FoodTextureManager();
    StationUIController stationUIController = new StationUIController(stage, null);
    CustomerManager customerManager = new CustomerManager(1, overlay, world, 5, 0);

    ChefManager chefManager = new ChefManager(1, overlay, world, kbInput);

    Texture fake = new Texture(
        Gdx.files.internal(
            "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"
        )
    );
    Chef chef = new Chef(fake, Vector2.Zero, chefManager);

    MapLoader mapLoader = new MapLoader("test-map.tmx");

    /**
     * Test the functionality of init()
     */
    @Test
    public void initTests() {
        mapLoader.loadWaypoints("Waypoints", "cookspawnid", "aispawnid", "lightid", "aiobjective");
        mapLoader.createStations(
            "Stations",
            "Sensors",
            chefManager,
            stage,
            stationUIController,
            textureManager,
            customerManager
        );
        int defaultTime = customerManager.getSpawnTimer().getRemainingTime();
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
        assertEquals("Init should identify the correct number of objectives.", 10, objectives);
        assertEquals(
            "Init should identify objectives correctly.",
            new Vector2(21, 11),
            customerManager.getObjective(0).getPosition()
        );
        assertEquals(
            "Init should identify objectives correctly.",
            new Vector2(21, 8),
            customerManager.getObjective(1).getPosition()
        );
        assertEquals(
            "Init should identify objectives correctly.",
            new Vector2(26, 8),
            customerManager.getObjective(2).getPosition()
        );
        assertEquals(
            "Init should identify objectives correctly.",
            new Vector2(26, 13),
            customerManager.getObjective(3).getPosition()
        );
        assertEquals(
            "Init should identify objectives correctly.",
            new Vector2(21, 10),
            customerManager.getObjective(4).getPosition()
        );
        assertEquals(
            "Init should identify objectives correctly.",
            new Vector2(21, 9),
            customerManager.getObjective(5).getPosition()
        );
        assertEquals(
            "Init should identify objectives correctly.",
            new Vector2(21, 7),
            customerManager.getObjective(6).getPosition()
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
            customerManager.getCustomer(0).getPosition()
        );
        assertEquals(
            "A PlayerState difficulty of 0 should multiply the difficulty by 2",
            2f * defaultTime,
            customerManager.getSpawnTimer().getRemainingTime(),
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
            "Calling init multiple times should reset / not duplicate objectives.",
            objectives,
            customerManager.getObjectives().size()
        );
        assertEquals("Init should reset customers.", 1, customerManager.getCustomerQueue().size());
        assertEquals(
            "A PlayerState difficulty of 2 should multiply the difficulty by 0.75",
            1.5f * defaultTime,
            customerManager.getSpawnTimer().getRemainingTime(),
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
            customerManager.getSpawnTimer().getRemainingTime(),
            0.1
        );
    }

    /**
     * Test the functionality of endless mode in relation to customerManager.
     */
    @Test
    public void endlessTests() {
        customerManager = new CustomerManager(1, overlay, world, 0, 0);
        mapLoader.loadWaypoints("Waypoints", "cookspawnid", "aispawnid", "lightid", "aiobjective");
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
        assertTrue(
            "EndlessTimer should be running in endless mode.",
            customerManager.getEndlessTimer().getRunning()
        );

        customerManager.getSpawnTimer().reset();
        // Loops until it reaches maxSpawnRate
        for (
            int i = customerManager.getSpawnTimer().getDelay();
            i >= customerManager.getMaxSpawnRate();
            Math.round(i *= 0.95f)
        ) {
            assertEquals(
                "spawnTimer should decrease by 5% when endlessTimer.tick(delta) is true",
                i,
                customerManager.getSpawnTimer().getDelay(),
                10f
            );
            customerManager.act(customerManager.getEndlessTimer().getDelay());
        }
        customerManager.act(1000f);
        assertEquals(customerManager.getMaxSpawnRate(), customerManager.getSpawnTimer().getDelay());
    }

    /**
     * Test the functionality of act in CustomerManager, and by relation Customer.
     */
    @Test
    public void actTests() {
        customerManager = new CustomerManager(1, overlay, world, 5, 0);
        mapLoader.loadWaypoints("Waypoints", "cookspawnid", "aispawnid", "lightid", "aiobjective");
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
        float spawnTime = (customerManager.getSpawnTimer().getRemainingTime()) / 500;
        assertEquals("Reputation should be 3 by default.", 3, customerManager.getReputation());
        customerManager.getCustomer(0).act(spawnTime * 500);
        assertEquals(
            "Customers should cause a loss of reputation when they have not been served in a timely manner.",
            2,
            customerManager.getReputation()
        );
        customerManager.getCustomer(0).act(spawnTime);
        assertEquals(
            "A single customer should not reduce reputation multiple times.",
            2,
            customerManager.getReputation()
        );
        act(spawnTime);
        assertEquals(
            "Act should spawn customers if the correct amount of time has passed.",
            2,
            customerManager.getCustomerQueue().size()
        );
        act(spawnTime);
        assertEquals(
            "Act should spawn customers if the correct amount of time has passed.",
            3,
            customerManager.getCustomerQueue().size()
        );
        act(spawnTime);
        assertEquals(
            "Act should spawn customers if the correct amount of time has passed.",
            4,
            customerManager.getCustomerQueue().size()
        );
        for (float i = 0; i < 2; i++) {
            customerManager.act(spawnTime);
            assertEquals(
                "customerManager act should not spawn customers above the given amount of customers.",
                5,
                customerManager.getCustomerQueue().size()
            );
        }
        for (int i = 1; i < 5; i++) {
            act(spawnTime * 500);
            customerManager.nextRecipe(customerManager.getCustomer(0));
            assertEquals(
                "Act combined with getRecipe should spawn the correct number of total customers and complete orders.",
                i,
                customerManager.getCompletedOrders()
            );
        }
        assertEquals(
            "Act and nextRecipe should despawn customers.",
            1,
            customerManager.getCustomerQueue().size()
        );
        assertEquals("Reputation should not go below 0.", 0, customerManager.getReputation());
    }

    /**
     * Test the movement of customers and its relation to CustomerManager.
     */
    @Test
    public void movementTests() {
        int customers = 30;
        customerManager = new CustomerManager(1, overlay, world, customers, 0);
        mapLoader.loadWaypoints("Waypoints", "cookspawnid", "aispawnid", "lightid", "aiobjective");
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
        float spawnTime = (customerManager.getSpawnTimer().getRemainingTime());
        for (int i = 0; i < 6; i++) {
            act(spawnTime);
            Customer currentCustomer = customerManager.getCustomer(i);
            Box2dLocation objective = customerManager.getObjective(i);
            for (float n = 0; n <= 10f; n += 1f / 60) {
                currentCustomer.act(1f / 60);
                world.step(1f / 60, 6, 2);
            }
            assertEquals(objective.getPosition().x, currentCustomer.getPosition().x, 1f);
            assertTrue(objective.getPosition().epsilonEquals(currentCustomer.getPosition(), 1f));
        }
    }

    /**
     * Acts for customerManager and each customer within it.
     *
     * @param delta Time in seconds
     */
    private void act(float delta) {
        customerManager.act(delta);
        for (Customer customer : customerManager.getCustomerQueue()) {
            customer.act(delta);
        }
    }
}
