package cs.eng1.piazzapanic.customer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import cs.eng1.piazzapanic.food.recipes.Recipe;
import cs.eng1.piazzapanic.ui.Timer;

public class Customer extends Actor implements Disposable {

    private Timer repTimer;
    private Recipe order;
    private CustomerManager customerManager;
    private boolean reputation = true;

    public Customer(Recipe order, CustomerManager customerManager) {
        repTimer = new Timer(3000, true, false);
        this.order = order;
        this.customerManager = customerManager;
    }

    public Recipe getOrder() {
        return order;
    }

    @Override
    public void act(float delta) {
        if (repTimer.tick(delta) && reputation) {
            customerManager.loseReputation();
        }
    }

    @Override
    public void dispose() {
    }
}
