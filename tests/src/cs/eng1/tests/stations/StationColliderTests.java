package cs.eng1.tests.stations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.stations.IngredientStation;
import cs.eng1.piazzapanic.stations.StationCollider;
import cs.eng1.piazzapanic.ui.StationUIController;
import cs.eng1.piazzapanic.ui.UIOverlay;
import cs.eng1.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the StationCollider class, the behaviour of which being determined by notify observer, registering (as well as deregister), and act.
 *
 * @author Sabid Hossain
 */
@RunWith(GdxTestRunner.class)
public class StationColliderTests {

    World world = new World(Vector2.Zero, true);
    private final UIOverlay overlay = mock(UIOverlay.class);
    ChefManager chefManager = new ChefManager(0, overlay, world, null);
    Chef chef = new Chef(null, null, chefManager);
    private final StationUIController uiController = mock(StationUIController.class);
    IngredientStation station = new IngredientStation(0, null, uiController, null, null, null);

    /**
     * Tests that notfiyObservers and getLastNotification work correctly
     */
    @Test
    public void testNotifyObservers() {
        StationCollider stationCollider = new StationCollider(chefManager);
        stationCollider.register(station);
        stationCollider.notifyObservers(chef);
        station.update(null);
        assertEquals("tests that notfiyObservers tells the observers(stations) about a chef", chef, station.nearbyChef);
        assertEquals(
            "tests getLastNotification returns the last thing that was notified",
            chef,
            stationCollider.getLastNotification()
        );
    }

    /**
     * Tests that register and deregister work correctly
     */
    @Test
    public void testDeregister() {
        StationCollider stationCollider = new StationCollider(chefManager);
        stationCollider.register(station);
        stationCollider.deregister(station);
        stationCollider.notifyObservers(chef);
        station.update(null);
        assertNull("tests that deregister removes the nearbyChef", station.nearbyChef);
    }

    /**
     * Tests that clearAllObservesr corectly deregisters the station from all its
     * current observers
     */
    @Test
    public void testDeregisterFromAllSubjects() {
        StationCollider stationCollider = new StationCollider(chefManager);
        stationCollider.register(station);
        stationCollider.clearAllObservers();
        stationCollider.notifyObservers(chef);
        station.update(null);
        assertNull("tests that clearAllObservers removes all nearbyChef", station.nearbyChef);
    }

    /**
     * Tests that register adds the station to the station coliders observers
     */
    @Test
    public void testRegister() {
        StationCollider stationCollider = new StationCollider(chefManager);
        stationCollider.register(station);
        stationCollider.notifyObservers(chef);
        station.update(null);
        assertEquals(
            "tests that register adds the nearbyChef when a station has been registered",
            chef,
            station.nearbyChef
        );
    }

    /**
     * tests that act works corectly
     */
    @Test
    public void testAct() {
        StationCollider stationCollider = new StationCollider(chefManager);
        chefManager.setCurrentChef(chefManager.getChefs().get(0));
        chefManager.getChefs().get(0).setX(stationCollider.getX());
        chefManager.getChefs().get(0).setY(stationCollider.getY());
        stationCollider.register(station);
        stationCollider.setWidth(1);
        stationCollider.setHeight(1);
        stationCollider.act(0);
        station.update(null);
        assertEquals(chefManager.getChefs().get(0), station.nearbyChef);
    }
}
