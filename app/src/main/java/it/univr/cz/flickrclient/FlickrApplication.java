package it.univr.cz.flickrclient;

import android.app.Application;

import it.univr.cz.flickrclient.controller.Controller;
import it.univr.cz.flickrclient.model.Model;

/**
 * FlickrApplication class: it sets the mvc triple for this application.
 *
 * @Author Ceoletta Valentina
 * @Author Zenari Nicolo
 *
 * @Date June 2017
 */

public class FlickrApplication extends Application {

    /**
     * The MVC triple
     */
    private MVC mvc;

    /**
     * Application creator called when the application is starting.
     */
    @Override
    public void onCreate() {
        super.onCreate();

        mvc = new MVC(new Model(), new Controller());
    }

    /**
     * Returns the MVC
     * @return MVC
     */
    public MVC getMVC() {
        return mvc;
    }
}
