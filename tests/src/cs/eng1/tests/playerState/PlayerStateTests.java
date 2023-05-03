package cs.eng1.tests.playerState;

import static org.junit.Assert.*;

import cs.eng1.piazzapanic.PlayerState;
import cs.eng1.piazzapanic.PlayerState.PowerUp;
import cs.eng1.piazzapanic.utility.Timer;
import cs.eng1.tests.GdxTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests the functionality of PlayerState - being functions related to costs, cash, powerUps and act().
 *
 * @author Ross Holmes
 * @author Joel Paxman
 */
@RunWith(GdxTestRunner.class)
public class PlayerStateTests {

    /**
     * Test if PlayerState returns and increments the price when purchased
     */
    @Test
    public void getCostsTests() {
        PlayerState state = PlayerState.getInstance();
        float initialCost = state.getUpgradeCost(false);
        assertEquals("", initialCost, state.getUpgradeCost(true), 0.1f);
        assertEquals("", initialCost += 100f, state.getUpgradeCost(false), 0.1f);
        assertEquals("", initialCost, state.getUpgradeCost(false), 0.1f);

        int initialChefCost = state.getChefHireCost(false);
        assertEquals("", initialChefCost, state.getChefHireCost(true), 0.1f);
        assertEquals("", initialChefCost *= 10, state.getChefHireCost(false), 0.1f);
        assertEquals("", initialChefCost, state.getChefHireCost(false), 0.1f);
    }

    /**
     * Tests if PlayerState correctly earns and spends cash
     * Tests the different possible multipliers on earning cash
     */
    @Test
    public void cashTests() {
        PlayerState.reset();
        PlayerState state = PlayerState.getInstance();

        assertEquals("Cash should be 0 at the start.", 0, state.getCash(), 0);

        state.earnCash(100, false);

        assertEquals("Cash should 100 after earning.", 100, state.getCash(), 0);

        state.earnCash(100, true);

        assertEquals(
            "Cash should be 230 after earning with happiness multiplier.",
            230,
            state.getCash(),
            0
        );

        state.activateBuff(PowerUp.MORE_MONEY);
        state.earnCash(100, false);
        assertEquals(
            "Cash should be 430 after earning with money power up.",
            430,
            state.getCash(),
            0
        );

        state.earnCash(100, true);
        assertEquals(
            "Cash should be 690 after earning with money power up.",
            690,
            state.getCash(),
            0
        );

        state.spendCash(0);
        assertEquals("Cash should be 690 after spending 0 cash.", 690, state.getCash(), 0);

        state.spendCash(700);
        assertEquals(
            "Cash should still be at 690 after attempt to spend more cash than you have.",
            690,
            state.getCash(),
            0
        );

        state.spendCash(350);
        assertEquals("Cash should be 340 after spending 350 cash.", 340, state.getCash(), 0);

        state.spendCash(340);
        assertEquals("Cash should be 0 after spending 340 cash.", 0, state.getCash(), 0);
    }

    /**
     * Tests if PowerUp names are returned and formatted correctly
     */
    @Test
    public void powerUpNameTests() {
        PlayerState.reset();
        PlayerState state = PlayerState.getInstance();
        assertEquals(
            "Expects Walk fast from getPowerupName(PowerUp.WALK_FAST)",
            "Walk fast",
            state.getPowerupName(PowerUp.WALK_FAST)
        );
        assertEquals(
            "Expects Cook fast from getPowerupName(PowerUp.COOK_FAST)",
            "Cook fast",
            state.getPowerupName(PowerUp.COOK_FAST)
        );
        assertEquals(
            "Expects More money from getPowerupName(PowerUp.MORE_MONEY)",
            "More money",
            state.getPowerupName(PowerUp.MORE_MONEY)
        );
        assertEquals(
            "Expects No rep loss from getPowerupName(PowerUp.NO_REP_LOSS)",
            "No rep loss",
            state.getPowerupName(PowerUp.NO_REP_LOSS)
        );
        assertEquals(
            "Expects No spoiling from getPowerupName(PowerUp.NO_SPOILING)",
            "No spoiling",
            state.getPowerupName(PowerUp.NO_SPOILING)
        );
    }

    /**
     * Tests if the PlayerState act function correctly tick all PowerUp timers correctly
     */
    @Test
    public void actTests() {
        PlayerState.reset();
        PlayerState state = PlayerState.getInstance();

        for (Timer powerUp : state.getPowerUpTimers().values()) {
            assertFalse("All PowerUps should be inactive by default.", powerUp.getRunning());
        }

        state.activateBuff(PowerUp.COOK_FAST);
        state.activateBuff(PowerUp.MORE_MONEY);
        state.activateBuff(PowerUp.NO_SPOILING);
        state.activateBuff(PowerUp.WALK_FAST);
        state.activateBuff(PowerUp.NO_REP_LOSS);

        for (Timer powerUp : state.getPowerUpTimers().values()) {
            assertTrue(
                "All PowerUps should be active after being activated.",
                powerUp.getRunning()
            );
        }

        for (int i = 0; i < 60; i++) {
            state.act(1f);
            for (Timer powerUp : state.getPowerUpTimers().values()) {
                assertTrue(
                    "All PowerUp timers should be running after they are activated.",
                    powerUp.getRunning()
                );
            }
        }

        state.act(1f);

        for (Timer powerUp : state.getPowerUpTimers().values()) {
            assertFalse(
                "All PowerUps should not be running after their duration elaspes.",
                powerUp.getRunning()
            );
        }
    }
}
