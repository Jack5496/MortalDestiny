package com.uos.mortaldestiny.entitys;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class entity{

	public ModelInstance model;
	
	
	public float getX(){
		Vector3 vec = model.transform.getTranslation(null);
		return vec.x;
	}
	
	public float getY(){
		Vector3 vec = model.transform.getTranslation(null);
		return vec.y;
	}
	
	public float getZ(){
		Vector3 vec = model.transform.getTranslation(null);
		return vec.z;
	}
	
}
