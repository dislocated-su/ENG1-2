package cs.eng1.piazzapanic.customer;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import cs.eng1.piazzapanic.food.recipes.Recipe;
import cs.eng1.piazzapanic.utility.Timer;

public class Customer extends Actor implements Disposable {

    private Timer repTimer;
    private Recipe order;
    private CustomerManager customerManager;
    private boolean reputation = true;
    private boolean orderCompleted = false;

    public Customer(Recipe order, CustomerManager customerManager) {
        repTimer = new Timer(6000, true, false);
        this.order = order;
        this.customerManager = customerManager;
    }

    public Recipe getOrder() {
        return order;
    }

    public void fulfillOrder() {
        orderCompleted = true;
    }

    @Override
    public void act(float delta) {
        if (!orderCompleted && reputation && repTimer.tick(delta)) {
            customerManager.loseReputation();
            reputation = false;
        }
    }

    @Override
    public void dispose() {}
}
