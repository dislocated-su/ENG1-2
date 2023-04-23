package cs.eng1.piazzapanic.utility;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

public class KeyboardInput implements InputProcessor {

    public boolean left, right, up, down;

    public boolean changeCooks;

    public boolean disableHud;

    public void clearInputs() {
        left = false;
        right = false;
        up = false;
        down = false;
        changeCooks = false;
    }

    @Override
    public boolean keyDown(int keycode) {
        boolean processed = true;
        switch (keycode) {
            case Keys.LEFT:
            case Keys.A:
                left = true;
                break;
            case Keys.RIGHT:
            case Keys.D:
                right = true;
                break;
            case Keys.UP:
            case Keys.W:
                up = true;
                break;
            case Keys.DOWN:
            case Keys.S:
                down = true;
                break;
            case Keys.E:
                changeCooks = true;
                break;
            default:
                processed = false;
        }
        return processed;
    }

    @Override
    public boolean keyUp(int keycode) {
        boolean processed = true;
        switch (keycode) {
            case Keys.LEFT:
            case Keys.A:
                left = false;
                break;
            case Keys.RIGHT:
            case Keys.D:
                right = false;
                break;
            case Keys.UP:
            case Keys.W:
                up = false;
                break;
            case Keys.DOWN:
            case Keys.S:
                down = false;
                break;
            case Keys.E:
                changeCooks = false;
                break;
            default:
                processed = false;
        }
        return processed;
    }

    @Override
    public boolean keyTyped(char character) {
        boolean processed = true;
        switch (character) {
            default:
                processed = false;
        }
        return processed;
    }

    @Override
    public boolean touchDown(
        int screenX,
        int screenY,
        int pointer,
        int button
    ) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
