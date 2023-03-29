// package cs.eng1.tests;

// import static org.junit.Assert.*;

// import com.badlogic.gdx.Gdx;
// import com.badlogic.gdx.Input;
// import com.badlogic.gdx.Input.Keys;
// import com.badlogic.gdx.InputProcessor;
// import com.badlogic.gdx.graphics.Texture;
// import com.badlogic.gdx.graphics.Texture;
// import com.badlogic.gdx.graphics.g2d.Batch;
// import com.badlogic.gdx.math.Vector2;
// import com.badlogic.gdx.math.Vector2;
// import com.badlogic.gdx.physics.box2d.Body;
// import com.badlogic.gdx.physics.box2d.BodyDef;
// import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
// import com.badlogic.gdx.physics.box2d.CircleShape;
// import com.badlogic.gdx.physics.box2d.FixtureDef;
// import com.badlogic.gdx.scenes.scene2d.Actor;
// import com.badlogic.gdx.scenes.scene2d.Stage;
// import com.badlogic.gdx.utils.Disposable;
// import cs.eng1.piazzapanic.chef.Chef;
// import cs.eng1.piazzapanic.chef.ChefManager;
// import cs.eng1.piazzapanic.chef.KeyboardInput;
// import cs.eng1.piazzapanic.food.FoodTextureManager;
// import cs.eng1.piazzapanic.food.ingredients.BasicGrillable;
// import cs.eng1.piazzapanic.food.ingredients.Ingredient;
// import cs.eng1.piazzapanic.food.ingredients.Ingredient;
// import cs.eng1.piazzapanic.food.ingredients.Patty;
// import cs.eng1.piazzapanic.food.interfaces.Grillable;
// import cs.eng1.piazzapanic.food.interfaces.Holdable;
// import cs.eng1.piazzapanic.food.recipes.Recipe;
// import org.junit.Test;
// import org.junit.runner.RunWith;

// @RunWith(GdxTestRunner.class)
// public class ChefTests {
//     Stage stage = new Stage();
//     KeyboardInput kbInput = new KeyboardInput();
//     ChefManager chefManager = new ChefManager(1f, null, null, kbInput);
//     Chef chef = new Chef(null, null, chefManager);
//     Vector2 vector = new Vector2(0, 0);

//     @Test
//     public void keyDownTests() {
//         kbInput.keyDown(Keys.LEFT);
//         assertEquals(-1f, chef.getX(), 0.1f);
//         assertEquals(0, chef.getY(), 0.1f);
//     }
// }
