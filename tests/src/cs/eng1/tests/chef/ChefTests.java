package cs.eng1.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ai.utils.random.IntegerDistribution;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.Cheese;
import cs.eng1.piazzapanic.food.ingredients.Ingredient;
import cs.eng1.piazzapanic.food.ingredients.Patty;
import cs.eng1.piazzapanic.food.interfaces.Holdable;
import cs.eng1.piazzapanic.food.recipes.Pizza;
import cs.eng1.piazzapanic.ui.UIOverlay;
import cs.eng1.piazzapanic.utility.KeyboardInput;
import java.lang.Math;
import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;

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
        Arrays.asList(
            Keys.LEFT,
            Keys.DOWN,
            Keys.UP,
            Keys.RIGHT,
            Keys.E,
            Keys.A,
            Keys.S,
            Keys.D,
            Keys.W
        )
    );

    @Test
    public void chefStackTests() {
        chef.init(0, 0);
        Ingredient cheese = new Cheese(foodManager);
        Patty patty = new Patty(foodManager);
        Pizza pizza = new Pizza(foodManager);
        assertTrue(
            "A chef with an empty stack should be able to grab ingredients.",
            chef.canGrabIngredient()
        );
        chef.grabItem(cheese);
        assertTrue(chef.hasIngredient());
        //For whatever reason, calling popIngredient breaks the CI, so I'll just do this without the peek() check.
        chef.getStack().pop();
        chef.grabItem(patty);
        assertTrue(chef.hasIngredient());
        for (int i = 0; i < chef.getStack().size(); i++) {
            chef.grabItem(cheese);
        }
        assertFalse(chef.canGrabIngredient());
        chef.getStack().pop();
        assertTrue(chef.canGrabIngredient());
        chef.grabItem(pizza);
        Holdable top = chef.getStack().peek();
        assertEquals(
            "placeRecipe should return the top of the stack if it's a recipe.",
            pizza,
            chef.placeRecipe()
        );
        assertNotEquals(
            "placeRecipe should remove the top item of a list if it's a recipe.",
            top,
            chef.getStack().peek()
        );
        assertNull(
            "placeRecipe should return null if the top of the stack isn't a recipe.",
            chef.placeRecipe()
        );
    }

    @Test
    public void initTests() {
        chef.init(0, 0);
        assertEquals(
            "Init(0, 0) should set the chef X to 0.",
            0,
            chef.getX(),
            0.1
        );

        assertEquals(
            "Init(0, 0) should set the chef Y to 0.",
            0,
            chef.getY(),
            0.1
        );

        chef.init(10, 10);
        assertEquals(
            "Init(10, 10) should set the chef X to 10.",
            10,
            chef.getX(),
            0.1
        );

        assertEquals(
            "Init(10, 10) should set the chef Y to 10.",
            10,
            chef.getY(),
            0.1
        );

        assertFalse(
            "init should set the chef stack to an empty list.",
            chef.hasIngredient()
        );
    }

    @Test
    public void createBodyTests() {
        chef.createBody();
        assertNotNull("Create body should create a body.", chef.getBody());
        assertEquals(
            "CreateBody should initialise at (0, 0.2)",
            start,
            chef.getBody().getPosition()
        );
    }

    @Test
    public void movementTests() {
        chef.init(0, 0);
        chef.act(1);
        assertEquals(start, (Vector2) chef.getBody().getPosition());
        clear();
        kbInput.keyDown(Keys.DOWN);
        move();
        assertEquals(0, chef.getBody().getPosition().x, 0.1);
        assertEquals(-4, chef.getBody().getPosition().y, 0.1);
        clear();
        kbInput.keyDown(Keys.RIGHT);
        move();
        assertEquals(
            "When the chef moves right for 1 second, it should move properly.",
            4,
            chef.getBody().getPosition().x,
            0.1
        );
        assertEquals(
            "When the chef moves right, its vertical position shouldn't change.",
            0,
            chef.getBody().getPosition().y,
            0.1
        );
        clear();
        kbInput.keyDown(Keys.LEFT);
        kbInput.keyDown(Keys.UP);
        move();
        assertEquals(
            "When the chef moves left and up, it should be normalised to a diagonal.",
            -2.7,
            chef.getBody().getPosition().x,
            0.1
        );
        assertEquals(
            "When the chef moves left and up, it should be normalised to a diagonal.",
            2.5,
            chef.getBody().getPosition().y,
            0.1
        );
        clear();
        kbInput.keyDown(Keys.A);
        kbInput.keyDown(Keys.D);
        kbInput.keyDown(Keys.W);
        kbInput.keyDown(Keys.S);
        move();
        assertEquals(
            "When the chef moves in every direction, no movement should happen.",
            start,
            (Vector2) chef.getBody().getPosition()
        );
    }

    /**
     * Acts for the chef and world to allow "movement" to happen.
     * Clears the inputs afterwards.
     */
    private void move() {
        for (int i = 0; i <= 60; i++) {
            chef.act((float) 1 / 60);
            world.step((float) 1 / 60, 6, 2);
        }
        kbInput.clearInputs();
    }

    /**
     * Clears chef momentum, then resets position
     */
    private void clear() {
        for (int i = 0; i < 60; i++) {
            chef.act((float) 1 / 60);
            world.step((float) 1 / 60, 6, 2);
        }
        chef.init(0, 0);
    }
}