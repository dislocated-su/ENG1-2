package cs.eng1.piazzapanic.customer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.ai.steer.utils.rays.CentralRayWithWhiskersConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import cs.eng1.piazzapanic.PlayerState;
import cs.eng1.piazzapanic.box2d.Box2dLocation;
import cs.eng1.piazzapanic.box2d.Box2dRadiusProximity;
import cs.eng1.piazzapanic.box2d.Box2dRaycastCollisionDetector;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.recipes.*;
import cs.eng1.piazzapanic.stations.SubmitStation;
import cs.eng1.piazzapanic.ui.UIOverlay;
import cs.eng1.piazzapanic.utility.Timer;
import cs.eng1.piazzapanic.utility.saving.SavedCustomer;
import cs.eng1.piazzapanic.utility.saving.SavedCustomerManager;
import java.util.*;

public class CustomerManager {

    public final int totalCustomers;
    final World world;
    private final LinkedList<Customer> customerQueue;
    private final HashMap<Integer, SubmitStation> recipeStations;
    private final UIOverlay overlay;
    private final int maxSpawnRate = 10000;
    private final float customerScale;
    private final Timer spawnTimer;
    private final Timer endlessTimer;
    // Separate random instances are used to not break existing tests relying on a
    // set permutation of orders.
    private final Random randomOrders;
    private final Random randomTextures;
    private final Random randomCustomerCount;
    private final List<Integer> objectiveIds = new ArrayList<>();
    private final Map<Integer, Boolean> objectiveAvailability;
    private final String[] customerSprites = new String[] {
        "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Hitman 1/hitman1_hold.png",
        "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Hitman 2/hitman2_hold.png",
        "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Old/manOld_hold.png",
        "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Survivor 2/survivor2_hold.png",
        "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Survivor 1/survivor1_hold.png",
    };
    private int completedOrders = 0;
    private Recipe[] possibleRecipes;
    private int reputation = 3;
    private int spawnedCustomers = 0;
    private Map<Integer, Box2dLocation> objectives;
    private List<Vector2> spawnLocations;
    private Stage stage;
    private Integer[] stationsRanked;

    /**
     * @param customerScale how big are customers as a multiplier of the unit scale.
     * @param overlay       {@link UIOverlay}
     * @param world         {@link World}
     * @param customers     how many customers will spawn total. For endless mode, set this to 0.
     */
    public CustomerManager(float customerScale, UIOverlay overlay, World world, int customers) {
        this.overlay = overlay;
        this.recipeStations = new HashMap<>();

        totalCustomers = customers;
        randomOrders = new Random();
        randomTextures = new Random();
        this.customerScale = customerScale;
        this.world = world;

        spawnTimer = new Timer(60000, false, true);
        endlessTimer = new Timer(8000, false, true);

        objectiveAvailability = new HashMap<>();
        customerQueue = new LinkedList<>();
        randomCustomerCount = new Random();
    }

    public CustomerManager(
        SavedCustomerManager save,
        float customerScale,
        UIOverlay overlay,
        World world,
        FoodTextureManager textureManager
    ) {
        this.overlay = overlay;
        this.recipeStations = new HashMap<>();

        totalCustomers = save.totalCustomers;
        spawnedCustomers = save.spawnedCustomers;
        randomOrders = new Random();
        randomTextures = new Random();
        this.customerScale = customerScale;
        this.world = world;

        spawnTimer = save.spawnTimer;
        endlessTimer = save.endlessTimer;

        reputation = save.reputation;
        randomCustomerCount = new Random();
        objectiveAvailability = save.objectiveAvailabilities.get();

        possibleRecipes =
            new Recipe[] {
                new Burger(textureManager),
                new Salad(textureManager),
                new Pizza(textureManager),
                new JacketPotato(textureManager),
            };

        customerQueue = new LinkedList<>();
        for (SavedCustomer savedCustomer : save.customerQueue) {
            Texture texture = new Texture(Gdx.files.internal(savedCustomer.imagePath));
            Customer customer = new Customer(
                texture,
                new Vector2(texture.getWidth() * customerScale, texture.getHeight() * customerScale),
                savedCustomer.position,
                Recipe.fromString(savedCustomer.order, textureManager),
                this
            );
            customer.currentObjective = savedCustomer.currentObjective;
            this.customerQueue.addLast(customer);
        }

        for (Integer id : objectiveAvailability.keySet()) {
            objectiveIds.add(id);
        }

        Collections.sort(objectiveIds);
    }

    /**
     * Initialise CustomerManager with an empty state and set random seed. This is
     * useful for testing.
     *
     * @param overlay   {@link UIOverlay} (probably mocked)
     * @param customers The total number of customers to spawn - 0 means endless
     * @param seed      seed for the {@link Random} instance to generate set orders
     */
    public CustomerManager(float customerScale, UIOverlay overlay, World world, int customers, long seed) {
        this(customerScale, overlay, world, customers);
        randomOrders.setSeed(seed);
        randomCustomerCount.setSeed(seed);
    }

