package com;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.bytopia.oboobs.OboobsApp;
import com.bytopia.oboobs.R;
import com.flurry.android.FlurryAgent;

public class BaseActivity extends ActionBarActivity {

	protected ActionBar bar;
	protected OboobsApp app;
	protected FragmentManager fragmentManager;

	private InputMethodManager imm;

	public static String flurryKey;

	@Override
	protected void onCreate(Bundle arg0) {
        //setTheme(R.style.Theme_AppCompat);
		super.onCreate(arg0);

		app = (OboobsApp) getApplication();

		setTheme(R.style.Theme_AppCompat); // Used for theme switching in samples

		fragmentManager = getSupportFragmentManager();

		bar = getSupportActionBar();

		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		FlurryAgent.onStartSession(this, flurryKey);
	}

	protected void hideKeyboard(TextView textView) {
		imm.hideSoftInputFromWindow(textView.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		FlurryAgent.onEndSession(this);
	}

}
