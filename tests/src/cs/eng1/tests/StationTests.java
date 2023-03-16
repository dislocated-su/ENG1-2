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
}
