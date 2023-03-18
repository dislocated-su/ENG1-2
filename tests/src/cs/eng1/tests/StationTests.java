package cs.eng1.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.glutils.FileTextureData;

import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.observable.Subject;
import cs.eng1.piazzapanic.stations.Station;
import cs.eng1.piazzapanic.ui.StationUIController;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class StationTests {
    Chef chef = new Chef(null, null, null);
    Chef chef2 = new Chef(null, null, null);
    Subject<Chef> chefSubject;    
    StationUIController uiController = mock(StationUIController.class);


    @Test
    public void testUpdate(){
        Station station = new Station(0, null, uiController, null);
        station.update(chef);
        assertEquals("tests that update changes nearbyChef when there's a chef",chef, station.nearbyChef);
        station.update(null);
        assertEquals("tests that nearbyChef is set to null if there is no chef",null, station.nearbyChef);

        
    }
    @Test
    public void testSubject(){
        Station station = new Station(0, null, uiController, null);
        station.addSubject(chefSubject);
        assertEquals("tests that addSubject adds a subject to the list",chefSubject, station.getChefSubjects().get(0));

        station.removeSubject(chefSubject);
        assertEquals("tests that removeSubject removes a subject from the list",0, station.getChefSubjects().size());
    }

    @Test
    public void testDeregisterFromAllSubjects(){
        Station station = new Station(0, null, uiController, null);
        station.deregisterFromAllSubjects();
        assertEquals("tests that deregisterFromAllSubjects removes all subjects from the list",0, station.getChefSubjects().size());
    }

    @Test
    public void testGetActionTypesNoActions(){
        Station station = new Station(0, null, uiController, null);
        List<String> actionTypes = new LinkedList<>();
        assertEquals("tests that getActionTypes returns an empty list if there are no actions",actionTypes, station.getActionTypes());
    }

    @Test
    public void testDoStationActionNothing(){
        Station station = new Station(0, null, uiController, null);
        station.doStationAction(null);
        assertEquals("tests that doStationAction does nothing if the action is null",null, station.nearbyChef);
    }

    @Test 
    public void testGetId(){
        Station station = new Station(0, null, uiController, null);
        assertEquals("tests that getId returns the correct id",0, station.getId());
    }
    @Test
    public void testSetImageRotation(){
        Station station = new Station(0, null, uiController, null);
        float rotation = 45.0f;
        station.setImageRotation(rotation);
        assertEquals("tests that setImageRotation sets the correct image rotation", rotation, station.getImageRotation(), 0.01f);
    }


    @Test
    public void testUpdateNotNull(){
        Station station = new Station(0, null, uiController, null);
        station.update(chef);
        assertEquals("tests that update changes nearbyChef when there's a chef",chef, station.nearbyChef);
        station.update(chef2);
        assertEquals("tests that update changes nearbyChef when there's a chef",chef2, station.nearbyChef);
    }

}
