package cs.eng1.piazzapanic.utility.saving;

import com.badlogic.gdx.math.Vector2;
import cs.eng1.piazzapanic.customer.Customer;
import cs.eng1.piazzapanic.utility.Timer;

/**
 * SavedCustomer stores the values of a Customer relevant for saving in a manner
 * that can be stored as a json
 * 
 * SavedCustomer stores the customer's position, whether they have deducted rep
 * or had their order already, their time until they deduct rep, their order,
 * their image and their current destination
 * 
 * @author Ross Holmes
 * @author Andrey Samoilov
 */
public class SavedCustomer {

    public Vector2 position;
    public Timer repTimer;
    public boolean reputation, orderCompleted;
    public String order;
    public Integer currentObjective;
    public String imagePath;

    public SavedCustomer() {
    }

    public SavedCustomer(Customer customer) {
        this.position = customer.steeringBody.getBody().getPosition();
        this.repTimer = customer.getRepTimer();
        this.reputation = customer.getReputation();
        this.orderCompleted = customer.getOrderCompleted();
        this.currentObjective = customer.currentObjective;
        this.order = customer.getOrder().getType();
        this.imagePath = customer.getTexture().getTextureData().toString();
    }
}
