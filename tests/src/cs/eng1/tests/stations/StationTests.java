package cs.eng1.tests.stations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.stations.Station;
import cs.eng1.piazzapanic.stations.StationCollider;
import cs.eng1.piazzapanic.ui.StationUIController;
import cs.eng1.tests.GdxTestRunner;

import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class StationTests {

    Chef chef = new Chef(null, null, null);
    Chef chef2 = new Chef(null, null, null);
    StationCollider chefSubject = new StationCollider(null);
    StationUIController uiController = mock(StationUIController.class);

    @Test
    /**
     * Tests that station.update corectly changes nearby chef to any chef stood near to the
     * station, it also tests addSubject and removeSubject as they are used during this test
     */
    public void testUpdate() {
        Station station = new Station(0, null, uiController, null, null);
        chefSubject.register(station);
        station.update(chef);
        assertEquals(
            "tests that update changes nearbyChef when there's a chef",
            chef,
            station.nearbyChef
        );
        station.nearbyChef = chef;
        chefSubject.notifyObservers(chef2);
        station.update(null);
        assertEquals(
            "tests that nearby chef chenges when a chef leaves but a different chef is still in range of the station",
            chef2,
            station.nearbyChef
        );
        chefSubject.deregister(station);
        station.update(null);
        assertEquals(
            "tests that nearbyChef is set to null if there is no chef",
            null,
            station.nearbyChef
        );
    }

    @Test
    /**
     * Tests that deregisterFromAllSubjects corectly deregisters the station from all its
     * current subjects
     */
    public void testDeregisterFromAllSubjects() {
        Station station = new Station(0, null, uiController, null, null);
        chefSubject.register(station);
        StationCollider chefSubject2 = new StationCollider(null);
        chefSubject2.register(station);
        station.deregisterFromAllSubjects();
        assertEquals(
            "tests that deregisterFromAllSubjects removes all subjects from the list",
            0,
            station.getSubjects().size()
        );
    }

    @Test
    /**
     * Tests that getActionTypes returns an empty list if there are no actions
     */
    public void testGetActionTypesNoActions() {
        Station station = new Station(0, null, uiController, null, null);
        List<String> actionTypes = new LinkedList<>();
        assertEquals(
            "tests that getActionTypes returns an empty list if there are no actions",
            actionTypes,
            station.getActionTypes()
        );
    }

    @Test
    /**
     * Tests that doStationAction does nothing if the action is null
     */
    public void testDoStationActionNothing() {
        Station station = new Station(0, null, uiController, null, null);
        station.doStationAction(null);
        assertEquals(
            "tests that doStationAction does nothing if the action is null",
            null,
            station.nearbyChef
        );
    }

    @Test
    /**
     * Tests that getId returns the correct id
     */
    public void testGetId() {
        Station station = new Station(0, null, uiController, null, null);
        assertEquals(
            "tests that getId returns the correct id",
            0,
            station.getId()
        );
    }
}
