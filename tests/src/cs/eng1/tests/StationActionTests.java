package cs.eng1.tests;

import static org.junit.Assert.*;

import cs.eng1.piazzapanic.stations.StationAction;
import cs.eng1.piazzapanic.stations.StationAction.ActionType;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class StationActionTests {

    @Test
    public void testGetActionDescription() {
        assertEquals(
            "Chop",
            StationAction.getActionDescription(ActionType.CHOP_ACTION)
        );
        assertEquals(
            "Cook",
            StationAction.getActionDescription(ActionType.COOK_ACTION)
        );
        assertEquals(
            "Flip Item",
            StationAction.getActionDescription(ActionType.FLIP_ACTION)
        );
        assertEquals(
            "Grab Item",
            StationAction.getActionDescription(ActionType.GRAB_INGREDIENT)
        );
        assertEquals(
            "Place Item",
            StationAction.getActionDescription(ActionType.PLACE_INGREDIENT)
        );
        assertEquals(
            "Make Burger",
            StationAction.getActionDescription(ActionType.MAKE_BURGER)
        );
        assertEquals(
            "Make Salad",
            StationAction.getActionDescription(ActionType.MAKE_SALAD)
        );
        assertEquals(
            "Make Jacket Potato",
            StationAction.getActionDescription(ActionType.MAKE_JACKET)
        );
        assertEquals(
            "Make Pizza",
            StationAction.getActionDescription(ActionType.MAKE_PIZZA)
        );
        assertEquals(
            "Submit Order",
            StationAction.getActionDescription(ActionType.SUBMIT_ORDER)
        );
        assertEquals(
            "Assemble Pizza",
            StationAction.getActionDescription(ActionType.ASSEMBLE_PIZZA)
        );
        assertEquals(
            "Unlock station (100.0)",
            StationAction.getActionDescription(ActionType.BUY_STATION)
        );
    }
}
