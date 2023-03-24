package cs.eng1.tests;

import static org.junit.Assert.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.BasicGrillable;
import cs.eng1.piazzapanic.food.ingredients.Ingredient;
import cs.eng1.piazzapanic.food.ingredients.Patty;
import cs.eng1.piazzapanic.food.interfaces.Grillable;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import cs.eng1.piazzapanic.food.ingredients.Ingredient;
import cs.eng1.piazzapanic.food.interfaces.Holdable;
import cs.eng1.piazzapanic.food.recipes.Recipe;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.chef.ChefManager;
@RunWith(GdxTestRunner.class)
public class ChefTests {
    ChefManager chefManager = new ChefManager(1f, null, null);
    Chef chef = new Chef(null, null, chefManager);
    Vector2 vector = new Vector2(0, 0);
    @Test
    public void keyDownTests() {
        chef.keyDown(21, 1);
        assertEquals(-1f, chef.getX(), 0.1f);
        assertEquals(0, chef.getY(), 0.1f);
    }
}
