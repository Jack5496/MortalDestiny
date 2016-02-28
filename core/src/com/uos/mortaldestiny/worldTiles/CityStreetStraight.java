package com.uos.mortaldestiny.worldTiles;

import com.uos.mortaldestiny.worldGenerators.CityGenerator;
import com.uos.mortaldestiny.worldGenerators.WorldGenerator;

public class CityStreetStraight extends WorldTile {

	public static String path = "Straight/GroundTile5x5Straight.g3db";

	public static final int DIRECTION_X = 1;
	public static final int DIRECTION_Z = 0;

	public int direction;

	public CityStreetStraight(int x, int y, boolean rendered, int direction) {
		super(x, y, rendered);
		this.direction = direction;
		this.name = "GroundTile5x5Straight";
		this.degree = this.direction*90;
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
