package com.bytopia.oboobs.fragments;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListFragment;
import com.bytopia.oboobs.ImageReceiver;
import com.bytopia.oboobs.OboobsApp;
import com.bytopia.oboobs.adapters.BoobsListAdapter;
import com.bytopia.oboobs.model.Boobs;
import com.bytopia.oboobs.providers.ImageProvider;
import com.bytopia.oboobs.utils.Utils;

public class BoobsListFragment extends SherlockListFragment {

	private static final int SENDER_TYPE = 42;
	Activity activity;
	OboobsApp app;
	BoobsListAdapter adapter;
	ImageProvider currentProvider;
	View footer;
	private int currentOffset;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
		app = (OboobsApp) activity.getApplication();
		app.setCurentReceiver(mImageReceiver);
		setRetainInstance(true);
	}

	public void fill(List<Boobs> boobs) {
		adapter = new BoobsListAdapter(activity, boobs, SENDER_TYPE);
		getListView().addFooterView(createFooter());
		setListAdapter(adapter);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	private View createFooter() {
		if (footer != null) {
			getListView().removeFooterView(footer);
		}
		ActionBar actionBar = getSherlockActivity().getSupportActionBar();

		ProgressBar bar = new ProgressBar(activity) {
			@Override
			protected void onAttachedToWindow() {
				downloadMoreBoobs();
			}
		};
		bar.setIndeterminate(true);
		// if(Build.VERSION.SDK_INT<Build.VERSION_CODES.HONEYCOMB){
		// bar.setIndeterminateDrawable(activity.getResources().getDrawable(R.styleable.SherlockActionBar_progressBarStyle));
		// }
		// bar.setIndeterminateDrawable(activity.getResources().getDrawable(android.R.drawable.prog));
		// bar.addOnAttachStateChangeListener(mOnAttachStateChangeListener);
		footer = bar;
		return bar;
	}

	public void updateItem(Integer a, Bitmap b) {
		adapter.update();

	}

	private ImageReceiver mImageReceiver = new ImageReceiver() {

		@Override
		public void receiveImage(int imageId, Bitmap bitmap) {
			adapter.updateViews(imageId, bitmap, getListView());
		}

		@Override
		public int getSenderType() {
			return SENDER_TYPE;
		}
	};

	public List<Boobs> manageNewBoobs(ImageProvider provider)
			throws IOException {
		currentProvider = provider;
		return provider.getBoobs(currentOffset);
	}

	public void clearAdapter() {
		setListAdapter(null);
		setListShown(false);
		currentOffset = 0;
	}

	// private OnAttachStateChangeListener mOnAttachStateChangeListener = new
	// OnAttachStateChangeListener() {
	//
	// @Override
	// public void onViewAttachedToWindow(View v) {
	// downloadMoreBoobs();
	// }
	//
	// @Override
	// public void onViewDetachedFromWindow(View v) {
	// // TODO Auto-generated method stub
	//
	// }
	// };
	private final BoobsLoadCallback newBoobsCallback = new BoobsLoadCallback() {

		@Override
		public void receiveBoobs(List<Boobs> boobs) {
			fill(boobs);
		}

		@Override
		public void getReadyForBoobs() {
			clearAdapter();
		}

	};

	private final BoobsLoadCallback moreBoobsCallback = new BoobsLoadCallback() {

		@Override
		public void receiveBoobs(List<Boobs> boobs) {
			if (boobs != null) {
				getListView().addFooterView(createFooter());
				for (Boobs boob : boobs) {
					adapter.add(boob);
				}
			} else {
				getListView().removeFooterView(footer);
			}
		}

		@Override
		public void getReadyForBoobs() {
			// TODO show progress
		}

	};

	public void getBoobsFrom(ImageProvider provider) {
		asyncLoadBoobs(newBoobsCallback, provider);
	}

	private void downloadMoreBoobs() {
		currentOffset += Utils.getBoobsChunk();
		asyncLoadBoobs(moreBoobsCallback, currentProvider);
	}

	private void asyncLoadBoobs(final BoobsLoadCallback callback,
			ImageProvider provider) {
		new AsyncTask<ImageProvider, Void, List<Boobs>>() {

			protected void onPreExecute() {
				callback.getReadyForBoobs();
			};

			@Override
			protected List<Boobs> doInBackground(ImageProvider... params) {
				try {
					return manageNewBoobs(params[0]);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(final java.util.List<Boobs> result) {
				if (result != null) {
					callback.receiveBoobs(result);
				}
			};
		}.execute(provider);

		Log.d("provider", provider.getClass().getName());
	}

}

interface BoobsLoadCallback {
	void receiveBoobs(List<Boobs> boobs);

	void getReadyForBoobs();
}
