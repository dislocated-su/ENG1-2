package cs.eng1.tests;

import static org.junit.Assert.*;

import cs.eng1.piazzapanic.box2d.MapBodyBuilder;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import cs.eng1.piazzapanic.chef.FixedStack;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.utility.MapLoader;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class MapBodyBuilderTests {
    MapLoader mapLoader = new MapLoader("test-map.tmx");
    World world = new World(new Vector2(0, 0), true);
    MapLayer layer = mapLoader.getMap().getLayers().get("Obstacles");
    @Test
    public void buildShapesTest() {
        Array bodies = MapBodyBuilder.buildShapes(layer, 16, world);
        assertEquals("", bodies.get(1).getClass().toString());
    }
}
