package cs.eng1.tests;

import static org.junit.Assert.*;

import com.badlogic.gdx.Input.Keys;
import cs.eng1.piazzapanic.utility.KeyboardInput;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class KeyboardInputTests {

    KeyboardInput kbInput = new KeyboardInput();

    @Test
    public void nullTest() {
        assertFalse(
            "Keys other than ones permitted should be unprocessed.",
            kbInput.keyDown(Keys.ANY_KEY) && kbInput.keyUp(Keys.ANY_KEY)
        );
    }

    @Test
    public void variablesTest() {
        assertNotNull(
            "All keys for movement and chef switching should exist.",
            kbInput.left &&
            kbInput.right &&
            kbInput.up &&
            kbInput.down &&
            kbInput.changeCooks
        );
    }

    @Test
    public void keyDownTest() {
        assertTrue(
            "All keys for movement and chef switching should be processed.",
            kbInput.keyDown(Keys.LEFT) &&
            kbInput.keyDown(Keys.A) &&
            kbInput.keyDown(Keys.UP) &&
            kbInput.keyDown(Keys.W) &&
            kbInput.keyDown(Keys.RIGHT) &&
            kbInput.keyDown(Keys.D) &&
            kbInput.keyDown(Keys.DOWN) &&
            kbInput.keyDown(Keys.S) &&
            kbInput.keyDown(Keys.E)
        );
    }

    @Test
    public void keyUpTest() {
        assertTrue(
            "All keys for movement and chef switching should be processed.",
            kbInput.keyUp(Keys.LEFT) &&
            kbInput.keyUp(Keys.A) &&
            kbInput.keyUp(Keys.UP) &&
            kbInput.keyUp(Keys.W) &&
            kbInput.keyUp(Keys.RIGHT) &&
            kbInput.keyUp(Keys.D) &&
            kbInput.keyUp(Keys.DOWN) &&
            kbInput.keyUp(Keys.S) &&
            kbInput.keyUp(Keys.E)
        );
    }

    @Test
    public void absentFunctionTest() {
        assertFalse(
            "None of these functions should return true.",
            kbInput.keyTyped('a') &&
            kbInput.touchDown(0, 0, 0, 0) &&
            kbInput.touchUp(0, 0, 0, 0) &&
            kbInput.touchDragged(0, 0, 0) &&
            kbInput.mouseMoved(0, 0) &&
            kbInput.scrolled(0, 0)
        );
    }
}
