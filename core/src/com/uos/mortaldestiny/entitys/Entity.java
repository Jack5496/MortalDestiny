package com.uos.mortaldestiny.entitys;

import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.uos.mortaldestiny.GameClass;

public class Entity{

	private ModelInstance model;
	private float health;
	private String name;
	private float speed;
		
	public Entity(ModelInstance model){
		this.model = model;
		this.model.transform.setToTranslation(0, 1, 0);
		this.health = 100;
		this.name = "Entity";
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
	
	
		
	public void setRotation(float lookDir){
		Vector3 position = getVector();
		getModelInstance().transform.setToRotation(new Vector3(0,1,0), lookDir);
		getModelInstance().transform.trn(position);
		
		System.out.println("Set: "+lookDir+" | Get: "+getRotation());
	}
	
	public float getRotation(){
		Vector3 position = getVector();
		Quaternion q = new Quaternion(new Vector3(0,1,0),0);
		getModelInstance().transform.getRotation(q);
		return q.getAngle();
	}
	
	public void move(float dir) {
		Vector3 v = new Vector3(0,0,1);
		v = v.rotate(new Vector3(0,1,0), dir);	//not exactly calc somehow
		v.clamp(0, 1);	//normalize Direction
		v.scl(speed);	
		
		getModelInstance().transform.trn(v.x, 0, v.z);
	}
	
}
