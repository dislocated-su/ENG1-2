package cs.eng1.piazzapanic.utility;

// Simple timer by @tedigc

// https://gist.github.com/tedigc/fe28616706025b00c6c540af4d03c827

/**
 * Simple timer class suitable for this project.
 * Modified for this project.
 *
 * @author tedigc
 */
public class Timer {

    private int delay;
    private int elapsed;
    private boolean running;
    private boolean looping;

    /**
     * Create a timer.
     *
     * @param delay   delay in milliseconds.
     * @param running whether the timer is running when it's created
     * @param looping does the timer restart itself after elapsing.
     */
    public Timer(int delay, boolean running, boolean looping) {
        this.delay = delay;
        this.running = running;
        this.looping = looping;
    }

    /**
     * Progress the timer
     *
     * @param delta time since last frame.
     * @return Whether the timer is finished.
     */
    public boolean tick(float delta) {
        if (running) {
            elapsed += delta * 1000;
            if (elapsed > delay) {
                elapsed -= looping ? delay : 0;
                return true;
            }
        }
        return false;
    }

    public void start() {
        this.running = true;
    }

    public void stop() {
        this.running = false;
    }

    /**
     * Reset the timer. This does not stop it, for that use {@code GdxTimer.stop()}
     */
    public void reset() {
        this.elapsed = 0;
    }

    public boolean getRunning() {
        return this.running;
    }
}
