package cs.eng1.tests;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.recipes.Burger;
import cs.eng1.piazzapanic.food.recipes.JacketPotato;
import cs.eng1.piazzapanic.food.recipes.Pizza;
import cs.eng1.piazzapanic.food.recipes.Recipe;
import cs.eng1.piazzapanic.food.recipes.Salad;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class RecipeTests {
    /*
     * Define a variable for each recipe
     */
    FoodTextureManager textureManager = new FoodTextureManager();
    Recipe pizza = new Recipe("pizza", textureManager);
    Recipe burger = new Recipe("burger", textureManager);
    Recipe salad = new Recipe("salad", textureManager);
    Recipe potato = new Recipe("jacket_potato", textureManager);
    /*
     * These test getTexture finds the right texture for the recipes
     */
    @Test
    public void getTextureTests() {
        assertEquals("food/glitch/misc/pizza_01.png", pizza.getTexture().toString());
        assertEquals("food/glitch/misc/sandwich_burger_04.png", burger.getTexture().toString());
        assertEquals("food/glitch/misc/salad.png", salad.getTexture().toString());
        assertEquals("food/glitch/misc/ice_cube.png", potato.getTexture().toString());
    }
    /*
     * These test getType finds the right type for the recipe
     */
    @Test
    public void getTypeTests() {
        assertEquals("pizza", pizza.getType());
        assertEquals("burger", burger.getType());
        assertEquals("salad", salad.getType());
        assertEquals("jacket_potato", potato.getType());
    }
    /*
     * These test getRecipeIngredients returns a list of the right ingredients for the recipe
     */
    @Test
    public void getRecipeIngredientsTests() {
        assertEquals("getRecipeIngredients should return the list of ingredients for a burger", "[bun, patty_cooked]", burger.getRecipeIngredients().toString());
        assertEquals("getRecipeIngredients should return the list of ingredients for a pizza", "[dough, tomato_chopped, cheese_sliced]", pizza.getRecipeIngredients().toString());
        assertEquals("getRecipeIngredients should return the list of ingredients for a salad", "[tomato_chopped, lettuce_chopped]", salad.getRecipeIngredients().toString());
        assertEquals("getRecipeIngredients should return the list of ingredients for a jacket potato", "[potato_cooked, cheese_sliced]", potato.getRecipeIngredients().toString());
    }
    /*
     * These test fromString returns the right type for the recipe given
     */
    @Test
    public void fromStringTests() {
        assertEquals("MAKE_BURGER should return a recipe of type burger", "burger", Recipe.fromString("MAKE_BURGER", textureManager).getType());
        assertEquals("MAKE_PIZZA should return a recipe of type pizza", "pizza", Recipe.fromString("MAKE_PIZZA", textureManager).getType());
        assertEquals("MAKE_SALAD should return a recipe of type salad", "salad", Recipe.fromString("MAKE_SALAD", textureManager).getType());
        assertEquals("MAKE_JACKET should return a recipe of type jacket_potato", "jacket_potato", Recipe.fromString("MAKE_JACKET", textureManager).getType());
    }
    /*
     * This tests getTextureManager finds the texturemanager of the recipe.
     */
    @Test
    public void getTextureManagerTests() {
        assertEquals(textureManager, pizza.getTextureManager());
    }
}
