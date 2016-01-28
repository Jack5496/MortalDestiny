package com.uos.mortaldestiny.world;

import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.GameClass;

public class WorldManager {
	
	Physics physics;

	public WorldManager(){
		this.physics = GameClass.getInstance().physics;
		
		initTestWorld();
	}
	
	public void initTestWorld(){	
		initTestWorldGround(5,5);
		physics.spawn("sphere");
		
	}
	
	public void initTestWorldGround(int wide, int depth){
		int size = 10;
		for(int x=0; x<wide; x++){
			for(int z=0; z<depth; z++){
				physics.spawnGroundTile(new Vector3(x*size,-1,z*size));
			}
		}
	}
	
}
