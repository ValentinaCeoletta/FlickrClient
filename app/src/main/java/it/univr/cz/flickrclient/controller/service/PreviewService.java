package it.univr.cz.flickrclient.controller.service;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.univr.cz.flickrclient.FlickrApplication;
import it.univr.cz.flickrclient.MVC;
import it.univr.cz.flickrclient.model.Action;
import it.univr.cz.flickrclient.model.FlickrPic;
import it.univr.cz.flickrclient.view.View;

/**
 * PreviewService class: creates the service to download preview photos
 *
 * @Author Ceoletta Valentina
 * @Author Zenari Nicolo
 *
 * @Date June 2017
 */
public class PreviewService extends ServiceExecutor {

    /**
     * Tag string of this class
     */
    private final static String TAG = PreviewService.class.getName();

    /**
     * The parameter of download preview photos intent
     */
    private final static String PARAM_DL_PREVIEW_INDEX = "indexPreview";

    /**
     * The MVC triple
     */
    private MVC mvc;


    /**
     * Class constructor
     */
    public PreviewService() {
        super("Download image service");
    }

    /**
     * Creates and starts an intent specifying the action and setting parameters
     * @param context application context
     * @param index photo position in the model LinkedList
     * @param action intent action
     */
    @UiThread
    public static void downloadPreview(Context context, int index, Action action) {
        Log.d(TAG, "downloadInfo preview");
        Intent intent = new Intent(context, PreviewService.class);
        intent.setAction(action.toString());
        intent.putExtra(PARAM_DL_PREVIEW_INDEX, index);
        context.startService(intent);
    }

    /**
     * Creates an Executor Service
     * @return a newCacheThreadPool
     */
    @Override
    protected ExecutorService mkExecutorService() {
        return Executors.newCachedThreadPool();
    }


    /**
     * Manages the intent passed to startService
     * @param intent the value passed to startService
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        mvc = ((FlickrApplication) getApplication()).getMVC();

        Log.d(TAG, "onHandleIntent: ACTION_DL_PREVIEW");

        int index = intent.getIntExtra(PARAM_DL_PREVIEW_INDEX, 0);

        Action action = Action.valueOf(intent.getAction());

        FlickrPic preview = mvc.model.getPictures(action)[index];
        preview.setPreview(extractImage(preview.getUrlPreview()));

        // update listview adapter
        mvc.forEachView(View::onItemChanged);
    }

    /**
     * Downloads and returns the photo at the specified url
     * @param url an URL giving the location of the photo
     * @return the photo at specified url
     */
    private Bitmap extractImage(String url) {
        Log.d(TAG, "extractImage");

        URL image_url = null;
        try {
            image_url = new URL(url);
            return BitmapFactory.decodeStream(image_url.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
