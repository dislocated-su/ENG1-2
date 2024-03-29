package cs.eng1.piazzapanic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import cs.eng1.piazzapanic.PiazzaPanicGame;

/**
 * PauseOverlay dispays a pause menu over the rendered world of the game.
 * The pause menu allows the player to save the game, exit to the game mode selection screen, or resume playing.
 *
 * @author Andrey Samoilov
 * @author Ross Holmes
 */
public class PauseOverlay {

    private final Table root;

    public boolean saving = false;

    public PauseOverlay(Stage stage, PiazzaPanicGame game, UIOverlay hud) {
        root = new Table();
        root.setFillParent(true);
        root.setVisible(false);
        root.pad(30f);
        // root.debug();

        LabelStyle pauseScreenStyle = new LabelStyle(game.getFontManager().getTitleFont(), Color.BLACK);
        Label pauseScreenTitle = new Label("Pause Menu", pauseScreenStyle);

        TextButton resumeButton = game.getButtonManager().createTextButton("Resume", ButtonManager.ButtonColour.GREY);
        TextButton saveButton = game.getButtonManager().createTextButton("Save Game", ButtonManager.ButtonColour.GREY);
        TextButton exitButton = game.getButtonManager().createTextButton("Exit Game", ButtonManager.ButtonColour.GREY);

        resumeButton.addListener(
            new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    hide();
                    hud.show();
                    hud.resume();
                }
            }
        );

        saveButton.addListener(
            new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.log("Save Triggered", "");
                    // SaveManager.getInstance().save();
                    saving = true;
                }
            }
        );

        exitButton.addListener(
            new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.loadHomeScreen();
                }
            }
        );

        root.add(pauseScreenTitle).padBottom(30f).row();
        root.add(resumeButton).pad(10f).row();
        root.add(saveButton).pad(10f).row();
        root.add(exitButton).pad(10f).row();

        stage.addActor(root);
    }

    public void show() {
        root.setVisible(true);
    }

    public void hide() {
        root.setVisible(false);
    }
}
