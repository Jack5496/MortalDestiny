package com.uos.mortaldestiny.world;

import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.GameClass;

public class WorldManager {

	Physics physics;

	public WorldManager() {
		this.physics = GameClass.getInstance().physics;

		initTestWorld();
	}

	public void initTestWorld() {
//		initTestWorldGround(5, 5);
		initTestWorld(world);
		physics.spawn("sphere");

	}

//	String world = "10x10:" + "1111111111" + "0110111110" + "0110110110" + "0110110110" + "0111110110" + "0110001110"
//			+ "0111101110" + "0111001110" + "0111111110" + "0000000000";
	
	String world = "10x10:" + "1111111111" + "1111111111" + "1111111111" + "1111111111" + "1111111111" + "1111111111"
			+ "1111111111" + "1111111111" + "1111111111" + "1111111111";

	public int getWidth(String parameters) {
		return Integer.parseInt(parameters.split(parameterSizeBreak)[0]);
	}

	public int getHeight(String parameters) {
		System.out.println(parameters);
		return Integer.parseInt(parameters.split(parameterSizeBreak)[1]);
	}

	String parameterSizeBreak = "x";
	String parameterBreak = ":";

	public String getParameters(String world) {
		return world.split(parameterBreak)[0];
	}

	public String getWorld(String world) {
		return world.split(parameterBreak)[1];
	}

	public void initTestWorld(String world) {
		String params = getParameters(world);
		System.out.println("params: "+params);
		int width = getWidth(params);
		int height = getHeight(params);
		String map = getWorld(world);

		int size = 10;
		for (int x = 0; x < width; x++) {
			for (int z = 0; z < height; z++) {
				if ("1".equalsIgnoreCase(map.substring(x + z * width, x + z * width + 1))) {
					physics.spawnGroundTile(new Vector3(x * size, -1, z * size));
				}
			}
		}
	}

	public void initTestWorldGround(int wide, int depth) {
		int size = 10;
		for (int x = 0; x < wide; x++) {
			for (int z = 0; z < depth; z++) {
				physics.spawnGroundTile(new Vector3(x * size, -1, z * size));
			}
		}
	}

}
