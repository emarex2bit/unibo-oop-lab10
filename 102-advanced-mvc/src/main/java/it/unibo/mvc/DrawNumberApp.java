package it.unibo.mvc;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

/**
 */
public final class DrawNumberApp implements DrawNumberViewObserver {

    private final DrawNumber model;
    private final List<DrawNumberView> views;

    /**
     * @param views
     *            the views to attach
     */
    public DrawNumberApp(final DrawNumberView... views) {
        /*
         * Side-effect proof
         */
        this.views = Arrays.asList(Arrays.copyOf(views, views.length));
        for (final DrawNumberView view: views) {
            view.setObserver(this);
            view.start();
        }
        Configuration configuration = ConfigLoader.load("/config.yml");
        this.model = new DrawNumberImpl(configuration.getMin(), configuration.getMax(), configuration.getAttempts());
    }

    @Override
    public void newAttempt(final int n) {
        try {
            final DrawResult result = model.attempt(n);
            for (final DrawNumberView view: views) {
                view.result(result);
            }
        } catch (final IllegalArgumentException e) {
            for (final DrawNumberView view: views) {
                view.numberIncorrect();
            }
        }
    }

    @Override
    public void resetGame() {
        this.model.reset();
    }

    @Override
    @SuppressFBWarnings(
        value = "DM_EXIT",
        justification = "Acceptable for exercising purposes."
    )
    public void quit() {
        /*
         * A bit harsh. A good application should configure the graphics to exit by
         * natural termination when closing is hit. To do things more cleanly, attention
         * should be paid to alive threads, as the application would continue to persist
         * until the last thread terminates.
         */
        System.exit(0);
    }

    /**
     * @param args
     *            ignored
     * @throws FileNotFoundException if the configuration file cannot be fetched
     */
    public static void main(final String... args) throws FileNotFoundException {
        new DrawNumberApp(new DrawNumberViewImpl(), new DrawNumberViewImpl(), new PrintStreamView(System.out));
    }

}
