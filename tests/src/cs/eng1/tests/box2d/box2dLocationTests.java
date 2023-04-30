package cs.eng1.tests.box2d;


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.ai.utils.Location;

import cs.eng1.piazzapanic.box2d.Box2dLocation;
import cs.eng1.tests.GdxTestRunner;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class box2dLocationTests {  
    @Test
    public void newLocationTests() {
        Box2dLocation location = new Box2dLocation(new Vector2(1, 0), 1f);
        Location<Vector2> location2 = location.newLocation();
        assertEquals(
            "A new location should have co-ordinates (0, 0).", 
            new Vector2(0, 0),
            location2.getPosition()
        );
        assertEquals(
            "A new location should have an orientation of 0.", 
            0, 
            location2.getOrientation(),
            0.1
        );
    }
  
    @Test
    public void vectorToAngleTests() {
        Box2dLocation location = new Box2dLocation(new Vector2(), 0);
        assertEquals(
            "vectorToAngle should calculate the proper angle when x and y are positive.", 
            -0.8f,
            location.vectorToAngle(new Vector2(1, 1)), 
            0.1
        );
        assertEquals(
            "vectorToAngle should calculate the proper angle when x and y are positive and the vector ratio changes.", 
            -1.2, 
            location.vectorToAngle(new Vector2(5, 2)), 
            0.1
        );
        assertEquals(
            "vectorToAngle should calculate the proper angle with negative x and positive y.", 
            0.8f,
            location.vectorToAngle(new Vector2(-1, 1)), 
            0.1
        );
        assertEquals(
            "vectorToAngle should calculate the proper angle with positive x and negative y.", 
            -2.4f,
            location.vectorToAngle(new Vector2(1, -1)), 
            0.1
        );
        assertEquals(
            "vectorToAngle should calculate the proper angle when x and y are negative.", 
            2.4f,
            location.vectorToAngle(new Vector2(-1, -1)), 
            0.1
        );
        
    }
  
    @Test
    public void angleToVectorTests() {
        Box2dLocation location = new Box2dLocation(new Vector2(), 0);
        assertEquals(
            "Expect calculated vector to be correct.",
            new Vector2(-0.84147096f, 0.5403023f),
            location.angleToVector(new Vector2(), 1f)
        );
        assertEquals(
            "Expect calculated vector to be correct with negative angle.",
            new Vector2(0.84147096f, 0.5403023f),
            location.angleToVector(new Vector2(), -1f)
        );
    }
}