    /**
     * Add customers to stage and reset + initialise the state of CustomerManager.
     *
     * @param textureManager The manager of food textures that can be passed to the
     *                       recipes
     * @param stage          The stage to display customers on.
     * @param objectives     The objectives loaded from the map
     * @param spawnLocations Spawn locations loaded from the map
     */
    public void init(
        FoodTextureManager textureManager,
        Stage stage,
        Map<Integer, Box2dLocation> objectives,
        List<Vector2> spawnLocations
    ) {
        customerQueue.clear();

        possibleRecipes =
            new Recipe[] {
                new Burger(textureManager),
                new Salad(textureManager),
                new Pizza(textureManager),
                new JacketPotato(textureManager),
            };

        this.stage = stage;
        this.objectives = objectives;
        this.spawnLocations = spawnLocations;

        for (Integer id : objectives.keySet()) {
            objectiveAvailability.put(id, true);
            objectiveIds.add(id);
        }

        Collections.sort(objectiveIds);

        generateCustomer();
        float difficultyMod = 1f;
        Gdx.app.log(PlayerState.getInstance().getDifficulty() + "", "");

        switch (PlayerState.getInstance().getDifficulty()) {
            case 0:
                difficultyMod = 2f;
                break;
            case 2:
                difficultyMod = 0.75f;
                break;
        }
        spawnTimer.setDelay((int) (spawnTimer.getDelay() * difficultyMod));
        spawnTimer.start();

        if (totalCustomers == 0) {
            endlessTimer.start();
        }
    }

    /**
     * A version of {@link CustomerManager#init(FoodTextureManager, Stage, Map, List)} with fewer arguments and logic.
     * This is because a lot of the state is loaded from the save. <br>
     * When loaded from a {@link SavedCustomerManager}, use this instead of {@code .init()}
     *
     * @param stage          The stage to display customers on.
     * @param objectives     The objectives loaded from the map
     * @param spawnLocations Spawn locations loaded from the map
     */
    public void load(Stage stage, Map<Integer, Box2dLocation> objectives, List<Vector2> spawnLocations) {
        this.stage = stage;
        this.objectives = objectives;
        this.spawnLocations = spawnLocations;

        for (Customer c : customerQueue) {
            stage.addActor(c);
            updateCustomerLocation(c, c.currentObjective);
        }
    }

    /**
     * @return a {@link List} containing all orders represented as a {@link Recipe}, in the order they have been added.
     */
    public List<Recipe> getOrders() {
        LinkedList<Recipe> output = new LinkedList<>();
        for (Customer c : customerQueue) {
            output.add(c.getOrder());
        }
        return output;
    }

    /**
     * Needs to be called every frame to occasionally spawn customers.
     *
     * @param delta time since last frame in seconds
     */
    public void act(float delta) {
        if (reputation == 0) {
            overlay.finishGameUI();
        }

        if (endlessTimer.getRunning()) {
            if (endlessTimer.tick(delta)) {
                spawnTimer.setDelay(Math.max(Math.round(spawnTimer.getDelay() * 0.95f), maxSpawnRate));
                Gdx.app.log("Changing spawnTimer delay", spawnTimer.getDelay() + "");
            }
        }
        checkSpawn(delta);
    }

    /**
     * Lose a reputation point. Is supposed to be called by a customer.
     */
    protected void loseReputation() {
        if (reputation > 0) {
            reputation--;
            overlay.updateReputationCounter(reputation);
        }
    }

    /**
     * Complete the provided customers order and move on to the next one. Then update the UI.
     * If all the recipes are completed, then show the winning UI.
     * <p>
     * With the current implementation, it is possible to have endless mode use the
     * totalCustomers value of 0 without requiring changes.
     *
     * @param customer The customer whose order is to be fulfilled.
     */
    public void nextRecipe(Customer customer) {
        completedOrders++;
        customerQueue.remove(customer);
        customer.fulfillOrder();
        if (customerQueue.size() >= 3) {
            for (Customer c : customerQueue.subList(3, customerQueue.size())) {
                Integer objective = findAvailableObjective();
                if (objective == null) {
                    continue;
                }
                updateCustomerLocation(c, objective);
            }
        }
        notifySubmitStations();
        // requires updating overlay to allow for multiple orders being displayed at
        // once
        overlay.updateOrders(getOrders());
        if (completedOrders == totalCustomers) {
            spawnTimer.stop();
            overlay.updateRecipeUI(null);
            overlay.finishGameUI();
        }
    }

    /**
     * If one recipe station has been updated, let all the other ones know that
     * there is a new recipe to be built.
     */
    private void notifySubmitStations() {
        for (SubmitStation recipeStation : recipeStations.values()) {
            recipeStation.updateOrderActions();
        }
    }

    public void addStation(SubmitStation station) {
        recipeStations.put(station.getId(), station);
        stationsRanked = recipeStations.keySet().toArray(new Integer[0]);
        Arrays.sort(stationsRanked);
    }

