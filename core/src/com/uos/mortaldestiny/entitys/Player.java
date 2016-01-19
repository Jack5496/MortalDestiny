package com.uos.mortaldestiny.entitys;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;

public class Player{

	public ModelInstance model;
	
	public Player(ModelInstance model){
		this.model = model;
	}
	
	public Vector3 getVector(){
		Vector3 vec = new Vector3();
		model.transform.getTranslation(vec);
		return vec;
	}
	
}
