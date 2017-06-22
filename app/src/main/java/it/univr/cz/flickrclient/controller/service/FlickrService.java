package it.univr.cz.flickrclient.controller.service;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import it.univr.cz.flickrclient.FlickrApplication;
import it.univr.cz.flickrclient.MVC;
import it.univr.cz.flickrclient.model.Action;
import it.univr.cz.flickrclient.model.FlickrPic;
import it.univr.cz.flickrclient.view.View;

/**
 * FlickrService class: creates the service to download information and image
 *
 * @Author Ceoletta Valentina
 * @Author Zenari Nicolo
 *
 * @Date June 2017
 */

public class FlickrService  extends ServiceExecutor {

    /**
     * Tag string of this class
     */
    private final static String TAG = FlickrService.class.getName();

    /**
     * The parameter of download photos information
     */
    private final static String PARAM_DL_INFO = "searchString";

    /**
     * The parameter of download photos
     */
    private final static String PARAM_DL_IMAGE = "urlImage";

    /**
     * The parameter of download a specific photo
     */
    private final static String PARAM_DL_IMAGE_POSITION = "positionImage";

    /**
     * The parameter of download last user photos
     */
    private final static String PARAM_DL_USER_PHOTO = "ownerId";


    /**
     * A part of URI that invokes Flickr API
     */
    private final static String API_FLICKR = "https://api.flickr.com/services/rest?method=";

    /**
     * A part of URI that invokes Flickr API method to download photos
     */
    private final static String API_FLICKR_SEARCH_PHOTO = "flickr.photos.search&extras=url_z,url_s&per_page=50&text=";

    /**
     * A part of URI that invokes Flickr API method to download recent photos
     */
    private final static String API_FLICKR_RECENT_PHOTO = "flickr.photos.getRecent&extras=url_z,url_s&per_page=50";

    /**
     * A part of URI that invokes Flickr API method to download popular photos
     */
    private final static String API_FLICKR_POPULAR_PHOTO = "flickr.interestingness.getList&extras=url_z,url_s&per_page=50";

    /**
     * A part of URI that invokes Flickr API method to download last user photos
     */
    private final static String API_FLICKR_USER_PHOTO = "flickr.photos.search&extras=url_z,url_s&per_page=50&user_id=";

    /**
     * The API key to granted access to Flickr API
     */
    private final static String API_KEY = "&api_key=be4922ffb4ded82d452af0477842bdba";

    /**
     * The MVc triple
     */
    private static MVC mvc;


    /**
     * Class constructor
     */
    public FlickrService() {
        super("flickr service");
    }


    /**
     * Creates and starts an intent specifying the action and setting parameters
     * @param context application context
     * @param searchText the text to search
     */
    @UiThread
    public static void downloadInfo(Context context, String searchText) {
        Log.d(TAG, "downloadInfo method");
        Intent intent = new Intent(context, FlickrService.class);
        intent.setAction(Action.ACTION_DL_INFO.toString());
        intent.putExtra(PARAM_DL_INFO, searchText);
        context.startService(intent);
    }

    /**
     * Creates and starts an intent specifying the action and setting parameters
     * @param context application context
     * @param urlImage an URL giving the location of the photo
     * @param position photo position
     */
    @UiThread
    public static void downloadImage(Context context, String urlImage, int position) {
        Intent intent = new Intent(context, FlickrService.class);
        intent.setAction(Action.ACTION_DL_IMAGE.toString());
        intent.putExtra(PARAM_DL_IMAGE, urlImage);
        intent.putExtra(PARAM_DL_IMAGE_POSITION, position);
        context.startService(intent);
    }

    /**
     * Creates and starts an intent specifying the action and setting parameters
     * @param context application context
     */
    @UiThread
    public static void downloadRecentPhoto(Context context) {
        Intent intent = new Intent(context, FlickrService.class);
        intent.setAction(Action.ACTION_DL_RECENT_PHOTO.toString());
        context.startService(intent);
    }

    /**
     * Creates and starts an intent specifying the action and setting parameters
     * @param context application context
     */
    @UiThread
    public static void downloadPopularPhoto(Context context) {
        Intent intent = new Intent(context, FlickrService.class);
        intent.setAction(Action.ACTION_DL_POPULAR_PHOTO.toString());
        context.startService(intent);
    }

