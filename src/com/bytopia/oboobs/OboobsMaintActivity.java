package com.bytopia.oboobs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.bytopia.oboobs.adapters.ImageProviderAdapter;
import com.bytopia.oboobs.fragments.BoobsListFragment;
import com.bytopia.oboobs.model.Boobs;
import com.bytopia.oboobs.model.Order;
import com.bytopia.oboobs.providers.IdBoobsProvider;
import com.bytopia.oboobs.providers.ImageProvider;
import com.bytopia.oboobs.providers.InterestBoobsProvider;
import com.bytopia.oboobs.providers.NoiseBoobsProvider;
import com.bytopia.oboobs.providers.RankBoobsProvider;
import com.bytopia.oboobs.utils.NetworkUtils;

public class OboobsMaintActivity extends SherlockFragmentActivity implements
		ActionBar.OnNavigationListener {

	private ActionBar bar;
	private OboobsApp app;
	private FragmentManager fragmentManager;
	
	private Map<Integer, ImageProvider> providers;

	private BoobsListFragment boobsListFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		app = (OboobsApp) getApplication();

		setTheme(R.style.Theme_Sherlock); // Used for theme switching in samples
		super.onCreate(savedInstanceState);
		
		initProviders();

		setContentView(R.layout.main);

		bar = getSupportActionBar();
		Context barContext = bar.getThemedContext();
		
		
		List<String> providerNames = new ArrayList<String>();
		for(Integer id : providers.keySet()){
			providerNames.add(getString(id));
		}
		
		ArrayAdapter<String> list = new ImageProviderAdapter(barContext, R.layout.sherlock_spinner_item, providers);
		list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);

		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		bar.setListNavigationCallbacks(list, this);

		fragmentManager = getSupportFragmentManager();

		boobsListFragment = (BoobsListFragment) fragmentManager
				.findFragmentByTag("BoobsList");

	}

	private void initProviders() {
		providers = new HashMap<Integer, ImageProvider>();
		providers.put(R.string.by_rank, new RankBoobsProvider());
		providers.put(R.string.by_interest, new InterestBoobsProvider());
		providers.put(R.string.by_date, new IdBoobsProvider());
		providers.put(R.string.random, new NoiseBoobsProvider());
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		
		final ImageProvider provider = providers.get((int)itemId);
		
		new AsyncTask<Void, Void, List<Boobs>>() {

			@Override
			protected List<Boobs> doInBackground(Void... params) {
				try {
					//FIXME real offset
					List<Boobs> boobs = provider.getBoobs(0);

					return boobs;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(final java.util.List<Boobs> result) {
				if (result != null) {
					boobsListFragment.fill(result);

				}
			};
		}.execute();
		
		Log.d("provider", provider.getClass().getName());
		
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Used to put dark icons on light action bar
		boolean isDark = app.isDark;

		// menu.add("Save")
		// .setIcon(isLight ? R.drawable.ic_compose_inverse :
		// R.drawable.ic_compose)
		// .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		// menu.add("Search")
		// .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM |
		// MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		//
		// menu.add("Refresh")
		// .setIcon(isLight ? R.drawable.ic_refresh_inverse :
		// R.drawable.ic_refresh)
		// .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM |
		// MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;
	}
}