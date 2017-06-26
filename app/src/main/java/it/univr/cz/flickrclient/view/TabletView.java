package it.univr.cz.flickrclient.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Toast;

import it.univr.cz.flickrclient.FlickrApplication;
import it.univr.cz.flickrclient.MVC;
import it.univr.cz.flickrclient.R;
import it.univr.cz.flickrclient.model.Action;


/**
 * TabletView class: The tablet view is show when the app is running on a tablet.
 * The left fragment is static and the right fragment is dynamic.
 * It extends a Linear Layout and implements View interface.
 *
 * @Author Ceoletta Valentina
 * @Author Zenari Nicolo
 *
 * @Date June 2017
 */
public class TabletView extends LinearLayout implements View {

    /**
     * The MVC triple.
     */
    private MVC mvc;

    /**
     * Class constructor
     * @param context Application context
     */
    public TabletView(Context context) {
        super(context);
    }

    /**
     * Class constructos
     * @param context Application context
     * @param attrs Attribute set
     */
    public TabletView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Called to attach a fragment to a window
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mvc = ((FlickrApplication) getContext().getApplicationContext()).getMVC();
        mvc.register(this);

        if (getFragment() == null) {
            getFragmentManager().beginTransaction().add(R.id.tablet_right_view, new ImageResultFragment()).commit();
        }
    }

    /**
     * Called to dettach a fragment from a window
     */
    @Override
    protected void onDetachedFromWindow() {
        mvc.unregister(this);
        super.onDetachedFromWindow();
    }

    /**
     * Called when the model changes
     */
    @Override
    public void onModelChanged() {
        getSearchFragment().onModelChanged();
        getFragment().onModelChanged();
    }

    /**
     * Called to show the fragment with search result
     */
    @Override
    public void showImageResult() {
        if (mvc.model.isEmpty()) {
            Toast.makeText(getContext(), new String(getResources().getString(R.string.no_photos)), Toast.LENGTH_SHORT).show();
        } else {
            getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.tablet_right_view, new ImageResultFragment()).commit();
        }
    }

    /**
     * Called to shoe the fragment with last user photos search result
     */
    @Override
    public void showUserImageResult() {
        getFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.tablet_right_view, new UserImageResultFragment()).commit();
    }

    /**
     * Called to show an image
     * @param position The position into the list
     * @param action intent action
     */
    @Override
    public void showImage(int position, Action action) {
        ImageFragment imageFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("action", action.toString());
        imageFragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.tablet_right_view, imageFragment).addToBackStack(null).commit();
    }

    /**
     * Called when an list' item changes
     */
    @Override
    public void onItemChanged() {
        getFragment().onItemChanged();
    }


    /**
     * Obtain the fragment manager of the context
     *
     * @return FragmentManager
     */
    private FragmentManager getFragmentManager() {
        return ((Activity) getContext()).getFragmentManager();
    }

    /**
     * Obtain the Search fragment
     * @return
     */
    private AbstractFragment getSearchFragment() {
        return (AbstractFragment) getFragmentManager().findFragmentById(R.id.search_fragment);
    }

    /**
     * Obtain the right fragment in the tablet view
     * @return
     */
    private AbstractFragment getFragment() {
        return (AbstractFragment) getFragmentManager().findFragmentById(R.id.tablet_right_view);
    }

}
