package cs.eng1.tests.chef;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
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
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class ChefManagerTests {

    World world = new World(Vector2.Zero, true);
    UIOverlay overlay = mock(UIOverlay.class);
    KeyboardInput kbInput = new KeyboardInput();
    ChefManager chefManager = new ChefManager(1, overlay, world, kbInput);
    Stage stage = mock(Stage.class);
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

    Texture fake = new Texture(Gdx.files.internal("badlogic.jpg"));
    Chef fakeChef = new Chef(fake, Vector2.Zero, chefManager);

    MapLoader mapLoader = new MapLoader("test-map.tmx");

    @Test
    public void startTests() {
        assertEquals(
            "The game should start with 2 chefs.",
            2,
            chefManager.getChefs().size()
        );
        for (Chef chef : chefManager.getChefs()) {
            assertNotEquals(
                "The chef textures should not be badlogic.",
                "badlogic.jpg",
                chef.getTexture().toString()
            );
            assertFalse(
                "Chefs should not take inputs by default.",
                chef.isInputEnabled()
            );
        }
    }

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
        chefManager.init(mapLoader.cookSpawnpoints);
        int i = 0;
        for (Chef chef : chefManager.getChefs()) {
            assertTrue(
                mapLoader.cookSpawnpoints
                    .get(i)
                    .epsilonEquals(chef.getBody().getPosition(), 1f)
            );
            i++;
        }
    }

    @Test
    public void hireChefTests() {
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
        ArrayList<Vector2> spawnPoints = mapLoader.cookSpawnpoints;
        for (int i = 2; i < 4; i++) {
            chefManager.hireChef(spawnPoints.get(i - 2), stage);
            assertEquals(
                new Vector2(
                    Math.round(spawnPoints.get(i - 2).x),
                    Math.round(spawnPoints.get(i - 2).y)
                ),
                new Vector2(
                    Math.round(chefManager.getChefs().get(i).getX()),
                    Math.round(chefManager.getChefs().get(i).getY())
                )
            );
            assertEquals(
                "Hire Chef should add a chef to the chefManagers chefs.",
                i + 1,
                chefManager.getChefs().size()
            );

            assertFalse(
                "Chefs should not take inputs by default after being hired.",
                chefManager.getChefs().get(i).isInputEnabled()
            );
        }
        chefManager.hireChef(spawnPoints.get(0), stage);
        assertEquals(
            "Hire Chef shouldn't add chefs over 4.",
            4,
            chefManager.getChefs().size()
        );
    }

    @Test
    public void actTests() {
        mapLoader.loadWaypoints(
            "Waypoints",
            "cookspawnid",
            "aispawnid",
            "lightid",
            "aiobjective"
        );
        ArrayList<Vector2> spawnPoints = mapLoader.cookSpawnpoints;
        assertNull(
            "currentChef should be null by default.",
            chefManager.getCurrentChef()
        );
        int maxChefs = 4;
        for (int chefIndex = 0; chefIndex < maxChefs; chefIndex++) {
            kbInput.keyDown(Keys.E);
            chefManager.act(1);
            assertEquals(
                "After E is pressed, the current chef should be initialised.",
                chefManager.getCurrentChef(),
                chefManager.getChefs().get(chefIndex)
            );
            if (chefIndex < 2) {
                chefManager.hireChef(spawnPoints.get(chefIndex), stage);
            }
        }
    }
}
