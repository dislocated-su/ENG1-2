package cs.eng1.tests;

import static org.junit.Assert.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.glutils.FileTextureData;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.utility.KeyboardInput;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class KeyboardInputTests {
    KeyboardInput kbInput = new KeyboardInput();
    @Test
    public void nullTest() {
        assertFalse(kbInput.keyDown(Keys.ANY_KEY));
        assertFalse(kbInput.keyUp(Keys.ANY_KEY));
    }
}