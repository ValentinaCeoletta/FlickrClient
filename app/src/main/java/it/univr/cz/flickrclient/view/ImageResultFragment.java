package it.univr.cz.flickrclient.view;

import android.app.ListFragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
 * ImageResultFragment class: This fragment contains the search result list.
 *
 * @Author Ceoletta Valentina
 * @Author Zenari Nicolo
 *
 * @Date June 2017
 */
public class ImageResultFragment extends ListFragment implements AbstractFragment {

    /**
     * Tag string of this class
     */
    private final static String TAG = ImageResultFragment.class.getName();
    /**
     * The MVC triple
     */
    private MVC mvc;
    /**
     * The custom image adapter that shows photos list
     */
    private ImageAdapter imageAdapter;

    /**
     * Creates the activity and gets the mvc
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mvc = ((FlickrApplication) getActivity().getApplicationContext()).getMVC();

        imageAdapter = new ImageAdapter();

        registerForContextMenu(getListView());

        onModelChanged();
    }

    /**
     * Performs the click on an item
     * @param l ListView
     * @param v View
     * @param position Position
     * @param id ID
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d(TAG, "onListItemClick");

        FlickrPic picture = (mvc.model.getPictures(Action.IMAGE_RESULT_FRAGMENT))[position];

        mvc.controller.downloadImage(getContext(), picture.getUrlImage(), picture.getId(), position);
    }

    /**
     * Called when the model changes
     */
    @Override
    public void onModelChanged() {
        setListAdapter(imageAdapter);
    }

    /**
     * Called when an list' item changes
     */
    @Override
    public void onItemChanged() { //aggiunto
        imageAdapter.notifyDataSetChanged();
    }

    /**
     * Creates the context menu
     * @param menu ContextMenu
     * @param v View
     * @param menuInfo ContextmenuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.image_result_fragment_context, menu);
    }

    /**
     * Performs the click on an item context menu
     * @param item
     * @return boolean
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle()==getResources().getString(R.string.title_share)){

            int position = ((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
            Uri uri = saveImage(position);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(uri, getContext().getContentResolver().getType(uri));

            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intent, "Share Image:"));

            return true;

        }
        else if (item.getTitle()==getResources().getString(R.string.title_lastUserPhoto)){

            int position = ((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
            String ownerId = mvc.model.getPictures(Action.IMAGE_RESULT_FRAGMENT)[position].getOwnerId();

            Log.d(TAG, "OwnerID= " + ownerId);

            mvc.controller.downloadUserPhoto(getContext(), ownerId);

            return true;
        }
        else
            return super.onContextItemSelected(item);
    }

    /**
     * Stores the image into application cache directory
     * @param position The position in the model LinkedList
     * @return An Uri giving the photo location
     */
    private Uri saveImage(int position) {
        Bitmap image = mvc.model.getPictures(Action.IMAGE_RESULT_FRAGMENT)[position].getImage();

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
     * ImageAdapter class: Represents the custom image adapter that shows photos list
     */
    private class ImageAdapter extends ArrayAdapter<FlickrPic> {

        private final FlickrPic[] pictures = mvc.model.getPictures(Action.IMAGE_RESULT_FRAGMENT);

        private ImageAdapter() {
            super(getActivity(), R.layout.image_item_layout, mvc.model.getPictures(Action.IMAGE_RESULT_FRAGMENT));
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;

            // optimization for item
            if (row == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                row = inflater.inflate(R.layout.image_item_layout, parent, false);
            }

            FlickrPic pic = pictures[position];



            ((ImageView) row.findViewById(R.id.icon_item)).setImageBitmap(pic.getPreview());
            ((TextView) row.findViewById(R.id.title_item)).setText(pic.getTitle());

            return row;
        }
    }
}
