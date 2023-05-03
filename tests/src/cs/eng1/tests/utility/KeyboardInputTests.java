package cs.eng1.tests.utility;

import static org.junit.Assert.*;

import com.badlogic.gdx.Input.Keys;
import cs.eng1.piazzapanic.utility.KeyboardInput;
import cs.eng1.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the behaviour of the KeyboardInput class, being not processing
 * disallowed keys, processing allowed keys, and removing unused functions in
 * the class by default.
 * 
 * @author Joel Paxman
 */
@RunWith(GdxTestRunner.class)
public class KeyboardInputTests {

        KeyboardInput kbInput = new KeyboardInput();

        /**
         * Asserts non allowed keys are not processed.
         */
        @Test
        public void nullTest() {
                assertFalse(
                                "Keys other than ones permitted should be unprocessed.",
                                kbInput.keyDown(Keys.ANY_KEY) && kbInput.keyUp(Keys.ANY_KEY));
        }

        /**
         * Asserts movement is disabled by default.
         */
        @Test
        public void variablesTest() {
                assertFalse(
                                "All keys for movement and chef switching should start as false.",
                                kbInput.left &&
                                                kbInput.right &&
                                                kbInput.up &&
                                                kbInput.down &&
                                                kbInput.changeCooks);
        }

        /**
         * Assert keyDown processes all enabled keys.
         */
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
                                                kbInput.keyDown(Keys.E));
        }

        /**
         * Assert keyUp processes all enabled keys.
         */
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
                                                kbInput.keyUp(Keys.E));
        }

        /*
         * Assert unused but necessary implemented functions that have been changed all
         * return false.
         */
        @Test
        public void absentFunctionTest() {
                assertFalse(
                                "None of these functions should return true.",
                                kbInput.keyTyped('a') &&
                                                kbInput.touchDown(0, 0, 0, 0) &&
                                                kbInput.touchUp(0, 0, 0, 0) &&
                                                kbInput.touchDragged(0, 0, 0) &&
                                                kbInput.mouseMoved(0, 0) &&
                                                kbInput.scrolled(0, 0));
        }
}
