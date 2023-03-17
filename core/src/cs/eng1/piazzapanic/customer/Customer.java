package cs.eng1.piazzapanic.customer;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

import cs.eng1.piazzapanic.food.recipes.Recipe;
import cs.eng1.piazzapanic.ui.Timer;

public class Customer extends Actor implements Disposable {

    private Timer repTimer;
    private Recipe order;

    public Customer(Recipe order) {
        repTimer = new Timer(30000, true, false);
        this.order = order;
    }

    @Override
    public void dispose() {
    }

}