    /**
     * Create a new customer with an order.
     */
    public void generateCustomer() {
        Integer customerObjective = findAvailableObjective();
        if (customerObjective != null) {
            spawnedCustomers++;
            // implement random generation of two or three customers at once here
            Texture texture = new Texture(customerSprites[randomTextures.nextInt(customerSprites.length - 1)]);
            Customer customer = new Customer(
                texture,
                new Vector2(texture.getWidth() * customerScale, texture.getHeight() * customerScale),
                spawnLocations.get(0),
                possibleRecipes[randomOrders.nextInt(4)],
                this
            );
            customerQueue.addLast(customer);
            stage.addActor(customer);
            updateCustomerLocation(customer, customerObjective);
        }
    }

    /**
     * Tick a spawn timer and check if we can spawn a customer. Can spawn multiple if enough time has passed.
     */
    private void checkSpawn(float delta) {
        if (spawnedCustomers != totalCustomers && spawnTimer.tick(delta)) {
            int customerCount = (spawnTimer.getDelay() < 45000) ? randomCustomerCount.nextInt(1, 4) : 1;

            for (int i = 0; i < customerCount; i++) {
                generateCustomer();
            }
            overlay.updateOrders(getOrders());

            spawnTimer.reset();
        }
    }

    private void updateCustomerLocation(Customer customer, Integer objective) {
        makeItGoThere(customer, objective);
        if (objective < 4) {
            recipeStations.get(stationsRanked[objective]).customer = customer;
        }
    }

    /**
     * Give the customer an objective to go to.
     *
     * @param locationID and id from objectives
     */
    private void makeItGoThere(Customer customer, int locationID) {
        if (customer.currentObjective != null) {
            objectiveAvailability.put(customer.currentObjective, true);
        }

        Box2dLocation there = objectives.get(locationID);

        Arrive<Vector2> arrive = new Arrive<>(customer.steeringBody)
            .setTimeToTarget(3f)
            .setArrivalTolerance(0.1f)
            .setDecelerationRadius(2)
            .setTarget(there);

        Proximity<Vector2> proximity = new Box2dRadiusProximity(customer.steeringBody, world, 0.5f);
        CollisionAvoidance<Vector2> collisionAvoidance = new CollisionAvoidance<>(customer.steeringBody, proximity);

        RaycastObstacleAvoidance<Vector2> wallAvoidance = new RaycastObstacleAvoidance<>(customer.steeringBody);
        wallAvoidance
            .setRayConfiguration(new CentralRayWithWhiskersConfiguration<>(customer.steeringBody, 0.1f, 0.3f, 0.35f))
            .setRaycastCollisionDetector(new Box2dRaycastCollisionDetector(world))
            .setDistanceFromBoundary(locationID);

        BlendedSteering<Vector2> blendedAvoidance = new BlendedSteering<>(customer.steeringBody)
            .add(collisionAvoidance, 0.5f)
            .add(wallAvoidance, 0.5f);

        PrioritySteering<Vector2> prioritySteering = new PrioritySteering<>(customer.steeringBody)
            .add(blendedAvoidance)
            .add(arrive);

        customer.steeringBody.setSteeringBehavior(prioritySteering);
        customer.currentObjective = locationID;
        objectiveAvailability.put(customer.currentObjective, false);

        if (locationID == -1) {
            customer.steeringBody.setOrientation((float) (1.5f * Math.PI));
        } else {
            customer.steeringBody.setOrientation((float) -(1.5f * Math.PI));
        }
    }

    /**
     * Calls makeItGoThere but with objective set to -1, effectively making the agent walk back to the despawn point.
     */
    public void walkBack(Customer customer) {
        this.makeItGoThere(customer, -1);
    }

    private Integer findAvailableObjective() {
        for (Integer id : objectiveIds) {
            if (id < 0) {
                continue;
            }
            if (objectiveAvailability.get(id)) {
                return id;
            }
        }
        return null;
    }

    public Box2dLocation getObjective(int id) {
        return objectives.get(id);
    }

    public Map<Integer, SubmitStation> getRecipeStations() {
        return recipeStations;
    }

    public Timer getSpawnTimer() {
        return spawnTimer;
    }

    public int getSpawnedCustomers() {
        return spawnedCustomers;
    }

    public LinkedList<Customer> getCustomerQueue() {
        return customerQueue;
    }

    public Customer getCustomer(int pos) {
        return customerQueue.get(pos);
    }

    public int getReputation() {
        return reputation;
    }

    public Map<Integer, Box2dLocation> getObjectives() {
        return objectives;
    }

    public Timer getEndlessTimer() {
        return endlessTimer;
    }

    public List<Vector2> getSpawnLocations() {
        return spawnLocations;
    }

    public int getCompletedOrders() {
        return completedOrders;
    }

    public int getMaxSpawnRate() {
        return maxSpawnRate;
    }

    public Map<Integer, Boolean> getAvailabilities() {
        return objectiveAvailability;
    }
}
