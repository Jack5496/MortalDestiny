package com.uos.mortaldestiny.entitys;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.GameClass;

public class Player{

	private ModelInstance model;
	private float health;
	private String name;
	private float speed;
		
	public Player(ModelInstance model){
		this.model = model;
		this.model.transform.setToTranslation(0, 1, 0);
		this.health = 100;
		this.name = "Jack";
		this.speed = 0.5f;
	}
	
	public ModelInstance getModelInstance(){
		return this.model;
	}

	public Vector3 getVector(){
		Vector3 vec = new Vector3();
		model.transform.getTranslation(vec);
		return vec;
	}
		
	public void setDirection(float lookDir){
		Vector3 position = getVector();
		getModelInstance().transform.setToRotation(new Vector3(0,1,0), lookDir);
		getModelInstance().transform.trn(position);
	}
	
	public void move(float dir) {
		Vector3 v = new Vector3(0,0,1);
		v = v.rotate(new Vector3(0,1,0), dir);	//not exactly calc somehow
		v.clamp(0, 1);	//normalize Direction
		v.scl(speed);	
		
		System.out.println("length: "+v.len());
		getModelInstance().transform.trn(v.x, 0, v.z);
	}
	
}
