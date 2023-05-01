package cs.eng1.piazzapanic.customer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.steer.utils.rays.CentralRayWithWhiskersConfiguration;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import cs.eng1.piazzapanic.PlayerState;
import cs.eng1.piazzapanic.box2d.Box2dLocation;
import cs.eng1.piazzapanic.box2d.Box2dRadiusProximity;
import cs.eng1.piazzapanic.box2d.Box2dRaycastCollisionDetector;
import cs.eng1.piazzapanic.chef.Chef;
import cs.eng1.piazzapanic.food.FoodTextureManager;
import cs.eng1.piazzapanic.food.recipes.Burger;
import cs.eng1.piazzapanic.food.recipes.JacketPotato;
import cs.eng1.piazzapanic.food.recipes.Pizza;
import cs.eng1.piazzapanic.food.recipes.Recipe;
import cs.eng1.piazzapanic.food.recipes.Salad;
import cs.eng1.piazzapanic.stations.SubmitStation;
import cs.eng1.piazzapanic.ui.UIOverlay;
import cs.eng1.piazzapanic.utility.Timer;
import java.util.*;

public class CustomerManager {

    private final LinkedList<Customer> customerQueue = new LinkedList<Customer>();
    private final Map<Integer, SubmitStation> recipeStations;
    private final UIOverlay overlay;
    private final int totalCustomers;
    private final int maxSpawnRate = 10000;
    private final float customerScale;
    final World world;
    private int completedOrders = 0;
    private Recipe[] possibleRecipes;

    private final Timer spawnTimer = new Timer(1000, false, true);
    private final Timer endlessTimer = new Timer(8000, false, true);
    // Separate random instances are used to not break existing tests relying on a
    // set permutation of orders.
    private final Random randomOrders;
    private final Random randomTextures;

    private int reputation = 3;

    private Map<Integer, Box2dLocation> objectives;
    private final List<Integer> objectiveIds = new ArrayList<>();
    private final Map<Integer, Boolean> objectiveAvailability = new HashMap<>();
    private List<Vector2> spawnLocations;
    private Stage stage;
    private Integer[] stationsRanked;

    private final String[] customerSprites = new String[] {
            "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Hitman 1/hitman1_hold.png",
            "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Hitman 2/hitman2_hold.png",
            "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Old/manOld_hold.png",
            "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Survivor 2/survivor2_hold.png",
            "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Survivor 1/survivor1_hold.png",
    };

    public CustomerManager(
            float customerScale,
            UIOverlay overlay,
            World world,
            int customers) {
        this.overlay = overlay;
        this.recipeStations = new HashMap<>();

        totalCustomers = customers;
        randomOrders = new Random();
        randomTextures = new Random();
        this.customerScale = customerScale;
        this.world = world;
    }

    /**
     * Initialise CustomerManager with an empty state and set random seed. This is
     * useful for testing.
     *
     * @param overlay   {@link UIOverlay} (probably mocked)
     * @param customers The total number of customers to spawn - 0 means endless
     * @param seed      seed for the {@link Random} instance to generate set orders
     */
    public CustomerManager(
            float customerScale,
            UIOverlay overlay,
            World world,
            int customers,
            long seed) {
        this(customerScale, overlay, world, customers);
        randomOrders.setSeed(seed);
    }

