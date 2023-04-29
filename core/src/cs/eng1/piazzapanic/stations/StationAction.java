package cs.eng1.piazzapanic.stations;

import cs.eng1.piazzapanic.PlayerState;

public class StationAction {

    public enum ActionType {
        CHOP_ACTION,
        COOK_ACTION,
        FLIP_ACTION,
        PLACE_INGREDIENT,
        GRAB_INGREDIENT,
        ASSEMBLE_PIZZA,
        MAKE_BURGER,
        MAKE_SALAD,
        MAKE_PIZZA,
        MAKE_JACKET,
        SUBMIT_ORDER,
        BUY_STATION,
    }

    public static String getActionDescription(ActionType actionType) {
        switch (actionType) {
            case CHOP_ACTION:
                return "Chop";
            case COOK_ACTION:
                return "Cook";
            case FLIP_ACTION:
                return "Flip Item";
            case GRAB_INGREDIENT:
                return "Grab Item";
            case PLACE_INGREDIENT:
                return "Place Item";
            case MAKE_BURGER:
                return "Make Burger";
            case MAKE_SALAD:
                return "Make Salad";
            case MAKE_JACKET:
                return "Make Jacket Potato";
            case MAKE_PIZZA:
                return "Make Pizza";
            case SUBMIT_ORDER:
                return "Submit Order";
            case ASSEMBLE_PIZZA:
                return "Assemble Pizza";
            case BUY_STATION:
                return (
                    "Unlock station (" +
                    PlayerState.getInstance().getUpgradeCost(false) +
                    ")"
                );
            default:
                return "Unknown Action";
        }
    }
}
