package cs.eng1.tests.box2d;

import static org.junit.Assert.*;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import cs.eng1.piazzapanic.box2d.Box2dSteeringBody;
import cs.eng1.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class box2dSteeringBodyTests {

    // SteeringBehavior<Vector2> behaviour;
    // Limiter limiter;
    // Location<Vector2> location;
    World world = new World(Vector2.Zero, true);
    BodyDef bd = new BodyDef();
    double dpi = Math.PI;
    float pi = (float) dpi;
    Body body = world.createBody(bd);
    Box2dSteeringBody steeringBody = new Box2dSteeringBody(body, true, 1f);

    @Test
    public void vectorToAngleTests() {
        assertEquals(
            "The angle from a body with vector (0, 0) should be 0.",
            0,
            steeringBody.vectorToAngle(new Vector2(0, 0)),
            0.1f
        );
        assertEquals(
            "The angle from a body with a vector (1, 0) should be -45 degrees.",
            -pi / 2,
            steeringBody.vectorToAngle(new Vector2(1, 0)),
            0.1f
        );
        assertEquals(
            "The angle from a body with a vector (-1, 0) should be 45 degrees.",
            pi / 2,
            steeringBody.vectorToAngle(new Vector2(-1, 0)),
            0.1f
        );
        assertEquals(
            "The angle from a body with a vector (0, -1) should be -90 degrees.",
            -pi,
            steeringBody.vectorToAngle(new Vector2(0, -1)),
            0.1f
        );
        assertEquals(
            "The angle from a body with a vector (0, 1) should be 0 degrees.",
            0,
            steeringBody.vectorToAngle(new Vector2(0, 1)),
            0.1f
        );
        assertEquals(
            "The angle from a body with a vector (1, 1) should be 45 degrees.",
            -pi / 4,
            steeringBody.vectorToAngle(new Vector2(1, 1)),
            0.1f
        );
    }

    @Test
    public void angleToVectorTests() {
        //These can't be in a single assert, delta can't be defined for vector2 so they are always equal if they are both a Vector2.
        Vector2 vector = Vector2.Zero;
        steeringBody.angleToVector(vector, 0);
        assertEquals(0, vector.x, 0.1);
        assertEquals(1, vector.y, 0.1);

        steeringBody.angleToVector(vector, -pi / 2);
        assertEquals(1, vector.x, 0.1);
        assertEquals(0, vector.y, 0.1);

        steeringBody.angleToVector(vector, pi / 2);
        assertEquals(-1, vector.x, 0.1);
        assertEquals(0, vector.y, 0.1);

        steeringBody.angleToVector(vector, -pi);
        assertEquals(0, vector.x, 0.1);
        assertEquals(-1, vector.y, 0.1);

        steeringBody.angleToVector(vector, pi);
        assertEquals(0, vector.x, 0.1);
        assertEquals(-1, vector.y, 0.1);

        steeringBody.angleToVector(vector, pi / 4);
        assertEquals(-0.7, vector.x, 0.1);
        assertEquals(0.7, vector.y, 0.1);
        for (float i = 1 / 6000; i < 1; i += 1 / 6000f) {
            steeringBody.angleToVector(vector, -pi * i);
            assertEquals(
                "angleToVector should always return a vector with a length of 1",
                1,
                vector.len(),
                0.000001
            );
        }
    }

    @Test
    public void steeringBehaviorTests() {
        assertNull(
            "SteeringBehaviour should be null by default.",
            steeringBody.getSteeringBehavior()
        );
        // Tried testing this then realised it was protected, I'll leave this for if we change it.
        //     behaviour.setEnabled(true);
        //     behaviour.setOwner(steeringBody);
        //     behaviour.setLimiter(limiter);
        //     location = steeringBody.newLocation();
        //     behaviour.newVector(location);
        //     steeringBody.setSteeringBehavior(behaviour);
        //     assertEquals(
        //         location,
        //         steeringBody.getSteeringBehavior().get(0)
        //     );
    }
}