    /**
     * Reset the scenario to the default scenario.
     *
     * @param textureManager The manager of food textures that can be passed to the
     *                       recipes
     */
    public void init(
            FoodTextureManager textureManager,
            Stage stage,
            Map<Integer, Box2dLocation> objectives,
            List<Vector2> spawnLocations) {
        customerQueue.clear();

        possibleRecipes = new Recipe[] {
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

    public List<Recipe> getOrders() {
        LinkedList<Recipe> output = new LinkedList<>();
        for (Customer c : customerQueue) {
            output.add(c.getOrder());
        }
        return output;
    }

    public void act(float delta) {
        if (reputation == 0) {
            overlay.finishGameUI();
        }

        if (endlessTimer.getRunning()) {
            if (endlessTimer.tick(delta)) {
                spawnTimer.setDelay(Math.max(Math.round(spawnTimer.getDelay() * 0.95f), maxSpawnRate));
                Gdx.app.log(
                        "Changing spawnTimer delay",
                        spawnTimer.getDelay() + "");
            }
        }
        checkSpawn(delta);

        for (Customer customer : customerQueue) {
            customer.act(delta);
        }
    }

    public void checkSpawn(float delta) {
        if (spawnTimer.tick(delta)) {
            generateCustomer();
            overlay.updateOrders(getOrders());

            spawnTimer.reset();
        }
    }

    public void loseReputation() {
        if (reputation > 0) {
            reputation--;
            overlay.updateReputationCounter(reputation);
        }
    }

    // /**
    // * Check to see if the recipe matches the currently requested order.
    // *
    // * @param recipe The recipe to check against the current order.
    // * @return a boolean signifying if the recipe is correct.
    // */
    // public boolean checkRecipe(Recipe recipe, int stationID) {
    // if (customerQueue.isEmpty()) {
    // return false;
    // }

    // // could be changed to allow entering in any order, allowing you to do later
    // // recipes by checking with .contains and then getting first index.
    // return
    // recipe.getType().equals(recipeStations.get(stationID).customer.getOrder().getType());
    // }

    /**
     * Complete the current order nad move on to the next one. Then update the UI.
     * If all the recipes are completed, then show the winning UI.
     *
     * With the current implementation, it is possible to have endless mode use the
     * totalCustomers value of 0 without requiring changes
     */
    public void nextRecipe(Chef chef, Customer customer) {
        completedOrders++;
        customerQueue.remove(customer);
        customer.fulfillOrder();

        notifySubmitStations();
        // requires updating overlay to allow for multiple orders being displayed at
        // once
        overlay.updateOrders(getOrders());
        overlay.updateChefUI(chef);
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
        stationsRanked = recipeStations.keySet().toArray(new Integer[recipeStations.keySet().size()]);
        Arrays.sort(stationsRanked);
    }

    public void generateCustomer() {
        Integer customerObjective = findAvailableObjective();
        if (customerObjective != null) {
            // implement random generation of two or three customers at once here
            Texture texture = new Texture(
                    customerSprites[randomTextures.nextInt(
                            customerSprites.length - 1)]);
            Customer customer = new Customer(
                    texture,
                    new Vector2(
                            texture.getWidth() * customerScale,
                            texture.getHeight() * customerScale),
                    spawnLocations.get(0),
                    possibleRecipes[randomOrders.nextInt(4)],
                    this);
            customerQueue.addLast(customer);
            stage.addActor(customer);
            updateCustomerLocation(customer, customerObjective);
        }
    }

    private void updateCustomerLocation(Customer customer, Integer objective) {
        makeItGoThere(customer, objective);
        if (objective < 4) {
            recipeStations.get(stationsRanked[objective]).customer = customer;
        }
    }

    // public Recipe getFirstOrder() {
    // if (customerQueue.isEmpty())
    // return null;
    // return customerQueue.first().getOrder();
    // }

    /**
     * Give the customer an objective to go to.
     * 
     * @param locationID and id from objectives
     */
    private void makeItGoThere(Customer customer, int locationID) {
        objectiveAvailability.put(customer.currentObjective, true);

        Box2dLocation there = objectives.get(locationID);

        Arrive<Vector2> arrive = new Arrive<>(customer.steeringBody)
                .setTimeToTarget(3f)
                .setArrivalTolerance(0.1f)
                .setDecelerationRadius(2)
                .setTarget(there);

        Proximity<Vector2> proximity = new Box2dRadiusProximity(
                customer.steeringBody,
                world,
                0.5f);
        CollisionAvoidance<Vector2> collisionAvoidance = new CollisionAvoidance<>(customer.steeringBody, proximity);

        RaycastObstacleAvoidance<Vector2> wallAvoidance = new RaycastObstacleAvoidance<>(customer.steeringBody);
        wallAvoidance
                .setRayConfiguration(
                        new CentralRayWithWhiskersConfiguration<>(customer.steeringBody, 0.1f, 0.3f, 0.35f))
                .setRaycastCollisionDetector(new Box2dRaycastCollisionDetector(world))
                .setDistanceFromBoundary(locationID);

        BlendedSteering<Vector2> blendedAvoidance = new BlendedSteering<>(customer.steeringBody)
                .add(collisionAvoidance, 0.5f)
                .add(wallAvoidance, 0.5f);

        PrioritySteering<Vector2> prioritySteering = new PrioritySteering<>(
                customer.steeringBody)
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

    public Map<Integer, SubmitStation> getRecipeStations() {
        return recipeStations;
    }

    public Timer getTimer() {
        return spawnTimer;
    }

    public LinkedList<Customer> getCustomerQueue() {
        return customerQueue;
    }

    public int getReputation() {
        return reputation;
    }
}
