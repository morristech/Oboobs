package com.bytopia.oboobs;

import android.graphics.Bitmap;

public interface BoobsFragmentHolder {
	public void imageReceived(int position, Bitmap bitmap);
	public boolean isFullScreen();
	public void setFullScreen(boolean fs);
}
