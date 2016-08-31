package com.toro.tarifdefterim;

import java.util.ArrayList;

import com.toro.adapters.adapter_tarifler;
import com.toro.interfaces.torolistener;
import com.toro.objects.globalObject;
import com.toro.objects.tarifBeans;
import com.toro.screens.Screens;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class NavigationDrawerFragment extends Fragment {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    private NavigationDrawerCallbacks mCallbacks;

    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	SharedPreferences sp = PreferenceManager
		.getDefaultSharedPreferences(getActivity());
	mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

	if (savedInstanceState != null) {
	    mCurrentSelectedPosition = savedInstanceState
		    .getInt(STATE_SELECTED_POSITION);
	    mFromSavedInstanceState = true;
	}

	// Select either the default item (0) or the last selected item.
	selectItem(mCurrentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
	super.onActivityCreated(savedInstanceState);
	// Indicate that this fragment would like to influence the set of
	// actions in the action bar.
	setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
	    Bundle savedInstanceState) {
	mDrawerListView = (ListView) inflater.inflate(
		R.layout.fragment_navigation_drawer, container, false);
	mDrawerListView
		.setOnItemClickListener(new AdapterView.OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> parent, View view,
			    int position, long id) {
			selectItem(position);
		    }
		});
	mDrawerListView
		.setOnItemLongClickListener(new OnItemLongClickListener() {
		    @Override
		    public boolean onItemLongClick(AdapterView<?> arg0,
			    View arg1, int position, long id) {
			final String item = (String) mDrawerListView
				.getAdapter().getItem(position);
			if (item != null && item.length() > 0) {
			    Screens.showAlert(item + " silinsin mi?",
				    new OnClickListener() {
					@Override
					public void onClick(
						DialogInterface dialog,
						int which) {
					    globalObject.getDbHandler()
						    .deleteKategori(item);
					    if (globalObject.getFragment() != null) {
						globalObject.getFragment()
							.loadKategoriler();
					    } else {
						System.out.println("Null mus!");
					    }
					}
				    }, true);
			}

			return false;
		    }
		});

	String[] items = new String[] { "Kategorilenmemiþ" };

	mDrawerListView.setAdapter(new ArrayAdapter<String>(getActionBar()
		.getThemedContext(), android.R.layout.simple_list_item_1,
		android.R.id.text1, items));
	mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

	globalObject.setNavigationView(mDrawerListView);
	return mDrawerListView;
    }

    public boolean isDrawerOpen() {
	return mDrawerLayout != null
		&& mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation
     * drawer interactions.
     *
     * @param fragmentId
     *            The android:id of this fragment in its activity's layout.
     * @param drawerLayout
     *            The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
	mFragmentContainerView = getActivity().findViewById(fragmentId);
	mDrawerLayout = drawerLayout;

	// set a custom shadow that overlays the main content when the drawer
	// opens
	mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
		GravityCompat.START);
	// set up the drawer's list view with items and click listener

	ActionBar actionBar = getActionBar();
	actionBar.setDisplayHomeAsUpEnabled(true);
	actionBar.setHomeButtonEnabled(true);

	// ActionBarDrawerToggle ties together the the proper interactions
	// between the navigation drawer and the action bar app icon.
	mDrawerToggle = new ActionBarDrawerToggle(getActivity(), /*
								  * host
								  * Activity
								  */
	mDrawerLayout, /* DrawerLayout object */
	R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
	R.string.navigation_drawer_open, /*
					  * "open drawer" description for
					  * accessibility
					  */
	R.string.navigation_drawer_close /*
					  * "close drawer" description for
					  * accessibility
					  */
	) {
	    @Override
	    public void onDrawerClosed(View drawerView) {
		super.onDrawerClosed(drawerView);
		if (!isAdded()) {
		    return;
		}

		getActivity().supportInvalidateOptionsMenu(); // calls
							      // onPrepareOptionsMenu()
	    }

	    @Override
	    public void onDrawerOpened(View drawerView) {
		super.onDrawerOpened(drawerView);
		if (!isAdded()) {
		    return;
		}

		if (!mUserLearnedDrawer) {
		    // The user manually opened the drawer; store this flag to
		    // prevent auto-showing
		    // the navigation drawer automatically in the future.
		    mUserLearnedDrawer = true;
		    SharedPreferences sp = PreferenceManager
			    .getDefaultSharedPreferences(getActivity());
		    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true)
			    .commit();
		}

		getActivity().supportInvalidateOptionsMenu(); // calls
							      // onPrepareOptionsMenu()
	    }
	};

	// If the user hasn't 'learned' about the drawer, open it to introduce
	// them to the drawer,
	// per the navigation drawer design guidelines.
	if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
	    mDrawerLayout.openDrawer(mFragmentContainerView);
	}

	// Defer code dependent on restoration of previous instance state.
	mDrawerLayout.post(new Runnable() {
	    @Override
	    public void run() {
		mDrawerToggle.syncState();
	    }
	});

	mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void selectItem(int position) {
	mCurrentSelectedPosition = position;
	if (mDrawerListView != null) {
	    mDrawerListView.setItemChecked(position, true);
	    String item = (String) mDrawerListView.getAdapter().getItem(
		    position);
	    globalObject.setKategori(item);
	}
	if (mDrawerLayout != null) {
	    mDrawerLayout.closeDrawer(mFragmentContainerView);
	}
	if (mCallbacks != null) {
	    mCallbacks.onNavigationDrawerItemSelected(position);
	}

    }

    @Override
    public void onAttach(Activity activity) {
	super.onAttach(activity);
	try {
	    mCallbacks = (NavigationDrawerCallbacks) activity;
	} catch (ClassCastException e) {
	    throw new ClassCastException(
		    "Activity must implement NavigationDrawerCallbacks.");
	}
    }

    @Override
    public void onDetach() {
	super.onDetach();
	mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
	super.onSaveInstanceState(outState);
	outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
	super.onConfigurationChanged(newConfig);
	// Forward the new configuration the drawer toggle component.
	mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	// If the drawer is open, show the global app actions in the action bar.
	// See also
	// showGlobalContextActionBar, which controls the top-left area of the
	// action bar.
	if (mDrawerLayout != null && isDrawerOpen()) {
	    inflater.inflate(R.menu.global, menu);
	    showGlobalContextActionBar();
	}
	super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	if (mDrawerToggle.onOptionsItemSelected(item)) {
	    return true;
	}

	if (item.getItemId() == R.id.action_ara) {
	    EditText edit = (EditText) getActivity().findViewById(
		    R.id.searchfield);
	    globalObject.getFragment().loadTarifler(edit.getText().toString());
	    /*
	     * Toast.makeText(getActivity(), "Example action." +
	     * edit.getText().toString(), Toast.LENGTH_SHORT).show();
	     */
	    return true;

	} else if (item.getItemId() == R.id.action_kategoriekle) {
	    Screens.kategoriEkle("Kategori Adý", "Yeni Kategori",
		    new torolistener() {
			@Override
			public void onResult(Object sender, Object o) {
			    if (globalObject.getFragment() != null) {
				globalObject.getFragment().loadKategoriler();
			    } else {
				System.out.println("Null mus!");
			    }
			}
		    });

	} else if (item.getItemId() == R.id.action_kategorisil) {
	    if (globalObject.getKategori() != null
		    && globalObject.getKategori().length() > 0) {
		Screens.showAlert(globalObject.getKategori() + " silinsin mi?",
			new OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog,
				    int which) {
				globalObject.getDbHandler().deleteKategori(
					globalObject.getKategori());
				if (globalObject.getFragment() != null) {
				    globalObject.getFragment()
					    .loadKategoriler();
				} else {
				    System.out.println("Null mus!");
				}
			    }
			}, true);
	    }

	} else if (item.getItemId() == R.id.action_settings) {

	}

	return super.onOptionsItemSelected(item);
    }

    private void showGlobalContextActionBar() {
	ActionBar actionBar = getActionBar();
	actionBar.setDisplayShowTitleEnabled(true);
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
	return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    public static interface NavigationDrawerCallbacks {
	void onNavigationDrawerItemSelected(int position);
    }
}
