package it.univr.cz.flickrclient.view;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

import it.univr.cz.flickrclient.FlickrApplication;
import it.univr.cz.flickrclient.MVC;
import it.univr.cz.flickrclient.R;
import it.univr.cz.flickrclient.model.Action;
import it.univr.cz.flickrclient.model.FlickrPic;

/**
 * ImageFragment class: This fragment contains the clicked photo and its comments.
 *
 * @Author Ceoletta Valentina
 * @Author Zenari Nicolo
 *
 * @Date June 2017
 */
public class ImageFragment extends Fragment implements AbstractFragment {

    /**
     * Tag string of this class
     */
    private final static String TAG = ImageFragment.class.getName();
    /**
     * The position in the model LinkedList
     */
    private static int position;

    /**
     * Intent action
     */
    private static Action action;

    /**
     * The MVC triple
     */
    private MVC mvc;
    /**
     * ImageView to show
     */
    private ImageView image;
    /**
     * The image title
     */
    private TextView image_title;
    /**
     * ListView with comments
     */
    private ListView list_view;
    /**
     * Standard ArrayAdapter that shows photo comments
     */
    private ArrayAdapter<String> adapter;

    /**
     * Class constructor
     */
    public ImageFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        position = getArguments().getInt("position");
        action = Action.valueOf(getArguments().getString("action"));

        Log.d(TAG, "Position: " + position);
    }

    /**
     * Gets and assigns the graphic objects.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_fragment_layout, container, false);

        image = (ImageView) view.findViewById(R.id.image);
        image_title = (TextView) view.findViewById(R.id.image_title);
        list_view = (ListView) view.findViewById(R.id.list_view);


        // https://stackoverflow.com/questions/23260602/using-listview-in-a-scroll-view
        list_view.setOnTouchListener((view1, event) -> {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    view1.getParent().requestDisallowInterceptTouchEvent(true);
                    break;

                case MotionEvent.ACTION_UP:
                    view1.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }

            view1.onTouchEvent(event);
            return false;
        });

        mvc = ((FlickrApplication) getActivity().getApplicationContext()).getMVC();
        FlickrPic pic = mvc.model.getPictures(action)[position];

        image.setImageBitmap(pic.getImage());
        image_title.setText(pic.getTitle());

        //Log.d(TAG, "comments len: " + pic.getComments().length + " id: " + pic.getId());
        adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_1, pic.getComments());
        list_view.setAdapter(adapter);

        image.setOnClickListener(v -> {
            Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.image_zoom_layout);

            ImageView image1 = (ImageView)dialog.findViewById(R.id.image_zoom);
            image1.setImageBitmap(pic.getImage());
            image1.setOnClickListener(__ -> dialog.dismiss());

            Button close = (Button)dialog.findViewById(R.id.button_close);
            close.setBackgroundResource(R.drawable.ic_close_black_24dp);
            close.setOnClickListener(__ -> dialog.dismiss());
            dialog.show();
        });


        return view;
    }

    /**
     * Creates the activity and gets the mvc
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onModelChanged();
    }

    /**
     * Creates the menu
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.image_fragment_share, menu);
    }

    /**
     * Performs the menu item selection
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_share) {
            Uri uri = saveImage();

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, getContext().getContentResolver().getType(uri));

            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intent, "Share Image:"));

            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    /**
     * Stores the image into application cache directory
     * @return An Uri giving the photo location
     */
    private Uri saveImage() {
        Bitmap image = mvc.model.getPictures(action)[position].getImage();

        FileOutputStream stream = null;
        File file = null;

        try {
            File cachePath = new File(getContext().getCacheDir(), "images");
            cachePath.mkdirs();

            file = new File(cachePath, "image.png");
            stream = new FileOutputStream(file);

            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return FileProvider.getUriForFile(getContext(), "it.univr.cz.flickrclient", file);

    }

    /**
     * Called when the model changes
     */
    @Override
    public void onModelChanged() {
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onItemChanged() {
        // nothing
    }

}
