package cs.eng1.piazzapanic.utility.saving;

// import java.util.Map.Entry;

import cs.eng1.piazzapanic.customer.Customer;
import cs.eng1.piazzapanic.customer.CustomerManager;
import cs.eng1.piazzapanic.utility.Timer;
import java.util.List;

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
