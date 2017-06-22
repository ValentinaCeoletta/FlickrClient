package it.univr.cz.flickrclient.view;


import android.support.annotation.UiThread;

import it.univr.cz.flickrclient.model.Action;

/**
 * View Intargace: Interface aims to declare methods to interact with view.
 * All the methods must be thread safe.
 *
 * @Author Ceoletta Valentina
 * @Author Zenari Nicolo
 *
 * @Date June 2017
 */
public interface View {

    /**
     * Called when the model changes.
     */
    @UiThread
    void onModelChanged();

    /**
     * Called to show the search result.
     */
    @UiThread
    void showImageResult();


    /**
     * Called to show the last user photos search result
     */
    @UiThread
    void showUserImageResult();

    /**
     * Called to show a single image.
     * @param position
     * @param action intent action
     */
    @UiThread
    void showImage(int position, Action action);

    /**
     * Called when a showed item changes.
     */
    @UiThread
    void onItemChanged();
}