    /**
     * Creates and starts an intent specifying the action and setting parameters
     * @param context application context
     * @param ownerId the string represent the owner ID
     */
    @UiThread
    public static void downloadUserPhoto(Context context, String ownerId) {
        Intent intent = new Intent(context, FlickrService.class);
        intent.setAction(Action.ACTION_DL_USER_PHOTO.toString());
        intent.putExtra(PARAM_DL_USER_PHOTO, ownerId);
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
    @WorkerThread
    protected void onHandleIntent(@Nullable Intent intent) {

        mvc = ((FlickrApplication) getApplication()).getMVC();

        Action action = Action.valueOf(intent.getAction());

        switch (action) {

            case ACTION_DL_INFO:
            case ACTION_DL_RECENT_PHOTO:
            case ACTION_DL_POPULAR_PHOTO: {
                Log.d(TAG, "onHandleIntent: ACTION_DL_INFO");

                mvc.model.clearList(action);
                parseInfo(downloadXML(intent), action);

                // change fragment
                mvc.forEachView(View::showImageResult);

                // set adapter
                mvc.forEachView(View::onModelChanged);

            }
            break;

            case ACTION_DL_USER_PHOTO: {
                Log.d(TAG, "onHandleIntent: ACTION_DL_INFO");

                mvc.model.clearList(action);
                parseInfo(downloadXML(intent), action);

                // change fragment
                mvc.forEachView(View::showUserImageResult);

                // set adapter
                mvc.forEachView(View::onModelChanged);
            }
            break;

            case ACTION_DL_IMAGE: {
                Log.d(TAG, "onHandleIntent: ACTION_DL_IMAGE");

                String urlImage = intent.getStringExtra(PARAM_DL_IMAGE);
                int position = intent.getIntExtra(PARAM_DL_IMAGE_POSITION, 0);

                FlickrPic picture = mvc.model.getPictures(action)[position];
                picture.setImage(extractImage(urlImage));

                // change fragment
                mvc.forEachView(view -> view.showImage(position, action));
            }

            break;
        }

    }


    /**
     * Downloads and returns the photos information from Flickr website as a XML page
     * @param intent the intent
     * @return String XML page
     */
    private String downloadXML(Intent intent) {

        URL url = null;

        Action action = Action.valueOf(intent.getAction());

        try {
            switch (action) {
                case ACTION_DL_INFO:
                    url = new URL(API_FLICKR + API_FLICKR_SEARCH_PHOTO + intent.getStringExtra(PARAM_DL_INFO) + API_KEY);
                    break;
                case ACTION_DL_RECENT_PHOTO:
                    url = new URL(API_FLICKR + API_FLICKR_RECENT_PHOTO + API_KEY);
                    break;
                case ACTION_DL_POPULAR_PHOTO:
                    url = new URL(API_FLICKR + API_FLICKR_POPULAR_PHOTO + API_KEY);
                    break;
                case ACTION_DL_USER_PHOTO:
                    url = new URL(API_FLICKR + API_FLICKR_USER_PHOTO + intent.getStringExtra(PARAM_DL_USER_PHOTO) + API_KEY);
                    break;

            }
        } catch (MalformedURLException e) {
            return "";
        }


        try {
            URLConnection conn = url.openConnection();
            String answer = "";
            BufferedReader in = null;

            try {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String line;
                while ((line = in.readLine()) != null)
                    answer += line + "\n";
            } finally {
                if (in != null)
                    in.close();
            }

            return answer;

        } catch (IOException e) {
            return "";
        }
    }


    /**
     * Parses photos information of the XML page return from downloadXML method
     * @param XML XML page
     * @param action intent action
     */
    private void parseInfo(String XML, Action action) {

        int count = -1;
        int nextPhoto = -1;

        do {
            nextPhoto = XML.indexOf("<photo id", nextPhoto + 1);
            if (nextPhoto >= 0) {
                int photo_idPos = XML.indexOf("id=", nextPhoto) + 4;
                int owner_idPos = XML.indexOf("owner=", nextPhoto) + 7;
                int titlePos = XML.indexOf("title=", nextPhoto) + 7;
                int url_zPos = XML.indexOf("url_z=", nextPhoto) + 7;
                int url_sPos = XML.indexOf("url_s=", nextPhoto) + 7;
                String id = XML.substring(photo_idPos, XML.indexOf("\"", photo_idPos + 1));
                String owner_id = XML.substring(owner_idPos, XML.indexOf("\"", owner_idPos + 1));
                String title = XML.substring(titlePos, XML.indexOf("\"", titlePos + 1));
                String url_image = XML.substring(url_zPos, XML.indexOf("\"", url_zPos + 1));
                String url_preview = XML.substring(url_sPos, XML.indexOf("\"", url_sPos + 1));

                mvc.model.storePicture(new FlickrPic(url_image, url_preview, null, null, title, owner_id, id), ++count, action);

                PreviewService.downloadPreview(this, count, action);
                CommentService.downloadComments(this, id, action);
            }
        }
        while (nextPhoto != -1);
    }

    /**
     * Downloads and returns the photo at the specified url
     * @param url an URL giving the location of the photo
     * @return the photo at specified url
     */
    private Bitmap extractImage(String url) {
        //Log.d(TAG, "extractImage url " + url);
        URL image_url;

        try {
            image_url = new URL(url);
            return BitmapFactory.decodeStream(image_url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
