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
    World world = new World(new Vector2(0, 0), true);
    UIOverlay overlay = mock(UIOverlay.class);
    KeyboardInput kbInput = new KeyboardInput();
    ChefManager chefManager = new ChefManager(1, overlay, world, kbInput);
    Texture fake = new Texture(Gdx.files.internal("Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Brown/manBrown_hold.png"));
    Chef chef = new Chef(fake, new Vector2(10, 10), chefManager);

    @Test
    public void initTests() {
        chef.init(0,0);
        assertEquals(0, chef.getX(), 0.1);
        assertEquals(0, chef.getY(), 0.1);
        assertEquals(chef.getStack(), new ArrayList<>());
    }

    @Test
    public void actTests() {
        kbInput.keyDown(Keys.RIGHT);
        chef.init(0,0);
        chef.act(1f);
        assertEquals(1, chef.getX(), 0.1);
    }
} 
