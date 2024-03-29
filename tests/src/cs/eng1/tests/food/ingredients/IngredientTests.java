package cs.eng1.tests.food.ingredients;

import static org.junit.Assert.*;

import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.ingredients.*;
import cs.eng1.piazzapanic.food.recipes.Pizza;
import cs.eng1.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the behaviour of Ingredient and various ingredients - being getTexture,
 * getType, getRecipeIngredients, fromString and getTextureManager.
 *
 * @author Joel Paxman
 */
@RunWith(GdxTestRunner.class)
public class IngredientTests {

    FoodTextureManager textureManager = new FoodTextureManager();
    Ingredient patty = new Patty(textureManager);
    Ingredient lettuce = new Lettuce(textureManager);
    Ingredient tomato = new Tomato(textureManager);
    Ingredient potato = new Potato(textureManager);
    Ingredient cheese = new Cheese(textureManager);
    Ingredient pizza = new UncookedPizza(textureManager);

    /**
     * Assert behaviour of fromString
     */
    @Test
    public void testFromString() {
        Ingredient pattyFromString = Ingredient.fromString("patty", textureManager);
        assertNotNull("fromString should find an ingredient.", pattyFromString);
        assertEquals(
            "The class of the ingredient found should be the correct class.",
            Patty.class,
            pattyFromString.getClass()
        );
    }

    /**
     * Assert isCooked properly accesses cooked
     */
    @Test
    public void testIsCooked() {
        assertFalse("The ingredient shouldn't be immediately cooked.", patty.getCooked());

        patty.setIsCooked(true);
        assertTrue("getCooked should be true when cooked is true.", patty.getCooked());

        patty.setIsCooked(false);
        assertFalse("getCooked should be false when cooked is false.", patty.getCooked());
    }

    /**
     * Assert isChopped properly accesses chopped
     */
    @Test
    public void testIsChopped() {
        assertFalse("An ingredient should not be chopped immediately.", lettuce.getChopped());

        lettuce.setChopped(true);
        assertTrue("getChopped should be true when chopped is true.", lettuce.getChopped());

        lettuce.setChopped(false);
        assertFalse("getChopped should be false when chopped is false.", lettuce.getChopped());
    }

    /**
     * Assert cheese behaviour is the the same from a BasicChoppable and textureManager
     */
    @Test
    public void testCheese() {
        Cheese cheese2 = new Cheese(textureManager);
        assertEquals(
            "An ingredient of type Cheese should have the same texture as the BasicChoppable of type Cheese.",
            cheese.getTexture(),
            cheese2.getTexture()
        );
        cheese2.setChopped(true);
        assertNotEquals(
            "Cheese should return a different texture when chopped.",
            cheese.getTexture(),
            cheese2.getTexture()
        );
    }

    /**
     * Assert isGrilled properly accesses grilled.
     */
    @Test
    public void testIsGrilled() {
        assertFalse("An ingredient should not be grilled immediately.", patty.getGrilled());

        patty.setIsGrilled(true);
        assertTrue("getGrilled should be true when grilled is true.", patty.getGrilled());

        patty.setIsGrilled(false);
        assertFalse("getGrilled should be false when grilled is false.", patty.getGrilled());
    }

    /**
     * Assert toString gets the right name for each state of an ingredient
     */
    @Test
    public void testToString() {
        assertEquals("Ingredient should be raw without interaction.", "patty_raw", patty.toString());
        patty.setIsGrilled(true);
        assertEquals("Ingredient should be changed to _grilled when fully grilled.", "patty_grilled", patty.toString());
        patty.setUseable(false);
        assertEquals("Ingredient should be _ruined when not useable.", "patty_ruined", patty.toString());

        assertEquals("Ingredient should be raw without interaction.", "lettuce_raw", lettuce.toString());
        lettuce.setChopped(true);
        assertEquals(
            "Ingredient should be changed to _choppedwhen fully chopped.",
            "lettuce_chopped",
            lettuce.toString()
        );
        lettuce.setUseable(false);
        assertEquals("Ingredient should be _ruined when not useable.", "lettuce_ruined", lettuce.toString());
        assertEquals("Ingredient should be raw without interaction.", "potato_raw", potato.toString());
        potato.setIsCooked(true);
        assertEquals("Ingredient should be changed to _cooked when fully cooked.", "potato_cooked", potato.toString());
        potato.setUseable(false);
        assertEquals("Ingredient should be _ruined when not useable.", "potato_ruined", potato.toString());
        cheese.setChopped(true);
        assertEquals("Ingredient should be _chopped when chopped.", "cheese_chopped", cheese.toString());
        assertEquals("Ingredient should be _raw when raw", "uncooked_pizza_raw", pizza.toString());
        pizza.setIsCooked(true);
        assertEquals("Ingredient should be _raw when raw", "uncooked_pizza_cooked", pizza.toString());
    }

    /**
     * Assert getUseable accesses useable and useable is true at the correct times.
     */
    @Test
    public void testUseable() {
        assertTrue("Ingredient should be useable by default", patty.getUseable());

        patty.setUseable(false);
        assertFalse("getUseable should be false when useable is false.", patty.getUseable());

        patty.setUseable(true);
        assertTrue("getUseable should be true when useable is true.", patty.getUseable());
    }

    /**
     * Assert arrayFromString splits and gets correct textures from an array
     */
    @Test
    public void arrayFromStringTests() {
        Ingredient[] ingredients = Ingredient.arrayFromString(
            "patty,tomato,lettuce,bun,cheese,potato,dough,uncooked_pizza",
            textureManager
        );
        assertEquals("arrayFromString should properly split the given string properly.", 8, ingredients.length);
        lettuce.setChopped(false);
        assertEquals(
            "arrayFromString should get textures from the split string.",
            lettuce.getType(),
            ingredients[2].getType()
        );
    }

    /**
     * Assert getTexture gets the right textures.
     */
    @Test
    public void getTextureTests() {
        assertEquals(
            "getTexture() should return the correct texture for a pizza.",
            textureManager.getTexture("uncooked_pizza"),
            pizza.getTexture()
        );
        assertEquals(
            "getTexture() should return the correct texture for cheese.",
            textureManager.getTexture("cheese"),
            cheese.getTexture()
        );
        assertEquals(
            "getTexture() should return the correct texture for a patty.",
            textureManager.getTexture("patty_raw"),
            patty.getTexture()
        );
        assertEquals(
            "getTexture() should return the correct texture for lettuce.",
            textureManager.getTexture("lettuce_raw"),
            lettuce.getTexture()
        );
        assertEquals(
            "getTexture() should return the correct texture for tomato.",
            textureManager.getTexture("tomato_raw"),
            tomato.getTexture()
        );
    }

    /**
     * Assert uncookedPizza behaviour.
     */
    @Test
    public void testUncookedPizza() {
        UncookedPizza unPizza = new UncookedPizza(textureManager);

        assertNull(unPizza.getCookingResult());
        assertEquals(pizza.getTexture(), unPizza.getTexture());

        unPizza.setIsCooked(true);

        assertEquals(textureManager.getTexture("pizza"), unPizza.getTexture());
        assertEquals(Pizza.class, unPizza.getCookingResult().getClass());

        unPizza.setUseable(false);
        pizza.setUseable(false);

        assertEquals(textureManager.getTexture("burnt"), unPizza.getTexture());
        assertEquals("uncooked_pizza_ruined", unPizza.getCookingResult().toString());
    }
}
