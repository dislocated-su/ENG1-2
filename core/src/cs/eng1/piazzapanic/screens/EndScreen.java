package cs.eng1.piazzapanic.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import cs.eng1.piazzapanic.PiazzaPanicGame;
import cs.eng1.piazzapanic.ui.ButtonManager;
import cs.eng1.piazzapanic.ui.FontManager;

/**
 * Screen that shows at the end of the game, displaying whether the player won
 * or not as well as related stats.
 * Made for assessment 2.
 * 
 * @author Louis Warren
 * @author Joel Paxman
 */
public class EndScreen implements Screen {

    private final Table table;
    PiazzaPanicGame game;
    private final Stage stage;

    /**
     * @param game       Game to that the screen will be shown on.
     * @param text       Time elapsed since game start
     * @param reputation Reputation at the end (0 if lost)
     */
    public EndScreen(PiazzaPanicGame game, String text, String reputation) {
        ScreenViewport viewport = new ScreenViewport();
        stage = new Stage(viewport);

        int rep = Integer.parseInt(reputation.split(" ")[1]);

        this.game = game;
        table = new Table();
        table.setFillParent(true);
        table.setVisible(true);
        table.center();
        Pixmap bgPixmap = new Pixmap(1, 1, Pixmap.Format.RGB565);
        bgPixmap.setColor(Color.LIGHT_GRAY);
        bgPixmap.fill();

        TextureRegionDrawable textureRegionDrawableBg = new TextureRegionDrawable(new Texture(bgPixmap));
        table.setBackground(textureRegionDrawableBg);

        LabelStyle hudTitleFont = new Label.LabelStyle();
        FontManager fontManager = new FontManager();
        hudTitleFont.font = fontManager.getTitleFont();

        String title = "Game Over!";
        if (rep > 0) {
            title = "Congratulations!";
        }

        Label endText = new Label(title, hudTitleFont);

        Label time = new Label("You took " + text, hudTitleFont);

        Label repLabel = new Label(reputation, hudTitleFont);

        TextButton backButton = game.getButtonManager().createTextButton("Again?", ButtonManager.ButtonColour.GREY);
        backButton.addListener(
                new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        game.getModeSelectOverlay().hide();
                        game.loadHomeScreen();
                    }
                });

        table.add(endText).row();
        table.add(time).row();
        table.add(repLabel).row();
        table.add(backButton);
        stage.addActor(table);
    }

    public void show() {
        table.setVisible(true);
        Gdx.input.setInputProcessor(stage);
    }

    public void hide() {
        table.setVisible(false);
    }

    @Override
    public void render(float delta) {
        stage.act(delta);

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        this.stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
