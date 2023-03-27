package cs.eng1.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;

import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.stations.IngredientStation;
import cs.eng1.piazzapanic.stations.StationCollider;
import cs.eng1.piazzapanic.ui.StationUIController;

@RunWith(GdxTestRunner.class)

public class StationColliderTests{
    ChefManager chefManager = new ChefManager(0, null, null);
    Chef chef = new Chef(null, null, chefManager);
    private StationUIController uiController = mock(StationUIController.class);
    IngredientStation station = new IngredientStation(0, null, uiController, null, null);

    
    @Test
    /**
     * Tests that notfiyObservers and getLastNotification work correctly
     */
    public void testNotifyObservers(){
        StationCollider stationCollider = new StationCollider(chefManager);
        stationCollider.register(station);
        stationCollider.notifyObservers(chef);
        station.update(null);
        assertEquals("tests that notfiyObservers tells the observers(stations) about a chef" ,chef, station.nearbyChef);
        assertEquals("tests getLastNotification returns the last thing that was notified" ,chef, stationCollider.getLastNotification());
    }
    @Test
    /**
     * Tests that register and deregister work correctly
     */
    public void testDeregister(){
        StationCollider stationCollider = new StationCollider(chefManager);
        stationCollider.register(station);
        stationCollider.deregister(station);
        station.update(null);
        assertEquals("tests that deregister removes the nearbyChef" ,null , station.nearbyChef);
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
        station.update(null);
        assertEquals("tests that clearAllObservers removes all nearbyChef" ,null , station.nearbyChef);
    }

    @Test

    public void testRegister(){
        StationCollider stationCollider = new StationCollider(chefManager);
        stationCollider.register(station);
        stationCollider.notifyObservers(chef);
        station.update(null);
        assertEquals("tests that register adds the nearbyChef" ,chef , station.nearbyChef);
    }
}