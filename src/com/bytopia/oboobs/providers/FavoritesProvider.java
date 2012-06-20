package com.bytopia.oboobs.providers;

import java.io.IOException;
import java.util.List;

import com.bytopia.oboobs.db.DbUtils;
import com.bytopia.oboobs.model.Boobs;

public class FavoritesProvider implements ImageProvider {

	@Override
	public List<Boobs> getBoobs(int from) throws IOException {
		if (from == 0) {
			List<Boobs> boobs = DbUtils.getFavoriteBoobs();
			return boobs;
		} else {
			return null;
		}
	}

	@Override
	public boolean hasOrder() {
		return false;
	}

	@Override
	public void setOrder(int order) {
	}

}