package com.uos.mortaldestiny.worldGenerators;

import com.uos.mortaldestiny.worldTiles.CityHouse;
import com.uos.mortaldestiny.worldTiles.CityStreetCross;
import com.uos.mortaldestiny.worldTiles.CityStreetStraight;
import com.uos.mortaldestiny.worldTiles.WorldTile;

public class CityGenerator implements GeneratorInterface {

	public static String path = "data/models/MapParts/City/";

	@Override
	public WorldTile getTileAt(int x, int z) {
		CityStreetCross cross = new CityStreetCross(x, z, false);
		CityStreetStraight straightX = new CityStreetStraight(x, z, false, CityStreetStraight.DIRECTION_X);
		CityStreetStraight straightZ = new CityStreetStraight(x, z, false, CityStreetStraight.DIRECTION_Z);

		if (cross.canBeGenerated())
			return cross;
		if (straightX.canBeGenerated())
			return straightX;
		if (straightZ.canBeGenerated())
			return straightZ;

		return new CityHouse(x, z, false);
	}

	// STRAIGHT
	public static boolean doesFit(CityStreetStraight a, WorldTile t) {
		if (t.getClass() == CityStreetCross.class)
			return doesFit(a, (CityStreetCross) t);
		if (t.getClass() == CityStreetStraight.class)
			return doesFit(a, (CityStreetStraight) t);
		return true;
	}

	public static boolean doesFit(CityStreetStraight a, CityStreetStraight b) {
		if (nextNeumann(a, b)) {
			if (Math.abs(a.p.x - b.p.x) == 1) {
				return a.direction == CityStreetStraight.DIRECTION_X && b.direction == CityStreetStraight.DIRECTION_X;
			}
			return a.direction == CityStreetStraight.DIRECTION_Z && b.direction == CityStreetStraight.DIRECTION_Z;
		}
		return false;
	}

	public static boolean doesFit(CityStreetStraight a, CityStreetCross b) {
		if (a.direction == CityStreetStraight.DIRECTION_X) {
			return Math.abs(a.p.x - b.p.x) == 1;
		}
		if (a.direction == CityStreetStraight.DIRECTION_Z) {
			return Math.abs(a.p.y - b.p.y) == 1;
		}
		return false;
	}
	// STRAIGHT END

	// CROSS
	public static boolean doesFit(CityStreetCross a, WorldTile t) {
		if (t.getClass() == CityStreetCross.class)
			return doesFit(a, (CityStreetCross) t);
		if (t.getClass() == CityStreetStraight.class)
			return doesFit(a, (CityStreetStraight) t);
		return true;
	}

	public static boolean doesFit(CityStreetCross a, CityStreetCross b) {
		if (nextNeumann(a, b)) {
			int x = Math.abs(a.p.x - b.p.x);
			int z = Math.abs(a.p.y - b.p.y);

			return (x + z >= 4);
		}
		return false;
	}

	public static boolean nextNeumann(WorldTile a, WorldTile b) {
		int x = Math.abs(a.p.x - b.p.x);
		int z = Math.abs(a.p.y - b.p.y);

		return (x == 1 ^ z == 1);
	}

	public static boolean doesFit(CityStreetCross b, CityStreetStraight a) {
		return doesFit(a, b);
	}
	// CROSS END
}
