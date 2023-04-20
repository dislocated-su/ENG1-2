package cs.eng1.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.chef.ChefManager;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.Lettuce;
import cs.eng1.piazzapanic.food.ingredients.Patty;
import cs.eng1.piazzapanic.stations.ChoppingStation;
import cs.eng1.piazzapanic.stations.StationAction;
import cs.eng1.piazzapanic.stations.StationAction.ActionType;
import cs.eng1.piazzapanic.ui.StationUIController;
import cs.eng1.piazzapanic.ui.UIOverlay;
import cs.eng1.piazzapanic.utility.KeyboardInput;
import java.util.List;
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
    ChefManager chefManager = new ChefManager(1, overlay, null, kbInput);

    @Test
    public void actTest() {
        kbInput.keyDown(Keys.E);
        chefManager.act(1);
        assertEquals(chefManager.getCurrentChef(), chefManager.getChefs());
    }
}
