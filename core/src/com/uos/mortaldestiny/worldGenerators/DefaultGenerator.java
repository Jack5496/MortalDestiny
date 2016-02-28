package com.uos.mortaldestiny.worldGenerators;

import com.uos.mortaldestiny.worldTiles.WorldTile;

public class DefaultGenerator implements GeneratorInterface{

	@Override
	public WorldTile getTileAt(int x, int z) {
		return new WorldTile(x, z, false);
	}
	
}
