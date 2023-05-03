package cs.eng1.tests.food.recipes;

import static org.junit.Assert.assertEquals;

import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.recipes.Recipe;
import cs.eng1.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the functionality of Recipe and various Recipes, as well as their behaviour -
 * being getTexture, getType, getRecipeIngredients, and fromString.
 *
 * @author Joel Paxman
 */
@RunWith(GdxTestRunner.class)
public class RecipeTests {

    FoodTextureManager textureManager = new FoodTextureManager();
    Recipe pizza = new Recipe("pizza", textureManager);
    Recipe burger = new Recipe("burger", textureManager);
    Recipe salad = new Recipe("salad", textureManager);
    Recipe potato = new Recipe("jacket_potato", textureManager);

    /**
     * Asserts getTexture finds the right texture for the recipes given
     */
    @Test
    public void getTextureTests() {
        assertEquals("food/buff_chef/cooked_pizza.png", pizza.getTexture().toString());
        assertEquals("food/buff_chef/burger.png", burger.getTexture().toString());
        assertEquals("food/buff_chef/salad.png", salad.getTexture().toString());
        assertEquals("food/buff_chef/jacket_potato.png", potato.getTexture().toString());
    }

    /**
     * Asserts getType finds the right type for the recipe
     */
    @Test
    public void getTypeTests() {
        assertEquals("pizza", pizza.getType());
        assertEquals("burger", burger.getType());
        assertEquals("salad", salad.getType());
        assertEquals("jacket_potato", potato.getType());
    }

    /**
     * Asserts getRecipeIngredients returns a list of the right ingredients for the recipe
     */
    @Test
    public void getRecipeIngredientsTests() {
        assertEquals(
            "getRecipeIngredients should return the list of ingredients for a burger",
            "[bun, patty_grilled]",
            burger.getRecipeIngredients().toString()
        );
        assertEquals(
            "getRecipeIngredients should return the list of ingredients for a pizza",
            "[dough, tomato_chopped, cheese_sliced]",
            pizza.getRecipeIngredients().toString()
        );
        assertEquals(
            "getRecipeIngredients should return the list of ingredients for a salad",
            "[tomato_chopped, lettuce_chopped]",
            salad.getRecipeIngredients().toString()
        );
        assertEquals(
            "getRecipeIngredients should return the list of ingredients for a jacket potato",
            "[potato_cooked, cheese_sliced]",
            potato.getRecipeIngredients().toString()
        );
    }

    /**
     * Asserts fromString returns the right holdable
     */
    @Test
    public void fromStringTests() {
        assertEquals(
            "MAKE_BURGER should return a recipe of type burger",
            "burger",
            Recipe.fromString("burger", textureManager).getType()
        );
        assertEquals(
            "MAKE_PIZZA should return a recipe of type pizza",
            "pizza",
            Recipe.fromString("pizza", textureManager).getType()
        );
        assertEquals(
            "MAKE_SALAD should return a recipe of type salad",
            "salad",
            Recipe.fromString("salad", textureManager).getType()
        );
        assertEquals(
            "MAKE_JACKET should return a recipe of type jacket_potato",
            "jacket_potato",
            Recipe.fromString("jacket_potato", textureManager).getType()
        );
    }

    /**
     * Asserts getTextureManager finds the texturemanager of the recipe.
     */
    @Test
    public void getTextureManagerTests() {
        assertEquals(textureManager, pizza.getTextureManager());
    }
}
