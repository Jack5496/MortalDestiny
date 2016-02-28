package com.uos.mortaldestiny.worldTiles;

import java.awt.Point;

import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.worldGenerators.WorldGenerator;

public class WorldTile implements WorldTileInterface{
	public static int size = 10;
	public float degree;
	
	public String name = "GroundTile5x5";
	
	public Point p;
	public boolean rendered;

	public WorldTile(int x, int y, boolean rendered) {
		this.p = new Point(x, y);
		this.rendered = rendered;
		degree = 0;
	}

	public WorldTile(int x, int y) {
		this(x, y, false);
	}

	public boolean canBeGenerated() {
		return true;
	}

	public void generate() {
		if (!isFinished()) {
			if (canBeGenerated()) {
				forceGeneration();
			}
		}
	}
	
	public void forceGeneration(){
		int x = p.x;
		int z = p.y;
		GameClass.getInstance().physics.spawnGroundTile(new Vector3(x * size, -1, z * size),name, degree);
		finished();
	}

	public boolean isFinished() {
		return this.rendered;
	}

	public void finished() {
		this.rendered = true;
	}

}
