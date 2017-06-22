package it.univr.cz.flickrclient.view;

import android.support.annotation.UiThread;

/**
 * AbstractFragment class: interface aims to declare methods to interact with fragment
 * All method must be thread safe.
 *
 * @Author Ceoletta Valentina
 * @Author Zenari Nicolo
 *
 * @Date June 2017
 */
public interface AbstractFragment {

    /**
     * Called when model changes
     */
    @UiThread
    void onModelChanged();

    /**
     * Called when a showed item changes
     */
    @UiThread
    void onItemChanged();
}
