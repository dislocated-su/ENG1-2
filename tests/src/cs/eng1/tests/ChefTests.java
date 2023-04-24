package cs.eng1.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.*;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.ui.UIOverlay;
import cs.eng1.piazzapanic.utility.KeyboardInput;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

@RunWith(GdxTestRunner.class)
public class ChefTests {
    public float delta = 1;
    World world = new World(new Vector2(0, 0), true);
    UIOverlay overlay = mock(UIOverlay.class);
    KeyboardInput kbInput = new KeyboardInput();
    ChefManager chefManager = new ChefManager(1, overlay, world, kbInput);
    Texture fake = new Texture(Gdx.files.internal("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"));
    Chef chef = new Chef(fake, new Vector2(10, 10), chefManager);
    ArrayList inputs = new ArrayList<>(Arrays.asList(Keys.LEFT, Keys.DOWN, Keys.UP, Keys.RIGHT, Keys.E, Keys.A, Keys.S, Keys.D, Keys.W));

    @Test
    public void initTests() {
        chef.init(0,0);
        assertEquals(0, chef.getX(), 0.1);
        assertEquals(0, chef.getY(), 0.1);
        assertEquals(chef.getStack(), new ArrayList<>());
    }

    @Test
    public void movementTests() {
        kbInput.keyDown(Keys.RIGHT);
        chef.init(0,0);
        chef.act(delta/60);
        assertEquals(-0.3, chef.getY(), 0.1);
        assertEquals(-0.5, chef.getX(), 0.1);
        move();
        assertEquals(3.3, chef.getX(), 0.1);
        assertEquals(-0.3, chef.getY(), 0.1);
        chef.init(0,0);
        kbInput.clearInputs();
        kbInput.keyDown(Keys.LEFT);
        kbInput.keyDown(Keys.W);
        move();
        assertEquals(-3.2, chef.getX(), 0.1);
        assertEquals(2.4, chef.getY(), 0.1);
        chef.init(0,0);
        kbInput.clearInputs();
        kbInput.keyDown(Keys.A);
        kbInput.keyDown(Keys.D);
        move();
        assertEquals(-0.5, chef.getX(), 0.1);
        assertEquals(-0.3, chef.getY(), 0.1);
    }

    public void move() {
        for(int i = 0; i <= 60; i++) {
            chef.act(delta/60);
            world.step(delta/60, 6, 2);
        }
    }
    // for (int i = 0; i < keys.size(); i++) {
    //     kbInput.keyDown(keys.get(i));
    // }
} 