package com.uos.mortaldestiny.world;

import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.GameClass;
import com.uos.mortaldestiny.Physics;

public class WorldManager {
	
	Physics physics;

	public WorldManager(){
		this.physics = GameClass.getInstance().physics;
		
		initTestWorld();
	}
	
	public void initTestWorld(){	
		initTestWorldGround(10,10);
		physics.spawn("sphere");
	}
	
	public void initTestWorldGround(int wide, int depth){
		int size = 10;
		for(int x=-wide/2; x<wide; x++){
			for(int z=-depth/2; z<depth; z++){
				physics.spawnGroundTile(new Vector3(x*size,-1,z*size));
			}
		}
	}
	
}
