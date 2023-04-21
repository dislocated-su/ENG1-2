package cs.eng1.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.ui.UIOverlay;
import cs.eng1.piazzapanic.utility.KeyboardInput;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

@RunWith(GdxTestRunner.class)
public class ChefManagerTests {
    World world = new World(new Vector2(0, 0), true);
    UIOverlay overlay = mock(UIOverlay.class);
    KeyboardInput kbInput = new KeyboardInput();
    ChefManager chefManager = new ChefManager(1, overlay, world, kbInput);

    @Test
    public void actTest() {
        kbInput.keyDown(Keys.E);
        chefManager.act(1);
        int chefIndex = chefManager.getChefs().indexOf(chefManager.getCurrentChef());
        assertEquals(
            "After E is pressed, the current chef should be initialised.", 
            chefManager.getCurrentChef(), 
            chefManager.getChefs().get(chefIndex));
        kbInput.keyDown(Keys.E);
        chefManager.act(1);
        assertEquals(
            "Pressing E after having a current chef should change the current chef.", 
            chefManager.getCurrentChef(), 
            chefManager.getChefs().get(chefIndex+1));
        kbInput.keyDown(Keys.E);
        chefManager.act(1);
        assertEquals(
            "When E is pressed and currentchef is the end of the array, it should wrap to the first chef.", 
            chefManager.getCurrentChef(), 
            chefManager.getChefs().get(chefIndex));
    }

    @Test
    public void setCurrentChefTests() {
        assertNull(chefManager.getCurrentChef());
        for (int i = 0; i < chefManager.getChefs().size(); i++) {
            chefManager.setCurrentChef(chefManager.getChefs().get(i));
            assertEquals(
                "setCurrentChef should properly assign currentChef.", 
                chefManager.getChefs().get(i), 
                chefManager.getCurrentChef());
        }
    }

}
