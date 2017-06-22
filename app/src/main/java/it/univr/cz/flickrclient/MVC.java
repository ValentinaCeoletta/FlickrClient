package it.univr.cz.flickrclient;

import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import it.univr.cz.flickrclient.controller.Controller;
import it.univr.cz.flickrclient.model.Model;
import it.univr.cz.flickrclient.view.View;

/**
 * MVC class: it contains model-view-controller triple and manage its.
 *
 * @Author Ceoletta Valentina
 * @Author Zenari Nicolo
 *
 * @Date June 2017
 */
public class MVC {
    /**
     * The model manages the data, logic and rules of the application.
     */
    public final Model model;

    /**
     * The controller accepts input and converts it to commands for the model or view
     */
    public final Controller controller;

    /**
     * List of View rappresents the output information
     */
    private final List<View> views = new CopyOnWriteArrayList<>();

    /**
     * Class constructor specifying model and contoller
     */
    public MVC(Model model, Controller controller) {
        this.model = model;
        this.controller = controller;

        model.setMVC(this);
        controller.setMVC(this);
    }

    /**
     * Registers the view
     * @param view View to register
     */
    public void register(View view) {
        views.add(view);
    }

    /**
     * Unregisters the view
     * @param view View to unregister
     */
    public void unregister(View view) {
        views.remove(view);
    }


    /**
     * Run a runnuble in the UI thread
     * @param task Task to execute
     */
    public void forEachView(ViewTask task) {
        new Handler(Looper.getMainLooper()).post(() -> {
            for (View view : views)
                task.process(view);
        });
    }

    /**
     * Interface aim to process a runnable
     */
    public interface ViewTask {
        void process(View view);
    }
}
