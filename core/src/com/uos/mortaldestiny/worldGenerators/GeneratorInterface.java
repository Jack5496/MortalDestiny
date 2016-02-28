package com.uos.mortaldestiny.worldGenerators;

import com.uos.mortaldestiny.worldTiles.WorldTile;

public interface GeneratorInterface {
	public WorldTile getTileAt(int x, int z);
}
