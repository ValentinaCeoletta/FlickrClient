package it.univr.cz.flickrclient.view;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import it.univr.cz.flickrclient.FlickrApplication;
import it.univr.cz.flickrclient.MVC;
import it.univr.cz.flickrclient.R;

/**
 * SearchFragment class: This fragment contains the elements to make a search.
 *
 * @Author Ceoletta Valentina
 * @Author Zenari Nicolo
 *
 * @Date June 2017
 */
public class SearchFragment extends Fragment implements AbstractFragment {

    /**
     * Tag string of this class
     */
    private final static String TAG = SearchFragment.class.getName();
    /**
     * The MVC triple
     */
    private MVC mvc;
    /**
     * EditText where the user insert the search string
     */
    private EditText search_string;
    /**
     * Button to start the search
     */
    private Button search_button;
    /**
     * Button to show the recent photos
     */
    private Button recent_button;
    /**
     * Button to show the popular photos
     */
    private Button popular_button;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        View view = inflater.inflate(R.layout.search_fragment_layout, container, false);

        search_string = (EditText) view.findViewById(R.id.search_string);

        search_button = (Button) view.findViewById(R.id.search_button);
        search_button.setOnClickListener(__ -> {
            try  {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (! search_string.getText().toString().isEmpty())
                mvc.controller.searchString(getActivity().getApplicationContext(), search_string.getText().toString());
        });

        recent_button = (Button) view.findViewById(R.id.recent_button);
        recent_button.setOnClickListener(__ -> {
            try  {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mvc.controller.searchRecent(getActivity().getApplicationContext());
        });

        popular_button = (Button) view.findViewById(R.id.popular_button);
        popular_button.setOnClickListener(__ -> {
            try  {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();

            }
            mvc.controller.searchPopular(getActivity().getApplicationContext());
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
        mvc = ((FlickrApplication) getActivity().getApplicationContext()).getMVC();
    }

    /**
     * Creates the menu
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_fragment_info, menu);
    }

    /**
     * Performs the menu item selection
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_info) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setMessage(getResources().getString(R.string.info_authors) + "\n"
                                + getResources().getString(R.string.info_date) +"\n"
                                + getResources().getString(R.string.info_version))
                    .setTitle(R.string.title_info)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                        }
                    })
                    .setIcon(R.drawable.ic_info_black_24dp);

            AlertDialog dialog = builder.create();
            dialog.show();


            return true;
        } else
            return super.onOptionsItemSelected(item);
    }


    @Override
    public void onModelChanged() {
        // nothing
    }


    @Override
    public void onItemChanged() {
        // nothing
    }
}
