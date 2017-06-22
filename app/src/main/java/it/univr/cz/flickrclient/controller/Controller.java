package it.univr.cz.flickrclient.controller;


import android.content.Context;
import android.support.annotation.UiThread;
import android.util.Log;

import it.univr.cz.flickrclient.MVC;
import it.univr.cz.flickrclient.controller.service.FlickrService;

/**
 * Controller class: accepts input and converts it to commands for the model or view
 *
 * @Author Ceoletta Valentina
 * @Author Zenari Nicolo
 *
 * @Date June 2017
 */
public class Controller {

    /**
     * Tag string of this class
     */
    private final static String TAG = Controller.class.getName();

    /**
     * The MVC triple
     */
    private MVC mvc;

    /**
     * Registers the mvc triple to this class
     * @param mvc MVc triple
     */
    public void setMVC(MVC mvc) {
        this.mvc = mvc;
    }

    /**
     * Invokes the FlickrService to download photos information
     * @param context application context
     * @param searchText the text to search
     */
    @UiThread
    public void searchString(Context context, String searchText) {
        Log.d(TAG, "searchString method");
        FlickrService.downloadInfo(context, searchText);
    }

    /**
     * Invokes the FlickrService to download recent photos
     * @param context application context
     */
    @UiThread
    public void searchRecent(Context context) {
        Log.d(TAG, "searchRecent method");
        FlickrService.downloadRecentPhoto(context);
    }

    /**
     * Invokes the FlickrService to download popular photos
     * @param context application context
     */
    @UiThread
    public void searchPopular(Context context) {
        Log.d(TAG, "searchPopular method");
        FlickrService.downloadPopularPhoto(context);
    }

    /**
     * Invokes the FlickrService to download the photos at the specified url
     * @param context application context
     * @param url the URL giving the location of the photo
     * @param photoId the photo id
     * @param position the photo position in the model LinkedList
     */
    @UiThread
    public void downloadImage(Context context, String url, String photoId, int position) {
        Log.d(TAG, "downloadImage method");
        FlickrService.downloadImage(context, url, position);
    }

    /**
     * Invokes the FlickrService to download last user photos
     * @param context application context
     * @param ownerId the owner id
     */
    @UiThread
    public void downloadUserPhoto(Context context, String ownerId){
        Log.d(TAG, "downloadUserPhoto method");
        FlickrService.downloadUserPhoto(context, ownerId);
    }
}
