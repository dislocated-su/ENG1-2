package cs.eng1.piazzapanic.utility.saving;

import cs.eng1.piazzapanic.customer.Customer;
import cs.eng1.piazzapanic.customer.CustomerManager;
import cs.eng1.piazzapanic.utility.Timer;
import java.util.List;

/**
 * SavedCustomerManager stores the values of a CustomerManager relevant for
 * saving in a manner that can be stored as a json
 *
 * SavedCustomerManager stores the max customers (0 in endless), how many have
 * been spawned so far, the player's reputation, the timers for spawning and
 * reducing spawn timer in endless, the availability of customer position
 * objectives and the customers themselves
 *
 * @author Ross Holmes
 * @author Andrey Samoilov
 */
public class SavedCustomerManager {

    public SavedCustomer[] customerQueue;
    public int totalCustomers, spawnedCustomers;
    public Timer spawnTimer, endlessTimer;
    public int reputation;
    public SerializableMap<Integer, Boolean> objectiveAvailabilities;

    public SavedCustomerManager(CustomerManager customerManager) {
        this.totalCustomers = customerManager.totalCustomers;
        this.spawnedCustomers = customerManager.getSpawnedCustomers();
        this.spawnTimer = customerManager.getSpawnTimer();
        this.endlessTimer = customerManager.getEndlessTimer();
        this.reputation = customerManager.getReputation();
        this.objectiveAvailabilities =
            new SerializableMap<>(customerManager.getAvailabilities());
        List<Customer> customers = customerManager.getCustomerQueue();
        customerQueue = new SavedCustomer[customers.size()];
        for (int i = 0; i < customers.size(); i++) {
            customerQueue[i] = customers.get(i).getSavedCustomer();
        }
    }

    public SavedCustomerManager() {}
}
