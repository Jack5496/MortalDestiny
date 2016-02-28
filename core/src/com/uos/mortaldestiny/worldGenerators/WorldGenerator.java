package com.uos.mortaldestiny.worldGenerators;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.player.Player;
import com.uos.mortaldestiny.worldTiles.WorldTile;

public class WorldGenerator {

	public static Map<Point, WorldTile> tiles;

	WorldGeneratorThread wgt;
	Thread t;

	GeneratorInterface gen;

	Player pla;

	public WorldGenerator() {
		wgt = new WorldGeneratorThread(this);
		t = new Thread(wgt);
		t.start();

		tiles = new HashMap<Point, WorldTile>();

		gen = new DefaultGenerator();
		gen = new CityGenerator();
		WorldTile t = new WorldTile(0, 0, false);
		t.forceGeneration();
	}

	public void addToGeneratePlayerArea(Player p) {
		Vector3 pos = p.getObjPos();
		int x = (((int) pos.x)) / WorldTile.size;
		int z = (((int) pos.z)) / WorldTile.size;
		GameClass.log(getClass(), "x:" + x + " | z:" + z);

		addToGenerate(x, z);
		for(Point point : getNeighborsNeumann(new Point(x,z))){
			addToGenerate(point.x, point.y);
		}
		for(Point point : getNeighborsNeumann(new Point(x,z))){
			for(Point p1 : getNeighborsNeumann(point)){
				addToGenerate(p1.x, p1.y);
			}
		}


	}

	public void addToGenerateArea(int xStart, int zStart, int xEnd, int zEnd) {

		for (int x = xStart; x < xEnd + 1; x++) {
			for (int z = zStart; z < zEnd + 1; z++) {
				addToGenerate(x, z);
			}
		}
	}

	public void addToGenerate(int x, int z) {
		Point p = new Point(x, z);
		WorldTile t = gen.getTileAt(x, z);
		if (!tiles.containsKey(p) && !wgt.toGenerate.contains(t)) {
			wgt.toGenerate.add(t);
		}
	}

	public static WorldTile getTile(int x, int z) {
		Point p = new Point(x, z);
		if (!tiles.containsKey(p))
			return null;
		return tiles.get(p);
	}

	public static WorldTile getTileFromOffset(int x, int z, WorldTile t) {
		Point p = new Point(t.p.x + x, t.p.y + z);
		if (!tiles.containsKey(p))
			return null;
		return tiles.get(p);
	}

	public static List<WorldTile> getNeighborsNeumann(WorldTile t) {
		List<WorldTile> neighbors = new ArrayList<WorldTile>();
		neighbors.add(getTileFromOffset(-1, 0, t));
		neighbors.add(getTileFromOffset(1, 0, t));
		neighbors.add(getTileFromOffset(0, -1, t));
		neighbors.add(getTileFromOffset(0, 1, t));
		return neighbors;
	}
	
	public static List<Point> getNeighborsNeumann(Point p) {
		List<Point> neighbors = new ArrayList<Point>();
		neighbors.add(new Point(p.x-1, p.y));
		neighbors.add(new Point(p.x+1, p.y));
		neighbors.add(new Point(p.x, p.y-1));
		neighbors.add(new Point(p.x, p.y+1));
		return neighbors;
	}
	
	// public static List<WorldTile> getNeighborsMoore(WorldTile t){
	// List<WorldTile> neighbors = getNeighborsNeumann(t);
	// neighbors.add(getTileFromOffset(-1,-1,t));
	// neighbors.add(getTileFromOffset(1,1,t));
	// neighbors.add(getTileFromOffset(1,-1,t));
	// neighbors.add(getTileFromOffset(-1,1,t));
	// return neighbors;
	// }

}
