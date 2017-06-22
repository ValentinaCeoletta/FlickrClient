package it.univr.cz.flickrclient.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import it.univr.cz.flickrclient.FlickrApplication;
import it.univr.cz.flickrclient.MVC;
import it.univr.cz.flickrclient.R;
import it.univr.cz.flickrclient.model.Action;

/**
 * PhoneView class: The tablet view is show when the app is running on a smarthphone.
 * It shows a fragment per time.
 * It extends a Linear Layout and implements View interface.
 *
 * @Author Ceoletta Valentina
 * @Author Zenari Nicolo
 *
 * @Date June 2017
 */
public class PhoneView extends FrameLayout implements View {

    /**
     * The MVC triple.
     */
    private MVC mvc;

    /**
     * Class constructor
     * @param context Application context
     */
    public PhoneView(@NonNull Context context) {
        super(context);
    }

    /**
     * Class constructor
     * @param context Application context
     * @param attrs Attribute set
     */
    public PhoneView(@NonNull Context context, @Nullable AttributeSet attrs) {
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

        // initial fragment
        if (getFragment() == null)
            getFragmentManager().beginTransaction().add(R.id.phone_view, new SearchFragment()).commit();
    }

    /**
     * Called to deattach a fragment from a window
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
        getFragment().onModelChanged(); // delega all'attuale frammento
    }

    /**
     * Called to show the fragment with search result
     */
    @Override
    public void showImageResult() {
        getFragmentManager().beginTransaction().replace(R.id.phone_view, new ImageResultFragment()).addToBackStack(null).commit();
    }

    /**
     * Called to show the fragment with last user photos search result
     */
    @Override
    public void showUserImageResult() {
        getFragmentManager().beginTransaction().replace(R.id.phone_view, new UserImageResultFragment()).addToBackStack(null).commit();
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
        getFragmentManager().beginTransaction().replace(R.id.phone_view, imageFragment).addToBackStack(null).commit();
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
     * @return FragmentManager
     */
    private FragmentManager getFragmentManager() {
        return ((Activity) getContext()).getFragmentManager();
    }

    /**
     * Obtain the right fragment in the tablet view
     * @return AbstractFragment
     */
    private AbstractFragment getFragment() {
        return (AbstractFragment) getFragmentManager().findFragmentById(R.id.phone_view);
    }
}
