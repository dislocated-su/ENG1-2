package cs.eng1.piazzapanic.customer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Queue;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class CustomerManager {

    private final Queue<Customer> customerQueue;
    private final List<SubmitStation> recipeStations;
    private final UIOverlay overlay;
    private final int totalCustomers;
    private final float customerScale;
    final World world;
    private int completedOrders = 0;
    private Recipe[] possibleRecipes;
    private final Timer timer = new Timer(60000, false, true);
    // Separate random instances are used to not break existing tests relying on a set permutation of orders.
    private final Random randomOrders;
    private final Random randomTextures;
    private int reputation = 3;

    private Stage stage;

    private final String[] customerSprites = new String[] {
            "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Hitman 1/hitman1_hold.png",
            "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Hitman 2/hitman2_hold.png",
            "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Man Old/manOld_hold.png",
            "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Survivor 2/survivor2_hold.png",
            "Kenney-Game-Assets-2/2D assets/Topdown Shooter (620 assets)/PNG/Survivor 1/survivor1_hold.png"
    };

    public CustomerManager(float customerScale, UIOverlay overlay, World world, int customers) {
        this.overlay = overlay;
        this.recipeStations = new LinkedList<>();
        customerQueue = new Queue<>();
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
    public CustomerManager(float customerScale, UIOverlay overlay, World world, int customers, long seed) {
        this(customerScale, overlay, world, customers);
        randomOrders.setSeed(seed);
    }

    /**
     * Reset the scenario to the default scenario.
     *
     * @param textureManager The manager of food textures that can be passed to the
     *                       recipes
     */
    public void init(FoodTextureManager textureManager, Stage stage) {
        customerQueue.clear();

        possibleRecipes =
            new Recipe[] {
                new Burger(textureManager),
                new Salad(textureManager),
                new Pizza(textureManager),
                new JacketPotato(textureManager),
            };

        this.stage = stage;

        generateCustomer();

        timer.start();
    }

    public void act(float delta) {
        if (reputation == 0) {
            overlay.finishGameUI();
        }
        checkSpawn(delta);

        for (Customer customer : customerQueue) {
            customer.act(delta);
        }
    }

    public void checkSpawn(float delta) {
        if (timer.tick(delta)) {
            generateCustomer();
            overlay.updateRecipeUI(getFirstOrder());
            timer.reset();
        }
    }

    public void loseReputation() {
        reputation--;
    }

    /**
     * Check to see if the recipe matches the currently requested order.
     *
     * @param recipe The recipe to check against the current order.
     * @return a boolean signifying if the recipe is correct.
     */
    public boolean checkRecipe(Recipe recipe) {
        if (customerQueue.isEmpty()) {
            return false;
        }

        // could be changed to allow entering in any order, allowing you to do later
        // recipes by checking with .contains and then getting first index.
        return recipe.getType().equals(getFirstOrder().getType());
    }

    /**
     * Complete the current order nad move on to the next one. Then update the UI.
     * If all the recipes are completed, then show the winning UI.
     *
     * With the current implementation, it is possible to have endless mode use the
     * totalCustomers value of 0 without requiring changes
     */
    public void nextRecipe(Chef chef) {
        completedOrders++;
        overlay.updateRecipeCounter(completedOrders);
        customerQueue.first().fulfillOrder();
        customerQueue.removeFirst();

        notifySubmitStations();
        // requires updating overlay to allow for multiple orders being displayed at
        // once
        overlay.updateRecipeUI(getFirstOrder());
        overlay.updateChefUI(chef);
        if (completedOrders == totalCustomers) {
            timer.stop();
            overlay.updateRecipeUI(null);
            overlay.finishGameUI();
        }
    }

    /**
     * If one recipe station has been updated, let all the other ones know that
     * there is a new recipe to be built.
     */
    private void notifySubmitStations() {
        for (SubmitStation recipeStation : recipeStations) {
            recipeStation.updateOrderActions();
        }
    }

    public void addStation(SubmitStation station) {
        recipeStations.add(station);
    }

    public void generateCustomer() {
        // implement random generation of two or three customers at once here
        Texture texture = new Texture(customerSprites[randomTextures.nextInt(customerSprites.length - 1)]);
        Customer customer = new Customer(
                texture,
                new Vector2(texture.getWidth() * customerScale, texture.getHeight() * customerScale),
                possibleRecipes[randomOrders.nextInt(4)],
                this
        );
        customerQueue.addLast(
                customer
        );
        stage.addActor(customer);
    }

    public Recipe getFirstOrder() {
        if (customerQueue.isEmpty()) return null;
        return customerQueue.first().getOrder();
    }

    public List<SubmitStation> getRecipeStations() {
        return recipeStations;
    }

    public Timer getTimer() {
        return timer;
    }

    public Queue<Customer> getCustomerQueue() {
        return customerQueue;
    }

    public int getReputation() {
        return reputation;
    }
}
