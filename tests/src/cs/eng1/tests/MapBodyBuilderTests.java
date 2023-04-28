package cs.eng1.tests;

import static org.junit.Assert.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import cs.eng1.piazzapanic.box2d.MapBodyBuilder;
import cs.eng1.piazzapanic.chef.FixedStack;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.utility.MapLoader;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class MapBodyBuilderTests {

    Vector2 centre = new Vector2(0, 0);
    MapLoader mapLoader = new MapLoader("test-map.tmx");
    World world = new World(centre, true);
    MapLayer layer = mapLoader.getMap().getLayers().get("Obstacles");

    @Test
    public void buildShapesTest() {
        Array<Body> bodies = MapBodyBuilder.buildShapes(layer, 16, world);
        for (int i = 0; i < bodies.size; i++) {
            assertTrue(
                "Each object in buildShapes should be a body.",
                bodies.get(i) instanceof Body
            );
        }
        assertEquals(
            "buildShapes should properly parse the objects in an object layer.",
            11,
            bodies.size
        );
        assertEquals(centre, bodies.get(0).getPosition());
    }
}
