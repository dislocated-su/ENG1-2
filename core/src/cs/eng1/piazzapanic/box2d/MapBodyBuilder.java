package cs.eng1.piazzapanic.box2d;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * Populates the world with bodies created from the obstacle layer. Used for
 * creating collisions.
 *
 * This class is present in many other libgdx projects and our
 * implementation only contains minor changes based on what we need.
 *
 * The original code was found in a forum post and links to a tutorial that is no longer available.
 * <a href="https://gamedev.stackexchange.com/questions/66924/how-can-i-convert-a-tilemap-to-a-box2d-world">...</a>
 * The original author is assumed to be the person claiming to be in said forum.
 * @author David Saltares
 * @author Andrey Samoilov
 */
public class MapBodyBuilder {

    // The pixels per tile. If your tiles are 16x16, this is set to 16f
    private static float ppt = 16;

    /**
     * Create collision boxes from the MapObjects on the Obstacle object layer.
     * @param layer {@link MapLayer} to create bodies from.
     * @param pixels Pixels per tile (default 16).
     * @param world Box2D {@link World} to create objects in.
     * @return Array of built shapes for further processing.
     */
    public static Array<Body> buildShapes(MapLayer layer, float pixels, World world) {
        ppt = pixels;
        MapObjects shadowedObjects = layer.getObjects();

        Array<Body> bodies = new Array<>();

        for (MapObject object : shadowedObjects) {
            Shape shape = decideShape(object);

            if (shape == null) {
                continue;
            }

            BodyDef bd = new BodyDef();
            bd.type = BodyType.StaticBody;
            Body body = world.createBody(bd);
            body.createFixture(shape, 1);

            bodies.add(body);

            shape.dispose();
        }

        return bodies;
    }

    private static Shape decideShape(MapObject object) {
        if (object instanceof TextureMapObject) {
            return null;
        }

        Shape shape;

        if (object instanceof RectangleMapObject) {
            shape = getRectangle((RectangleMapObject) object);
        } else if (object instanceof PolygonMapObject) {
            shape = getPolygon((PolygonMapObject) object);
        } else if (object instanceof PolylineMapObject) {
            shape = getPolyline((PolylineMapObject) object);
        } else {
            return null;
        }

        return shape;
    }

    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2(
            (rectangle.x + rectangle.width * 0.5f) / ppt,
            (rectangle.y + rectangle.height * 0.5f) / ppt
        );
        polygon.setAsBox(rectangle.width * 0.5f / ppt, rectangle.height * 0.5f / ppt, size, 0.0f);
        return polygon;
    }

    private static PolygonShape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            System.out.println(vertices[i]);
            worldVertices[i] = vertices[i] / ppt;
        }

        polygon.set(worldVertices);
        return polygon;
    }

    private static ChainShape getPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] / ppt;
            worldVertices[i].y = vertices[i * 2 + 1] / ppt;
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);
        return chain;
    }

    public Shape getShape(MapObject object) {
        return decideShape(object);
    }
}
