package cs.eng1.tests.box2d;

import static org.junit.Assert.*;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import cs.eng1.piazzapanic.box2d.MapBodyBuilder;
import cs.eng1.piazzapanic.utility.MapLoader;
import cs.eng1.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the public function of MapBodyBuilder - being buildShapes and decideShapes - given a map.
 *
 * @author Joel Paxman
 */
@RunWith(GdxTestRunner.class)
public class MapBodyBuilderTests {

    MapLoader mapLoader = new MapLoader("test-map.tmx");
    World world = new World(Vector2.Zero, true);
    MapLayer layer = mapLoader.getMap().getLayers().get("Obstacles");
    Array<Body> bodies = MapBodyBuilder.buildShapes(layer, 16, world);
    MapBodyBuilder bodyBuilder = new MapBodyBuilder();

    /**
     * Asserts that buildShapes finds each shape in a given map.
     */
    @Test
    public void buildShapesTest() {
        for (int i = 0; i < bodies.size; i++) {
            assertTrue("Each object in buildShapes should be a body.", bodies.get(i) instanceof Body);
        }
        assertEquals("buildShapes should properly parse the objects in an object layer.", 11, bodies.size);
    }

    /**
     * Asserts the function of decideShape - being parsing Polygons, Lines, and Rectangles - within a map.
     */
    @Test
    public void decideShapeTest() {
        MapObject polygon = layer.getObjects().get(0);
        MapObject line = layer.getObjects().get(1);
        MapObject rectangle = layer.getObjects().get(2);
        MapObject fake = new MapObject();
        assertNull(
            "Decideshape (getShape) should return null if the given MapObject is of indeterminate shape.",
            bodyBuilder.getShape(fake)
        );
        assertTrue(
            "Decideshape (getShape) should return a PolygonShape if the given MapObject is a polygon.",
            bodyBuilder.getShape(polygon) instanceof PolygonShape
        );
        assertTrue(
            "Decideshape (getShape) should return a ChainShape if the given MapObject is a chain.",
            bodyBuilder.getShape(line) instanceof ChainShape
        );
        assertTrue(
            "Decideshape (getShape) should return a PolygonShape if the given MapObject is a rectangle (square).",
            bodyBuilder.getShape(rectangle) instanceof PolygonShape
        );
        assertEquals(
            "The PolygonShape from DecideShape should be a rectangle if the given MapObject is a rectangle (square).",
            new Rectangle(896, 2560, 128, 128),
            ((RectangleMapObject) rectangle).getRectangle()
        );
    }
}
