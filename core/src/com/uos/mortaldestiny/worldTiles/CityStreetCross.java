package com.uos.mortaldestiny.worldTiles;

import com.uos.mortaldestiny.worldGenerators.CityGenerator;
import com.uos.mortaldestiny.worldGenerators.WorldGenerator;

public class CityStreetCross extends WorldTile {

	public static String path = "Cross/GroundTile5x5Cross.g3db";

	public CityStreetCross(int x, int y, boolean rendered) {
		super(x, y, rendered);
		this.name = "GroundTile5x5Cross";
	}

	@Override
	public boolean canBeGenerated() {
		boolean possible = true;
		for (WorldTile t : WorldGenerator.getNeighborsNeumann(this)) {
			if (t != null) {
				if (!CityGenerator.doesFit(this, t)) {
					possible = false;
				}
			}
		}
		return possible;
	}

}
