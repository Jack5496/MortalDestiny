package com.uos.mortaldestiny.worldTiles;

public class CityHouse extends WorldTile {
	
	public CityHouse(int x, int y, boolean rendered) {
		super(x, y, rendered);
	}

	@Override
	public boolean canBeGenerated() {
		return true;
	}

}
