package com.toro.tarifdefterim;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.Sharer.Result;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.toro.adapters.adapter_tarifler;
import com.toro.interfaces.torolistener;
import com.toro.objects.globalObject;
import com.toro.objects.tarifBeans;
import com.toro.screens.Screens;
import com.toro.util.DataBaseHandler;

public class MainActivity extends ActionBarActivity implements
	NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the
     * navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in
     * {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private static ListView listView;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	globalObject.setCurrentActivity(getParent());
	globalObject.getFragment().loadTarifler("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	/*
	 * try { PackageInfo info = getPackageManager().getPackageInfo(
	 * "com.toro.tarifdefterim",// Projenin paket ismini // yazýyoruz
	 * PackageManager.GET_SIGNATURES); for (Signature signature :
	 * info.signatures) { MessageDigest md =
	 * MessageDigest.getInstance("SHA"); md.update(signature.toByteArray());
	 * 
	 * Log.d("KeyHash:", Base64.encodeToString(md.digest(),
	 * Base64.DEFAULT)); } } catch (PackageManager.NameNotFoundException e)
	 * { Log.d("KeyHash:", e.toString()); } catch (NoSuchAlgorithmException
	 * e) { Log.d("KeyHash:", e.toString()); }
	 */

	globalObject.setMainActivity(this);
	globalObject.setContext(getApplicationContext());
	globalObject.setDbHandler(new DataBaseHandler(getApplicationContext()));

	ActionBar actionBar = getSupportActionBar();
	actionBar.setCustomView(R.layout.actionbar_view);
	EditText search = (EditText) actionBar.getCustomView().findViewById(
		R.id.searchfield);
	actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
		| ActionBar.DISPLAY_SHOW_HOME);

	mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
		.findFragmentById(R.id.navigation_drawer);
	mTitle = getTitle();

	// Set up the drawer.
	mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
		(DrawerLayout) findViewById(R.id.drawer_layout));

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
	// update the main content by replacing fragments
	FragmentManager fragmentManager = getSupportFragmentManager();
	fragmentManager
		.beginTransaction()
		.replace(R.id.container,
			PlaceholderFragment.newInstance(position + 1)).commit();
    }

    public void onSectionAttached(int number) {
	switch (number) {
	case 1:
	    mTitle = "Section 1";
	    break;
	case 2:
	    mTitle = "Section 2";
	    break;
	case 3:
	    mTitle = "Section 3";
	    break;
	}
    }

    public void restoreActionBar() {
	ActionBar actionBar = getSupportActionBar();
	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
	actionBar.setDisplayShowTitleEnabled(true);
	actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

	if (!mNavigationDrawerFragment.isDrawerOpen()) {
	    // Only show items in the action bar relevant to this screen
	    // if the drawer is not showing. Otherwise, let the drawer
	    // decide what to show in the action bar.
	    getMenuInflater().inflate(R.menu.main, menu);
	    restoreActionBar();
	    return true;
	}
	return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// Handle action bar item clicks here. The action bar will
	// automatically handle clicks on the Home/Up button, so long
	// as you specify a parent activity in AndroidManifest.xml.
	int id = item.getItemId();
	if (id == R.id.action_settings) {
	    return true;
	}
	return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this
	 * fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static PlaceholderFragment newInstance(int sectionNumber) {
	    PlaceholderFragment fragment = new PlaceholderFragment();
	    Bundle args = new Bundle();
	    args.putInt(ARG_SECTION_NUMBER, sectionNumber);
	    fragment.setArguments(args);
	    return fragment;
	}

	public PlaceholderFragment() {
	}

	private Boolean initialize = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
	    View rootView = inflater.inflate(R.layout.fragment_main, container,
		    false);

	    globalObject.setCurrentActivity(getActivity());

	    if (!initialize) {
		try {
		    globalObject.getDbHandler().createdatabase();
		} catch (IOException e) {
		    e.printStackTrace();
		}

		((ImageButton) rootView.findViewById(R.id.btn_sil))
			.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View view) {
				globalObject.setCurrentActivity(getActivity());
				if (globalObject.getTarif() != null) {
				    Screens.showAlert(globalObject.getTarif()
					    .getAd() + " silinsin mi?",
					    new OnClickListener() {
						@Override
						public void onClick(
							DialogInterface dialog,
							int which) {
						    globalObject
							    .getDbHandler()
							    .deleteTarif(
								    globalObject
									    .getTarif());
						    loadTarifler("");
						}
					    }, true);
				}
			    }
			});

		((ImageButton) rootView.findViewById(R.id.btn_hakkinda))
			.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View view) {
				globalObject.setCurrentActivity(getActivity());
				Intent intent = new Intent(getActivity(),
					activity_hakkinda.class);
				startActivityForResult(intent, 6);
			    }
			});

		((ImageButton) rootView.findViewById(R.id.btn_ekle))
			.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View view) {
				globalObject.setCurrentActivity(getActivity());
				Intent intent = new Intent(getActivity(),
					activity_yenitarif.class);
				startActivityForResult(intent, 7);

			    }
			});

		listView = (ListView) rootView.findViewById(R.id.listView1);
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> a, View v,
			    int position, long id) {
			globalObject.setCurrentActivity(getActivity());
			Object o = listView.getItemAtPosition(position);
			((adapter_tarifler) listView.getAdapter())
				.setSelectedPosition(position);
			globalObject.setTarif((tarifBeans) o);
			Intent intent = new Intent(getActivity(),
				activity_tarifdetay.class);
			intent.putExtra("tarifeId", 1);
			startActivity(intent);
		    }
		});
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {
		    public boolean onItemLongClick(AdapterView<?> adapter,
			    View view, int position, long id) {

			Object o = listView.getItemAtPosition(position);
			((adapter_tarifler) listView.getAdapter())
				.setSelectedPosition(position);
			globalObject.setTarif((tarifBeans) o);
			globalObject.setCurrentActivity(getActivity());

			Screens.showAlert(globalObject.getTarif().getAd()
				+ " silinsin mi?", new OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog,
				    int which) {
				globalObject.getDbHandler().deleteTarif(
					globalObject.getTarif());
				loadTarifler("");
			    }
			}, true);

			return true;
		    }
		});

		loadKategoriler();
		loadTarifler("");

		globalObject.setFragment(this);
		initialize = true;
	    } // end of initialize

	    return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    ((MainActivity) activity).onSectionAttached(getArguments().getInt(
		    ARG_SECTION_NUMBER));
	}

	public void loadKategoriler() {
	    if (globalObject.getNavigationView() != null) {
		String[] items = globalObject.getDbHandler().getAllKategori();
		if (items != null) {
		    globalObject.getNavigationView().setAdapter(
			    new ArrayAdapter<String>(getActivity(),
				    android.R.layout.simple_list_item_1,
				    android.R.id.text1, items));
		}

	    }

	}

	public void loadTarifler(String filter) {
	    ArrayList<tarifBeans> data = globalObject.getDbHandler()
		    .getAllTarifler(filter, false);
	    adapter_tarifler adapter = new adapter_tarifler(getActivity(), data);
	    listView.setAdapter(adapter);
	}
    }

}
