package com.uos.mortaldestiny.worldTiles;

import java.awt.Point;

import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.worldGenerators.WorldGenerator;

public interface WorldTileInterface {

	public boolean canBeGenerated();
	public void generate();
	public boolean isFinished();
	public void finished();

}
