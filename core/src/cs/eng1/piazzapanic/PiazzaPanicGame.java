package cs.eng1.piazzapanic;

import com.badlogic.gdx.Game;
import cs.eng1.piazzapanic.screens.GameScreen;
import cs.eng1.piazzapanic.screens.HomeScreen;
import cs.eng1.piazzapanic.ui.ButtonManager;
import cs.eng1.piazzapanic.ui.FontManager;
import cs.eng1.piazzapanic.ui.ModeSelectOverlay;
import cs.eng1.piazzapanic.ui.SettingsOverlay;
import cs.eng1.piazzapanic.ui.TutorialOverlay;
import cs.eng1.piazzapanic.ui.UpgradesUi;
import cs.eng1.piazzapanic.utility.saving.SaveState;

public class PiazzaPanicGame extends Game {

    private FontManager fontManager;
    private ButtonManager buttonManager;
    private GameScreen gameScreen;
    private HomeScreen homeScreen;
    private TutorialOverlay tutorialOverlay;
    private SettingsOverlay settingsOverlay;
    private ModeSelectOverlay modeSelect;

    /**
     * Creates new instances of all fields, and loads HomeScreen
     */
    @Override
    public void create() {
        fontManager = new FontManager();
        buttonManager = new ButtonManager(fontManager);
        tutorialOverlay = new TutorialOverlay(this);
        settingsOverlay = new SettingsOverlay(this);
        modeSelect = new ModeSelectOverlay(this);
        loadHomeScreen();
    }

    /**
     * Disposes of GameScreen, HomeScreen, FontManager and ButtonManager
     */
    @Override
    public void dispose() {
        if (gameScreen != null) {
            gameScreen.dispose();
        }
        if (homeScreen != null) {
            homeScreen.dispose();
        }
        fontManager.dispose();
        buttonManager.dispose();
    }

    /**
     * Sets the current screen as HomeScreen, creating a new instance of it if one
     * did not already exist
     */
    public void loadHomeScreen() {
        if (homeScreen == null) {
            homeScreen = new HomeScreen(this);
        }
        setScreen(homeScreen);
    }

    /**
     * Sets the current screen as the GameScreen
     *
     * @param customers,  the user selected number of customers (1+ for scenario
     *                    mode, 0 for endless)
     * @param difficulty, the user selected difficulty
     */
    public void loadGameScreen(int customers, int difficulty) {
        setScreen(new GameScreen(this, customers, difficulty));
    }

    public void loadGameScreen(SaveState save) {
        setScreen(new GameScreen(this, save));
    }

    /**
     * @returns single instance of TutorialOverlay
     */
    public TutorialOverlay getTutorialOverlay() {
        return tutorialOverlay;
    }

    /**
     * @returns single instance of SettingsOverlay
     */
    public SettingsOverlay getSettingsOverlay() {
        return settingsOverlay;
    }

    /**
     * @returns single isntance of ModeSelectOverlay
     */
    public ModeSelectOverlay getModeSelectOverlay() {
        return modeSelect;
    }

    /**
     * @returns single instance of FontManager
     */
    public FontManager getFontManager() {
        return fontManager;
    }

    /**
     * @returns single instance of ButtonManager
     */
    public ButtonManager getButtonManager() {
        return buttonManager;
    }
}
