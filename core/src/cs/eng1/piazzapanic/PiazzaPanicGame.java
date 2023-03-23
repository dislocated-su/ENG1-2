package cs.eng1.piazzapanic;

import com.badlogic.gdx.Game;
import cs.eng1.piazzapanic.screens.GameScreen;
import cs.eng1.piazzapanic.screens.HomeScreen;
import cs.eng1.piazzapanic.ui.ButtonManager;
import cs.eng1.piazzapanic.ui.FontManager;
import cs.eng1.piazzapanic.ui.ModeSelectOverlay;
import cs.eng1.piazzapanic.ui.SettingsOverlay;
import cs.eng1.piazzapanic.ui.TutorialOverlay;

public class PiazzaPanicGame extends Game {

    private FontManager fontManager;
    private ButtonManager buttonManager;
    private GameScreen gameScreen;
    private HomeScreen homeScreen;
    private TutorialOverlay tutorialOverlay;
    private SettingsOverlay settingsOverlay;
    private ModeSelectOverlay modeSelect;

    @Override
    public void create() {
        fontManager = new FontManager();
        buttonManager = new ButtonManager(fontManager);
        tutorialOverlay = new TutorialOverlay(this);
        settingsOverlay = new SettingsOverlay(this);
        modeSelect = new ModeSelectOverlay(this);
        loadHomeScreen();
    }

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

    public void loadHomeScreen() {
        if (homeScreen == null) {
            homeScreen = new HomeScreen(this);
        }
        setScreen(homeScreen);
    }

    public void loadGameScreen(int customers) {
        setScreen(new GameScreen(this, customers));
    }

    public TutorialOverlay getTutorialOverlay() {
        return tutorialOverlay;
    }

    public SettingsOverlay getSettingsOverlay() {
        return settingsOverlay;
    }

    public ModeSelectOverlay getModeSelectOverlay() {
        return modeSelect;
    }

    public FontManager getFontManager() {
        return fontManager;
    }

    public ButtonManager getButtonManager() {
        return buttonManager;
    }
}
