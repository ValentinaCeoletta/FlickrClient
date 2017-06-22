package it.univr.cz.flickrclient.controller.service;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import it.univr.cz.flickrclient.FlickrApplication;
import it.univr.cz.flickrclient.MVC;
import it.univr.cz.flickrclient.R;
import it.univr.cz.flickrclient.model.Action;
import it.univr.cz.flickrclient.model.FlickrPic;

/**
 * CommentService class: creates the service to download comments
 *
 * @Author Ceoletta Valentina
 * @Author Zenari Nicolo
 *
 * @Date June 2017
 */

public class CommentService extends ServiceExecutor {

    /**
     * Tag string of this class
     */
    private final static String TAG = CommentService.class.getName();

    /**
     * The parameter of download comments intent
     */
    private final static String PARAM_DL_COMMENTS = "photoId";

    /**
     * A part of URI that invokes Flickr API method to download the list of comments
     */
    private final static String API_FLICKR_COMMENTS = "flickr.photos.comments.getList&extras=url_z,url_s&per_page=50&photo_id=";

    /**
     * The API key to granted access to Flickr API
     */
    private final static String API_KEY = "&api_key=be4922ffb4ded82d452af0477842bdba";

    /**
     * A part of URI that invokes Flickr API
     */
    private final static String API_FLICKR = "https://api.flickr.com/services/rest?method=";

    /**
     * The MVC triple
     */
    private MVC mvc;

    /**
     * Class constructor
     */
    public CommentService(){
        super("Download comments");
    }


    /**
     * Creates and starts an intent specifying the action and setting parameters
     * @param context application context
     * @param photoId the string represent photo id
     * @param action intent action
     */
    @UiThread
    public static void downloadComments(Context context, String photoId, Action action) {
        Intent intent = new Intent(context, CommentService.class);
        intent.setAction(action.toString());
        intent.putExtra(PARAM_DL_COMMENTS, photoId);
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

        Log.d(TAG, "onHandleIntent: ACTION_DL_COMMENTS");

        Action action = Action.valueOf(intent.getAction());

        String id = intent.getStringExtra(PARAM_DL_COMMENTS);
        FlickrPic picture = mvc.model.getPictures(action)[mvc.model.getPicturePosition(id, action)];

        picture.addComments(parseComments(downloadXML(intent)));

        //mvc.forEachView(View::onModelChanged);
    }

    /**
     * Downloads and returns the photos information from Flickr website as a XML page
     * @param intent the value passed to startService
     * @return the XML page
     */
    private String downloadXML(Intent intent) {

        URL url = null;

        try {
            url = new URL(API_FLICKR + API_FLICKR_COMMENTS + intent.getStringExtra(PARAM_DL_COMMENTS) + API_KEY);
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
     * Parses comments of the XML page return from downloadXML method
     * @param XML XML page
     * @return Iterable of comments representing as string
     */
    private Iterable<String> parseComments(String XML) {

        LinkedList<String> list = new LinkedList<>();

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Carichiamo il nostro documento da un file (assicuratevi sia nel path giusto)
            Document doc = builder.parse(new InputSource(new StringReader(XML)));

            // prendiamo il primo nodo
            Node root = doc.getFirstChild();

            for (int i = 0; i < root.getChildNodes().getLength(); i++) {
                Node comments = root.getChildNodes().item(i);
                for (int j=0; j<comments.getChildNodes().getLength(); j++){
                    Node comment = comments.getChildNodes().item(j);
                    if (comment.getNodeType() == Node.ELEMENT_NODE) {
                        //Log.d(TAG, "commento: "+comment.getTextContent());

                        list.add(new String(clean(comment.getTextContent())));
                    }
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException pce){
            pce.printStackTrace();
        }

        if(list.size() == 0)
            list.add(new String(getResources().getString(R.string.comments)));
        Log.d(TAG, "parseComments - size: " + list.size());
        return list;

    }

    /**
     * Cleans comments from html references
     * @param comment string of comment
     * @return clean comment
     */
    private String clean(String comment){
        String result = "";

        // remove link <a href ...>
        result = comment.replaceAll("<.*?>","");
        if ( ! (comment.equalsIgnoreCase(result))){
            Log.d("REPLACE ALL", "comment: " + comment + " - result " + result);
        }


        return result;
    }
}
