package cs.eng1.tests.chef;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.Cheese;
import cs.eng1.piazzapanic.food.ingredients.Ingredient;
import cs.eng1.piazzapanic.food.ingredients.Patty;
import cs.eng1.piazzapanic.ui.UIOverlay;
import cs.eng1.piazzapanic.utility.KeyboardInput;
import cs.eng1.tests.GdxTestRunner;
import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the function of Chef - being chefStack, init, createBody and movement.
 *
 * @author Joel Paxman
 */
@RunWith(GdxTestRunner.class)
public class ChefTests {

    World world = new World(Vector2.Zero, true);
    UIOverlay overlay = mock(UIOverlay.class);
    KeyboardInput kbInput = new KeyboardInput();
    ChefManager chefManager = new ChefManager(1, overlay, world, kbInput);
    Vector2 start = new Vector2((float) 0.0, (float) 0.2);
    FoodTextureManager foodManager = new FoodTextureManager();
    Texture fake = new Texture(
        Gdx.files.internal(
            "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"
        )
    );
    Chef chef = new Chef(fake, new Vector2(1, 1), chefManager);
    List<Integer> inputs = new ArrayList<>(
        Arrays.asList(Keys.LEFT, Keys.DOWN, Keys.UP, Keys.RIGHT, Keys.E, Keys.A, Keys.S, Keys.D, Keys.W)
    );

    /**
     * Asserts that the chef's ingredient stack behaves correctly.
     */
    @Test
    public void chefStackTests() {
        chef.init(0, 0);
        Ingredient cheese = new Cheese(foodManager);
        Patty patty = new Patty(foodManager);
        assertTrue("A chef with an empty stack should be able to grab ingredients.", chef.canGrabIngredient());
        chef.grabItem(cheese);
        assertTrue("hasIngredient should be true when the chef has an ingredient.", chef.hasIngredient());
        chef.getStack().pop();
        chef.grabItem(patty);
        assertTrue(chef.hasIngredient());
        for (int i = 0; i < chef.getStack().size(); i++) {
            chef.grabItem(cheese);
        }
        assertFalse(chef.canGrabIngredient());
        chef.getStack().pop();
        assertTrue(chef.canGrabIngredient());
    }

    /**
     * Asserts that chef init both sets a chefs position and reset the chefs stack.
     */
    @Test
    public void initTests() {
        chef.init(0, 0);
        assertEquals("Init(0, 0) should set the chef X to 0.", 0, chef.getX(), 1f);

        assertEquals("Init(0, 0) should set the chef Y to 0.", 0, chef.getY(), 1f);

        chef.grabItem(new Cheese(foodManager));

        chef.init(10, 10);
        assertEquals("Init(10, 10) should set the chef X to 10.", 10, chef.getX(), 1f);

        assertEquals("Init(10, 10) should set the chef Y to 10.", 10, chef.getY(), 1f);

        assertFalse("init should set the chef stack to an empty list.", chef.hasIngredient());
    }

    /**
     * Asserts that chef createBody properly creates a body in the right position.
     */
    @Test
    public void createBodyTests() {
        chef.createBody();
        assertNotNull("Create body should create a body.", chef.getBody());
        assertEquals("CreateBody should initialise at (0, 0.2)", start, chef.getBody().getPosition());
        world = clearBodies(world);
    }

    /**
     * Asserts that chef movement works as currently expected.
     */
    @Test
    public void movementTests() {
        world = clearBodies(world);

        chef.init(0, 0);
        chef.act(1);
        assertEquals(start, (Vector2) chef.getBody().getPosition());
        clear();
        kbInput.keyDown(Keys.DOWN);
        move();
        assertEquals(-0.5, chef.getX(), 1f);
        assertEquals(-0.7, chef.getY(), 1f);
        clear();
        kbInput.keyDown(Keys.RIGHT);
        move();
        assertEquals("When the chef moves right for 1 second, it should move properly.", -0.3, chef.getX(), 1f);
        assertEquals("When the chef moves right, its vertical position shouldn't change.", -0.3, chef.getY(), 1f);
        clear();
        kbInput.keyDown(Keys.LEFT);
        kbInput.keyDown(Keys.UP);
        move();
        assertEquals("When the chef moves left and up, it should be normalised to a diagonal.", -0.8, chef.getX(), 1f);
        assertEquals("When the chef moves left and up, it should be normalised to a diagonal.", -0.3, chef.getY(), 1f);
        clear();
        kbInput.keyDown(Keys.A);
        kbInput.keyDown(Keys.D);
        kbInput.keyDown(Keys.W);
        kbInput.keyDown(Keys.S);
        move();
        assertTrue(
            "When the chef moves in every direction, no movement should happen.",
            start.epsilonEquals((Vector2) chef.getBody().getPosition(), 1f)
        );
        world = clearBodies(world);
    }

    /**
     * Acts for the chef and world to allow brief "movement" to happen.
     * Clears the inputs during this action.
     */
    private void move() {
        for (int i = 0; i <= 60; i++) {
            chef.act((float) 1 / 60);
            world.step((float) 1 / 60, 6, 2);
            kbInput.clearInputs();
        }
    }

    /**
     * Clears chef momentum, then resets its position.
     */
    private void clear() {
        for (int i = 0; i < 60; i++) {
            chef.act((float) 1 / 60);
            world.step((float) 1 / 60, 6, 2);
        }
        chef.init(0, 0);
    }

    /**
     * Clears the bodies on the testing World, to reduce memory impact and issues arising from large amounts of bodies.
     *
     * @param world the world to clear
     * @returns world with all bodies destroyed
     */
    public World clearBodies(World world) {
        Array<Body> bodies = new Array<>(100);
        world.getBodies(bodies);
        for (Body body : bodies) {
            world.destroyBody(body);
        }
        return world;
    }
}
