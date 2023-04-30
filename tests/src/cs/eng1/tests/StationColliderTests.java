package cs.eng1.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.stations.IngredientStation;
import cs.eng1.piazzapanic.stations.StationCollider;
import cs.eng1.piazzapanic.ui.StationUIController;
import cs.eng1.piazzapanic.ui.UIOverlay;
import java.util.Set;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class StationColliderTests {

    World world = new World(new Vector2(0, 0), true);
    private UIOverlay overlay = mock(UIOverlay.class);
    ChefManager chefManager = new ChefManager(0, overlay, world, null);
    Chef chef = new Chef(null, null, chefManager);
    private StationUIController uiController = mock(StationUIController.class);
    IngredientStation station = new IngredientStation(
        0,
        null,
        uiController,
        null,
        null,
        null
    );

    @Test
    /**
     * Tests that notfiyObservers and getLastNotification work correctly
     */
    public void testNotifyObservers() {
        StationCollider stationCollider = new StationCollider(chefManager);
        stationCollider.register(station);
        stationCollider.notifyObservers(chef);
        station.update(null);
        assertEquals(
            "tests that notfiyObservers tells the observers(stations) about a chef",
            chef,
            station.nearbyChef
        );
        assertEquals(
            "tests getLastNotification returns the last thing that was notified",
            chef,
            stationCollider.getLastNotification()
        );
    }

    @Test
    /**
     * Tests that register and deregister work correctly
     */
    public void testDeregister() {
        StationCollider stationCollider = new StationCollider(chefManager);
        stationCollider.register(station);
        stationCollider.deregister(station);
        stationCollider.notifyObservers(chef);
        station.update(null);
        assertEquals(
            "tests that deregister removes the nearbyChef",
            null,
            station.nearbyChef
        );
    }

    @Test
    /**
     * Tests that clearAllObservesr corectly deregisters the station from all its
     * current observers
     */
    public void testDeregisterFromAllSubjects() {
        StationCollider stationCollider = new StationCollider(chefManager);
        stationCollider.register(station);
        stationCollider.clearAllObservers();
        stationCollider.notifyObservers(chef);
        station.update(null);
        assertEquals(
            "tests that clearAllObservers removes all nearbyChef",
            null,
            station.nearbyChef
        );
    }

    @Test
    /**
     * Tests that register adds the station to the station coliders observers
     */
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

    @Test
    ///**
    // * tests that act works corectly
    // */
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
