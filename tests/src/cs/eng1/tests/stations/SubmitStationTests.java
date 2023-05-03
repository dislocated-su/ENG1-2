package cs.eng1.tests.stations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.customer.CustomerManager;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.Patty;
import cs.eng1.piazzapanic.food.recipes.Pizza;
import cs.eng1.piazzapanic.stations.StationAction;
import cs.eng1.piazzapanic.stations.StationAction.ActionType;
import cs.eng1.piazzapanic.stations.SubmitStation;
import cs.eng1.piazzapanic.ui.StationUIController;
import cs.eng1.piazzapanic.ui.UIOverlay;
import cs.eng1.piazzapanic.utility.KeyboardInput;
import cs.eng1.piazzapanic.utility.MapLoader;
import cs.eng1.tests.GdxTestRunner;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class SubmitStationTests {

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
    StationUIController uiController = mock(StationUIController.class);
    MapLoader mapLoader = new MapLoader("test-map.tmx");
    ChefManager chefManager = new ChefManager(1, overlay, world, kbInput);

    Texture fake = new Texture(
        Gdx.files.internal(
            "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"
        )
    );
    Chef chef = new Chef(fake, Vector2.Zero, chefManager);

    /**
     * Tests that getActionTypes returns nothing when the user is unable to use the
     * station
     * because there is no nearby chef, the chef isn't holding anything or they
     * aren't
     * holding a recipe
     */
    @Test
    public void testGetActionTypesNothing() {
        SubmitStation station = new SubmitStation(
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
        station.nearbyChef = chef;
        actionTypes = station.getActionTypes();
        assertTrue(
            "nothing is added to action types if the chef and station have no ingredients",
            actionTypes.isEmpty()
        );
        chef.grabItem(new Patty(textureManager));
        actionTypes = station.getActionTypes();
        assertTrue(
            "Nothing is added to action types if the chef has an item that is not a Recipe.",
            actionTypes.isEmpty()
        );
    }

    /**
     * Tests that getActionTypes returns SUBMIT_ORDER when the chef has the next
     * recipe
     * that needs to be submited and doesn't when the chef does not.
     */
    @Test
    public void testCorrectRecipe() {
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
            uiController,
            textureManager,
            customerManager
        );
        customerManager.init(
            textureManager,
            stage,
            mapLoader.aiObjectives,
            mapLoader.aiSpawnpoints
        );
        SubmitStation station = customerManager.getRecipeStations().get(63);
        station.nearbyChef = chef;
        station.customer = customerManager.getCustomer(0);

        List<StationAction.ActionType> actionTypes = station.getActionTypes();
        assertFalse(
            "Submit order is not added to action types if the chef has an incorrect recipe",
            actionTypes.contains(ActionType.SUBMIT_ORDER)
        );
        chef.getStack().clear();
        chef.grabItem(new Pizza(textureManager));

        actionTypes = station.getActionTypes();
        assertTrue(
            "Submit order is added to action types if the chef has a correct recipe",
            actionTypes.contains(ActionType.SUBMIT_ORDER)
        );
    }
}
